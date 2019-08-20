package gynsconfigdom4j;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import domain.Depend;
import domain.MockNetIODelay;
import dynamicref.BeanRefChanger;

public class Testdraft implements UnderlineDomRead {
	private static final String FILEPATH = "D:\\creditcard.xml";
	private static final String FILEPATHOUT = "D:\\creditcard2.xml";
	private static final String FILELOG = "D:\\notfound.txt";
	private static Set<File> set = new HashSet<File>();
	public class Stud{
		public int age;
	}
	@Test
	public void tertutil() throws Exception{
		List<Integer>[] lis=new LinkedList[10];
		lis[0]=new LinkedList<Integer>();
		
		System.out.println(Math.floor(Math.log10(10000.0)));
		int[] is=new int[1024];
		is[0]=4;
		is[1]=6;
		int[][] ms=new int[1024][2];
		ms[0]=new int[]{3,4};
		
		System.out.println(Arrays.toString(ms[0]));
		System.out.println(Arrays.toString(ms[1]));
		System.out.println(ms[1].length);
		System.out.println(ms.length);
//		 int count =0;
//	        Stack<Integer> positions = new Stack<Integer>();
//	        for(int tmp=n;0<tmp;tmp>>=1){
//	            if(tmp%2==1) {
//	                positions.push(count++);
//	                
//	            }
//	        }
//	        List<Integer> answer=new LinkedList<Integer>();
//	        answer.add(count);
//	        while(!positions.empty()){
//	            answer.add(count-positions.pop());
//	        }
//	        System.out.println(answer);
//		File f = new File("D:/1/21.RQM");
//		System.out.println(f.getParent());
//		SAXReader r = this.getUnderlineSaxreader();
//		Document doc = r.read(f);
//		Element root = doc.getRootElement();
//		List<Element> ss = root.elements("String");
//		ss.stream().forEach(s->{
//			s.addAttribute("name", "v");
//		});
//		FileOutputStream out = new FileOutputStream(f);
//		OutputFormat format = OutputFormat.createPrettyPrint();
//		format.setEncoding("UTF-8");
//		XMLWriter writer = new XMLWriter(out, format);
//		writer.write(doc);
//		writer.close();
	}
	@Test
	public void testconvertutil() throws Exception{
//		String s="gwe<dfee${p.b}>few${p.b}dc";
//		String ns = s.replace("${p.b}", "${name.p.b}");
//		System.out.println(ns);
		ConvertUtil cu = new ConvertUtil();
		String s="D:/1/pe-gyns-mcs-core/src/main/resources/META-INF/peconfig/dmconfig.xml";
		File dmc = new File(s);
		File dir = cu.getConfigDirByDmconfigxml(dmc);
		String pn = cu.getProjectnameByDmconfigxml(dmc);
		List<String> sss = cu.addProjectnamePrefixToDmconfigxml(dmc, pn);
		List<File> fs = cu.getBeanDefinationXmlsInOneConfigDir(dir);
		cu.addProjectnamePrefixToXmlbeanDefinationFiles(fs,sss,pn);
//		sss.stream().forEach(System.out::println);
//		System.out.println(f.getAbsolutePath());
//		System.out.println(f.exists());
//		List<File> list = cu.getBeanDefinationXmlsInOneConfigDir(f);
//		list.forEach(fi-> System.out.println(fi.getName()) );
//		System.out.println(cu.getProjectnameByDmconfigxml(dmc));
//		File f = new File("d:/1/p.properties");
//		ResourceBundle r=new PropertyResourceBundle(new FileInputStream(f));
//		String s = r.getString("p-w-b.w");
//		System.out.println(s);
	}
	@Test
	public void readfindco() throws Exception{
		File f = new File("D:/1/1.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line=null;
		Set<String> set=new HashSet<String>();
		while(null!=(line=br.readLine())){
			String parse = line.trim().replace("\r\n", "");
			if(!"".equals(parse)){
				if(!set.contains(parse)){
					set.add(parse);
				}else{
					System.out.println("conflict: "+parse);
				}
			}
		}
		br.close();
	}
	@Test
	public void sqlmapdocstruct() throws DocumentException{
		File f = new File("D:/1/a.xml");
		SAXReader r = this.getUnderlineSaxreader();
		Document doc = r.read(f);
		Element sqlmap1 = doc.getRootElement();
		List<Element> list1 = sqlmap1.elements();
		list1.stream().filter(e -> "select".equals(e.getName()) || "insert".equals(e.getName())
				|| "update".equals(e.getName()) || "delete".equals(e.getName())).forEach(e -> {
					String idvalue = e.attributeValue("id");
					System.out.println(e.getName());
					System.out.println(idvalue+"\n==============");
				});
	}
	@Test
	public void generateDependencyForBatch() throws Exception{
		File f = new File("src/test/resources/depebatch.txt");
		BufferedReader br = new BufferedReader(new FileReader(f));
		String line=null;
		List<String> list=new LinkedList<String>();
		while(null!=(line=br.readLine())){
			if(line.startsWith("p")){
				list.add(line);
			}
		}
		StringBuffer sb = new StringBuffer();
		
		for (String str : list) {
			/*<dependency>
		<groupId>com.csii.gyns</groupId>
		<version>1.0.1</version>
	<artifactId>pe-gyns-base-cmn</artifactId>
	</dependency>*/
			sb.append("<dependency>\n<groupId>com.csii.gyns</groupId>\n<version>1.0.1</version>\n<artifactId>");
			sb.append(str);
			sb.append("</artifactId>\n</dependency>\n\n");
		}
		System.out.println(sb.toString());
	}
	@Test
	public void otesplit() throws Exception{
		//Spring-Context: META-INF/config/*.xml,META-INF/config/trs/*.xml,META-INF/config/mca/*.xml
		String s="fww";
		String[] sp = s.split(",");
		System.out.println(sp.length);
		for (int i = 0; i < sp.length; i++) {
			System.out.println(sp[i]);
		}
//		File f = new File("D:\\1\\11");
//		f.mkdir();
		File f = new File("D:\\1\\dynamicservice.xml");
		SAXReader r = this.getUnderlineSaxreader();
		Document d = r.read(f);
		Element ro = d.getRootElement();
		Iterator<Element> ite = ro.elementIterator("service");
		while(ite.hasNext()){
			Element e = ite.next();
			e.detach();
		}
		Iterator<Element> ite2 = ro.elementIterator("reference");
		while(ite2.hasNext()){
			Element e = ite2.next();
			e.detach();
		}
		FileOutputStream out = new FileOutputStream(f);
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter writer = new XMLWriter(out, format);
		writer.write(d);
		writer.close();
	}
	@Test
	public void olambda(){
//		List<Depend> l=new ArrayList<Depend>();
//		Depend d1 = new Depend();
//		d1.setAge(5);
//		Depend d2 = new Depend();
//		d2.setAge(58);
//		l.add(d2);
//		l.add(d1);
//		System.out.println(l);
//		l.sort((a,z)->a.getAge()-z.getAge());
//		System.out.println(l);
		
		Stud s1=new Stud();
		s1.age=82;
		Stud s2=new Stud();
		s2.age=8;
		List<Stud> list=new  ArrayList<Stud>();
		list.add(s1);
		list.add(s2);
		for (Stud stud : list) {
			System.out.println(stud.age);
		}
		list.sort((a,z) -> a.age-z.age);
		for (Stud stud : list) {
			System.out.println(stud.age);
		}
	}
	@Test
	public void ootwithcolondomparsete() throws Exception{
		
		//D:/workpla/ee/24thMay/gynsconfigdom4j/src/test/resources/rootwithcolondomparsetest.xml
		File f = new File("D:/workpla/ee/24thMay/gynsconfigdom4j/src/test/resources/rootwithcolondomparsetest.xml");
		SAXReader r = this.getUnderlineSaxreader();
		Document d = r.read(f);
		System.out.println(d.getRootElement().getNamespacePrefix());
//		System.out.println(d.getRootElement().getNamespaceURI());
		System.out.println(d.getRootElement().getName());
		System.out.println(d.getRootElement().element("config").attributeValue("namespace"));
		
		
	}
	@Test
	public void fweeef() throws Exception{
//		String e="p@ssw0rd123456789^$@@";
		String e="p@ssw0r$d1234567w^$";
		e=e.replace("$", "\\$");
		System.out.println(e);
		String ta="5";
		ta=ta.replaceFirst("5", e);
		
		System.out.println(ta);
	}
	@Test
	public void fwef() throws Exception{
		Pattern p = Pattern.compile("ff:");
		String input="ff: com.cs";
		Matcher m = p.matcher(input);
		if(m.find()){
			System.out.println(m.end());
		}
		System.out.println(input.indexOf(":",3));
	}
	@Test
	public void movetocorrectdircreatepom() throws Exception{
		File f = new File("D:/1/META-INF");
		File df = new File("D:/1/src/resources");
		File jf = new File("D:/1/pedynamic.jar");
		File pf = new File("D:/1/pom.xml");
		FileUtils.moveDirectoryToDirectory(f, df, true);
		
		// move finish
		
		JarNameSplitter js = new JarNameSplitter();
		String[] av = js.getAvByJar(jf);
		String g="com.csii.gyns.local";
		StringBuffer sb = new StringBuffer("<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">\n  <modelVersion>4.0.0</modelVersion>\n    <groupId>");
		sb.append(g);
		sb.append("</groupId>\n\t<artifactId>");
		sb.append(av[0]);
		sb.append("</artifactId>\n    <version>");
		sb.append(av[1]);
		sb.append("</version>\n  \n  <packaging>jar</packaging>\n  \n</project>");
		if(!pf.exists()) pf.createNewFile();
		BufferedWriter bw = new BufferedWriter(new FileWriter(pf));
		bw.write(sb.toString());
		bw.flush();
		bw.close();
		
		System.out.println("app finished without error");
	}
	@Test
	public void finddynamicservicesonname() throws Exception{
		Searcher sc = new Searcher("/wxban", "dynamicservice.xml", false);
		SAXReader r = getUnderlineSaxreader();
		sc.scan();
		File[] files = sc.outputFiles();
		for (int i = 0; i < files.length; i++) {
			Document doc = r.read(files[i]);
			Element ro = doc.getRootElement();
			List<Element> es = ro.elements();
			es.stream().filter(e -> e.getName().equals("<b"));
		}
	}
	@Test
	public void gotolable() {
		int i = 0;
		 la:{
			if (i == 3) {
				System.out.println("pre occur");
			}
			 for (; i < 5; i++) {
				System.out.println("now i is " + i);
				if (i == 3) {
					break la;
				}
			}
			if (i == 3) {
				System.out.println("post occur");
			}
		}
	}

	@Test
	public void num1() throws DocumentException {
		File x = new File("D:/1/chain.xml");
		Document doc = getUnderlineSaxreader().read(x);
		Element root = doc.getRootElement();
		List<Element> es = root.elements();
		String name = es.get(0).getName();
		// System.out.println(name);
		// String name4 = es.get(4).getName();
		// System.out.println(name4);
		List<Element> li = root.elements();
		Iterator<Element> ite = root.elementIterator();
		while (ite.hasNext()) {
			Element e = ite.next();
			System.out.println(e.getName());
		}
	}

	@Test
	public void charat() {
		String s = ".bb";
		if (s.charAt(0) == '.') {
			s = s.substring(1);
		}
		List<String> l = Arrays.asList("ni", "kan", "bu", "dao", "wo");
		List<String> li2 = l.stream().filter(st -> st.startsWith("n")).collect(Collectors.toList());
		li2.forEach(System.out::println);
	}

	@Test
	public void bolobj() {
		boolean[] bs = { false };
		System.out.println(bs[0]);
		changeboole(bs);
		System.out.println(bs[0]);
	}

	private void changeboole(boolean[] bs) {
		bs[0] = true;
	}

	@Test
	public void testbeanrefchan() throws IOException {
		BeanRefChanger b = new BeanRefChanger("D:/1");
		b.deal();
	}

	@Test
	public void ref() throws DocumentException {
		File f = new File("D:/1/ecifservices.xml");
		SAXReader r = this.getUnderlineSaxreader();
		Document doc = r.read(f);
		Element ro = doc.getRootElement();
		recurseNode(ro);

	}

	private void recurseNode(Element e) {
		List<Element> es = e.elements();
		if (null != es && !es.isEmpty()) {
			for (Element one : es) {
				this.recurseNode(one);
			}
		} else if ("ref".equals(e.getName())) {
			System.out.println("getname: " + e.getName());
			System.out.println(e.getText());
		}
	}

	@Test
	public void ors() throws DocumentException {
		File f = new File("D:/1/dynamicservice.xml");
		SAXReader r = this.getUnderlineSaxreader();
		Document doc = r.read(f);
		List<Element> es = doc.getRootElement().elements("reference");
		System.out.println("size: " + es.size());
		es.forEach(e -> {
			String bn = e.attributeValue("bean-name");
			String id = e.attributeValue("id");
			System.out.println("beanname:" + bn);
			System.out.println(id);
		});
	}

	@Test
	public void twosamefile() {
		File d1 = new File("D:/1");
		File d2 = new File("D:/1");
		System.out.println(d1.equals(d2));
	}

	@Test
	public void replslac() {
		String str = "c:\\\\geg\\\\bde\\yy\\nn";
		System.out.println(str);
		str = str.replaceAll("(?<!\\\\)\\\\(?!\\\\)", "\\\\\\\\");
		System.out.println(str);
	}

	@Test
	public void fffff() throws IOException {
		ExtensionFileFinder e = new ExtensionFileFinder("D:\\1", "xml");
		List<File> f = e.scanNOutput();
		Set<String> se = new HashSet<String>();
		f.forEach(ff -> {
			try {
				SAXReader r = getUnderlineSaxreader();
				Document d = r.read(ff);

				List<Element> es = d.getRootElement().elements();
				es.forEach(oe -> {
					String id = oe.attributeValue("id");
					if (id != null) {
						if (se.contains(id))
							System.out.println("conflict: id: " + id);
						else
							se.add(id);
					}
				});
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		});
	}

	@Test
	public void conspatt() {
		Pattern p = Pattern.compile("sql\\d*\\.xml");
		System.out.println(p.matcher("sqlgg.xml").matches());

	}

	@Test
	public void parentchild() {
		File f = new File("D:\\1");
		File f2 = new File(f, "src\\gweg\\gg\\n.txt");
		System.out.println(f2.exists());
		String p = f2.getAbsolutePath();
		p = p.substring(0, p.lastIndexOf("\\src"));
		System.out.println(p.substring(p.lastIndexOf("\\") + 1));
	}

	@Test
	public void endreg() {
		String s = "gge";
		Pattern p = Pattern.compile("gge");
		Matcher m = p.matcher(s);
		if (m.find()) {
			System.out.println(m.start());
		}
	}

	@Test
	public void sqlmapregex() {
		String content = "sqlMap\r\n.query(\"tttt.geer\",e);sqlMap\r\n.query(\"gewgew\",e);";
		// String content = new String(buf, "utf-8");
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
				sb.append(content.substring(cur, i) + "bala" + ".");
				cur = i;
			}
		}
		sb.append(content.substring(cur));
		System.out.println(sb.toString());
	}

	@Test
	public void plu() {
		int f = -17;
		int s = 0xff;
		System.out.println((f & s) == 0xef);
	}

	@Test
	public void bom() throws Exception {
		File f = new File("src/test/resources/bo.java").getCanonicalFile();
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

	}

	@Test
	public void lammap() {

		List<Integer> li = Arrays.asList(3, 2, 2, 7);
		// List<Integer> c =
		// li.stream().forEach(i->i*i).distinct().collect(Collectors.toList());
		IntSummaryStatistics ss = li.stream().mapToInt(x -> x).summaryStatistics();
		System.out.println("max" + ss.getMax());
		System.out.println("avg" + ss.getAverage());
		new Thread(() -> System.out.println("t")).start();
	}

	@Test
	public void multistream() {
		System.setProperty("java.util.concurrent.ForkJoinPool.commmon.parallelism", "2");
		// ForkJoinPool f = new ForkJoinPool(8);
		MockNetIODelay $1 = new MockNetIODelay();
		MockNetIODelay $2 = new MockNetIODelay();
		MockNetIODelay $3 = new MockNetIODelay();
		MockNetIODelay $4 = new MockNetIODelay();
		MockNetIODelay $5 = new MockNetIODelay();
		// MockNetIODelay $6 = new MockNetIODelay();
		// MockNetIODelay $7 = new MockNetIODelay();
		// MockNetIODelay $8 = new MockNetIODelay();
		// MockNetIODelay $9= new MockNetIODelay();
		// MockNetIODelay $10 = new MockNetIODelay();
		List<MockNetIODelay> li = new ArrayList<MockNetIODelay>();
		li.add($1);
		li.add($2);
		li.add($3);
		li.add($4);
		li.add($5);

		// li.add($6);
		// li.add($7);
		// li.add($8);
		// li.add($9);
		// li.add($10);
		li.parallelStream().forEach(n -> n.run());
		try {
			Thread.sleep(8000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ExecutorService ex = Executors.newFixedThreadPool(8);
		/*
		 * 1.0-1.4thread1.5concurrent1.6phaser1.7forkjoinpool1.8stream
		 * win10unregistered,idea,webstorm,msoffice,winrar,adobeps,vs, vmware,
		 */
		// for(int i=0;i<li.size();i++){
		// ex.execute(li.get(i));
		// }
		// ex.shutdown();
		// for(;;){
		// if(ex.isTerminated()){
		// System.out.println("end");
		// break;
		// }
		// }

	}

	@Test
	public void lambdadoublecolon() {
		List<String> li = Arrays.asList("apple", "banana", "caixukun", "dead", "egg", "fword");
		List<File> fli = new LinkedList<File>();
		li.parallelStream().forEach(System.out::println);
	}

	@Test
	public void ptrparentfile() throws IOException {
		File f = new File("src/test/resources/getrequirevaluefromonemf.mf").getCanonicalFile();
		System.out.println(f.getName());
		f = f.getParentFile();
		System.out.println(f.getName());
		f = f.getParentFile();
		System.out.println(f.getName());
	}

	@Test
	public void replace() {
		String s = "g\\d\\";
		s = s.replace('\\', '/');
		System.out.println(s);
	}

	@Test
	public void abpath() {
		File f = new File("src/test/resources/getrequirevaluefromonemf.mf");
		String s = f.getAbsolutePath();
		System.out.println(s);
	}

	@Test
	public void arrayinit() {
		String a = "d";
		String b = "y";
		String[] s = { a, b };
		System.out.println(Arrays.asList(s));
	}

	@Test
	public void cmdmvn() throws IOException {
		Runtime rt = Runtime.getRuntime();
		String cmd = "cmd /C mvn -help";
		// mvn -s D:/software/apache-maven-3.5.0/conf/settings.xml
		// deploy:deploy-file -DgroupId=x -DartifactId=1111111111 -Dversion=1.0
		// -Dpackaging=jar -Dfile=myja.jar
		// -Durl=http://118.242.36.102:35678/nexus/content/repositories/releases
		// -DrepositoryId=deployRelease
		// Process p = rt.exec(cmd, null, new
		// File("D:/software/apache-maven-3.5.0/conf"));
		Process p = rt.exec(cmd);
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while (null != (line = br.readLine()))
			System.out.println(line);
	}

	@Test
	public void multithread() throws InterruptedException {
		String[] shared = { "shareinit" };
		new Thread(() -> {
			try {
				System.out.println("from thread1 start share: " + shared[0]);
				String[] s = { "t1g", "t1a", "t1v" };
				System.out.println("this is thread1 " + Arrays.asList(s));
				shared[0] = "thread1 modify";
				System.out.println("from thread1 share: " + shared[0]);
				Thread.sleep(2000);
				System.out.println("from thread1 awake share: " + shared[0]);
				System.out.println("this is thread1 awake " + Arrays.asList(s));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();

		new Thread(() -> {
			try {
				System.out.println("from thread2 start share: " + shared[0]);
				String[] s = { "t2g", "t2a", "t2v" };
				System.out.println("this is thread2 " + Arrays.asList(s));
				Thread.sleep(1000);
				shared[0] = "thread2 modify";
				System.out.println("from thread2 share: " + shared[0]);
				System.out.println("this is thread2 awake " + Arrays.asList(s));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		Thread.sleep(4000);
	}

	@Test
	public void initarray() {
		String[] s = null;
		s = new String[2];
		s[0] = "ee";
		s[1] = "eeb";
		s[2] = "eev";
		System.out.println(s.length);
	}

	@Test
	public void ioutilhttp() throws Exception {
		String url = "http://search.maven.org/solrsearch/select?q=1:%22d31b0bc93ebc95c6aa0cdb1b044701ba17429d78%22&rows=20&wt=json";
		String resp = IOUtils.toString(new URL(url), "UTF-8");
		// System.out.println(resp);
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
			// System.out.println("g:"+g+" a:"+a+" v:"+v);
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
			oc += "aa.jar cannot find";
			BufferedWriter bw = new BufferedWriter(new FileWriter(log));
			bw.write(oc);
			bw.close();

		}
		/*
		 * {"responseHeader":{"status":0,"QTime":0,"params":{"q":
		 * "1:\"d31b0bc93ebc95c6aa0cdb1b044701ba174
		 * 29d78\"","indent":"off","fl":"id,g,a,v,p,ec,timestamp,tags","sort":"
		 * score desc,timestamp desc ,g asc,a asc,v
		 * desc","rows":"20","wt":"json","version":"2.
		 * 2"}},"response":{"numFound":1,"start"
		 * :0,"docs":[{"id":"org.codehaus.jackson:jackson-core-asl:1.5.2","g":
		 * "org.codehaus.jackson","a":" jackson-core-asl","v":"1.5.
		 * 2","p":"jar","timestamp":1272233257000,"ec":["-sources.jar",".jar","
		 * .pom"],"tags":["jackson","json","performance","generator","high","processor","parser
		 * "]}]}}
		 */

		/*
		 * {"responseHeader":{"status":0,"QTime":0,"params":{"q":
		 * "1:\"d31b0bc93ebc95c6aa7cdb1b044701ba17429d78\
		 * "","indent":"off","fl":"id,g,a,v,p,ec,timestamp,tags","sort":"score
		 * desc,timestamp desc,g asc,a asc, v
		 * desc","rows":"20","wt":"json","version":"2.
		 * 2"}},"response":{"numFound":0,"start":0,"docs":[]}}
		 */
	}

	@Test
	public void json() throws Exception {
		ObjectMapper om = new ObjectMapper();
		Depend d = new Depend();
		d.setVersion("5.3.2");
		d.setNotfound("abandon");
		d.setGroup("facebook");
		d.setArtifact("machinelearning");
		String j = om.writeValueAsString(d);
		System.out.println(j);
		// LinkedHashMap m = om.readValue(j, LinkedHashMap.class);
		LinkedHashMap m = om.readValue(j, LinkedHashMap.class);
		System.out.println(m);
		// System.out.println("deserialize: "+o.toString());
		// JsonNode ro = om.readTree(j);
		// String t = ro.path("grou2p").asText();
		// System.out.println("t=null: "+t==null);
		// System.out.println("t.len: "+t.length());
		// System.out.println("grouptreetext: "+t);
	}

	@Test
	public void sha1() throws Exception {
		MessageDigest md = MessageDigest.getInstance("sha1");
		File f = new File(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-router/pe-gyns-router-cert-cfca/src/main/resources/lib2/dom4j-1.6.1.jar");
		FileInputStream is = new FileInputStream(f);
		byte[] bu = new byte[1024];
		int l = 0;
		while (-1 != (l = is.read(bu)))
			md.update(bu, 0, l);

		is.close();
		byte[] d = md.digest();
		System.out.println(Arrays.toString(d));
		System.out.println(d.length);// 40*4=160bit,20*8=160bit
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < d.length; i++) {
			String s = Integer.toHexString((d[i] & 0xff) + 0x100).substring(1);
			sb.append(s);
		}
		System.out.println(sb.toString());
		// String
		// url="http://search.maven.org/solrsearch/select?q=1:%22d31b0bc93ebc95c6aa0cdb1b044701ba17429d78%22&rows=20&wt=json";
		// String resp = IOUtils.toString(new URL(url), "UTF-8");

	}

	@Test
	public void testRecurse() throws Exception {
		// String tobecodepath="D:/1";
		// String encode = URLEncoder.encode(tobecodepath, "UTF-8");
		// getAllCorrenspondFiles("D:/workpla/ee/defaultcopy - 副本
		// (2)/PE10code/pe-gyns-parent/pe-gyns-fsg/pe-gyns-fsg-pweb-ptransfer/src/main/resources/META-INF");
		// getAllCorrenspondFiles("D:/workpla/ee/defaultcopy - 副本
		// (2)/PE10code/pe-gyns-parent/pe-gyns-fsg/pe-gyns-fsg-pweb-pquery/src/main/resources/META-INF/config");
		getAllCorrenspondFiles("D:/1");
		for (File f : this.set) {
			String name = f.getName();
			// if(name.equals("uploadBatchBigTrs.xml")){
			// System.out.println("wwrong");
			// }
			// System.out.println(name);
			testfile(f);
		}
	}

	@Test
	public void Testfun1() throws Exception {
		File file = new File(FILEPATH);
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new EntityResolver() {

			@SuppressWarnings("deprecation")
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				// TODO Auto-generated method stub
				InputSource is = new InputSource(new StringBufferInputStream(""));
				is.setPublicId(publicId);
				is.setSystemId(systemId);
				return is;
			}
		});
		// reader.setValidation(false);
		// reader.setIncludeExternalDTDDeclarations(true);
		// reader.setIncludeInternalDTDDeclarations(true);
		Document doc = reader.read(file);
		String xmlEncoding = doc.getXMLEncoding();
		// DocumentType docType = doc.getDocType();
		// System.out.println(docType.getInternalDeclarations());
		// String name = docType.getName();
		// String nodeTypeName = docType.getNodeTypeName();
		// docType.setDocument(null);
		// System.out.println(doc.getDocType());

		// 指定文件输出的位置
		FileOutputStream out = new FileOutputStream(FILEPATHOUT);
		OutputFormat format = OutputFormat.createPrettyPrint(); // 漂亮格式：有空格换行
		format.setEncoding(xmlEncoding);

		// 1.创建写出对象
		XMLWriter writer = new XMLWriter(out, format);
		doc.setDocType(null);
		// Element rootElement = doc.getRootElement();
		// reversionNodes(rootElement);
		// Document docnew = DocumentHelper.createDocument();
		// docnew.setXMLEncoding(xmlEncoding);
		// Namespace nsspring = new Namespace("spring",
		// "http://www.springframework.org/schema/beans");
		// Namespace schma = new Namespace("xsi",
		// "http://www.w3.org/2001/XMLSchema-instance");
		// Element springbeans = docnew.addElement(new QName("beans",
		// nsspring));
		// Element springbeans =
		// docnew.addElement("spring:beans","http://www.csii.com.cn/schema/pe");
		// Element springbeans = docnew.addElement("spring:bean",
		// "http://www.springframework.org/schema/beans");
		// springbeans.addText("a");
		// springbeans.addNamespace("", "http://www.csii.com.cn/schema/pe");
		// springbeans.addNamespace("util",
		// "http://www.springframework.org/schema/util");
		// springbeans.addNamespace("xsi",
		// "http://www.w3.org/2001/XMLSchema-instance");
		// springbeans.addNamespace("xsi:loca", "htt");
		// springbeans.addAttribute(new QName("schemaLocation", schma),
		// "http://www.springframework.org/schema/beans\nhttp://www.springframework.org/schema/beans/spring-beans.xsd\nhttp://www.springframework.org/schema/util\nhttp://www.springframework.org/schema/util/spring-util.xsd\nhttp://www.csii.com.cn/schema/pe\nhttp://www.csii.com.cn/schema/pe/pe-1.0.xsd");
		// rootElement.setQName(new QName("config", nsspring));
		// Element springbeans = docnew.addElement("spring:beans");
		// springbeans.addAttribute("xmlns:spring",
		// "http://www.springframework.org/schema/beans");
		// springbeans.addNamespace("", "http://www.csii.com.cn/schema/pe");
		//// springbeans.addAttribute("xmlns",
		// "http://www.csii.com.cn/schema/pe");
		// springbeans.addAttribute("xmlns:util",
		// "http://www.springframework.org/schema/util");
		// springbeans.addAttribute("xmlns:xsi",
		// "http://www.w3.org/2001/XMLSchema-instance");
		// springbeans.addAttribute("xsi:schemaLocation",
		// "http://www.springframework.org/schema/beans\nhttp://www.springframework.org/schema/beans/spring-beans.xsd\nhttp://www.springframework.org/schema/util\nhttp://www.springframework.org/schema/util/spring-util.xsd\nhttp://www.csii.com.cn/schema/pe\nhttp://www.csii.com.cn/schema/pe/pe-1.0.xsd");
		// String string = springbeans.getNamespace().toString();
		// List declaredNamespaces = springbeans.declaredNamespaces();
		// for (Object object : declaredNamespaces) {
		// Namespace e=(Namespace)object;
		//// String string = e.toString();
		// System.out.println(e.toString());
		// }
		// springbeans.add(rootElement);
		// 2.写出Document对象
		writer.write(doc);

		// 3.关闭流
		writer.close();
	}

	@Test
	public void Testfun2() throws Exception {
		File file = new File("D:\\creditcard2.xml");
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new EntityResolver() {

			@SuppressWarnings("deprecation")
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				// TODO Auto-generated method stub
				InputSource is = new InputSource(new StringBufferInputStream(""));
				is.setPublicId(publicId);
				is.setSystemId(systemId);
				return is;
			}
		});
		// reader.setValidation(false);
		// reader.setIncludeExternalDTDDeclarations(true);
		// reader.setIncludeInternalDTDDeclarations(true);
		Document doc = reader.read(file);
		Element rootElement = doc.getRootElement();

		String name = rootElement.getName();
		String string = rootElement.getNamespace().toString();
		String attributeValue = rootElement.attributeValue("xmlns");
		String attributeValue2 = rootElement.attributeValue("xmlns:xsi");
		String attributeValue3 = rootElement.attributeValue("xsi:schemaLocation");
		String attributeValue4 = rootElement.attributeValue("xmlns:spring");

	}

	@Deprecated
	private void reversionNodes(Element e) {
		e.addNamespace("spring", "");
		// e.addNamespace("", "http://www.csii.com.cn/schema/pe");
		// e.addNamespace("", "http://www.csii.com.cn/schema/pe");
		// e.addNamespace("util", "http://www.springframework.org/schema/util");
		Iterator ite = e.nodeIterator();
		while (ite.hasNext()) {
			Object nextElement = ite.next();
			if (nextElement instanceof Element)
				reversionNodes((Element) nextElement);

		}

	}

	public void testfile(File file) throws Exception {
		// if(file.getName().indexOf("peconfigdev.xml")!=-1){
		// System.out.println("correspond");
		// }
		// File file = new File(path);
		// File file = new File(FILEPATHOUT);
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new EntityResolver() {

			@SuppressWarnings("deprecation")
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				// TODO Auto-generated method stub
				InputSource is = new InputSource(new StringBufferInputStream(""));
				is.setPublicId(publicId);
				is.setSystemId(systemId);
				return is;
			}
		});
		// reader.setValidation(false);
		// reader.setIncludeExternalDTDDeclarations(true);
		// reader.setIncludeInternalDTDDeclarations(true);
		// InputStream ifile=new FileInputStream(file);
		// InputStreamReader ir=new InputStreamReader(ifile, "UTF-8");
		Document doc = reader.read(file);
		// 若根标签是segment
		if (isSegment(doc))
			return;
		String encoding = doc.getXMLEncoding();
		// if(!"UTF-8".equalsIgnoreCase(encoding)) System.out.println("warning:
		// 有不是utf-8的");
		// if file doesnt exists, then create it
		// if (!file.exists()) {
		// try {
		// file.createNewFile();
		// } catch (Exception e) {
		// // TODO: handle exception
		// e.printStackTrace();
		// }
		// }

		// String encoding = "UTF-8";
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		FileInputStream in = new FileInputStream(file);
		in.read(filecontent);
		in.close();
		String oContent = new String(filecontent, encoding);
		// 若头已改过
		if (judgeIfChanged(oContent))
			return;
		String reg = "<!DOCTYPE.+?>";
		Pattern pattern = Pattern.compile(reg);
		String newhead = "<spring:beans\nxmlns:spring=\"http://www.springframework.org/schema/beans\"\nxmlns=\"http://www.csii.com.cn/schema/pe\"\nxmlns:util=\"http://www.springframework.org/schema/util\"\nxmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\nxsi:schemaLocation=\"http://www.springframework.org/schema/beans\nhttp://www.springframework.org/schema/beans/spring-beans.xsd\nhttp://www.springframework.org/schema/util\nhttp://www.springframework.org/schema/util/spring-util.xsd\nhttp://www.csii.com.cn/schema/pe\nhttp://www.csii.com.cn/schema/pe/pe-1.0.xsd\">";
		oContent = oContent.replaceAll(reg, newhead);
		oContent += "\r\n</spring:beans>";

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(oContent);
		bw.close();

	}

	private boolean judgeIfChanged(String oContent) {
		return oContent.indexOf("</spring:beans>") == -1 ? false : true;
	}

	public void getAllCorrenspondFiles(String dir) throws UnsupportedEncodingException {
		// String decodeDir = URLDecoder.decode(dir, "UTF-8");
		// Set<File> set = new HashSet<File>();
		File dirPath = new File(dir);
		File[] files = dirPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 若是文件夹
			if (files[i].isDirectory()) {
				// 同时若是名为以下的文件夹
				if ("sql-mapping".equalsIgnoreCase(files[i].getName())) {
					// System.out.println("sqlmapping dir detached");
					continue;
				}
				if ("mca".equalsIgnoreCase(files[i].getName())) {
					recurseRenameBak(files[i]);
					continue;
				}
				String anotherPath = files[i].getPath();
				getAllCorrenspondFiles(anotherPath);

				// 若不是文件夹
			} else {
				String filename = files[i].getName();
				// if("peconfigdev.xml".equalsIgnoreCase(filename)){
				// System.out.println("break");
				// }
				if (isReadyToDisable(filename)) {
					renameBak(files[i]);
					continue;
				}
				if (isReadyToSkip(filename)) {
					continue;
				}
				if (filename.matches(".+\\.xml"))
					this.set.add(files[i]);
			}
		}
		// return set;
	}

	private boolean isSegment(Document doc) {
		String name = doc.getRootElement().getName();
		return "segment".equalsIgnoreCase(name);
	}

	private boolean isReadyToDisable(String filename) {
		boolean result = false;
		if ("dynamicservice.xml".equalsIgnoreCase(filename))
			result = true;
		return result;
	}

	private void renameBak(File f) {
		try {
			if (f.getName().matches(".+\\.xml")) {

				String newPath = f.getAbsolutePath() + ".bak";
				FileUtils.moveFile(f, new File(newPath));
			} else if (f.getName().matches(".+(\\.bak){2,}")) {
				String absolutePath = f.getAbsolutePath();
				absolutePath = absolutePath.substring(0, absolutePath.indexOf(".bak")) + ".bak";
				FileUtils.moveFile(f, new File(absolutePath));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void recurseRenameBak(File directory) {
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory())
				recurseRenameBak(files[i]);
			else
				renameBak(files[i]);
		}
	}

	private boolean isReadyToSkip(String filename) {
		boolean result = false;
		if ("dmconfig.xml".equalsIgnoreCase(filename))
			result = true;
		return result;
	}
}
