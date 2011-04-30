package agitter.ui.presenter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


class SimpleTimer {

	interface HandleToAvoidLeaks {}

	private static final List<WeakReference<Runnable>> _sleepers = Collections.synchronizedList(new ArrayList<WeakReference<Runnable>>());
	static final int MILLIS_TO_SLEEP_BETWEEN_ROUNDS = 1000 * 10;

	
	static {
		Thread thread = new Thread("SimpleTimer") {  @Override public void run() {
			while (true) wakeUpNextRound();
		}};
		thread.setDaemon(true);
		thread.start();
	}
	
	
	static HandleToAvoidLeaks runNowAndPeriodically(final Runnable runnable) {
		runnable.run();
		
		_sleepers.add(new WeakReference<Runnable>(runnable));
		return new HandleToAvoidLeaks() { @Override protected void finalize() throws Throwable {
			_sleepers.remove(runnable);
		}};
	}

	
	private static void wakeUpNextRound() {
		int removeThisGcWhenVaadinSessionLeakMysteryIsSolved;
		System.gc();
		
		@SuppressWarnings("unchecked")
		WeakReference<Runnable>[] round = _sleepers.toArray(new WeakReference[0]);
		for (WeakReference<Runnable> sleeper : round)
			wakeUp(sleeper.get());
		sleep();
	}


	private static void sleep() {
		try {
			Thread.sleep(MILLIS_TO_SLEEP_BETWEEN_ROUNDS);
		} catch (InterruptedException e) {
			//Don't you love the Java API?
		}
	}


	private static void wakeUp(Runnable sleeper) {
		if (wasGarbageCollected(sleeper))
			return;
		sleeper.run();
	}


	private static boolean wasGarbageCollected(Object reference) {
		return reference == null;
	}

}
