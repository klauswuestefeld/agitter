package org.prevayler.bubble;

import java.io.IOException;
import java.util.Date;

import org.prevayler.Clock;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import com.sun.org.apache.xpath.internal.FoundIndex;

import sneer.foundation.lang.exceptions.NotImplementedYet;
import sneer.foundation.util.concurrent.Latch;

class PrevalentSession {

	Prevayler _prevayler;
	private final Latch _transactionLogReplayed = new Latch();

	private Object _prevalentSystem;
	private final Latch _prevalentSystemAvailable = new Latch();
	
	IdMap _idMap = new IdMap();

	PrevalentSession(final PrevaylerFactory factory) {
		Thread thread = new Thread(new Runnable() { @Override public void run() {

			_prevayler = createPrevayler(factory);
			setPrevalentSystemIfNecessary(_prevayler.prevalentSystem());
			_transactionLogReplayed.open();

		}}, "Prevalent transaction log replay.");
		thread.setDaemon(true);
		thread.start();
	}

	private static Prevayler createPrevayler(PrevaylerFactory factory) {
		factory.configureClock(new Clock() { @Override public Date time() {
			return new Date(sneer.foundation.lang.Clock.currentTimeMillis());
		}});

		try {
			return factory.create();
		} catch (IOException e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		} catch (ClassNotFoundException e) {
			throw new NotImplementedYet(e); // Fix Handle this exception.
		}
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