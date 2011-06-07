package infra.processreplacer.tests;

import infra.processreplacer.ProcessReplacer;
import infra.processreplacer.ProcessReplacer.ReplaceableProcess;

import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;
import sneer.foundation.util.concurrent.Latch;

public class ProcessReplacerTest extends TestWithMocks {

	{
		System.setProperty("ProcessReplacerPort", "44444");
	}
	
	private final ReplaceableProcess process1 = mock("p1", ReplaceableProcess.class);
	private final ReplaceableProcess process2 = mock("p2", ReplaceableProcess.class);

	@Test (timeout = 2000)
	public void replacement() throws Exception {
		checkProcess1TakingOver();
		
		final Latch latch = new Latch();
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToRetire(); inSequence();
			exactly(1).of(process2).prepareToTakeOver(); inSequence();
			exactly(1).of(process1).retire(); open(latch);
			exactly(1).of(process2).takeOver(); inSequence();
		}});
		ProcessReplacer pr = new ProcessReplacer(process2);
		latch.waitTillOpen();
		pr.close();
	}


	@Test (timeout = 2000)
	public void canceledReplacement() throws Exception {
		ProcessReplacer pr = checkProcess1TakingOver();
		
		final Latch latch = new Latch();
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToRetire(); inSequence();
			exactly(1).of(process2).prepareToTakeOver(); inSequence();
				will(throwException(new RuntimeException()));
			exactly(1).of(process1).cancelRetirement(); open(latch);
			exactly(1).of(process2).retire();inSequence();
		}});
		new ProcessReplacer(process2);
		latch.waitTillOpen();
		pr.close();
	}

	
	private ProcessReplacer checkProcess1TakingOver() throws Exception {
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToTakeOver();inSequence();
			exactly(1).of(process1).takeOver();inSequence();
		}});
		return new ProcessReplacer(process1);
	}

}
