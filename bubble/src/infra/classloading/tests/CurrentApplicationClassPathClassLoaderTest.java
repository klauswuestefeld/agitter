
package infra.classloading.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import infra.classloading.CurrentApplicationClassPathClassLoader;

import java.lang.reflect.Field;
import java.net.MalformedURLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CurrentApplicationClassPathClassLoaderTest {
	
	public static boolean RUNNING_INSIDE_JUNIT_FLAG = false;
	
	@BeforeClass
	public static void markClassIsLoadedByJunit() {
		RUNNING_INSIDE_JUNIT_FLAG = true;
	}
	
	@AfterClass
	public static void ummarkClassIsLoadedByJunit() {
		RUNNING_INSIDE_JUNIT_FLAG = false;
	}

	@Test
	public void testIsolation() throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, MalformedURLException {
		assertTrue( RUNNING_INSIDE_JUNIT_FLAG );
		final CurrentApplicationClassPathClassLoader runner = new CurrentApplicationClassPathClassLoader();
		
		Class<?> isolatedCloassloaderRunnerTest = runner.loadClass( CurrentApplicationClassPathClassLoaderTest.class.getName() );
		assertNotNull( isolatedCloassloaderRunnerTest );
		
		Field staticFlag = isolatedCloassloaderRunnerTest.getField( "RUNNING_INSIDE_JUNIT_FLAG" );
		assertNotNull( staticFlag );
		
		staticFlag.setAccessible( true );
		assertFalse( staticFlag.getBoolean( null ) );
	}

}
