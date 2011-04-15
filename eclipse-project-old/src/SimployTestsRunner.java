import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


class SimployTestsRunner {


	public static void runAllTestsIn(String testsPath, String libJarsFolder) throws Exception {
		runInSeparateClassLoader(testsPath, libJarsFolder);
	}


	private static void runInSeparateClassLoader(String testsPath, String libJarsFolder) throws Exception {
		URLClassLoader separateClassLoader = separateClassLoader(libJarsFolder);
		Class<?> runClass = separateClassLoader.loadClass(SimployTestsRun.class.getName());
		instantiate(runClass, testsPath);
	}


	static private void instantiate(Class<?> separateClass, String testsPath) throws Exception {
		Constructor<?> ctor = separateClass.getConstructor(String.class);
		ctor.setAccessible(true);
		try {
			ctor.newInstance(testsPath);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof Exception) throw (Exception) e.getCause();
			throw e;
		}
	}

	
	static private URLClassLoader separateClassLoader(String libJarsFolder) throws Exception {
		ClassLoader noParent = null;
		return new URLClassLoader(classpath(libJarsFolder), noParent);
	}
	
	
	private static URL[] classpath(String libJarsFolder) throws Exception {
		List<String> jarPaths = SimployFileUtils.fileNamesEndingWith(new File(libJarsFolder), ".jar");
		printClasspath(jarPaths);
		List<URL> result = convertToURLs(jarPaths);
		result.add(0, myOwnPath());
		return result.toArray(new URL[result.size()]);
	}


	private static void printClasspath(List<String> jarPaths) {
		System.out.println("Running tests using jars:");
		for (String path : jarPaths)
			System.out.println("   " + path);
		System.out.println();
	}


	private static URL myOwnPath() {
		return SimployTestsRunner.class.getResource("");
	}

	
	private static List<URL> convertToURLs(List<String> paths) throws Exception {
		List<URL> result = new ArrayList<URL>();
		for (String path : paths)
			result.add(new File(path).toURI().toURL());
		return result;
	}



}