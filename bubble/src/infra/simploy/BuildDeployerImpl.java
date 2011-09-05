package infra.simploy;

import java.io.File;
import java.io.IOException;

public class BuildDeployerImpl {

	private final File buildsRootFolder;

	
	BuildDeployerImpl(File buildsRootFolder) {
		this.buildsRootFolder = buildsRootFolder;
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
		
		System.out.println("Result: " + BuildFolders.waitForResult(newBuild));
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
		CommandRunner.exec("ant build -Dbuild=" + newBuild);
	}


	private void run(File newBuild) throws Exception {
		CommandRunner.execIn("ant run -Dbuild=" + newBuild, newBuild);
	}

	
	private File lastGoodBuild() throws IOException {
		return BuildFolders.findLastSuccessfulBuildFolderIn(buildsRootFolder);
	}

}
