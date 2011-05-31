package org.prevayler.bubble;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.List;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.Producer;
import sneer.foundation.lang.ProducerX;
import sneer.foundation.lang.ReadOnly;
import sneer.foundation.lang.exceptions.NotImplementedYet;
import sneer.foundation.lang.types.Classes;

class BubbleProxy implements InvocationHandler {
	
	private static final CacheMap<Object, Object> _proxiesByObject = CacheMap.newInstance();
	
	public static <T> T wrapped(final Object object, final ProducerX<Object, ? extends Exception> path) {
		return (T)_proxiesByObject.get(object, new Producer<Object>() { @Override public Object produce() {
			return newProxyFor(object, path);
		}});
	}


	private static Object newProxyFor(Object object, ProducerX<Object, ? extends Exception> path) {
		if (isRegistered(object))
			path = new MapLookup(object);

		InvocationHandler handler = new BubbleProxy(path);
		Class<?> delegateClass = object.getClass();
		return Proxy.newProxyInstance(delegateClass.getClassLoader(), Classes.allInterfacesOf(delegateClass), handler);
	}


	private BubbleProxy(ProducerX<Object, ? extends Exception> producer) {
		if (producer == null) throw new IllegalArgumentException();
		_invocationPath = producer;
	}


	private final ProducerX<Object, ? extends Exception> _invocationPath;
	
	
	@Override
	public Object invoke(Object proxyImplied, Method method, Object[] args) throws Exception {
		ProducerX<Object, Exception> path = extendedPath(method, args);
		Object result = path.produce();
		return wrapIfNecessary(result, path);
	}


	private Object wrapIfNecessary(Object object, ProducerX<Object, Exception> path) {
		if (object == null) return object;
		
		Class<?> type = object.getClass();
		if (type.isArray()) throwNotImplementedYet("array");
		if (List.class.isAssignableFrom(type)) {
			wrapListElementsIfNecessary((List<Object>)object);
			return object;
		}
		if (Collection.class.isAssignableFrom(type)) throwNotImplementedYet(type.getName());

		if (Immutable.isImmutable(type)) return object;
		if (ReadOnly.class.isAssignableFrom(type)) return object;
		
		return wrapped(object, path);
	}


	private void wrapListElementsIfNecessary(List<Object> list) {
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			list.set(i, wrapIfNecessary(obj, null)); //null means no path. Mutable objects in collections must be registered. 
		}
	}


	private void throwNotImplementedYet(String typeName) {
		throw new NotImplementedYet("" + BubbleProxy.class + " does not yet handle "+typeName+" return types. It does handle List returns though.");
	}


	private ProducerX<Object, Exception> extendedPath(Method method, Object[] args) {
		if (!isTransaction(method))
			return new Invocation(_invocationPath, method, args);

		TransactionInvocation transaction = new TransactionInvocation(_invocationPath, method, args);
		return PrevalenceFlag.isInsidePrevalence()
			? transaction
			: withPrevayler(transaction);
	}


	private ProducerX<Object, Exception> withPrevayler(final TransactionInvocation transaction) {
		return new ProducerX<Object, Exception>() { @Override public Object produce() throws Exception {
			return PrevalentBubble.execute(transaction);
		}};
	}


	private boolean isTransaction(Method method) {
		if (method.getReturnType() == Void.TYPE) return true;
		if (method.getAnnotation(Transaction.class) != null) return true;
		return false;
	}
	
	
	private static boolean isRegistered(Object object) {
		return PrevalentBubble.idMap().isRegistered(object);
	}

}
