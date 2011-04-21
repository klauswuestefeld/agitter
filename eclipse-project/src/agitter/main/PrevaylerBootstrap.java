package agitter.main;

import java.io.File;

import org.prevayler.bubble.PrevalentBubble;

import agitter.Agitter;
import agitter.AgitterImpl;

public class PrevaylerBootstrap {

	private static Agitter _agitter;

	synchronized
	public static void open(File dataFolder) {
		if (_agitter != null) throw new IllegalStateException("Execution already initilized");

		_agitter = PrevalentBubble.wrap(new AgitterImpl(), dataFolder);
	}

	synchronized
	public static void close() {
		PrevalentBubble.pop();
		_agitter = null;
	}

	synchronized
	public static Agitter agitter() {
		if (_agitter == null) throw new IllegalStateException("Execution not initilized");
		return _agitter;
	}
}
