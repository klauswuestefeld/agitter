package org.prevayler.bubble;

import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;

import sneer.foundation.lang.ProducerX;
import foundation.Logger;

public class TransactionInvocation extends Invocation implements TransactionWithQuery {

	private static final long serialVersionUID = 1L;

	private TransactionInvocation() {
		super(null, null, null);
		throw new IllegalStateException();
	}

	TransactionInvocation(ProducerX<Object, ? extends Exception> targetProducer, Method method, Object[] args) {
		super(targetProducer, method, args);
	}
	
	
	@Override
	public Object executeAndQuery(Object prevalentSystem, Date executionTimeIgnored) throws Exception {
		PrevalentContext.setPrevalentSystemIfNecessary(prevalentSystem);

		PrevalenceFlag.setInsidePrevalence(true);
		try {
			return produce();
		} finally {
			PrevalenceFlag.setInsidePrevalence(false);
		}
	}

	
	@Override
	public Object produce() throws Exception {
		try {
			return produceAndRegister();
		} catch (RuntimeException rx) {
			if (PrevalentContext.isReplayingTransactions())
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
		if (!PrevalentContext.idMap().requiresRegistration(object)) return;
		if (PrevalentContext.idMap().isRegistered(object)) return;
		PrevalentContext.idMap().register(object);
	}

	
}