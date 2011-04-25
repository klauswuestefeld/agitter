package org.prevayler.bubble;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

public class PrevalentBubble {

	static PrevalentSession _session;
	
	public static <T> T wrap(PrevaylerFactory factory) {
		if (_session != null) throw new IllegalStateException();
		_session = new PrevalentSession(factory);
		return (T) BubbleProxy.wrapped(_session.prevalentSystem(), null);
	}
	
	public static Prevayler prevayler() {
		return _session._prevayler;
	}
	
	/** Bursts the bubble, closing Prevayler. */
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

}