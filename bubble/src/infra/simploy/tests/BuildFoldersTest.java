package infra.simploy.tests;

import infra.simploy.BuildFolders;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import sneer.foundation.lang.Clock;
import sneer.foundation.testsupport.CleanTestBase;

public class BuildFoldersTest extends CleanTestBase {

	private final BuildFolders subject = new BuildFolders(tmpFolder());

	
	@Test
	public void noBuildFolders() {
		assertNull(subject.lastSuccessfulBuildFolder());
	}
	
	
	@Test
	public void buildFolderCreation() throws Exception {
		File build = subject.createNewBuildFolder();
		assertEquals(tmpFolder(), build.getParentFile());
		assertTrue(build.isDirectory());
	}

	
	@Test
	public void buildFolderName() throws Exception {
		setClock("2011-12-31_23-59-59");
		File build = subject.createNewBuildFolder();
		assertEquals("build-2011-12-31_23-59-59", build.getName());
	}

	
	@Test
	public void lastSuccessfulBuildFolderInAlfabeticalOrder() throws Exception {
		File build3 = createBuild(30000);
		File build1 = createBuild(10000);
		createBuild(20000);
		
		assertNull(subject.lastSuccessfulBuildFolder());
		
		subject.markAsSuccessful(build1);
		assertEquals(build1, subject.lastSuccessfulBuildFolder());
		
		subject.markAsSuccessful(build3);
		assertEquals(build3, subject.lastSuccessfulBuildFolder());
	}


	private File createBuild(int stamp) throws IOException {
		Clock.setForCurrentThread(stamp);
		return subject.createNewBuildFolder();
	}
	
	
	private void setClock(String stamp) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(stamp);
		Clock.setForCurrentThread(date.getTime());
	}
	
}
