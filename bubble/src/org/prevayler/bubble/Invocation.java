package org.prevayler.bubble;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.ProducerX;

abstract class Invocation implements ProducerX<Object, Exception>, Serializable {

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
	
	
	@Override
	public Object produce() throws Exception {
		Object target = _targetProducer.produce();
		return invokeOn(target);
	}


	protected Object produceInsidePrevalence(Object prevalentSystem, Date datetime) throws Exception {
		PrevalentBubble.setPrevalentSystemIfNecessary((IdMap)prevalentSystem);
	
		Long clockState = Clock.memento();
		Clock.setForCurrentThread(datetime.getTime());
		PrevalenceFlag.setInsidePrevalence(true);
		try {
			return produce();
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


	private Method methodOn(Object receiver) throws NoSuchMethodException {
		Method method = receiver.getClass().getMethod(_method, _argsTypes);
		method.setAccessible(true);
		return method;
	}

	
	static private Object[] unmarshal(Object[] args) {
		return PrevalentBubble.idMap().unmarshal(args);
	}

	
	private static final long serialVersionUID = 1L;
}
