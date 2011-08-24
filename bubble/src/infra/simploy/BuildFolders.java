package infra.simploy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import sneer.foundation.lang.Clock;

public class BuildFolders {

	private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	
	private final File buildsRootFolder;

	
	public BuildFolders(File buildsRootFolder) {
		this.buildsRootFolder = buildsRootFolder;
	}

	
	public File createNewBuildFolder() throws IOException {
		File result = new File(buildsRootFolder, "build-" + timestamp());
		if (!result.mkdirs()) throw new IOException("Unable to create folder: " + result);
		return result;
	}

	
	public void markAsSuccessful(File build) throws IOException {
		if (!build.getParentFile().equals(buildsRootFolder)) throw new IllegalArgumentException("Folder " + build + " must be inside builds root folder(" + buildsRootFolder + ")");
		new File(build, "build-ok-flag").createNewFile();
	}
	
	
	public File lastSuccessfulBuildFolder() {
		File[] builds = buildsRootFolder.listFiles();
		Arrays.sort(builds);
		for (int i = builds.length - 1; i >= 0; i--)
			if (new File(builds[i], "build-ok-flag").exists())
				return builds[i];
		return null;
	}
	
	
	private String timestamp() {
		String timestamp = FORMAT.format(new Date(Clock.currentTimeMillis()));
		return timestamp;
	}

}
