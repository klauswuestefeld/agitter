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

	@Test
	public void noBuildFolders() throws IOException {
		assertNull(BuildFolders.findLastSuccessfulBuildFolderIn(tmpFolder()));
	}
	
	
	@Test
	public void buildFolderCreation() throws Exception {
		File build = BuildFolders.createNewBuildFolderIn(tmpFolder());
		assertEquals(tmpFolder(), build.getParentFile());
		assertTrue(build.isDirectory());
	}

	
	@Test
	public void buildFolderName() throws Exception {
		setClock("2011-12-31_23-59-59");
		File build = BuildFolders.createNewBuildFolderIn(tmpFolder());
		assertEquals("build-2011-12-31_23-59-59", build.getName());
	}

	
	@Test (timeout = 2000)
	public void waitForBuildResult() throws Exception {
		File build = BuildFolders.createNewBuildFolderIn(new File(tmpFolder(), "1"));
		BuildFolders.markAsSuccessful(build);
		assertTrue(BuildFolders.waitForResult(build));

		File build2 = BuildFolders.createNewBuildFolderIn(new File(tmpFolder(), "2"));
		BuildFolders.markAsFailed(build2);
		assertFalse(BuildFolders.waitForResult(build2));
	}

	
	@Test (timeout = 2000)
	public void waitForBuildResultWithDeepStructure() throws Exception {
		File build = BuildFolders.createNewBuildFolderIn(tmpFolder());
		File deepFolder = new File(build, "deep/project/structure");
		deepFolder.mkdirs();
		BuildFolders.markAsSuccessful(deepFolder);
		assertTrue(BuildFolders.waitForResult(build));
	}

	
	@Test
	public void lastSuccessfulBuildFolderInAlfabeticalOrder() throws Exception {
		File build3 = createBuild(30000);
		File build1 = createBuild(10000);
		createBuild(20000);
		
		assertNull(BuildFolders.findLastSuccessfulBuildFolderIn(tmpFolder()));
		
		BuildFolders.markAsSuccessful(build1);
		assertEquals(build1, BuildFolders.findLastSuccessfulBuildFolderIn(tmpFolder()));
		
		BuildFolders.markAsSuccessful(build3);
		assertEquals(build3, BuildFolders.findLastSuccessfulBuildFolderIn(tmpFolder()));
	}


	private File createBuild(int stamp) throws IOException {
		Clock.setForCurrentThread(stamp);
		return BuildFolders.createNewBuildFolderIn(tmpFolder());
	}
	
	
	private void setClock(String stamp) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(stamp);
		Clock.setForCurrentThread(date.getTime());
	}
	
}
