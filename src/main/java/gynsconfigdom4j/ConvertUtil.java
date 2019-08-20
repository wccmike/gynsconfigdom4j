package gynsconfigdom4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Assert;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConvertUtil implements UnderlineDomRead{

	private static Set<File> set = new HashSet<File>();
	private static Set<File> recurseConfigSet = new HashSet<File>();

	public void dealDir(String dir) throws Exception {
		enterProjectOutterDir(new File(dir));
		for (File f : recurseConfigSet) {
			innerRecurse(f);
		}
	}

	public void innerRecurse(File dir) throws Exception {
		// String tobecodepath="D:/1";
		// String encode = URLEncoder.encode(tobecodepath, "UTF-8");
		// getAllCorrenspondFiles("D:/workpla/ee/defaultcopy - 副本
		// (2)/PE10code/pe-gyns-parent/pe-gyns-fsg/pe-gyns-fsg-pweb-ptransfer/src/main/resources/META-INF");
		// getAllCorrenspondFiles("D:/workpla/ee/defaultcopy - 副本
		// (2)/PE10code/pe-gyns-parent/pe-gyns-fsg/pe-gyns-fsg-pweb-pquery/src/main/resources/META-INF/config");
		getAllCorrenspondFiles(dir.getAbsolutePath());
		for (File f : this.set) {
			String name = f.getName();
			// if(name.equals("uploadBatchBigTrs.xml")){
			// System.out.println("wwrong");
			// }
			// System.out.println(name);
			testfile(f);
		}
		set.clear();
	}

	private void testfile(File file) throws Exception {
		SAXReader reader = new SAXReader();
		reader.setEntityResolver(new EntityResolver() {

			@SuppressWarnings("deprecation")
			@Override
			public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
				InputSource is = new InputSource(new StringBufferInputStream(""));
				is.setPublicId(publicId);
				is.setSystemId(systemId);
				return is;
			}
		});
		Document doc = reader.read(file);
		// 若doctype声明是system，且systemid是特定几个dtd
		if (isSegment(doc))
			return;
		String encoding = doc.getXMLEncoding();
		if (null == encoding || "".equals(encoding))
			encoding = "UTF-8";
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
		Matcher m = pattern.matcher(oContent);
		String newhead = "<spring:beans\nxmlns:spring=\"http://www.springframework.org/schema/beans\"\nxmlns=\"http://www.csii.com.cn/schema/pe\"\nxmlns:util=\"http://www.springframework.org/schema/util\"\nxmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\nxsi:schemaLocation=\"http://www.springframework.org/schema/beans\nhttp://www.springframework.org/schema/beans/spring-beans.xsd\nhttp://www.springframework.org/schema/util\nhttp://www.springframework.org/schema/util/spring-util.xsd\nhttp://www.csii.com.cn/schema/pe\nhttp://www.csii.com.cn/schema/pe/pe-1.0.xsd\">";
		if (m.find()) {

			oContent = oContent.replaceAll(reg, newhead);
			oContent += "\r\n</spring:beans>";
		} else if (!rootNodeHasNamespace(doc)) {
			// find ?xml or if that doesnot exist,add to start of file
			Matcher matcherHeader = Pattern.compile("<\\?xml\\s+version=\"1.0\"\\s+encoding=.+\\?>").matcher(oContent);
			if (matcherHeader.find()) {
				String pre = oContent.substring(0, matcherHeader.end() + 1);
				String post = oContent.substring(matcherHeader.end() + 1);
				oContent = pre + "\r\n" + newhead + "\r\n" + post + "\r\n</spring:beans>";
			} else {
				oContent = newhead + "\r\n" + oContent + "\r\n</spring:beans>";
			}
		}

//		FileWriter fw = new FileWriter(file.getAbsoluteFile());
//		BufferedWriter bw = new BufferedWriter(fw);
//		bw.write(oContent);
//		bw.close();
		FileOutputStream os = new FileOutputStream(file.getAbsoluteFile());
		byte[] o = oContent.getBytes(encoding);
		os.write(o);
		os.flush();
		os.close();
	}

	private boolean rootNodeHasNamespace(Document doc) throws IOException {
		if (null == doc)
			throw new IOException("document empty");
		Element r = doc.getRootElement();
		if (null == r)
			throw new IOException("root doesn't exist");
		String uri = r.getNamespaceURI();
		if (null == uri)
			throw new IOException("namespaceuri null ptr");
		return !"".equals(uri);
	}

	private boolean judgeIfChanged(String oContent) {
		return oContent.indexOf("</spring:beans>") != -1;
	}

	private void getAllCorrenspondFiles(String dir) throws UnsupportedEncodingException {
		File dirPath = new File(dir);
		File[] files = dirPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 若是文件夹
			if (files[i].isDirectory()) {
				// 同时若是名为以下的文件夹
				if ("sql-mapping".equalsIgnoreCase(files[i].getName())) {
					continue;
				}
//				if ("mca".equalsIgnoreCase(files[i].getName())) {
//					recurseRenameBak(files[i]);
//					continue;
//				}
				String anotherPath = files[i].getPath();
				getAllCorrenspondFiles(anotherPath);

				// 若不是文件夹
			} else {
				String filename = files[i].getName();
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
	}

	private boolean isSegment(Document doc) throws IOException {
		// String name = doc.getRootElement().getName();
		if (null == doc)
			throw new IOException("document is null ptr");
		boolean result = false;
		DocumentType docType = doc.getDocType();
		if (null != docType) {
			String systemID = docType.getSystemID();
			String publicID = docType.getPublicID();
			if ("packetutf8.dtd".equals(systemID) || "packet.dtd".equals(systemID)) {
				if (null == publicID)
					result = true;
			}
		}
		return result;
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

	private void enterProjectOutterDir(File generalDir) throws IOException {
		if (!generalDir.isDirectory())
			throw new IOException("要求是目录，此参不是目录");
		// File generalDir = new File(generalDir);
		File[] innerFiles = generalDir.listFiles();
		boolean hasSrcChild = false;
		// 第一个for看孩子中是否存在src目录
		for (int i = 0; i < innerFiles.length; i++) {
			if (innerFiles[i].isDirectory() && "src".equalsIgnoreCase(innerFiles[i].getName())) {
				hasSrcChild = true;
				break;
			}
		}
		// 若上文结果为有，路径接上之后目录，加入类的Hashset
		if (hasSrcChild) {
			String newpath = generalDir + File.separator + "src" + File.separator + "main" + File.separator
					+ "resources" + File.separator + "META-INF" + File.separator + "config";
			File file = new File(newpath);
			if (file.exists())
				this.recurseConfigSet.add(file);

		} else {
			// 把孩子中除.settings目录外的目录递归
			for (int i = 0; i < innerFiles.length; i++) {
				if (innerFiles[i].isDirectory()) {
					if (".settings".equalsIgnoreCase(innerFiles[i].getName())) {
						continue;
					}
					enterProjectOutterDir(innerFiles[i]);
				}
			}
		}

	}
	/**input: 1 dmconfig.xml 
	 * output:  resources/meta-inf/config dir file in the same project
	 * @param dmconfig
	 * @return
	 */
	public File getConfigDirByDmconfigxml(File dmconfig){
		Assert.assertTrue(dmconfig.isFile());
		Assert.assertTrue(dmconfig.getName().endsWith(".xml"));
		File f = dmconfig.getParentFile();
		Assert.assertNotNull(f);
		return new File(f.getParentFile(),"config");
	}
	
	/**
	 * @param dmconfig 1 dmconfig.xml
	 * @return the project name of this dmconfig.xml
	 */
	public String getProjectnameByDmconfigxml(File dmconfig){
		Assert.assertTrue(dmconfig.isFile());
		Assert.assertTrue(dmconfig.getName().endsWith(".xml"));
		String p = dmconfig.getAbsolutePath();
		p=p.substring(0, p.lastIndexOf("src")-1);
		return p.substring(p.lastIndexOf("\\")+1);
	}
	
	/**input: 1 config dir
	 * output: all bean defination xmls(exclude sql-map and xmlmessage)
	 * @param dir
	 * @return
	 * @throws Exception
	 */
	public List<File> getBeanDefinationXmlsInOneConfigDir(File dir) throws Exception {
		getAllCorrenspondFilesReal(dir.getAbsolutePath());
		List<File> list=new LinkedList<File>(set);
		set.clear();
		return list;
	}

	private void getAllCorrenspondFilesReal(String dir) throws Exception{

		File dirPath = new File(dir);
		File[] files = dirPath.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 若是文件夹
			if (files[i].isDirectory()) {
				// 同时若是名为以下的文件夹
				if ("sql-mapping".equalsIgnoreCase(files[i].getName())) {
					continue;
				}
//				if ("mca".equalsIgnoreCase(files[i].getName())) {
//					recurseRenameBak(files[i]);
//					continue;
//				}
				String anotherPath = files[i].getPath();
				getAllCorrenspondFilesReal(anotherPath);

				// 若不是文件夹
			} else {
				String filename = files[i].getName();
				if (isReadyToDisable(filename)) {
					renameBak(files[i]);
					continue;
				}
				if (isReadyToSkip(filename)) {
					continue;
				}
				if(filename.matches(".+\\.xml")){
					SAXReader reader = getUnderlineSaxreader();
					Document doc = reader.read(files[i]);
					if(isSegment(doc)){
						continue;
					}
				}
				if (filename.matches(".+\\.xml"))
					this.set.add(files[i]);
			}
		}
	
		
	}
	/** add projectname as prefix to dmconfig.xml's every vars
	 * PS:every time check the var's name whether is changed before or not
	 * @param dmconfig 1 dmconfig.xml file
	 * @param projectname 
	 * @return original vars' name(only those changed this time)
	 * @throws DocumentException 
	 * @throws IOException 
	 */
	public List<String> addProjectnamePrefixToDmconfigxml(File dmconfig,String projectname) throws DocumentException, IOException{
		List<String> list=new LinkedList<String>();
		Assert.assertTrue(dmconfig.isFile());
		Assert.assertTrue(dmconfig.getName().endsWith(".xml"));
		SAXReader reader = getUnderlineSaxreader();
		Assert.assertNotNull(reader);
		Document doc = reader.read(dmconfig);
		Assert.assertNotNull(doc);
		String encoding = doc.getXMLEncoding();
		
		Element root = doc.getRootElement();
		Assert.assertNotNull(root);
		List<Element> propdefs = root.elements("propdef");
		if(null!=propdefs&&!propdefs.isEmpty()){
			
			propdefs.stream().forEach(propdef -> {
				List<Element> propertiess = propdef.elements("properties");
				if(null!=propertiess&&!propertiess.isEmpty()){
					propertiess.stream().forEach(propert->{
						List<Element> propdefentrys = propert.elements("propdefentry");
						if(null!=propdefentrys&&!propdefentrys.isEmpty()){
							propdefentrys.stream().forEach(propdefen->{
								String key = propdefen.attributeValue("name");
								Assert.assertNotNull(key);
								Assert.assertTrue(!"".equals(key));
								if(!key.startsWith(projectname+".")){
									list.add(key);
									propdefen.addAttribute("name",projectname+"."+key);
								}
							});
						}
					});
				}
			});
		}
		FileOutputStream os = new FileOutputStream(dmconfig);
		OutputFormat fm = OutputFormat.createPrettyPrint();
		fm.setEncoding(doc.getXMLEncoding());
		XMLWriter wt = new XMLWriter(os, fm);
		wt.write(doc);
		wt.close();
		return list;
	}
	/** add project name as prefix in every '${xxx}'
	 * @param xmls xml files need to change
	 * @param origins original vars' names
	 * @param projectname project name as prefix
	 */
	public void addProjectnamePrefixToXmlbeanDefinationFiles(List<File> xmls,List<String> origins,String projectname){
		Assert.assertNotNull(xmls);
		Assert.assertNotNull(origins);
		Assert.assertNotNull(projectname);
		Assert.assertTrue(!xmls.isEmpty());
		Assert.assertTrue(!origins.isEmpty());
		Assert.assertTrue(!"".equals(projectname));
		xmls.stream().forEach(xml->{
			SAXReader reader = getUnderlineSaxreader();
			
			Long len = xml.length();
			byte[] buf=new byte[len.intValue()];
			FileInputStream fis=null;
			FileOutputStream fos=null;
			try {
				Document doc = reader.read(xml);
				String encod = doc.getXMLEncoding();
				
				fis = new FileInputStream(xml);
				fis.read(buf);
				fis.close();
				String text = new String(buf,encod);
				for(String rep:origins){
					text=text.replace("${"+rep+"}", "${"+projectname+"."+rep+"}");
				}
				fos=new FileOutputStream(xml);
				fos.write(text.getBytes(encod));
				fos.flush();
				fos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				if(null!=fis){
					try {
						fis.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if(null!=fos){
					try {
						fos.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
}
