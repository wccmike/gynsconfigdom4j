package renamepeconfigxml;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Main {

	/**
	 * rename all files whose names are "peconfig.xml" or "peconfigdev.xml" to
	 * "peconfig.xml.bak" or "peconfigdev.xml.bak"
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String dir = "D:/1";
		File f = null;
		try {
			f = new File(dir).getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		recurse(f);
	}

	public static void recurse(File dir) {
		if (!dir.isDirectory())
			throw new RuntimeException("isnot directory");
		File[] fs = dir.listFiles();
		if (fs != null && fs.length > 0) {
			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory())
					recurse(fs[i]);
				else if (fs[i].getName().equalsIgnoreCase("peconfigdev.xml")
						|| fs[i].getName().equalsIgnoreCase("peconfig.xml")) {
					try {
						FileUtils.moveFile(fs[i], new File(fs[i].getAbsolutePath() + ".bak"));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
