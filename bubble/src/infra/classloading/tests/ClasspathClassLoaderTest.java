
package infra.classloading.tests;

import static org.junit.Assert.assertFalse;
import infra.classloading.ClasspathClassLoader;

import java.lang.reflect.Field;
import java.net.MalformedURLException;

import org.junit.Test;

public class ClasspathClassLoaderTest {
	
	public static boolean STATIC_FLAG = false;
	

	@Test
	public void isolation() throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, MalformedURLException {
		STATIC_FLAG = true;

		ClasspathClassLoader separateLoader = new ClasspathClassLoader();
		Class<?> separateClass;
		try {
			separateClass = separateLoader.loadClass( ClasspathClassLoaderTest.class.getName() );
		} catch (ClassNotFoundException ignored) {
			return; //Happens when running in the same VM as ANT because the classpath system property is not updated. 
		}
		Field staticFlag = separateClass.getField( "STATIC_FLAG" );
		assertFalse( staticFlag.getBoolean( null ) );
	}

}
