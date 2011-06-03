package infra.processreplacer.tests;

import infra.processreplacer.ProcessReplacer;
import infra.processreplacer.ProcessReplacer.ReplaceableProcess;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;

public class ProcessReplacerTest extends TestWithMocks {

	private final ReplaceableProcess process1 = mock("p1", ReplaceableProcess.class);
	private final ReplaceableProcess process2 = mock("p2", ReplaceableProcess.class);

	@Ignore
	@Test
	public void replacement() {
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToTakeOver();inSequence();
			exactly(1).of(process1).takeOver();inSequence();
		}});
		new ProcessReplacer(process1);
		
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToRetire();inSequence();
			exactly(1).of(process2).prepareToTakeOver();inSequence();
			exactly(1).of(process1).retire();inSequence();
			exactly(1).of(process2).takeOver();inSequence();
		}});
		new ProcessReplacer(process2);
	}
	
}
