package delbom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import gynsconfigdom4j.ExtensionFileFinder;

public class Main {

	public static void main(String[] args) throws Exception {
		ExtensionFileFinder e = new ExtensionFileFinder("D:/workpla/ee/24thMay/PE10code", "java");
		List<File> li = e.scanNOutput();
		li.stream().forEach(f -> {
			FileOutputStream fos = null;
			try (FileInputStream fis = new FileInputStream(f)) {
				Long len = f.length();
				byte[] bu = new byte[len.intValue()];
				int count = fis.read(bu);
				if (count >= 3 && ((bu[0] & 0xff + 0x100) & 0x0ff) == 0xef && ((bu[1] & 0xff + 0x100) & 0x0ff) == 0xbb
						&& ((bu[2] & 0xff + 0x100) & 0x0ff) == 0xbf) {
					fos = new FileOutputStream(f);
					fos.write(bu, 3, bu.length - 3);
					fos.flush();
				}
			} catch (Exception ee) {
				ee.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});
	}

}
