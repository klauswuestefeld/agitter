package infra.simploy;

import java.io.File;
import java.io.IOException;

public class DeployerWorker {

	private final File buildsRootFolder;
	private final int port;

	
	DeployerWorker(String buildsRootFolder, int port) {
		this.buildsRootFolder = findSiblingFolderOnParents(buildsRootFolder);
		this.port = port;
	}
	
	
	void deployGoodBuild() throws Exception {
		File goodBuild = lastGoodBuild();
		if (goodBuild != null)
			run(goodBuild);
		else
			deployNewBuild();
	}

	
	void deployNewBuild() throws Exception {
		File newBuild = BuildFolders.createNewBuildFolderIn(buildsRootFolder);
		
		generate(newBuild);
		runAllTestsIn(newBuild);
		run(newBuild);
		
		System.out.println("Waiting for build result flag file.");
		BuildFolders.waitForResult(newBuild);
	}


	private void runAllTestsIn(File newBuild) throws Exception {
		try {
			SimployTestsRunner.runAllTestsIn(newBuild + "/src");
		} catch (Exception e) {
			BuildFolders.markAsFailed(newBuild, e);
			throw e;
		}
	}


	private void generate(File newBuild) throws Exception {
		//To test on Klaus's Windows machine: c:\\apache-ant-1.8.2\\bin\\ant.bat
		CommandRunner.exec("ant build -Dbuild=" + newBuild);
	}


	private void run(File newBuild) throws Exception {
		CommandRunner.execIn("ant run -Dbuild=" + newBuild + " -Dhttp.port=" + port, newBuild);
	}


	private File lastGoodBuild() throws IOException {
		return BuildFolders.findLastSuccessfulBuildFolderIn(buildsRootFolder);
	}

	
	private static File findSiblingFolderOnParents(String siblingFolder) {
		File result;
		File current;
		try {
			current = new File(".").getCanonicalFile();
			result = findSiblingFolderOnParents(current, siblingFolder);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		if (result == null) throw new IllegalStateException(siblingFolder + " subfolder not found in any parent folders of " + current);
		return result;
	}


	private static File findSiblingFolderOnParents(File folder, String sibling) {
		if (folder == null) return null;
		return new File(folder, sibling).exists()
			? new File(folder, sibling)
			: findSiblingFolderOnParents(folder.getParentFile(), sibling);
	}



}
