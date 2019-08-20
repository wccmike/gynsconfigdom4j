package gynsconfigdom4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NexusUploader {
	private String group;
	private Runtime rt = Runtime.getRuntime();
	private static final String ERRORLOGPATH = "D:/errorlog.txt";
	private static final Writer ERR;
	static {
		Writer er = null;
		try {
			File f = new File(ERRORLOGPATH);
			if (!f.exists())
				f.createNewFile();
			er = new OutputStreamWriter(new FileOutputStream(f,true), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		ERR = er;
	}

	public NexusUploader(String group) {
		this.group = group;
	}

	/**
	 * upload files to nexus dealed by jarnamesplitter ,return jarname,gav map
	 * 
	 * @param files
	 *            must be jar files
	 * @return k:jarfilename,v:gav in String[3]
	 */
	public Map<String, String[]> uploadSpecifiedFiles(List<File> files) throws Exception {
		if (null == files || files.isEmpty())
			throw new IOException("files empty or null");
		JarNameSplitter js = new JarNameSplitter();
		Map<String, String[]> resu = new HashMap<String, String[]>();
		for (File f : files) {
			if (null == f || !f.exists() || !f.getName().endsWith(".jar"))
				throw new IOException("single file empty or unsupported exten");
			String[] av = js.getAvByJar(f);
			if (null == av || av.length == 0)
				throw new IOException();
			StringBuffer sb = new StringBuffer(
					"cmd /C mvn -s D:/software/apache-maven-3.5.0/conf/settings.xml deploy:deploy-file -DgroupId=");
			sb.append(this.group);
			sb.append(" -DartifactId=" + av[0]);
			sb.append(" -Dversion=" + av[1]);
			sb.append(" -Dpackaging=jar -Dfile=");
			String fpath = f.getAbsolutePath().replace('\\', '/');
			sb.append(fpath);
			sb.append(
					" -Durl=http://118.242.36.102:35678/nexus/content/repositories/releases -DrepositoryId=deployRelease");
			Process p = this.rt.exec(sb.toString());
			InputStream is = p.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while (null != (line = br.readLine())) {
				System.out.println(line);
			}
			br.close();
			int ercount = p.waitFor();
			if (ercount != 0) {
				ERR.write("upload failed filename: " + fpath + "\r\n");
				ERR.flush();
			} else {
				String[] s = { this.group, av[0], av[1] };
				resu.put(f.getName(), s);
			}
		}
		return resu;
	}
	
	public void uploadSpecifiedOneJarWithPom(File jar,String pomPath) throws Exception {
		if (null == jar || !jar.exists() || !jar.getName().endsWith(".jar"))
			throw new IOException(" file empty or unsupported exten");
		JarNameSplitter js = new JarNameSplitter();

		String[] av = js.getAvByJar(jar);
		if (null == av || av.length == 0)
			throw new IOException();
//		 mvn -s D:/software/apache-maven-3.5.0/conf/settings.xml
//		 deploy:deploy-file -DgroupId=x -DartifactId=1111111111 -Dversion=1.0
//		 -Dpackaging=jar -Dfile=myja.jar
//		 -Durl=http://118.242.36.102:35678/nexus/content/repositories/releases
//		 -DrepositoryId=deployRelease -DpomFile=*.pom -DgeneratePom=false

		StringBuffer sb = new StringBuffer(
				"cmd /C mvn -s D:/software/apache-maven-3.5.0/conf/settings.xml deploy:deploy-file -DgroupId=");
		sb.append(this.group);
		sb.append(" -DartifactId=" + av[0]);
		sb.append(" -Dversion=" + av[1]);
		sb.append(" -Dpackaging=jar -Dfile=");
		String fpath = jar.getAbsolutePath().replace('\\', '/');
		sb.append(fpath);
		sb.append(
				" -Durl=http://118.242.36.102:35678/nexus/content/repositories/releases -DrepositoryId=deployRelease -DgeneratePom=false -DpomFile=");
		sb.append(pomPath);
		Process p = this.rt.exec(sb.toString());
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while (null != (line = br.readLine())) {
			System.out.println(line);
		}
		br.close();
		int ercount = p.waitFor();
		if (ercount != 0) {
			ERR.write("upload failed filename: " + fpath + "\r\n");
			ERR.flush();
		} 
	
	}
}
