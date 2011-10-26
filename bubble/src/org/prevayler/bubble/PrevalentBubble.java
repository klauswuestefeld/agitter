package org.prevayler.bubble;

import infra.logging.LogInfra;

import java.io.IOException;

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
	public static <T> T wrap(Object prevalentSystem, PrevaylerFactory factory) throws IOException, ClassNotFoundException {
		if (_session != null) throw new IllegalStateException("Prevalent Bubble session was already active (maybe not cleaned from previous session).");
		
		_session = new PrevalentSession(prevalentSystem, factory);
		_session.start(); //_session has to be set before start() so that the setPrevalentSystemIfNecessary method can be called.
		LogInfra.getLogger(PrevalentBubble.class).info("Wrapping prevalent system");
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
	

	static void setPrevalentSystemIfNecessary(IdMap idMap) {
		_session.setPrevalentSystemIfNecessary(idMap);
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

	public static void takeSnapshot() throws IOException {
		_session._prevayler.takeSnapshot();
	}


}
