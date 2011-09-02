package infra.simploy;

import infra.simploy.SimployMainLoop.BuildDeployer;

import java.io.File;
import java.io.IOException;

public class BuildDeployerImpl implements BuildDeployer {

	private final File buildsRootFolder;

	
	public BuildDeployerImpl(File buildsRootFolder) {
		this.buildsRootFolder = buildsRootFolder;
	}
	
	
	@Override
	public void deployGoodBuild() {
		try {
			File goodBuild = lastGoodBuild();
			if (goodBuild != null)
				run(goodBuild);
			else
				tryToDeployNewBuild();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void deployNewBuild() {
		try {
			tryToDeployNewBuild();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void tryToDeployNewBuild() throws Exception {
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
		CommandRunner.exec("C:\\apache-ant-1.8.2\\bin\\ant.bat build -Dbuild=" + newBuild);
	}


	private void run(File newBuild) throws Exception {
		CommandRunner.execIn("C:\\apache-ant-1.8.2\\bin\\ant.bat run -Dbuild=" + newBuild, newBuild);
	}

	
	private File lastGoodBuild() throws IOException {
		return BuildFolders.findLastSuccessfulBuildFolderIn(buildsRootFolder);
	}

}
