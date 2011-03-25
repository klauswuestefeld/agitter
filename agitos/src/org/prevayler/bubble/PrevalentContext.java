package org.prevayler.bubble;

import java.io.File;

import org.prevayler.Prevayler;

public class PrevalentContext {

	static PrevalentSession _session;
	
	public static void startSession(Object initialPrevalentSystem, File prevalenceBase) {
		closeSession();
		_session = new PrevalentSession(initialPrevalentSystem, prevalenceBase);
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