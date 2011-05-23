package org.prevayler.bubble;

import java.lang.reflect.Method;
import java.util.Date;

import org.prevayler.TransactionWithQuery;

import sneer.foundation.lang.Clock;
import sneer.foundation.lang.Logger;
import sneer.foundation.lang.ProducerX;

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
	public Object executeAndQuery(Object prevalentSystem, Date datetime) throws Exception {
		PrevalentBubble.setPrevalentSystemIfNecessary(prevalentSystem);

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
		registerIfNecessary(result);
		return result;
	}


	private static void registerIfNecessary(Object object) {
		if (!PrevalentBubble.idMap().requiresRegistration(object)) return;
		if (PrevalentBubble.idMap().isRegistered(object)) return;
		PrevalentBubble.idMap().register(object);
	}

	
}
