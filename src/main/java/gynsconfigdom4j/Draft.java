package gynsconfigdom4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

public class Draft implements UnderlineDomRead {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "Export-Package: com.csii.mca.datamodel.per.loanmgmt,\r\n com.csii.mca.service.per.loanmgmt";
		s = s.replaceAll(" ", "");
		s = s.replaceAll("\\r", "").replaceAll("\\n", "");
		// s=s.replaceAll("\\n", "");
		String[] sp = s.split(",");
		// System.out.println(Arrays.asList(sp));
		for (int i = 0; i < sp.length; i++) {
			System.out.println("the " + i + " th is:" + sp[i]);
		}
	}
	@Test
	public void hashst()throws Exception{
		ShaHasher s = new ShaHasher();
		String sha1 = s.sha1(new File("D:/workpla/mavenRepository/asm/asm/3.2/asm-3.2.jar"));
//		String sha2 = s.sha1(new File("D:/11/w.war"));
//		String sha2 = s.sha1(new File("D:\\workpla\\ee\\24thMay\\PE10code\\pe-gyns-parent\\pe-gyns-wxbank\\pe-gyns-wxbank-web\\target\\wxbank.war"));
		System.out.println(sha1);
//		System.out.println(sha2);
	}
	@Test
	public void settolist(){
		Set<String> s=new LinkedHashSet<String>();
		s.add("b");
		s.add("a");
		s.add("r");
		s.add("k");
		List<String> l=new ArrayList<String>(s);
		System.out.println(l);
//		for (String ss : l) {
//			System.out.println(ss);
//		}
	}
	@Test
	public void emptylen(){
		System.out.println("".length());
	}
	@Test
	public void indexof(){
		String s="im:feeg:ww";
		int i = s.indexOf(":");
		System.out.println(i);
		int i2 = s.indexOf(":", i+1);
		System.out.println(i2);
	}
	@Test
	public void outimportpack() throws Exception {
		Set<String> set = new HashSet<String>();
		BundleDependFiller b = new BundleDependFiller();
		// String dir =
		// "D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-fsg";
		String dir = "D:/workpla/ee/24thMay/PE10code";
		TargetDeleter td = new TargetDeleter();
		td.delete(new File(dir));
		Searcher searcher = new Searcher(dir, "MANIFEST.MF", false);
		searcher.scan();
		File[] mfs = searcher.outputFiles();
		for (int i = 0; i < mfs.length; i++) {
			if (null != mfs[i]) {
				String[] ss = b.getImportValueFromOneMF(mfs[i]);
				if (null != ss) {
					for (int j = 0; j < ss.length; j++) {
						if (null != ss[j] && !ss[j].startsWith("com.csii")) {

							set.add(ss[j]);
						}
					}
				}
			}
		}
		for (String str : set) {
			System.out.println(str);
		}
	}

	@Test
	public void t1() throws Exception {
		// File file=new
		// File("D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-per/pe-gyns-service-per-loanmgmt/src/main/resources/META-INF/MANIFEST.MF");
		File file = new File("D:/file.txt");
		FileInputStream fis = new FileInputStream(file);
		Long length = file.length();
		byte[] old = new byte[length.intValue()];
		fis.read(old);
		fis.close();
		String oldText = new String(old, "UTF-8");
		// System.out.println("primitive:"+oldText);
		// String nonblank=oldText.replaceAll("\\n", "").replaceAll("\\r", "");
		// System.out.println("coped:"+nonblank);
		Pattern p2 = Pattern.compile("Export-Package:");
		Matcher m2 = p2.matcher(oldText);
		// if keyword exists
		if (m2.find()) {
			int start = m2.end();
			// Pattern
			// p=Pattern.compile("Export-Package:([\\w\\s\\d\\r\\n])+?[:]");
			// Pattern
			// p=Pattern.compile("Export-Package:.+\\r\\n.+\\r\\nBundle-Activator:");
			Pattern p = Pattern.compile("Export-Package:(.|\\r|\\n)+?:");
			Matcher m = p.matcher(oldText);
			// int end=m.find()?m.end():oldText.length();

			String newText = null;
			if (m.find()) {
				newText = oldText.substring(start, m.end());
				newText = newText.substring(0, newText.lastIndexOf("\n"));
				// System.out.println("matched:"+newText+"eof");
			} else {
				newText = oldText.substring(start);
				// System.out.println("test for matcher.end :"+newText);
			}
			newText = newText.replaceAll("\\r", "").replaceAll("\\n", "");
			newText = newText.replaceAll(" ", "");
			// System.out.println("start"+newText+"eof");
			String[] packs = newText.split(",");

		}
	}

	@Test
	public void nonvsax() throws Exception {
		File file = new File("d:/1/new.xml");
		SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
		InputSource in = new InputSource(new FileReader(file));
		DefaultHandler2 hd = new DefaultHandler2() {

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
				// TODO Auto-generated method stub
				System.out.println("element:" + qName);
			}

			@Override
			public void startDTD(String name, String publicId, String systemId) throws SAXException {
				// TODO Auto-generated method stub
				System.out.println("doctype:" + name + "publicid:" + publicId + "sys:" + systemId);
			}
		};

		DocumentBuilderFactory d = DocumentBuilderFactory.newInstance();
		DocumentBuilder b = d.newDocumentBuilder();
		b.setEntityResolver(hd);
		b.parse(in);
	}

	@Test
	public void tsax() throws Exception {
		File file = new File("d:/1/new.xml");
		SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
		InputSource in = new InputSource(new FileReader(file));
		DefaultHandler2 hd = new DefaultHandler2() {

			@Override
			public void startElement(String uri, String localName, String qName, Attributes attributes)
					throws SAXException {
				// TODO Auto-generated method stub
				System.out.println("element:" + qName);
			}

			@Override
			public void startDTD(String name, String publicId, String systemId) throws SAXException {
				// TODO Auto-generated method stub
				System.out.println("doctype:" + name + "publicid:" + publicId + "sys:" + systemId);
			}
		};
		sp.parse(in, hd);
	}

	@Test
	public void regxmlheader() {
		String oContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<bea";
		Matcher matcherHeader = Pattern.compile("<\\?xml\\s+version=\"1.0\"\\s+encoding=.+\\?>").matcher(oContent);
		if (matcherHeader.find()) {
			String pre = oContent.substring(0, matcherHeader.end() + 1);
			String post = oContent.substring(matcherHeader.end() + 1);
			System.out.print("pre:" + pre);
			System.out.print("post:" + post);
		}
	}

	@Test
	public void tnamesp() throws Exception {
		File file = new File("d:/1/new.xml");
		SAXReader reader = getUnderlineSaxreader();
		Document dom = reader.read(file);
		Element e = dom.getRootElement();
		String string = e.getNamespace().toString();
		String namespacePrefix = e.getNamespacePrefix();
		String namespaceURI = e.getNamespaceURI();// http://www.. or""
		String asXML = e.getNamespace().asXML();// xmlns="htt.. or xmlns=""
		String name = e.getNamespace().getName();
		String prefix = e.getNamespace().getPrefix();
		String text = e.getNamespace().getText();// same namespaceURI
		String uri = e.getNamespace().getURI();// same namespaceURI
		File file2 = new File("d:/1/pquery.EmploymentQryInfo.xml");
		SAXReader reader2 = getUnderlineSaxreader();
		Document dom2 = reader2.read(file2);
		Element e2 = dom2.getRootElement();
		String string2 = e2.getNamespace().toString();
		String namespacePrefix2 = e2.getNamespacePrefix();
		String namespaceURI2 = e2.getNamespaceURI();
		String asXML2 = e2.getNamespace().asXML();
		String name2 = e2.getNamespace().getName();
		String prefix2 = e2.getNamespace().getPrefix();
		String text2 = e2.getNamespace().getText();
		String uri2 = e2.getNamespace().getURI();
	}

	@Test
	public void t2() throws DocumentException {
		SAXReader reader = getUnderlineSaxreader();
		Document dom = reader.read(new File("d:/1/pquery.EmploymentQryInfo.xml"));
		// Document dom = reader.read(new File("d:/1/new.xml"));
		// String s = dom.getRootElement().element("artifactId").toString();
		// System.out.println(s);
		DocumentType t = dom.getDocType();
		String systemID = t.getSystemID();
		// String elementName = t.getElementName();
		// String name = t.getName();
		// short nodeType = t.getNodeType();
		// String nodeTypeName = t.getNodeTypeName();
		// String path = t.getPath();
		// String stringValue = t.getStringValue();
		// String text = t.getText();
		// String uniquePath = t.getUniquePath();
		String publicID = t.getPublicID();
	}

	@Test
	public void searchermf() {
		Searcher searcher = new Searcher(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-share/pe-gyns-share-cif-service/src/main/resources/META-INF/peconfig",
				"dmconfig.xml", false);
		searcher.scan();
		File[] files = searcher.outputFiles();
	}

	@Test
	public void regex() throws Exception {
		Pattern p = Pattern.compile("</dependencies>(\\s|\\r|\\n)+<(?!/dependencyManagement>)");
		File file = new File("src/test/resources/new1.txt").getCanonicalFile();
		Long len = file.length();
		FileInputStream fis = new FileInputStream(file);
		byte[] buffer = new byte[len.intValue()];
		fis.read(buffer);
		fis.close();
		String s = new String(buffer, "UTF-8");
		Matcher m = p.matcher(s);
		System.out.println(m.find());
	}
	@Test
	public void mokiller() throws Exception {
//		 mvn -s D:/software/apache-maven-3.5.0/conf/settings.xml
//				 deploy:deploy-file -DgroupId=x -DartifactId=1111111111 -Dversion=1.0
//				 -Dpackaging=jar -Dfile=myja.jar
//				 -Durl=http://118.242.36.102:35678/nexus/content/repositories/releases
//				 -DrepositoryId=deployRelease -DpomFile=*.pom -DgeneratePom=false
		String dir = "C:/Users/q/Desktop/mo";
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
		bd.fillDependency(new File("C:/Users/q/Desktop/mo/pom.xml"), tofillDepend);
	}
}
