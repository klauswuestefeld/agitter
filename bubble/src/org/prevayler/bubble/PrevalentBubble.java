package org.prevayler.bubble;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

public class PrevalentBubble {

	static PrevalentSession _session;
	
	static String readOnlyMessage;
	
	synchronized
	public static void enterReadOnlyMode(String readOnlyMessage_) {
		readOnlyMessage = readOnlyMessage_;
	}
	
	synchronized
	public static void leaveReadOnlyMode() {
		readOnlyMessage = null;
	}

	synchronized
	public static <T> T wrap(PrevaylerFactory factory) throws IOException, ClassNotFoundException {
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
	
	public static Prevayler prevayler() {
		return _session.prevayler();
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

	synchronized
	static Object execute(TransactionInvocation transaction) throws Exception {
		if( isReadOnlyMode() ) {
			throw new RuntimeException(readOnlyMessage);
		}
		return _session._prevayler.execute( transaction );
	}

	private static boolean isReadOnlyMode() {
		return readOnlyMessage != null;
	}


}
