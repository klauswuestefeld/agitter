package agitter.main;

import java.io.File;

import org.prevayler.PrevaylerFactory;
import org.prevayler.bubble.PrevalentBubble;

import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;

public class PrevaylerBootstrap {

	private static Agitter _agitter;

	synchronized
	public static void open(File dataFolder) {
		if (_agitter != null) throw new IllegalStateException("Execution already initilized");
		
		_agitter = PrevalentBubble.wrap(createPrevaylerFactory(dataFolder));
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
	
	
	private static PrevaylerFactory createPrevaylerFactory(File prevalenceBase) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(new AgitterImpl());
		factory.configurePrevalenceDirectory(prevalenceBase.getAbsolutePath());
		factory.configureTransactionFiltering(false);
		return factory;
	}
	

}
