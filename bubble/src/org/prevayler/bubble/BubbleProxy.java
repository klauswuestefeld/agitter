package org.prevayler.bubble;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;

import basis.lang.CacheMap;
import basis.lang.Immutable;
import basis.lang.Producer;
import basis.lang.ProducerX;
import basis.lang.ReadOnly;
import basis.lang.exceptions.NotImplementedYet;
import basis.lang.types.Classes;


class BubbleProxy implements InvocationHandler {
	
	private static final CacheMap<Object, Object> _proxiesByObject = CacheMap.newInstance();
	
	public static <T> T wrapped(final Object object, final ProducerX<Object, ? extends Exception> path) {
		checkDoubleWrapping(object);
		return (T)_proxiesByObject.get(object, new Producer<Object>() { @Override public Object produce() {
			return newProxyFor(object, path);
		}});
	}


	private static void checkDoubleWrapping(final Object object) {
		if (Proxy.isProxyClass(object.getClass()))
			throw new IllegalStateException("Object being wrapped again. Domain might have returned a mutable collection or array instead of a copy.");
	}


	private static Object newProxyFor(Object object, ProducerX<Object, ? extends Exception> path) {
		if (isRegistered(object))
			path = new MapLookup(object);

		InvocationHandler handler = new BubbleProxy(object, path);
		Class<?> delegateClass = object.getClass();
		return Proxy.newProxyInstance(delegateClass.getClassLoader(), Classes.allInterfacesOf(delegateClass), handler);
	}


	private BubbleProxy(Object wrappedObject, ProducerX<Object, ? extends Exception> producer) {
		_wrappedObject = wrappedObject;
		_invocationPath = producer;
	}


	private final Object _wrappedObject;
	private final ProducerX<Object, ? extends Exception> _invocationPath;
	
	
	@Override
	public Object invoke(Object proxyImplied, Method method, Object[] args) throws Exception {
		if (PrevalenceFlag.isInsidePrevalence()) throw new IllegalStateException("Method called on bubble from within bubble.");

		return isTransaction(method)
			? invokeTransaction(method, args)
			: invokeQuery(method, args);
	}


	private Object invokeTransaction(Method method, Object[] args) throws Exception {
		TransactionInvocation transaction = new TransactionInvocation(_invocationPath, method, args);
		Object ret = PrevalentBubble.execute(transaction);
		return wrapIfNecessary(ret, null);
	}


	private Object invokeQuery(Method method, Object[] args) throws Exception {
		QueryInvocation extendedPath = new QueryInvocation(_wrappedObject, _invocationPath, method, args);
		Object ret = PrevalentBubble.execute(extendedPath);
		return wrapIfNecessary(ret, extendedPath);
	}


	private Object wrapIfNecessary(final Object object, ProducerX<Object, Exception> path) {
		if (object == null) return object;
		
		Class<?> type = object.getClass();
		if (type.isArray()) {
			wrapArrayElementsIfNecessary(object);
			return object;
		}
		if (List.class.isAssignableFrom(type)) {
			wrapListElementsIfNecessary((List<Object>)object);
			return object;
		}
		if (Collection.class.isAssignableFrom(type)) throwNotImplementedYet(type.getName());

		if (Immutable.isImmutable(type)) return object;
		if (ReadOnly.class.isAssignableFrom(type)) return object;
		
		if (path == null)
			path = new ProducerX<Object, Exception>() { @Override public Object produce() throws Exception {
				return object;
			}};
		
		return wrapped(object, path);
	}


	private void wrapListElementsIfNecessary(List<Object> list) {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			list.set(i, wrapIfNecessary(obj, null)); //null means no path. Mutable objects in collections must be registered. PrevalentBubble.getIdMap().register(...)
		}
	}


	private void wrapArrayElementsIfNecessary(Object arr) {		
		if (arr.getClass().getComponentType().isPrimitive())
			return; 
		
		Object[] array = (Object[])arr; 
		
		for (int i = 0; i < array.length; i++) {
			Object obj = array[i];
			array[i] = wrapIfNecessary(obj, null); //null means no path. Mutable objects in collections must be registered. PrevalentBubble.getIdMap().register(...)
		}
	}


	private void throwNotImplementedYet(String typeName) {
		throw new NotImplementedYet("" + BubbleProxy.class + " does not yet handle "+typeName+" return types. It does handle List returns though.");
	}


	private boolean isTransaction(Method method) {
		if (method.getReturnType() == Void.TYPE) return true;
		if (method.getAnnotation(Transaction.class) != null) return true;
		return false;
	}
	
	
	private static boolean isRegistered(Object object) {
		return PrevalentBubble.idMap().isRegistered(object);
	}


	long id() {
		return ((MapLookup)_invocationPath)._id;		
	}

}
