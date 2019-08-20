package gynsconfigdom4j;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class TargetDeleter {
	public void delete(File biggest) throws IOException {
		// File biggest = new File(dir);
		File[] files = biggest.listFiles();
		for (File f : files) {
			if (f.isDirectory()) {
				if ("target".equals(f.getName())) {
					FileUtils.deleteDirectory(f);
					
					continue;
				} else
					delete(f);
			}
		}
	}
}
