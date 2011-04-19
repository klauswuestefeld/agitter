package org.prevayler.bubble;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import sneer.foundation.lang.CacheMap;
import sneer.foundation.lang.Immutable;
import sneer.foundation.lang.Producer;
import sneer.foundation.lang.ProducerX;
import sneer.foundation.lang.ReadOnly;
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
		if (type.isArray()) return object;
		if (Collection.class.isAssignableFrom(type)) return object;
		if (Immutable.isImmutable(type)) return object;
		
		if (ReadOnly.class.isAssignableFrom(type)) return object;
		
		return wrapped(object, path);
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
			return PrevalentBubble.prevayler().execute(transaction);
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
