package org.prevayler.bubble;

import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.Query;

import sneer.foundation.lang.ProducerX;

public class QueryInvocation extends Invocation implements Query, ProducerX<Object, Exception> { private static final long serialVersionUID = 1L;

	QueryInvocation(Object target, ProducerX<Object, ? extends Exception> targetProducer, Method method, Object[] args) {
		super(targetProducer, method, args);
		_targetCache = target;
		_methodCache = accessible(method);
	}

	private transient final Object _targetCache;
	private transient final Method _methodCache;

	
	@Override
	public Object query(Object prevalentSystem, Date datetime) throws Exception {
		return super.invokeInsidePrevalence(prevalentSystem, datetime);
	}


	@Override
	public Object produce() throws Exception {
		return super.invoke();
	}


	@Override
	protected Method methodOn(Object receiver) throws NoSuchMethodException {
		return _methodCache != null
			? _methodCache
			: super.methodOn(receiver);
	}


	@Override
	protected Object target() throws Exception {
		return _targetCache != null
			? _targetCache
			: super.target();
	}
	
	
}
