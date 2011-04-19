package agitter;

import java.io.File;

import org.prevayler.Clock;
import org.prevayler.bubble.PrevalentBubble;

import agitter.util.AgitterClock;
import agitter.util.PrevaylerClockToAgitterClockAdapter;

public class AgitterSystem {

	private static AgitterExecution _execution;

	synchronized
	public static void open(File dataFolder) {
		if(_execution!=null) { throw new IllegalStateException("Execution already initilized"); }
		_execution = new AgitterExecution();
		PrevalentBubble.wrap(_execution, dataFolder);
		final Clock prevaylerClock = PrevalentBubble.prevayler().clock();
		final AgitterClock agitterClock = new PrevaylerClockToAgitterClockAdapter( prevaylerClock );
		execution().initializeHomes( agitterClock );
	}

	synchronized
	public static void close() {
		PrevalentBubble.pop();
		_execution = null;
	}

	synchronized
	public static AgitterExecution execution() {
		if(_execution==null) { throw new IllegalStateException("Execution not initilized"); }
		return (AgitterExecution) PrevalentBubble.prevayler().prevalentSystem();
	}
}
