package org.prevayler.bubble;

import org.prevayler.PrevaylerFactory;

public class PrevalentBubble {

	static PrevalentSession _session;
	
	
	synchronized
	public static <T> T wrap(PrevaylerFactory factory) {
		if (_session != null) throw new IllegalStateException();
		_session = new PrevalentSession(factory);
		_session.start(); //_session has to be set before start() so that the setPrevalentSystemIfNecessary method can be called.
		return (T) BubbleProxy.wrapped(_session.prevalentSystem(), null);
	}

	
	
	/** Bursts the bubble, closing Prevayler. */
	synchronized
	public static void pop() {
		if (_session == null) throw new IllegalStateException();
		_session.close();
		_session = null;
	}
	
	public static IdMap idMap() {
		return _session._idMap;
	}
	
	static void setPrevalentSystemIfNecessary(Object system) {
		_session.setPrevalentSystemIfNecessary(system);
	}


	static boolean isReplayingTransactions() {
		return _session.isReplayingTransactions();
	}


	static void waitForTransactionLogReplay() {
		_session.waitForTransactionLogReplay();
	}

	public static Object execute(TransactionInvocation transaction) throws Exception {
		return _session._prevayler.execute( transaction );
	}

}