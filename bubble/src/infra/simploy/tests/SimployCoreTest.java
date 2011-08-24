package infra.simploy.tests;

import infra.simploy.CommandRunner;
import infra.simploy.SimployCore;

import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;

public class SimployCoreTest extends TestWithMocks {

	private final CommandRunner runner = mock(CommandRunner.class);
	@SuppressWarnings("unused")
	private SimployCore subject;

	
	@Test
	public void firstTime() {
		checking(new Expectations(){{
			exactly(1).of(runner).exec(tmpFolder(), "ant build"); inSequence();
		}});
		initSubject();
	}
	
	
	private void initSubject() {
		subject =  new SimployCore(tmpFolder(), runner);
	}
		
}
