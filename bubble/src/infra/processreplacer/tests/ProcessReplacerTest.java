package infra.processreplacer.tests;

import infra.processreplacer.ProcessReplacer;
import infra.processreplacer.ProcessReplacer.ReplaceableProcess;

import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;
import sneer.foundation.util.concurrent.Latch;

public class ProcessReplacerTest extends TestWithMocks {

	private static final int PORT = randomizePortToAvoidClashesWithOtherVMsRunningTests();

	private final ReplaceableProcess process1 = mock("p1", ReplaceableProcess.class);
	private final ReplaceableProcess process2 = mock("p2", ReplaceableProcess.class);

	
	@Test (timeout = 2000)
	public void replacement() throws Exception {
		checkProcess1TakingOver();
		
		final Latch latch = new Latch();
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToRetire(); inSequence();
			exactly(1).of(process2).prepareToRun(); inSequence();
			exactly(1).of(process1).retire(); willOpen(latch);
			exactly(1).of(process2).run(); inSequence();
		}});
		ProcessReplacer pr = new ProcessReplacer(process2, PORT);
		latch.waitTillOpen();
		pr.close();
	}


	@Test (timeout = 2000)
	public void canceledReplacement() throws Exception {
		ProcessReplacer pr = checkProcess1TakingOver();
		
		final Latch latch = new Latch();
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToRetire(); inSequence();
			exactly(1).of(process2).prepareToRun(); inSequence();
				will(throwException(new Exception()));
			exactly(1).of(process1).cancelRetirement(); willOpen(latch);
			exactly(1).of(process2).forgetAboutRunning();inSequence();
		}});
		new ProcessReplacer(process2, PORT);
		latch.waitTillOpen();
		pr.close();
	}

	
	private ProcessReplacer checkProcess1TakingOver() throws Exception {
		checking(new Expectations(){{
			exactly(1).of(process1).prepareToRun();inSequence();
			exactly(1).of(process1).run();inSequence();
		}});
		return new ProcessReplacer(process1, PORT);
	}
	
	
	private static int randomizePortToAvoidClashesWithOtherVMsRunningTests() {
		return 10000 + (int)(System.nanoTime() % 40000);
	}

}
