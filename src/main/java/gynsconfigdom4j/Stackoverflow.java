package gynsconfigdom4j;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Stackoverflow {

	public static void main(String[] args) throws Exception {

		Set<String> set = new HashSet<String>();
		BundleDependFiller b = new BundleDependFiller();
		 String dir =
		 "D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-ent/pe-gyns-service-ent-invest";
//		String dir = "D:/workpla/ee/24thMay/PE10code";
		TargetDeleter td = new TargetDeleter();
		td.delete(new File(dir));
		Searcher searcher = new Searcher(dir, "MANIFEST.MF", false);
		searcher.scan();
		File[] mfs = searcher.outputFiles();
		for (int i = 0; i < mfs.length; i++) {
			if (null != mfs[i]) {
				String[] ss = b.getImportValueFromOneMF(mfs[i]);
				if (null != ss) {
					for (int j = 0; j < ss.length; j++) {
						if (null != ss[j] && !ss[j].startsWith("com.csii")) {

							set.add(ss[j]);
						}
					}
				}
			}
		}
		for (String str : set) {
			System.out.println(str);
		}

	}

}
