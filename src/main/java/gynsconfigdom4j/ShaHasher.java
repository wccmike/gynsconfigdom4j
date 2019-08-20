package gynsconfigdom4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class ShaHasher {
	/**
	 * @param jars jar files array
	 * @return map: k:jar filename,v:sha1 code
	 * @throws Exception
	 */
	public Map<String,String> sha1(File[] jars) throws Exception {
		if (null == jars || jars.length == 0)
			throw new IOException("files empty");
		Map<String,String> map = new HashMap<String,String>();
		for (int i = 0; i < jars.length; i++) {
			if (null == jars[i])
				throw new IOException("files contain null ptr");
			String s = sha1(jars[i]);
			if (null != s && s.length() != 0)
				map.put(jars[i].getName(), s);
		}
		return map;
	}

	/**
	 * @param jar
	 *            single jar file
	 * @return sha1 hashcode
	 * @throws Exception
	 */
	public String sha1(File jar) throws Exception {
//		if (null == jar || !jar.getName().endsWith(".jar"))
//			throw new IOException("only parse jar file");
		MessageDigest md = MessageDigest.getInstance("sha1");
		FileInputStream is = new FileInputStream(jar);
		byte[] bu = new byte[1024];
		int l = 0;
		while (-1 != (l = is.read(bu)))
			md.update(bu, 0, l);

		is.close();
		byte[] d = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < d.length; i++) {
			String s = Integer.toHexString((d[i] & 0xff) + 0x100).substring(1);
			sb.append(s);
		}
		return sb.toString();
	}
}
