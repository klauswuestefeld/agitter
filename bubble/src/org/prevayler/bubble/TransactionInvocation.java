package org.prevayler.bubble;

import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;

import sneer.foundation.lang.Logger;
import sneer.foundation.lang.ProducerX;

public class TransactionInvocation extends Invocation implements TransactionWithQuery {

	private static final long serialVersionUID = 1L;


	TransactionInvocation(ProducerX<Object, ? extends Exception> targetProducer, Method method, Object[] args) {
		super(targetProducer, method, args);
	}
	
	
	@Override
	public Object executeAndQuery(Object prevalentSystem, Date datetime) throws Exception {
		return produceInsidePrevalence(prevalentSystem, datetime);
	}


	@Override
	public Object produce() throws Exception {
		try {
			return produceAndRegister();
		} catch (RuntimeException rx) {
			if (PrevalentBubble.isReplayingTransactions())
				Logger.log(rx, "Exception thrown while replaying prevalent transactions: " + rx.getMessage());
			throw rx;
		}
	}


	private Object produceAndRegister() throws Exception {
		Object result = super.produce();
		PrevalentBubble.idMap().registerIfNecessary(result);
		return result;
	}

}
