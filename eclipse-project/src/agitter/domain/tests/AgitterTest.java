package agitter.domain.tests;

import org.junit.Test;

import sneer.foundation.testsupport.CleanTestBase;
import agitter.domain.Agitter;
import agitter.domain.AgitterImpl;
import agitter.domain.AgitterSession;

public class AgitterTest extends CleanTestBase {

	private final Agitter _subject = new AgitterImpl();

	
	@Test
	public void signup() {
		AgitterSession session = _subject.signup("Ana Almeida", "ana@gmail.com", "ana123");
		assertEquals("Ana Almeida", session.userName());
	}

}
