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
	public void noBuildFolders() {
		assertNull(BuildFolders.lastSuccessfulBuildFolder(tmpFolder()));
	}
	
	
	@Test
	public void buildFolderCreation() throws Exception {
		File build = BuildFolders.createNewBuildFolder(tmpFolder());
		assertEquals(tmpFolder(), build.getParentFile());
		assertTrue(build.isDirectory());
	}

	
	@Test
	public void buildFolderName() throws Exception {
		setClock("2011-12-31_23-59-59");
		File build = BuildFolders.createNewBuildFolder(tmpFolder());
		assertEquals("build-2011-12-31_23-59-59", build.getName());
	}

	
	@Test
	public void lastSuccessfulBuildFolderInAlfabeticalOrder() throws Exception {
		File build3 = createBuild(30000);
		File build1 = createBuild(10000);
		createBuild(20000);
		
		assertNull(BuildFolders.lastSuccessfulBuildFolder(tmpFolder()));
		
		BuildFolders.markAsSuccessful(build1);
		assertEquals(build1, BuildFolders.lastSuccessfulBuildFolder(tmpFolder()));
		
		BuildFolders.markAsSuccessful(build3);
		assertEquals(build3, BuildFolders.lastSuccessfulBuildFolder(tmpFolder()));
	}


	private File createBuild(int stamp) throws IOException {
		Clock.setForCurrentThread(stamp);
		return BuildFolders.createNewBuildFolder(tmpFolder());
	}
	
	
	private void setClock(String stamp) throws ParseException {
		Date date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").parse(stamp);
		Clock.setForCurrentThread(date.getTime());
	}
	
}
