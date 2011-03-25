package org.prevayler.bubble;

import java.io.File;
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

	PrevalentSession(final Object initialPrevalentSystem, final File prevalenceBase) {
		Thread thread = new Thread(new Runnable() { @Override public void run() {

			_prevayler = createPrevayler(initialPrevalentSystem, prevalenceBase);
			setPrevalentSystemIfNecessary(_prevayler.prevalentSystem());
			_transactionLogReplayed.open();

		}}, "Prevalent transaction log replay.");
		thread.setDaemon(true);
		thread.start();
	}

	private static Prevayler createPrevayler(Object prevalentSystem, final File prevalenceBase) {
		final PrevaylerFactory factory = createPrevaylerFactory(prevalentSystem, prevalenceBase);

		try {
			return factory.create();
		} catch (IOException e) {
			throw new foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new foundation.lang.exceptions.NotImplementedYet(e); // Fix Handle this exception.
		}
	}
		
	private static PrevaylerFactory createPrevaylerFactory(Object system, File prevalenceBase) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(system);
		factory.configurePrevalenceDirectory(prevalenceBase.getAbsolutePath());
		factory.configureTransactionFiltering(false);
		return factory;
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