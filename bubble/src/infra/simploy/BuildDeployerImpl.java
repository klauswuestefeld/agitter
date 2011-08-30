package infra.simploy;

import infra.simploy.SimployMainLoop.BuildDeployer;
import infra.simploy.old.SimployTestsRunner;

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
		File newBuild = BuildFolders.createNewBuildFolderIn(buildsRootFolder);
		CommandRunner.exec("ant build -Dbuild-dir=" + newBuild);
		SimployTestsRunner.runAllTestsIn(newBuild + "/src");
		CommandRunner.execIn("ant run -Dbuild-dir=" + newBuild + ";last-good-build=" + lastGoodBuild(), newBuild);
	}

}
