package org.prevayler.bubble;

import infra.logging.LogInfra;

import java.io.IOException;
import java.util.Date;

import org.prevayler.Clock;
import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.foundation.util.concurrent.Latch;

class PrevalentSession {

	private final PrevaylerFactory _factory;

	Prevayler _prevayler;
	private final Latch _transactionLogReplayed = new Latch();

	private Object _prevalentSystem;
	IdMap _idMap;

	
	PrevalentSession(Object prevalentSystem, PrevaylerFactory factory) {
		_factory = factory;
		_factory.configurePrevalentSystem(new IdMap(prevalentSystem));
	}

	void start() throws IOException, ClassNotFoundException {
		log("Prevayler init");
		initPrevayler();
		log("Getting idMap");
		IdMap idMap = (IdMap)_prevayler.prevalentSystem();
		log("Setting prevalent system");
		setPrevalentSystemIfNecessary(idMap);
		log("Prevalent system set");
		_transactionLogReplayed.open();
	}

	private void log(String msg) {
		LogInfra.getLogger(this).info(msg);
	}
	
	private void initPrevayler() throws IOException, ClassNotFoundException {
		_factory.configureClock(new Clock() { @Override public Date time() {
			return new Date(sneer.foundation.lang.Clock.currentTimeMillis());
		}});

		_prevayler = _factory.create();
	}
		
	void setPrevalentSystemIfNecessary(IdMap idMap) {
		if (_idMap != null) return;
		log("1");
		if (idMap == null) throw new IllegalArgumentException();
		log("2");

		_idMap = idMap;
		Object sys = _idMap.unmarshal(1);
		log("3");
		if (sys == null) throw new IllegalStateException("prevalentSystem should not be null.");
		log("4");
		_prevalentSystem = sys;
	}


	public Object prevalentSystem() {
		waitForTransactionLogReplay();
		if(_prevalentSystem == null) throw new IllegalStateException("prevalentSystem should not be null");
		return _prevalentSystem;
	}

	public Prevayler prevayler() {
		return _prevayler;
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