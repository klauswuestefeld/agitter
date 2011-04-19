package org.prevayler.bubble;

import java.io.File;

import org.prevayler.Prevayler;

public class PrevalentBubble {

	static PrevalentSession _session;
	
	public static <T> T wrap(T initialPrevalentSystem, File prevalenceBase) {
		pop();
		_session = new PrevalentSession(initialPrevalentSystem, prevalenceBase);
		return (T) BubbleProxy.wrapped(_session.prevalentSystem(), null);
	}
	
	public static Prevayler prevayler() {
		return _session._prevayler;
	}
	
	/** Bursts the bubble, closing Prevayler. */
	public static void pop() {
		if (_session != null) _session.close();
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