package agitter.main;

import infra.classloading.CurrentApplicationClassPathClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

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
		
		consolidateSnapshotInvokeUsingReflectionWithAnIsolatedClassloader( username );
	}

	private static void consolidateSnapshotInvokeUsingReflectionWithAnIsolatedClassloader(final String username) {
		try {
			final Method consolidateSnapshotIsolatedMethod = getConsolidateSnapshotIsolatedMethod();
			consolidateSnapshotIsolatedMethod.setAccessible( true );
			consolidateSnapshotIsolatedMethod.invoke( null, _dataFolder, username );
		}catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static Method getConsolidateSnapshotIsolatedMethod() throws ClassNotFoundException, MalformedURLException {
		final CurrentApplicationClassPathClassLoader isolatedClassloader = new CurrentApplicationClassPathClassLoader();
		final Class<?> isolatedPrevaylerBootstrapClass = isolatedClassloader.loadClass(PrevaylerBootstrap.class.getName());
		for(Method method : isolatedPrevaylerBootstrapClass.getDeclaredMethods()) {
			if("consolidateSnapshotIsolated".equals(method.getName())) {
				return method;
			}
		}
		throw new IllegalStateException( "Isolated method not found" );
	}
	
	@SuppressWarnings("unused")
	private static void consolidateSnapshotIsolated(final File dataFolder, final String username) throws IOException, ClassNotFoundException {
		open(dataFolder);
		try {
			_agitter.users().loginWithUsername(username, "abc123");
		} catch (Refusal e) {
			throw new IllegalStateException(e);
		}
		PrevalentBubble.prevayler().takeSnapshot();
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
