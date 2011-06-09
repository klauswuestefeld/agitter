
package infra.classloading;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class CurrentApplicationClassPathClassLoader extends URLClassLoader {
	
	public CurrentApplicationClassPathClassLoader() throws MalformedURLException {
		super(toUrl(getCurrentClasspath()), null);
	}
	
	private static URL[] toUrl(String[] paths) throws MalformedURLException {
		List<URL> ret = new ArrayList<URL>();
		for(String path : paths) {
			ret.add(new File(path).toURI().toURL());
		}
		return ret.toArray(new URL[0]);
	}
	
	private static String[] getCurrentClasspath() {
		return System.getProperty("java.class.path").split(";");
	}

}
