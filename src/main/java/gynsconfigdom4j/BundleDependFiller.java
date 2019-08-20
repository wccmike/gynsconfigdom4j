package gynsconfigdom4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class BundleDependFiller implements UnderlineDomRead {

	public String[] getImportValueFromOneMF(File file) throws Exception {
		String oldText = readFileIntoStringAll(file);
		return extractValuesFromMFTextByKey(oldText, "Import-Package");
	}

	/**
	 * @param file
	 *            mf file
	 * @return
	 * @throws IOException
	 */
	public File getPomInSameProjectFromMF(File file) throws IOException {
		String pathMF = file.getAbsolutePath();
		pathMF = pathMF.substring(0, pathMF.lastIndexOf("src")) + "pom.xml";
		File pomFile = new File(pathMF);
		if (!pomFile.exists())
			throw new IOException("pom file doesn't exist according to formed path");
		return pomFile;
	}

	public String getProjectNameFromMFPath(File file) throws Exception {
		String path = file.getCanonicalPath();
		path = path.substring(0, path.lastIndexOf("\\src"));
		path = path.substring(path.lastIndexOf("\\") + 1);
		if (null == path || "".equals(path))
			throw new IOException("project name can't be retrieved");
		return path;
	}

	public String[] getGAVFromFile(File pom) throws Exception {
		SAXReader reader = getUnderlineSaxreader();
		Document doc = reader.read(pom);
		Element project = doc.getRootElement();
		Element parent = project.element("parent");
		String g = null;
		String v = null;
		if (null == parent) {
			g = project.element("groupId").getText();

			v = project.element("version").getText();
		} else {
			g = parent.element("groupId").getText();
			v = parent.element("version").getText();
		}
		String a = project.element("artifactId").getText();
		String[] result = new String[3];
		result[0] = g;
		result[1] = a;
		result[2] = v;
		if (null == result || result.length == 0)
			throw new IOException("gav empty");
		return result;
	}

	public String[] getExportValueFromOneMF(File file) throws Exception {
		String oldText = readFileIntoStringAll(file);
		return extractValuesFromMFTextByKey(oldText, "Export-Package");

	}

	private String readFileIntoStringAll(File file)
			throws FileNotFoundException, IOException, UnsupportedEncodingException {
		FileInputStream fis = new FileInputStream(file);
		Long length = file.length();
		byte[] old = new byte[length.intValue()];
		fis.read(old);
		fis.close();
		String oldText = new String(old, "UTF-8");
		return oldText;
	}

	private String[] extractValuesFromMFTextByKey(String text, String key) {
		Pattern p2 = Pattern.compile(key + ":");
		Matcher m2 = p2.matcher(text);
		// if keyword exists
		String[] packs = null;
		if (m2.find()) {
			int start = m2.end();
			// Pattern p = Pattern.compile(key + ":(.|\\r|\\n)+?:");
			// Matcher m = p.matcher(text);
			int indexSecond = text.indexOf(": ", start + 1);

			String newText = null;
			// if (m.find()) {
			if (-1 != indexSecond) {
				// newText = text.substring(start, m.end());
				newText = text.substring(start, indexSecond);
				newText = newText.substring(0, newText.lastIndexOf("\n"));
			} else {
				newText = text.substring(start);
			}
			newText = newText.replaceAll("\\r", "").replaceAll("\\n", "");
			newText = newText.replaceAll(" ", "");
			packs = newText.split(",");

		}
		return packs;
	}

	public Boolean haveDependenciesNode(File pom) throws Exception {
		if (!pom.getName().endsWith(".xml"))
			throw new IOException("can't resolve not xml file");
		SAXReader reader = getUnderlineSaxreader();
		Document dom = reader.read(pom);
		Element project = dom.getRootElement();
		Element dependencies = project.element("dependencies");
		return dependencies != null;

	}

	public Boolean dependencyExists(File file, String[] gav) throws Exception {
		if (!file.getName().endsWith(".xml"))
			throw new IOException("can't resolve not xml file");
		if (null == gav || gav.length == 0)
			throw new IllegalArgumentException("string array empty");
		if (!haveDependenciesNode(file))
			throw new IOException("non Dependencies node in xml");
		Boolean result = false;
		SAXReader reader = getUnderlineSaxreader();
		Document dom = reader.read(file);
		Element project = dom.getRootElement();
		Element dependencies = project.element("dependencies");
		List<Element> list = dependencies.elements();
		for (Element e : list) {
			String g = e.element("groupId").getText();
			String a = e.element("artifactId").getText();
			String v = null;
			if (null != e.element("version"))
				v = e.element("version").getText();
			if (gav[0].equals(g) && gav[1].equals(a) && gav[2].equals(v)) {
				result = true;
				break;
			}
		}
		return result;
	}

	public int countDependency(File file) throws Exception {
		if (!file.getName().endsWith(".xml"))
			throw new IOException("can't resolve not xml file");
		SAXReader reader = getUnderlineSaxreader();
		Document dom = reader.read(file);
		Element project = dom.getRootElement();
		Element dependencies = project.element("dependencies");
		return dependencies.elements().size();
	}

	/**
	 * @param file
	 *            pomfile to be filled
	 * @param li
	 *            might contains gav existed in pom on coping
	 * @throws Exception
	 */
	public void fillDependency(File file, List<String[]> li) throws Exception {
		if (!file.getName().endsWith(".xml"))
			throw new IOException("can't resolve not xml file");
		if (null == li || li.size() == 0)
			return;
		Long len = file.length();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[len.intValue()];
		fis.read(buffer);
		fis.close();
		String content = new String(buffer, "UTF-8");
		String pre = null;
		String post = null;
		if (haveDependenciesNode(file)) {
			// abandon existed gav and empty element in list
			Iterator<String[]> ite = li.iterator();
			while (ite.hasNext()) {
				String[] el = ite.next();
				if (null == el || el.length == 0 || dependencyExists(file, el))
					ite.remove();
			}
			// for (int i = 0; i < li.size(); ++i) {
			// if (null == li.get(i) || li.get(i).length == 0)
			// continue;
			// if (dependencyExists(file, li.get(i))) {
			// li.remove(i);
			// continue;
			// }
			// }

			Pattern p = Pattern.compile("</dependencies>(\\s|\\r|\\n)+<(?!/dependencyManagement>)");
			Matcher m = p.matcher(content);
			if (m.find()) {
				int pivot = m.start();
				pre = content.substring(0, pivot);
				post = content.substring(pivot);

			} else
				throw new IOException("xml content format illegal");

		} else {
			int pivot = content.lastIndexOf("</project>");
			pre = content.substring(0, pivot) + "\r\n<dependencies>\r\n";
			post = "\r\n</dependencies>\r\n" + content.substring(pivot);
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < li.size(); ++i) {
			if (null == li.get(i) || li.get(i).length == 0)
				continue;
			sb.append("\r\n<dependency>\r\n<groupId>");
			sb.append(li.get(i)[0]);
			sb.append("</groupId>\r\n<artifactId>");
			sb.append(li.get(i)[1]);
			sb.append("</artifactId>\r\n<version>");
			sb.append(li.get(i)[2]);
			sb.append("</version>\r\n</dependency>\r\n");
		}
		String finishedContent = pre + sb.toString() + post;
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(finishedContent);
		bw.close();
	}

	/**
	 * @param one
	 * @param map
	 *            struct: k:export-package one name, v:project name
	 * @return null if didn't find
	 */
	public String findProjectFromMapBySingleImportPackage(String one, Map<String, String> map) {
		if (null == map || map.size() == 0)
			throw new IllegalArgumentException("map mustn't be empty");
		String result = map.get(one);
		if (null == result && one.startsWith("com.csii.")) {
			String again = one.substring(0, one.lastIndexOf("."));
			while (null == result && again.startsWith("com.csii.")) {
				result = map.get(again);
				again = again.substring(0, again.lastIndexOf("."));
			}
		}
		return result;
	}

	/**
	 * @param files
	 *            all MF files in an array
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> createExportProjectMap(File[] files) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 0; i < files.length; i++) {
			String[] ex = getExportValueFromOneMF(files[i]);
			String pro = getProjectNameFromMFPath(files[i]);
			if (null != ex) {
				for (int j = 0; j < ex.length; j++) {
					if (null != ex[j])
						map.put(ex[j], pro);
				}

			}
		}
		if (map.size() == 0)
			throw new IOException("map just created is empty");
		return map;
	}

	/**
	 * @param files
	 *            all MF files in an array
	 * @return
	 * @throws Exception
	 */
	public Map<String, String[]> createProjectStringArrMap(File[] files) throws Exception {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < files.length; i++) {
			String pro = getProjectNameFromMFPath(files[i]);
			File pomFile = getPomInSameProjectFromMF(files[i]);
			String[] gav = getGAVFromFile(pomFile);
			map.put(pro, gav);
		}
		if (map.size() == 0)
			throw new IOException("map just created is empty");
		return map;
	}

	/**
	 * @param mf
	 * @return abandon version string array
	 */
	public String[] getRequireValueFromOneMF(File mf) throws IOException {
		if (null == mf || !mf.getName().toLowerCase().endsWith(".mf")) {
			throw new IOException("invalid file extension or null file");
		}
		if (!mf.exists())
			throw new IOException("file doesnot exist");
		String oldText = readFileIntoStringAll(mf);
		if (null == oldText)
			throw new IOException("can't get content");
		String[] ssWithVersion = extractValuesFromMFTextByKey(oldText, "Require-Bundle");
		String[] result = null;
		if (null != ssWithVersion) {
			result = ssWithVersion;
			for (int i = 0; i < result.length; i++) {
				if (null != result[i]) {
					int ind = result[i].indexOf(";");
					if (-1 != ind)
						result[i] = result[i].substring(0, ind);
				}
			}
		}
		return result;
	}

	/**
	 * @param mf
	 * @return
	 */
	public String getSymbolicNameFromOneMF(File mf) throws IOException {
		if (null == mf || !mf.getName().toLowerCase().endsWith(".mf")) {
			throw new IOException("invalid file extension or null file");
		}
		if (!mf.exists())
			throw new IOException("file doesnot exist");
		String oldText = readFileIntoStringAll(mf);
		if (null == oldText)
			throw new IOException("can't get content");
		return extractSingleVFromMFTextByKey(oldText, "Bundle-SymbolicName");

	}

	private String extractSingleVFromMFTextByKey(String oldText, String key) {
		String result = null;
		if (null != oldText && null != key) {
			String reg = key + ":";
			int keyLen = reg.length();
			int ind = oldText.indexOf(reg);
			// if keyword exists
			if (-1 != ind) {
				oldText = oldText.substring(ind + keyLen);
				int indSecond = oldText.indexOf(": ");
				// if second colon exists
				if (-1 != indSecond) {
					oldText = oldText.substring(0, indSecond);
					int indexLF=-1;
					if("Bundle-SymbolicName".equals(key)){
						int semicolon=oldText.indexOf(";");
						// if semicolon exists in SymbolicName, e.g., org.eclipse.osgi; singleton:=true
						if(-1!=semicolon){
							indexLF=semicolon;
						}
					}
					if(indexLF==-1)
						indexLF = oldText.lastIndexOf("\n");
					if (-1 != indexLF) {
						oldText = oldText.substring(0, indexLF);

					} else{
//						System.out.println("error key: "+key);
//						System.out.println("error oldText: "+oldText);
						throw new RuntimeException("manifest format illegal");
					}
				}
				oldText = oldText.replaceAll("\\r", "").replaceAll("\\n", "");
				result = oldText.replaceAll(" ", "");
			}
		}
		return result;
	}

	public Map<String, String[]> createSymGavMapByMfFiles(File[] fs) throws Exception {
		if (null == fs || fs.length == 0)
			throw new IOException("files null ptr or length 0");
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (int i = 0; i < fs.length; i++) {
			if (null != fs[i] && fs[i].exists()) {
				String sym = getSymbolicNameFromOneMF(fs[i]);
				if (null != sym && !"".equals(sym)) {
					File pom = getPomInSameProjectFromMF(fs[i]);
					if (null == pom || !pom.exists())
						throw new IOException("Pom file In the Same Project as MF doesnot exist");
					String[] gav = getGAVFromFile(pom);
					if (null == gav || gav.length == 0)
						throw new IOException("gav empty");
					map.put(sym, gav);
				}
			}
		}
		if (map.isEmpty())
			throw new IOException("just created map is empty");
		return map;
	}

	/**
	 * @param j
	 *            jar file
	 * @return pom file
	 */
	public File getRelevantProjectPomFileByJarInLib(File j) throws IOException {
		if (null == j || !j.getName().endsWith(".jar"))
			throw new IOException("null file or wrong extension");
		File foundPom = null;
		OUTTER: for (File p = j.getParentFile(); p != null; p = p.getParentFile()) {
			File[] fs = p.listFiles();
			for (File f : fs) {
				if (!f.isDirectory() && "pom.xml".equalsIgnoreCase(f.getName())) {
					foundPom = f;
					break OUTTER;
				}
			}
		}
		return foundPom;
	}

	public void createOnePeconfigxmlFromOneMffile(File mf) throws Exception{
		String content = readFileIntoStringAll(mf);
		String nullorpaths = extractSingleVFromMFTextByKey(content, "Spring-Context");
		if(nullorpaths!=null){
			String[] paths = nullorpaths.split(",");
			for (int i = 0; i < paths.length; i++) {
				paths[i]="classpath:/"+paths[i];
			}
			this.createPeconfigxmlFromOneMffile(paths,mf);
		}
	}

	private void createPeconfigxmlFromOneMffile(String[] paths,File mf) throws IOException {
		String apath = mf.getAbsolutePath();
		apath=apath.substring(0, apath.lastIndexOf("\\META-INF"));
		apath=apath+"\\config";
		File peconfigdir = new File(apath);
		if(!peconfigdir.exists()) peconfigdir.mkdir();
		apath=apath+"\\pe-configs.xml";
		File peconfig = new File(apath);
		if(peconfig.exists()){
			return ;
		} else{
			peconfig.createNewFile();
			}
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<!DOCTYPE properties SYSTEM \"http://java.sun.com/dtd/properties.dtd\">\n\n<properties>\n    <comment>\n        PowerEngine Main Configuration\n    </comment>\n    <entry key=\"CurrentMode\">Mode.demo</entry>\n\t<entry key=\"Mode.demo\">");
		for (int i = 0; i < paths.length; i++) {
			sb.append(paths[i]+",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("</entry>\n</properties>\n");
		BufferedWriter bw = new BufferedWriter(new FileWriter(peconfig));
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	}

	
}
