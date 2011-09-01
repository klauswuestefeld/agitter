package infra.simploy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
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
		markBuild(build, OK_FLAG, "This build was successful. :)");
	}
	public static void markAsFailed(File build, Exception exception) throws IOException {
		markBuild(build, FAILED_FLAG, stackTrace(exception));
	}

	
	private static void markBuild(File folder, String flag, String message) throws IOException {
		File build = navigateToBuildFolder(folder);
		FileOutputStream out = new FileOutputStream(new File(build, flag));
		out.write(message.getBytes());
		out.close();
	}


	private static File navigateToBuildFolder(File folderInBuild) {
		while (!folderInBuild.getName().startsWith(BUILD_PREFIX))
			folderInBuild = folderInBuild.getParentFile();
		return folderInBuild;
	}
	
	
	private static String timestamp() {
		return FORMAT.format(new Date(Clock.currentTimeMillis()));
	}


	public static String waitForResult(File buildFolder) throws Exception {
		File ok = new File(buildFolder, OK_FLAG);
		File failed = new File(buildFolder, FAILED_FLAG);
		while (true) {
			if (ok.exists()) return read(ok);
			if (failed.exists()) return read(failed);
			Thread.sleep(20);
		}
	}

	
	private static String read(File file) throws IOException {
		byte[] buffer = new byte[(int)file.length()];
		RandomAccessFile raf = new RandomAccessFile(file, "r");
		raf.readFully(buffer);
		raf.close();
		return new String(buffer);
	}


	private static String stackTrace(Exception exception) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(buffer);
		exception.printStackTrace(writer);
		writer.flush();
		return buffer.toString();
	}

}
