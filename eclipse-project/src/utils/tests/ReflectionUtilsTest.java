package utils.tests;

import org.junit.Test;

import utils.ReflectionUtils;
import agitter.domain.events.tests.EventsTestBase;

class SomeRandomClass {
	private static final String TESTE = "teste";
	static public String print() {
		return TESTE;
	}
}

public class ReflectionUtilsTest extends EventsTestBase {
	@Test
	public void testChangeStaticFinalField() throws SecurityException, NoSuchFieldException, Exception {
		ReflectionUtils.setFinalStatic(SomeRandomClass.class, "TESTE", "Vitor");
		assertEquals("Vitor", SomeRandomClass.print());
	}
}
