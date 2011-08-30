package infra.simploy;

import java.io.File;

import sneer.foundation.lang.exceptions.NotImplementedYet;

public class Simploy {

	public interface Worker {
		void exec(String command);
		void execIn(String command, File workingFolder);
		
		void runAllTestsIn(File folder) throws Exception;
		
		File lastSuccessfulBuildFolder(File buildsRootFolder);
		File createNewBuildFolder(File buildsRootFolder);
		void waitForResultFileIn(File buildFolder);
		
		void waitForNextBuildEvent();
	}
	
	
	private final File buildsRootFolder;
	private final Worker worker;

	
	public Simploy(File buildsRootFolder, Worker worker) {
		this.buildsRootFolder = buildsRootFolder;
		this.worker = worker;
		start();
	}

	private void start() {
		File lastGoodBuild = worker.lastSuccessfulBuildFolder(buildsRootFolder);
		if (lastGoodBuild != null) throw new NotImplementedYet();
		File newBuild = worker.createNewBuildFolder(buildsRootFolder);
		worker.exec("ant build " + newBuild.getAbsolutePath());
		try {
			worker.runAllTestsIn(newBuild);
		} catch (Exception e) {
			throw new NotImplementedYet();
		}
		worker.execIn("ant run -Dlast-good-build=no-good-build", newBuild);
		worker.waitForResultFileIn(newBuild);
		
		worker.waitForNextBuildEvent();
	}


}
