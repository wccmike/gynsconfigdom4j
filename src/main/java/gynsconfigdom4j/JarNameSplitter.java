package gynsconfigdom4j;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JarNameSplitter {
	private static final String DEFAULT_VERSION = "1.0";

	public String[] getAvByJar(File f) throws IOException {
		if (null == f || !f.getName().endsWith(".jar"))
			throw new IOException("wrong extension or null file");
		String fn = f.getName();
		Matcher m = Pattern.compile("[^a-zA-Z0-9]\\d").matcher(fn);
		String a = null;
		String v = null;
		if (m.find()) {
			int sym = m.start();
			a = fn.substring(0, sym).replaceAll("[_\\.]", "-");
			v = fn.substring(sym + 1, fn.lastIndexOf(".jar"));
		} else {
			a = fn.substring(0, fn.lastIndexOf(".jar"));
			v = DEFAULT_VERSION;
		}
		String[] s = { a, v };
		return s;
	}

}
