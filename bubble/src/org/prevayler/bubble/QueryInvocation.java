package org.prevayler.bubble;

import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.Query;

import sneer.foundation.lang.ProducerX;

public class QueryInvocation extends Invocation implements Query { private static final long serialVersionUID = 1L;

	QueryInvocation(ProducerX<Object, ? extends Exception> targetProducer, Method method, Object[] args) {
		super(targetProducer, method, args);
	}

	
	@Override
	public Object query(Object prevalentSystem, Date datetime) throws Exception {
		return super.produceInsidePrevalence(prevalentSystem, datetime);
	}

}
