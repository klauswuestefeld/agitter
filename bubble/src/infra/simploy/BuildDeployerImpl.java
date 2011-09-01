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
	public void deployLastGoodBuild() {
		try {
			File lastGoodBuild = lastGoodBuild();
			if (lastGoodBuild != null)
				run(lastGoodBuild, "");
		} catch (Exception e) {
			int reportThisException;
			e.printStackTrace();
		}
	}


	private File lastGoodBuild() throws IOException {
		return BuildFolders.findLastSuccessfulBuildFolderIn(buildsRootFolder);
	}

	
	@Override
	public void deployNewBuild() {
		try {
			tryToDeployNewBuild();
		} catch (Exception e) {
			int reportThisException;
			e.printStackTrace();
		}
	}


	private void tryToDeployNewBuild() throws Exception {
		File newBuild = BuildFolders.createNewBuildFolderIn(buildsRootFolder);
		
		CommandRunner.exec("C:\\apache-ant-1.8.2\\bin\\ant.bat build -Dbuild=" + newBuild);
		
		SimployTestsRunner.runAllTestsIn(newBuild + "/src");
		
		run(newBuild, previousGoodBuildArg());
		
		System.out.println("Result: " + BuildFolders.waitForResult(newBuild));
	}


	private void run(File newBuild, String previousElectedBuildArg) throws Exception {
		CommandRunner.execIn("C:\\apache-ant-1.8.2\\bin\\ant.bat run -Dbuild=" + newBuild + " " + previousElectedBuildArg, newBuild);
	}


	private String previousGoodBuildArg() throws IOException {
		File result = lastGoodBuild();
		if (result == null) throw new IllegalStateException("Previous elected build not found.");
		return "-Dprevious-elected-build=" + result.getAbsolutePath();
	}

}
