package org.prevayler.bubble;

import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;

import foundation.Logger;

import sneer.foundation.lang.ProducerX;

public class TransactionInvocation extends Invocation implements TransactionWithQuery {

	private static final long serialVersionUID = 1L;


	TransactionInvocation(ProducerX<Object, ? extends Exception> targetProducer, Method method, Object[] args) {
		super(targetProducer, method, args);
	}
	
	
	@Override
	public Object executeAndQuery(Object prevalentSystem, Date executionTimeIgnored) throws Exception {
		PrevaylerHolder.setPrevalentSystemIfNecessary(prevalentSystem);
		return produce();
	}

	
	@Override
	public Object produce() throws Exception {
		try {
			return produceAndRegister();
		} catch (RuntimeException rx) {
			if (PrevaylerHolder.isReplayingTransactions())
				Logger.log(rx, "Exception thrown while replaying prevalent transactions: " + rx.getMessage());
			throw rx;
		}
	}


	private Object produceAndRegister() throws Exception {
		Object result = super.produce();
		registerIfNecessary(result);
		return result;
	}


	private static void registerIfNecessary(Object object) {
		if (!PrevalenceMap.requiresRegistration(object)) return;
		if (PrevalenceMap.isRegistered(object)) return;
		PrevalenceMap.register(object);
	}

	
}
