package infra.simploy;
import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;


public class SimployTestsRun {

	private static final int DOT_CLASS = ".class".length();

	private final String testsFolderPath;
	private Throwable firstTestFailure;

	
	public SimployTestsRun(String testsFolder) {
		testsFolderPath = testsFolder;
		
		JUnitCore junit = new JUnitCore();
		junit.addListener(failureListener());
		
		junit.run(findTestClasses());
		
		if (firstTestFailure != null)
			throw new RuntimeException("Some tests failed. This is the first failure.", firstTestFailure);
	}

	
	private RunListener failureListener() {
		return new RunListener() {
			
			@Override
			public void testStarted(Description description) throws Exception {
				System.out.println(description);
			}

			@Override
			public void testFailure(Failure failure) {
				if (firstTestFailure == null) firstTestFailure = failure.getException();
				System.out.println("FAILED\n");
				System.out.println(failure.getTrace());
			}
		};
	}

	private Class<?>[] findTestClasses() {
		try {
			return tryToFindTestClasses();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	private Class<?>[] tryToFindTestClasses() throws Exception {
		List<String> testFiles = SimployFileUtils.fileNamesEndingWith(testsFolder(), "Test.class");
		List<Class<?>> result = convertToClasses(testFiles);
		if (result.size() == 0) throw new Exception("No test classes found in: " + testsFolder());
		return result.toArray(new Class[0]);
	}

	
	private File testsFolder() throws Exception {
		File result = new File(testsFolderPath);
		if (!result.exists() || !result.isDirectory()) throw new Exception("Tests root folder not found or not a folder: " + result);
		return result;
	}

	
	private List<Class<?>> convertToClasses(final List<String> classFilePaths) throws Exception {
		List<Class<?>> result = new ArrayList<Class<?>>();

		String rootPath = testsFolder().getAbsolutePath();

		for (String filePath : classFilePaths) {
			String className = className(rootPath, filePath);
			
			Class<?> c = getClass().getClassLoader().loadClass(className);
			System.out.println(c.getResource(c.getSimpleName() + ".class").getPath());
			
			if (!Modifier.isAbstract(c.getModifiers()))
				result.add(c);
		}
		return result;
	}

	
	static private String className(String classpathRoot, String classFilePath) {
		if (!classFilePath.startsWith(classpathRoot)) throw new IllegalStateException("Class file: " + classFilePath + " should be inside subfolder of: " + classpathRoot);
		int afterRoot = classpathRoot.length() + 1;
		int beforeDotClass = classFilePath.length() - DOT_CLASS;
		return classFilePath.substring(afterRoot, beforeDotClass).replace('/', '.').replace('\\', '.');
	}


}