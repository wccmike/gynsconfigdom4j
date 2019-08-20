package gynsconfigdom4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Assert;
import org.junit.Test;

import dynamicref.BeanRefChanger;
import sqlmapparamaddnamespaceinjava.SqlmapFunctionNamespaceAdder;
import sqlmapparamaddnamespaceinjava.SqlxmlConflictNameScanner;

public class MultiFunctionMain implements UnderlineDomRead {
	@Test
	public void moveAllDirsInnewsrcdir() throws IOException{
		String dir="D:/1";
		File f = new File(dir);
		File[] fdir = f.listFiles();
		for (int i = 0; i < fdir.length; i++) {
			File src = new File(fdir[i],"src");
			if(src.exists()) throw new RuntimeException();
			File[] sdir = fdir[i].listFiles();
			for (int j = 0; j < sdir.length; j++) {
				if(sdir[j].isFile()){
					FileUtils.moveFileToDirectory(sdir[j], src, true);
				}else{
					FileUtils.moveDirectoryToDirectory(sdir[j], src, true);
				}
			}
		}
		System.out.println("finished");
	}
	/** add project name to dmconfig's var name and change refer every where in project
	 * @throws Exception
	 */
	@Test
	public void addProjectnamePrefixToDmconfigAndRefer()throws Exception{
		String dir="D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-batch";
		Searcher s = new Searcher(dir, "dmconfig.xml", false);
		ConvertUtil cu = new ConvertUtil();
		s.scan();
File[] dms = s.outputFiles();
for (int i = 0; i < dms.length; i++) {
	File cdir = cu.getConfigDirByDmconfigxml(dms[i]);
	String pn = cu.getProjectnameByDmconfigxml(dms[i]);
	List<String> sss = cu.addProjectnamePrefixToDmconfigxml(dms[i], pn);
	if(sss.isEmpty()) continue;
	List<File> fs = cu.getBeanDefinationXmlsInOneConfigDir(cdir);
	if(fs.isEmpty()) continue;
	cu.addProjectnamePrefixToXmlbeanDefinationFiles(fs,sss,pn);
}
System.out.println("finished");
	}
	
	/** input: 2 ibatis sql map xmls(including many conflict id)
	 *  do: delete file2's same CRUD element in xml
	 * @throws Exception
	 */
	@Test
	public void deleteSameSqlIdInTwoSqlmapxml() throws Exception {
		File f1 = new File("D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-share/pe-gyns-share-mcs-service/src/main/resources/META-INF/config/sql-mapping/rulesql.xml");
		File f2 = new File("D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-batch/pe-gyns-batch-only-fsgpweb-class/src/main/resources/META-INF/config/sql-mapping/rule.xml");
		SAXReader reader = this.getUnderlineSaxreader();
		Document doc1 = reader.read(f1);
		Element sqlmap1 = doc1.getRootElement();
		List<Element> list1 = sqlmap1.elements();
		Set<String> set=new HashSet<String>();
		list1.stream().filter(e -> "select".equals(e.getName()) || "insert".equals(e.getName())
				|| "update".equals(e.getName()) || "delete".equals(e.getName())).forEach(e -> {
					String idvalue = e.attributeValue("id");
					set.add(idvalue);
				});
		Document doc2 = reader.read(f2);
		Element sqlmap2 = doc2.getRootElement();
		Iterator<Element> ite = sqlmap2.elementIterator();
		while(ite.hasNext()){
			Element e = ite.next();
			if("select".equals(e.getName())|| "insert".equals(e.getName())
				|| "update".equals(e.getName()) || "delete".equals(e.getName())){
				String idvalue = e.attributeValue("id");
				if(set.contains(idvalue)){
					e.detach();
				}
			}
		}
		FileOutputStream out = new FileOutputStream(f2);
		OutputFormat format = OutputFormat.createPrettyPrint();
		String encode = doc2.getXMLEncoding();
		encode = encode == null || "".equals(encode) ? "UTF-8" : encode;
		format.setEncoding(encode);
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(doc2);
		writer.close();
		System.out.println("app runs without any error");
	}

	/**
	 * copy content from dynamicservice."xml" named "extraspringbean.xml" ,then
	 * delete every osgi:service and osgi:referrence tags
	 * 
	 * @throws Exception
	 * 
	 */
	@Test
	public void copyNdecreaseContentFromDynamicservicexml() throws Exception {
		String dir = "D:/1/11ni/pe-gyns-parent/pe-gyns-router/pe-gyns-router-direct";
		Searcher searcher = new Searcher(dir, "dynamicservice.xml", false);
		searcher.scan();
		File[] dynams = searcher.outputFiles();
		for (int i = 0; i < dynams.length; i++) {
			String srpath = dynams[i].getAbsolutePath();
			srpath = srpath.substring(0, srpath.lastIndexOf("\\") + 1) + "extraspringbean.xml";
			File srfile = new File(srpath);
			FileUtils.copyFile(dynams[i], srfile);
			SAXReader reader = this.getUnderlineSaxreader();
			Document dom = reader.read(srfile);
			Element beans = dom.getRootElement();

			Iterator<Element> ite = beans.elementIterator("reference");
			while (ite.hasNext()) {
				Element child = ite.next();
				child.detach();
			}
			Iterator<Element> ite2 = beans.elementIterator("service");
			while (ite2.hasNext()) {
				Element child = ite2.next();
				child.detach();
			}

			FileOutputStream out = new FileOutputStream(srfile);
			OutputFormat format = OutputFormat.createPrettyPrint();
			String encode = dom.getXMLEncoding();
			encode = encode == null || "".equals(encode) ? "UTF-8" : encode;
			format.setEncoding(encode);
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(dom);
			writer.close();
		}
		System.out.println("app runs without any error");
	}

	/**
	 * input: a big project dir (including many small projects) do: create a
	 * peconfig.xml in every project according to MANIFEST.MF's "spring-context"
	 * 
	 * @throws Exception
	 */
	@Test
	public void createpeconfigxml() throws Exception {
		String dir = "D:/1/11ni/pe-gyns-parent/pe-gyns-router/pe-gyns-router-direct";
		BundleDependFiller bd = new BundleDependFiller();
		Searcher searcher = new Searcher(dir, "MANIFEST.MF", false);
		searcher.scan();
		File[] mfs = searcher.outputFiles();
		for (int i = 0; i < mfs.length; i++) {
			bd.createOnePeconfigxmlFromOneMffile(mfs[i]);
		}
		System.out.println("app runs without any error");
	}

	/**
	 * upload jars with my poms to nexus
	 * 
	 * @throws Exception
	 */
	@Test
	public void uploadManyJarsInOneDirUsingJustCreatedPomInExtractedDirs() throws Exception {
		// mvn -s D:/software/apache-maven-3.5.0/conf/settings.xml
		// deploy:deploy-file -DgroupId=x -DartifactId=1111111111 -Dversion=1.0
		// -Dpackaging=jar -Dfile=myja.jar
		// -Durl=http://118.242.36.102:35678/nexus/content/repositories/releases
		// -DrepositoryId=deployRelease -DpomFile=*.pom -DgeneratePom=false

		String dealdir = "D:/1";
		File dir = new File(dealdir);
		NexusUploader nu = new NexusUploader("com.csii.gyns.local");
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (!fs[i].isDirectory() && fs[i].getName().endsWith(".jar")) {
				String jabsolutePath = fs[i].getAbsolutePath();
				String dirabsolutePath = jabsolutePath.substring(0, jabsolutePath.lastIndexOf(".jar"));
				dirabsolutePath = dirabsolutePath + "/pom.xml";
				File f = new File(dirabsolutePath);
				Assert.assertTrue(f.exists());
				nu.uploadSpecifiedOneJarWithPom(fs[i], dirabsolutePath);
			}
		}
		System.out.println("app finished without error");

	}

	/**
	 * given many jars(from bank)+extracted dirs from these jars . generate
	 * pom.xml by jarnamesplitter ,then move MANIFEST.MF to just-create
	 * dir:"/src/resources"
	 * 
	 * @throws Exception
	 */
	@Test
	public void movetocorrectdircreatepom() throws Exception {
		String dealdir = "D:/1";
		File dir = new File(dealdir);
		File[] fs = dir.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (!fs[i].isDirectory() && fs[i].getName().endsWith(".jar")) {
				singleDealForMovetoco(fs[i]);
			}
		}
		System.out.println("app finished without error");
	}

	/**
	 * @param jarfile
	 *            3rd jar file which provided by gyns bank
	 * @throws Exception
	 */
	private void singleDealForMovetoco(File jarfile) throws Exception {
		Assert.assertTrue(jarfile != null && jarfile.exists());
		String jabsolutePath = jarfile.getAbsolutePath();
		Assert.assertTrue(jabsolutePath.endsWith(".jar"));
		String dirabsolutePath = jabsolutePath.substring(0, jabsolutePath.lastIndexOf(".jar"));
		File f = new File(dirabsolutePath + "/META-INF");
		if (f.exists()) {
			Assert.assertTrue(f.isDirectory());
			File son = new File(f, "MANIFEST.MF");
			Assert.assertTrue(son.exists());
			File df = new File(dirabsolutePath + "/src/resources");
			// File jarfile = new File("D:/1/pedynamic.jar");
			File pf = new File(dirabsolutePath + "/pom.xml");
			FileUtils.moveDirectoryToDirectory(f, df, true);
			// move finish

			JarNameSplitter js = new JarNameSplitter();
			String[] av = js.getAvByJar(jarfile);
			String g = "com.csii.gyns.local";
			StringBuffer sb = new StringBuffer(
					"<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n  <modelVersion>4.0.0</modelVersion>\n    <groupId>");
			sb.append(g);
			sb.append("</groupId>\n\t<artifactId>");
			sb.append(av[0]);
			sb.append("</artifactId>\n    <version>");
			sb.append(av[1]);
			sb.append("</version>\n  \n  <packaging>jar</packaging>\n  \n</project>");
			Assert.assertTrue(!pf.exists());
			pf.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(pf));
			bw.write(sb.toString());
			bw.flush();
			bw.close();
		}

	}

	@Test
	public void scanbeanxmlconflict() {
		BeanRefChanger b = new BeanRefChanger("D:/workpla/ee/24thMay/PE10code/pe-gyns-parent");
		List<File> allBeanXml = b.getAllBeanXml();
		SqlxmlConflictNameScanner sc = new SqlxmlConflictNameScanner("");
		sc.scanNoutOtherExtensionFilesBean(allBeanXml);
	}

	@Test
	public void scanBeanNotFound() {
		BeanRefChanger b = new BeanRefChanger("D:/workpla/ee/24thMay/PE10code/pe-gyns-parent");
		b.scanNoutNotFoundBean();
	}

	@Test
	public void beanrefchan() throws IOException {
		BeanRefChanger b = new BeanRefChanger("D:/1/11ni/pe-gyns-parent/pe-gyns-router/pe-gyns-router-direct");
		b.deal();
	}

	@Test
	public void addbakOnDynamicFile() throws IOException {
		ExtensionFileFinder ef = new ExtensionFileFinder(
				"D:/1/11ni/pe-gyns-parent/pe-gyns-router/pe-gyns-router-direct", "xml");
		List<File> fs = ef.scanNOutput();
		fs.forEach(f -> {
			if (f.getName().matches("dynamicservice\\d*\\.xml")) {
				String p = f.getAbsolutePath();
				p = p + ".bak";
				try {
					FileUtils.moveFile(f, new File(p));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Test
	public void canclebakOnDynamicFile() throws IOException {
		ExtensionFileFinder ef = new ExtensionFileFinder(
				"D:/1/11ni/pe-gyns-parent/pe-gyns-router/pe-gyns-router-direct", "bak");
		List<File> fs = ef.scanNOutput();
		fs.forEach(f -> {
			if (f.getName().matches("dynamicservice\\d*\\.xml\\.bak")) {
				String p = f.getAbsolutePath();
				p = p.substring(0, p.lastIndexOf(".bak"));
				try {
					FileUtils.moveFile(f, new File(p));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * delete 'ecif.' prefix in xml
	 * 
	 * @throws IOException
	 */
	@Test
	public void delEcifPrefix() throws IOException {
		ExtensionFileFinder e = new ExtensionFileFinder("D:/workpla/ee/24thMay/PE10code", "xml");
		// ExtensionFileFinder e = new ExtensionFileFinder("D:/1", "xml");
		List<File> fs = e.scanNOutput();
		fs.forEach(f -> {
			try {
				SAXReader r = getUnderlineSaxreader();
				Document d = r.read(f);
				String enco = d.getXMLEncoding();
				Long len = f.length();
				byte[] buf = new byte[len.intValue()];
				FileInputStream fis = new FileInputStream(f);
				fis.read(buf);
				fis.close();

				String s = new String(buf, enco);
				s = s.replace("<ref name=\"cifService\">ecif.CIFService</ref>",
						"<ref name=\"cifService\">.CIFService</ref>");
				FileOutputStream os = new FileOutputStream(f);
				byte[] o = s.getBytes(enco);
				os.write(o);
				os.flush();
				os.close();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}

	/**
	 * add 'Map' between ref text value in xml
	 * 
	 * @throws Exception
	 */
	@Test
	public void addMapvariantInxml() throws Exception {
		ExtensionFileFinder e = new ExtensionFileFinder(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-share/pe-gyns-share-mcs-service/src/main/resources/META-INF/config",
				"xml");
		Pattern p = Pattern.compile("<ref name=\"sqlMap\">\\.?[a-zA-Z0-9]+(?=SqlExecutor</ref>)");
		List<File> fs = e.scanNOutput();
		for (File f : fs) {
			SAXReader r = getUnderlineSaxreader();
			Document d = r.read(f);
			String enco = d.getXMLEncoding();
			Long len = f.length();
			byte[] buf = new byte[len.intValue()];
			StringBuffer sb = new StringBuffer();
			int cur = 0;

			FileInputStream fis = new FileInputStream(f);

			fis.read(buf);
			fis.close();
			String content = new String(buf, enco);
			Matcher m = p.matcher(content);
			while (m.find()) {
				int cut = m.end() + 3;
				sb.append(content.substring(cur, cut));
				sb.append("Map");
				cur = cut;
			}
			sb.append(content.substring(cur));
			FileOutputStream os = new FileOutputStream(f);
			byte[] o = content.getBytes(enco);
			os.write(o);
			os.flush();
			os.close();

		}

	}

	/**
	 * find sqlxml's name conflict
	 */
	@Test
	public void sqlxmlconfli() {
		SqlxmlConflictNameScanner s = new SqlxmlConflictNameScanner("D:/workpla/ee/24thMay/PE10code");
		s.scanNout();
	}

	/**
	 * add sql.xml 's namespace in all java files in a directory. e.g.,
	 * 'this.sqlMap.query("balabala",m)' change to
	 * 'this.sqlMap.query("transfer.balabala",m)'
	 * 
	 * @throws IOException
	 */
	@Test
	public void addNamespaceInJava() throws IOException {
		SqlmapFunctionNamespaceAdder sf = new SqlmapFunctionNamespaceAdder("eacmgmt",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-ent/pe-gyns-service-ent-acmgmt");
		SqlmapFunctionNamespaceAdder sf2 = new SqlmapFunctionNamespaceAdder("pcreditcard",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-creditcard");
		SqlmapFunctionNamespaceAdder sf3 = new SqlmapFunctionNamespaceAdder("pacmgmt",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-acmgmt");
		SqlmapFunctionNamespaceAdder sf4 = new SqlmapFunctionNamespaceAdder("pcustomersvr",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-customersvr");
		SqlmapFunctionNamespaceAdder sf5 = new SqlmapFunctionNamespaceAdder("pquery",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-query");
		SqlmapFunctionNamespaceAdder sf6 = new SqlmapFunctionNamespaceAdder("ptelbankcreditcard",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-telbankcreditcard");
		SqlmapFunctionNamespaceAdder sf7 = new SqlmapFunctionNamespaceAdder("ptransfer",
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-transfer-impl");

		sf.dealDoubleQuoteParam();
		sf2.dealDoubleQuoteParam();
		sf3.dealDoubleQuoteParam();
		sf4.dealDoubleQuoteParam();
		sf5.dealDoubleQuoteParam();
		sf6.dealDoubleQuoteParam();
		sf7.dealDoubleQuoteParam();
	}

	@Test
	public void deletetarget() throws IOException {
		String dir = "D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-wxbank";
		TargetDeleter td = new TargetDeleter();
		td.delete(new File(dir));
	}

	/**
	 * similar in functionality to {@link #pedynamicruntimejardeal},target
	 * change to all lib directories
	 * 
	 * @throws Exception
	 */
	@Test
	public void allJarsInLibDirsDeal() throws Exception {
		BundleDependFiller bdf = new BundleDependFiller();
		ExtensionFileFinder f = new ExtensionFileFinder(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-wxbank/pe-gyns-wxbank-web-cmn/src/main/resources/lib",
				"jar");
		Map<File, List<File>> pomJarsMap = new HashMap<File, List<File>>();
		HttpMavenSearcher ms = new HttpMavenSearcher();
		NexusUploader nu = new NexusUploader("com.csii.gyns.local");

		List<File> jars = f.scanNOutput();
		if (null == jars || jars.isEmpty())
			throw new IOException("empty jar list");
		jars.forEach(j -> {
			try {
				File pom = bdf.getRelevantProjectPomFileByJarInLib(j);
				if (null == pom)
					throw new IOException("jar file doesnot match a pom file");
				if (pomJarsMap.containsKey(pom)) {
					List<File> jarList = pomJarsMap.get(pom);
					jarList.add(j);
				} else {
					List<File> jarListn = new LinkedList<File>();
					jarListn.add(j);
					pomJarsMap.put(pom, jarListn);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		});

		pomJarsMap.forEach((pom, jarsList) -> {
			if (null == jarsList || jarsList.isEmpty()) {
				try {
					throw new IOException();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<File> toUploadNexusList = new LinkedList<File>();
			List<String[]> gavToFillInPomList = new LinkedList<String[]>();
			jarsList.forEach(j -> {
				if (null != j) {
					String[] gav = null;
					try {
						gav = ms.getGavByJar(j);
						if (null == gav) {
							// add into to-upload file list
							toUploadNexusList.add(j);
						} else
							gavToFillInPomList.add(gav);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			try {
				Map<String, String[]> jarNameGavMap = null;
				if (null != toUploadNexusList && !toUploadNexusList.isEmpty()) {
					jarNameGavMap = nu.uploadSpecifiedFiles(toUploadNexusList);
				}
				if (null != jarNameGavMap && !jarNameGavMap.isEmpty()) {

					jarNameGavMap.forEach((jname, gav) -> {
						if (null != gav && gav.length > 0)
							gavToFillInPomList.add(gav);
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if (null != gavToFillInPomList && !gavToFillInPomList.isEmpty())
					bdf.fillDependency(pom, gavToFillInPomList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * target:"pedynamicruntime" directory , deal all jars in it(upload jar not
	 * found in mvnsearch,fill gav in pom including just uploaded )
	 * 
	 * @throws Exception
	 */
	@Test
	public void pedynamicruntimejardeal() throws Exception {
		String dir = "D:\\1\\1111";
		ExtensionFileFinder f = new ExtensionFileFinder(dir, "jar");

		HttpMavenSearcher ms = new HttpMavenSearcher();
		List<File> toup = new LinkedList<File>();
		List<String[]> tofillDepend = new LinkedList<String[]>();
		BundleDependFiller bd = new BundleDependFiller();
		NexusUploader nu = new NexusUploader("com.csii.gyns.local");
		List<File> li = f.scanNOutput();
		for (File fe : li) {
			if (null != fe) {
				String[] gav = ms.getGavByJar(fe);
				if (null == gav) {
					// add into to-upload file list
					toup.add(fe);
				} else
					tofillDepend.add(gav);
			}
		}

		// upload to nexus
		Map<String, String[]> succMap = nu.uploadSpecifiedFiles(toup);

		succMap.forEach((k, v) -> {
			if (null != v && v.length > 0)
				tofillDepend.add(v);
		});

		// fill dependency into parent pom
		bd.fillDependency(new File("D:\\1\\1111\\pom.xml"), tofillDepend);
	}
}
