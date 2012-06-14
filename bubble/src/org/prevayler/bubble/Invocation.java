package org.prevayler.bubble;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import basis.lang.Clock;
import basis.lang.ProducerX;


abstract class Invocation implements Serializable {

	Invocation(ProducerX<Object, ? extends Exception> targetProducer, Method method, Object[] args) {
		_targetProducer = targetProducer;
		_method = method.getName();
		_argsTypes = method.getParameterTypes();
		_args = args;
		
		PrevalentBubble.idMap().marshal(_args);
	}


	private final ProducerX<Object, ? extends Exception> _targetProducer;
	private final String _method;
	private final Class<?>[] _argsTypes;
	private final Object[] _args;
	
	
	protected Object invoke() throws Exception {
		return invokeOn(target());
	}


	protected Object invokeInsidePrevalence(Object prevalentSystem, Date datetime) throws Exception {
		PrevalentBubble.setPrevalentSystemIfNecessary((IdMap)prevalentSystem);
	
		Long clockState = Clock.memento();
		Clock.setForCurrentThread(datetime.getTime());
		PrevalenceFlag.setInsidePrevalence(true);
		try {
			return invoke();
		} finally {
			PrevalenceFlag.setInsidePrevalence(false);
			Clock.restore(clockState);
		}
	}


	private Object invokeOn(Object target) throws Exception {
		try {
			return methodOn(target).invoke(target, unmarshal(_args));
		} catch (InvocationTargetException e) {
			Throwable throwable = e.getTargetException();
			if (throwable instanceof Error) throw (Error)throwable;
			if (throwable instanceof Exception) throw (Exception)throwable;
			throw new IllegalStateException("Throwable thrown by " + target.getClass() + "." + _method, throwable);
		} catch (RuntimeException e) {
			throw new IllegalStateException("Exception trying to invoke " + target.getClass() + "." + _method, e);
		}
	}


	protected Method methodOn(Object receiver) throws NoSuchMethodException {
		return accessible(receiver.getClass().getMethod(_method, _argsTypes));
	}

	
	protected Object target() throws Exception {
		return _targetProducer.produce();
	}
	
	
	static private Object[] unmarshal(Object[] args) {
		return PrevalentBubble.idMap().unmarshal(args);
	}

	
	protected static Method accessible(Method method) {
		if (method != null)
			method.setAccessible(true);
		return method;
	}

	
	private static final long serialVersionUID = 1L;
}
