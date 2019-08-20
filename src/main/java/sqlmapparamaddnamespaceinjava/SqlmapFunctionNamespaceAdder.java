package sqlmapparamaddnamespaceinjava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gynsconfigdom4j.ExtensionFileFinder;

public class SqlmapFunctionNamespaceAdder {
	private String namespace;
	private String projectDir;
	private List<File> files;

	/**
	 * @param namespace
	 *            sqldb.xml中的namespace
	 * @param projectDir
	 *            要处理的工程目录
	 */
	public SqlmapFunctionNamespaceAdder(String namespace, String projectDir) {
		super();
		this.namespace = namespace;
		this.projectDir = projectDir;
	}

	public void dealDoubleQuoteParam() throws IOException {
		instantiate();
		this.files.forEach(f -> {
			Long len = f.length();
			byte[] buf = new byte[len.intValue()];
			FileInputStream fis = null;
			BufferedWriter bw = null;
			try {
				fis = new FileInputStream(f);
				fis.read(buf);
				fis.close();
				String content = new String(buf, "utf-8");
				Pattern p = Pattern.compile("sqlMap\\s*\\.[a-zA-Z0-9]+\\s*\\(\\s*\"([a-zA-Z0-9\\.]+)\"");
				Matcher m = p.matcher(content);
				StringBuffer sb = new StringBuffer();
				int cur = 0;
				while (m.find()) {
					String s = m.group(1);
					// if no dot, i.e., no namespace
					if (-1 == s.indexOf(".")) {
						int i = m.end() - m.group(1).length() - 1;
						// add namespace
						sb.append(content.substring(cur, i) + this.namespace + ".");
						cur = i;
					}
				}
				sb.append(content.substring(cur));
				bw = new BufferedWriter(new FileWriter(f));
				bw.write(sb.toString());
				bw.flush();
				bw.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		});
	}

	private void instantiate() throws IOException {
		ExtensionFileFinder ef = new ExtensionFileFinder(this.projectDir, "java");
		this.files = ef.scanNOutput();
	}
}
