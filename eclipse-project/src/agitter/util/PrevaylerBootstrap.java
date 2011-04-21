package agitter.util;

import java.io.File;

import org.prevayler.bubble.PrevalentBubble;

import agitter.Agitter;
import agitter.AgitterImpl;

public class PrevaylerBootstrap {

	private static Agitter _execution;

	synchronized
	public static void open(File dataFolder) {
		if(_execution!=null) { throw new IllegalStateException("Execution already initilized"); }
		_execution = PrevalentBubble.wrap(new AgitterImpl(), dataFolder);
	}

	synchronized
	public static void close() {
		PrevalentBubble.pop();
		_execution = null;
	}

	synchronized
	public static Agitter execution() {
		if(_execution==null) { throw new IllegalStateException("Execution not initilized"); }
		return _execution;
	}
}
