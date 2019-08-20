package gynsconfigdom4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DependFillMain {

	public static void main(String[] args) throws Exception {
		BundleDependFiller b = new BundleDependFiller();
		// first arg needn't postfixed by / if it's dir
		String dir = "D:/1";
		TargetDeleter td = new TargetDeleter();
		td.delete(new File(dir));
		Searcher searcher = new Searcher(dir, "MANIFEST.MF", false);
		searcher.scan();
		File[] mfs = searcher.outputFiles();
		Map<String, String> exportProjectMap = b.createExportProjectMap(mfs);
		Map<String, String[]> projectStringArrMap = b.createProjectStringArrMap(mfs);
		for (int i = 0; i < mfs.length; i++) {
			String[] imports = b.getImportValueFromOneMF(mfs[i]);
			if (null == imports)
				continue;
			// remember to avoid filling self's gav
			String selfProjectName = b.getProjectNameFromMFPath(mfs[i]);
			Set<String> distinctProjectNames = new LinkedHashSet<String>();
			for (int j = 0; j < imports.length; j++) {
				String toBeRefferedProjectName = b.findProjectFromMapBySingleImportPackage(imports[j],
						exportProjectMap);
				if (null != toBeRefferedProjectName)
					distinctProjectNames.add(toBeRefferedProjectName);
			}
			distinctProjectNames.remove(selfProjectName);
			List<String[]> allGavArrays = new LinkedList<String[]>();
			for (String pname : distinctProjectNames) {
				String[] ss = projectStringArrMap.get(pname);
				if (null != ss && ss.length != 0)
					allGavArrays.add(ss);
			}
			File pom = b.getPomInSameProjectFromMF(mfs[i]);
			b.fillDependency(pom, allGavArrays);
		}
		// fill according to import-package end,require-bundle start
		// fill according to import-package end,require-bundle start
		// fill according to import-package end,require-bundle start
		Map<String, String[]> symGavMap = b.createSymGavMapByMfFiles(mfs);
		if (null == symGavMap || symGavMap.size() == 0)
			throw new IOException("create an empty map");
		for (int i = 0; i < mfs.length; i++) {
			if (null == mfs[i] || !mfs[i].exists())
				throw new IOException();
			String[] requires = b.getRequireValueFromOneMF(mfs[i]);
			if (null != requires) {
				Set<String[]> abandonSame = new LinkedHashSet<String[]>();
				for (int j = 0; j < requires.length; j++) {
					if (null == requires[j] || "".equals(requires[j]))
						throw new IOException();
					String[] gav = symGavMap.get(requires[j]);
					if (null != gav) {
						abandonSame.add(gav);
					}
				}
				// if this project has require-bundle
				if (!abandonSame.isEmpty()) {
					List<String[]> li = new ArrayList<String[]>(abandonSame);
					if (null == li || li.isEmpty())
						throw new IOException();
					File pom = b.getPomInSameProjectFromMF(mfs[i]);
					b.fillDependency(pom, li);
				}
			}
		}
		System.out.println("app finished without error");
	}

}
