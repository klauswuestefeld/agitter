package infra.simploy;

import infra.simploy.SimployMainLoop.BuildDeployer;

import java.io.File;

public class BuildDeployerImpl implements BuildDeployer {

	private final File buildsRootFolder;

	
	public BuildDeployerImpl(File buildsRootFolder) {
		this.buildsRootFolder = buildsRootFolder;
	}
	
	
	@Override
	public void deployLastGoodBuild() {
		File lastGoodBuild = lastGoodBuild();
		if (lastGoodBuild == null) return;
		
	}


	private File lastGoodBuild() {
		return BuildFolders.findLastSuccessfulBuildFolderIn(buildsRootFolder);
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
		File newBuild = BuildFolders.createNewBuildFolderIn(buildsRootFolder).getCanonicalFile();
		CommandRunner.exec("C:\\apache-ant-1.8.2\\bin\\ant.bat build -Dbuild=" + newBuild);
		SimployTestsRunner.runAllTestsIn(newBuild + "/src");
		CommandRunner.execIn("C:\\apache-ant-1.8.2\\bin\\ant.bat run -Dbuild=" + newBuild + " -Dlast-good-build=" + lastGoodBuild(), newBuild);
	}

}
