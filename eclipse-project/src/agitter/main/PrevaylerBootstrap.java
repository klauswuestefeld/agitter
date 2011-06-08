package agitter.main;

import java.io.File;
import java.io.IOException;

import org.prevayler.Prevayler;
import org.prevayler.PrevaylerFactory;
import org.prevayler.bubble.PrevalentBubble;
import org.prevayler.foundation.serialization.XStreamSerializer;

import sneer.foundation.lang.exceptions.Refusal;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;

public class PrevaylerBootstrap {

	private static Agitter _agitter;
	private static File _dataFolder;

	synchronized
	public static void open(File dataFolder) throws IOException, ClassNotFoundException {
		if (_agitter != null) throw new IllegalStateException("Execution already initilized");
		
		_dataFolder = dataFolder;
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
	
	
	static void consolidateSnapshot() throws IOException, ClassNotFoundException {
		String username = "Fulano" + System.currentTimeMillis();
		try {
			agitter().users().signup(username, username + "@gmail.com", "abc123");
			agitter().users().loginWithUsername(username, "abc123");
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		
		Prevayler prevayler = createPrevaylerFactory(_dataFolder).create();
		try {
			((Agitter)prevayler.prevalentSystem()).users().loginWithUsername(username, "abc123");
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		
		prevayler.takeSnapshot();
	}
	
	
	private static PrevaylerFactory createPrevaylerFactory(File prevalenceBase) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalentSystem(new AgitterImpl());
		factory.configurePrevalenceDirectory(prevalenceBase.getAbsolutePath());
		factory.configureTransactionFiltering(false);
//		factory.configureJournalSerializer(new XStreamSerializer()); //This line can be used once the snapshot-based process replacement is working.
		factory.configureSnapshotSerializer(new XStreamSerializer());
		return factory;
	}

}
