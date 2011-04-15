package org.prevayler.bubble;

import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.foundation.util.concurrent.Latch;

class PrevalentSession {

	Prevayler _prevayler;
	private final Latch _transactionLogReplayed = new Latch();

	private Object _prevalentSystem;
	private final Latch _prevalentSystemAvailable = new Latch();
	
	IdMap _idMap = new IdMap();

	PrevalentSession(final PrevaylerFactory factory) {

			try { 
				_prevayler = factory.create();
			} catch (IOException e) {
				// Depois eu trago a Bubble limpa p ca...
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setPrevalentSystemIfNecessary(_prevayler.prevalentSystem());
			_transactionLogReplayed.open();
	}

	void setPrevalentSystemIfNecessary(Object system) {
		if (system == null) throw new IllegalArgumentException();
		
		if (_prevalentSystem == null) {
			_idMap.registerFirstObject(system);
			
			_prevalentSystem = system;
			_prevalentSystemAvailable.open();
		}
		if (system != _prevalentSystem) throw new IllegalStateException();
	}


	public Object prevalentSystem() {
		waitForTransactionLogReplay();
		return _prevalentSystem;
	}


	boolean isReplayingTransactions() {
		return !_transactionLogReplayed.isOpen();
	}


	void waitForTransactionLogReplay() {
		_transactionLogReplayed.waitTillOpen();
	}

	public void close() {
		try {
			_prevayler.close();
			_prevalentSystem = null;
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

}