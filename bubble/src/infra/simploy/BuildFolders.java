package infra.simploy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import sneer.foundation.lang.Clock;

public class BuildFolders {

	private static final String BUILD_PREFIX = "build-";
	private static final String OK_FLAG = "build-ok-flag";
	private static final String FAILED_FLAG = "build-failed-flag";


	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	
	public static File findLastSuccessfulBuildFolderIn(File buildsRootFolder) throws IOException {
		File[] builds = buildsRootFolder.listFiles();
		Arrays.sort(builds);
		for (int i = builds.length - 1; i >= 0; i--)
			if (new File(builds[i], OK_FLAG).exists())
				return builds[i].getCanonicalFile();
		return null;
	}
	
	
	public static File createNewBuildFolderIn(File buildsRootFolder) throws IOException {
		File result = new File(buildsRootFolder, BUILD_PREFIX + timestamp());
		if (!result.mkdirs()) throw new IOException("Unable to create folder: " + result);
		return result.getCanonicalFile();
	}

	
	public static void markAsSuccessful(File build) throws IOException {
		markBuild(build, OK_FLAG);
	}
	public static void markAsFailed(File build) throws IOException {
		markBuild(build, FAILED_FLAG);
	}

	private static void markBuild(File folder, String flag) throws IOException {
		while (!folder.getName().startsWith(BUILD_PREFIX))
			folder = folder.getParentFile();
		new File(folder, flag).createNewFile();
	}
	
	
	private static String timestamp() {
		return FORMAT.format(new Date(Clock.currentTimeMillis()));
	}


	public static boolean waitForResult(File buildFolder) throws Exception {
		File ok = new File(buildFolder, OK_FLAG);
		File failed = new File(buildFolder, FAILED_FLAG);
		while (true) {
			if (ok.exists()) return true;
			if (failed.exists()) return false;
			Thread.sleep(20);
		}
	}

}
