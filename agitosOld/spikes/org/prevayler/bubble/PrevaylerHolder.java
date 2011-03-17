package org.prevayler.bubble;

import static sneer.foundation.environments.Environments.my;

import java.io.File;
import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;

import sneer.bricks.hardware.cpu.threads.Threads;
import sneer.bricks.hardware.cpu.threads.latches.Latch;
import sneer.bricks.hardware.cpu.threads.latches.Latches;
import sneer.foundation.lang.Closure;

public class PrevaylerHolder {

	static Prevayler _prevayler;
	static private final Latch _transactionLogReplayed = my(Latches.class).produce();

	static private Object _prevalentSystem;
	static private final Latch _buildingAvailable = my(Latches.class).produce();

	public static void start(final Object initialPrevalentSystem, final File prevalenceBase) {
		my(Threads.class).startDaemon("Prevalent transaction log replay.", new Closure() { @Override public void run() {
			_prevayler = createPrevayler(initialPrevalentSystem, prevalenceBase);
			setPrevalentSystemIfNecessary(_prevayler.prevalentSystem());
			_transactionLogReplayed.open();
		}});
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
	
	static void setPrevalentSystemIfNecessary(Object building) {
		if (_prevalentSystem == null) {
			_prevalentSystem = building;
			_buildingAvailable.open();
		}
		if (building != _prevalentSystem) throw new IllegalStateException();
	}


	public static Object prevalentSystem() {
		_buildingAvailable.waitTillOpen();
		return _prevalentSystem;
	}


	static boolean isReplayingTransactions() {
		return !_transactionLogReplayed.isOpen();
	}


	static void waitForTransactionLogReplayIfNecessary() {
		_transactionLogReplayed.waitTillOpen();
	}

}