package gynsconfigdom4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpMavenSearcher {
	private ShaHasher sh = new ShaHasher();
	private static final String FILELOG = "D:\\notfound.txt";

	/**
	 * @param jar jar file
	 * @return group artifact version in string[3]
	 * @throws Exception
	 */
	public String[] getGavByJar(File jar) throws Exception {
		String[] gav = null;
		String sha1 = sh.sha1(jar);
		String url = "http://search.maven.org/solrsearch/select?q=1:%22" + sha1 + "%22&rows=20&wt=json";
		String resp = IOUtils.toString(new URL(url), "UTF-8");
		if (null == resp || resp.length() == 0)
			throw new IOException("json got is empty from url");
		ObjectMapper om = new ObjectMapper();
		JsonNode r = om.readTree(resp);
		JsonNode rp = r.path("response");
		int num = rp.path("numFound").intValue();
		if (num > 0) {
			// found
			JsonNode doc = rp.path("docs");
			JsonNode $0 = doc.get(0);
			String g = $0.path("g").textValue();
			String a = $0.path("a").textValue();
			String v = $0.path("v").textValue();
			if (null == g || g.length() == 0 || null == a || a.length() == 0 || null == v || v.length() == 0)
				throw new IOException("gav in json is empty");
			gav = new String[3];
			gav[0] = g;
			gav[1] = a;
			gav[2] = v;
		} else {
			// not found
			File log = new File(FILELOG);
			if (!log.exists())
				log.createNewFile();
			FileInputStream is = new FileInputStream(log);
			Long len = log.length();
			byte[] bu = new byte[len.intValue()];
			is.read(bu);
			is.close();
			String oc = new String(bu, "UTF-8");
			oc += jar.getAbsolutePath() + " cannot find\r\n";
			BufferedWriter bw = new BufferedWriter(new FileWriter(log));
			bw.write(oc);
			bw.flush();
			bw.close();
		}
		return gav;
	}
}
