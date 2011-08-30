package infra.simploy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


class SimployFileUtils {

	static List<String> fileNamesEndingWith(File rootFolder, String ending) throws Exception {
		List<String> result = new ArrayList<String>();
		accumulateFileNamesEndingWith(result, rootFolder, ending);
		return result;
	}
	
	static private void accumulateFileNamesEndingWith(List<String> classFiles, File folder, String ending) {
		for (File candidate : folder.listFiles())
			if (candidate.isDirectory())
				accumulateFileNamesEndingWith(classFiles, candidate, ending);
			else
				accumulateFileNameEndingWith(classFiles, candidate, ending);
	}

	static private void accumulateFileNameEndingWith(List<String> fileNames, File file, String ending) {
		String name = file.getName();
		if (name.endsWith(ending))
			fileNames.add(file.getAbsolutePath());
	}

}