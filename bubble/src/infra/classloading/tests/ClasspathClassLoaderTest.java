
package infra.classloading.tests;

import static org.junit.Assert.assertFalse;
import infra.classloading.ClasspathClassLoader;

import java.lang.reflect.Field;
import java.net.MalformedURLException;

import org.junit.Test;

public class ClasspathClassLoaderTest {
	
	public static boolean STATIC_FLAG = false;
	

	@Test
	public void testIsolation() throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, MalformedURLException {
		STATIC_FLAG = true;

		ClasspathClassLoader separateLoader = new ClasspathClassLoader();
		Class<?> separateClass = separateLoader.loadClass( ClasspathClassLoaderTest.class.getName() );
		Field staticFlag = separateClass.getField( "STATIC_FLAG" );
		assertFalse( staticFlag.getBoolean( null ) );
	}

}
