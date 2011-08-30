package infra.simploy.tests;

import infra.simploy.Simploy;

import java.io.File;

import org.junit.Ignore;
import org.junit.Test;

import sneer.foundation.testsupport.TestWithMocks;

public class SimployTest extends TestWithMocks {

	private static final File ROOT = new File("builds");

	private final Simploy.Worker worker = mock(Simploy.Worker.class);

	
	@Test
	public void firstTimeSuccessful() throws Exception {
		firstTimeSuccessfulExpectations();
		initSubject();
	}

	
	@Test @Ignore
	public void secondTimeSuccessful() throws Exception {
		firstTimeSuccessfulExpectations();

		checking(new Expectations() {{
			exactly(1).of(worker).lastSuccessfulBuildFolder(ROOT); inSequence();
				will(returnValue(null));
			File build1 = new File("builds/build1");
			exactly(1).of(worker).createNewBuildFolder(ROOT); inSequence();
				will(returnValue(build1));
			exactly(1).of(worker).exec("ant build " + build1.getAbsolutePath()); inSequence();
			exactly(1).of(worker).runAllTestsIn(build1); inSequence();
			exactly(1).of(worker).execIn("ant run -Dlast-good-build=no-good-build", build1); inSequence();
			exactly(1).of(worker).waitForResultFileIn(build1); inSequence();
		}});

		initSubject();
	}


	private void firstTimeSuccessfulExpectations() throws Exception {
		checking(new Expectations() {{
			exactly(1).of(worker).lastSuccessfulBuildFolder(ROOT); inSequence();
				will(returnValue(null));
			File build1 = new File("builds/build1");
			exactly(1).of(worker).createNewBuildFolder(ROOT); inSequence();
				will(returnValue(build1));
			exactly(1).of(worker).exec("ant build " + build1.getAbsolutePath()); inSequence();
			exactly(1).of(worker).runAllTestsIn(build1); inSequence();
			exactly(1).of(worker).execIn("ant run -Dlast-good-build=no-good-build", build1); inSequence();
			exactly(1).of(worker).waitForResultFileIn(build1); inSequence();
			
			exactly(1).of(worker).waitForNextBuildEvent();
		}});
	}
	
	
	private void initSubject() {
		new Simploy(ROOT, worker);
	}
		
}
