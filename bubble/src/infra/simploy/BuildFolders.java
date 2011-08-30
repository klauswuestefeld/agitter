package infra.simploy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import sneer.foundation.lang.Clock;

public class BuildFolders {

	private static final String OK_FLAG = "build-ok-flag";


	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	
	public static File findLastSuccessfulBuildFolderIn(File buildsRootFolder) {
		File[] builds = buildsRootFolder.listFiles();
		Arrays.sort(builds);
		for (int i = builds.length - 1; i >= 0; i--)
			if (new File(builds[i], OK_FLAG).exists())
				return builds[i];
		return null;
	}
	
	
	public static File createNewBuildFolderIn(File buildsRootFolder) throws IOException {
		File result = new File(buildsRootFolder, "build-" + timestamp());
		if (!result.mkdirs()) throw new IOException("Unable to create folder: " + result);
		return result;
	}

	
	public static void markAsSuccessful(File build) throws IOException {
		new File(build, OK_FLAG).createNewFile();
	}
	
	
	private static String timestamp() {
		return FORMAT.format(new Date(Clock.currentTimeMillis()));
	}

}
