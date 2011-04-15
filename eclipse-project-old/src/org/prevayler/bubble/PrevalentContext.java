package org.prevayler.bubble;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

public class PrevalentContext {

	static PrevalentSession _session;
	
	public static void startSession(PrevaylerFactory factory) {
		closeSession();
		_session = new PrevalentSession(factory);
	}
	
	static void setPrevalentSystemIfNecessary(Object system) {
		_session.setPrevalentSystemIfNecessary(system);
	}


	public static Object prevalentSystem() {
		return _session.prevalentSystem();
	}


	static boolean isReplayingTransactions() {
		return _session.isReplayingTransactions();
	}


	static void waitForTransactionLogReplay() {
		_session.waitForTransactionLogReplay();
	}

	public static Prevayler prevayler() {
		return _session._prevayler;
	}

	public static void closeSession() {
		if (_session != null) _session.close();
	}

	public static IdMap idMap() {
		return _session._idMap;
	}

}