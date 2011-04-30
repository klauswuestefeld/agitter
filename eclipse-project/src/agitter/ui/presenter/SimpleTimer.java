package agitter.ui.presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class SimpleTimer {

	private static class Callee {
		private final WeakReference<Object> _owner;
		private final Runnable _runnable;
		
		private Callee(Object owner, Runnable runnable) {
			_owner = new WeakReference<Object>(owner);
			_runnable = runnable;
		}
	}


	private static final List<Callee> _callees = Collections.synchronizedList(new ArrayList<Callee>());
	private static final long DURATION_OF_ROUND = 1000 * 10;

	
	static {
		Thread thread = new Thread("SimpleTimer") {  @Override public void run() {
			while (true) nextRound();
		}};
		thread.setDaemon(true);
		thread.start();
	}
	
	
	static void runNowAndPeriodically(Object owner, Runnable runnable) {
		runnable.run();
		_callees.add(new Callee(owner, runnable));
	}

	
	private static void nextRound() {
		long roundStart = System.currentTimeMillis();
		
		Callee[] round = _callees.toArray(new Callee[0]);
		for (Callee callee : round)
			takeTurn(callee);
		
		sleepTillEndOfRound(roundStart);
	}


	private static void sleepTillEndOfRound(long roundStart) {
		long durationOfCurrentRound = System.currentTimeMillis() - roundStart;
		long millisToSleep = DURATION_OF_ROUND - durationOfCurrentRound;
		if (millisToSleep < 1) return;
		try {
			Thread.sleep(millisToSleep);
		} catch (InterruptedException e) {
			//Don't you love the Java API?
		}
	}


	private static void takeTurn(Callee callee) {
		if (wasGarbageCollected(callee))
			return;
		callee._runnable.run();
	}


	private static boolean wasGarbageCollected(Callee callee) {
		if (callee._owner.get() == null) {
			_callees.remove(callee);
			return true;
		}
		return false;
	}

}
