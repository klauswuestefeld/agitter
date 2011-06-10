package agitter.main;

import infra.classloading.ClasspathClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;

import org.prevayler.PrevaylerFactory;
import org.prevayler.bubble.PrevalentBubble;
import org.prevayler.foundation.serialization.XStreamSerializer;

import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;

public class PrevaylerBootstrap {

	private static Agitter _agitter;
	private static File _dataFolder;

	
	synchronized
	public static void open(File dataFolder) throws IOException, ClassNotFoundException {
		if (_agitter != null) throw new IllegalStateException("Execution already initilized");
		
		_dataFolder = dataFolder;
		_agitter = PrevalentBubble.wrap(new AgitterImpl(), createPrevaylerFactory(dataFolder));
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
		consolidateSnapshotInIsolatedClassLoaderBecauseBubbleIsStatic();
	}

	
	private static void consolidateSnapshotInIsolatedClassLoaderBecauseBubbleIsStatic() {
		try {
			Method method = isolatedMethod("doConsolidateSnapshot");
			method.setAccessible( true );
			method.invoke( null, _dataFolder);
		} catch(Exception e) {
			throw new IllegalStateException(e);
		}
	}
	
	
	private static Method isolatedMethod(String methodName) throws ClassNotFoundException, MalformedURLException {
		ClasspathClassLoader isolatedLoader = new ClasspathClassLoader();
		Class<?> isolatedClass = isolatedLoader.loadClass(PrevaylerBootstrap.class.getName());
		
		for (Method method : isolatedClass.getDeclaredMethods())
			if (methodName.equals(method.getName()))
				return method;
		
		throw new IllegalStateException( "Method " + methodName + " not found" );
	}
	
	
	@SuppressWarnings("unused") //Called with reflection in isolated classloader.
	private static void doConsolidateSnapshot(final File dataFolder) throws IOException, ClassNotFoundException {
		open(dataFolder);
		PrevalentBubble.takeSnapshot();
	}
	
	
	private static PrevaylerFactory createPrevaylerFactory(File prevalenceBase) {
		PrevaylerFactory factory = new PrevaylerFactory();
		factory.configurePrevalenceDirectory(prevalenceBase.getAbsolutePath());
		factory.configureTransactionFiltering(false);
//		factory.configureJournalSerializer(new XStreamSerializer("UTF-8"));
		factory.configureSnapshotSerializer(new XStreamSerializer("UTF-8"));
		return factory;
	}

}
