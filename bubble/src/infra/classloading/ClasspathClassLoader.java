
package infra.classloading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClasspathClassLoader extends URLClassLoader {
	
	public ClasspathClassLoader() {
		super(toUrl(classpath()), null);
	}
	
	private static URL[] toUrl(String[] paths) {
		List<URL> ret = new ArrayList<URL>();
		for(String path : paths)
			ret.add(toURL(path));
		return ret.toArray(new URL[0]);
	}

	private static URL toURL(String path) {
		try {
			return new File(path).toURI().toURL();
		} catch (MalformedURLException e) {
			throw new IllegalStateException(e);
		}
	}
	
	private static String[] classpath() {
		return System.getProperty("java.class.path").split(";");
	}

}
