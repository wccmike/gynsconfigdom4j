package sqlmapparamaddnamespaceinjava;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import gynsconfigdom4j.UnderlineDomRead;

public class SqlxmlConflictNameScanner implements UnderlineDomRead {
	private static final Pattern p = Pattern.compile("sql\\d*\\.xml");
	private String dir;
	/**
	 * k:crud tag's id value;v:project name
	 */
	private Map<String, String> m = new HashMap<String, String>();
	private List<File> fs = new LinkedList<File>();

	public SqlxmlConflictNameScanner(String dir) {
		this.dir = dir;
	}
	
	
	
public void scanNoutOtherExtensionFilesBean(List<File> li){
	StringBuffer sb=new StringBuffer();
	SAXReader reader = this.getUnderlineSaxreader();
	Set<String> set=new HashSet<String>();
	li.forEach(f->{
		try {
			String abpath = f.getAbsolutePath();
			Document doc = reader.read(f);
			Element root = doc.getRootElement();
			List<Element> es = root.elements();
			if (null != es && !es.isEmpty()) {

				String connsp = es.get(0).attributeValue("namespace");
				if(null==connsp){
					recurseOtherExtensionFilesBean(root,set,abpath,sb);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	});
	File txt = new File("D:/1/2.txt");
	if(null!=txt&&txt.exists()) txt.delete();
	BufferedWriter bw=null;
	try {
		txt.createNewFile();
		bw=new BufferedWriter(new FileWriter(txt));
		bw.write(sb.toString());
		bw.flush();
		bw.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}finally{
		if(null!=bw){
			try {
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
private void recurseOtherExtensionFilesBean(Element e,Set<String> set,String abpath,StringBuffer sb){
	String tag = e.getName();
	String clazz = e.attributeValue("class");
	String id = e.attributeValue("id");
	if(null!=clazz&&null!=id){
		if(set.contains(id)){
			sb.append(abpath+" 's "+"<"+tag+" id="+id+">\r\n");
		}else{
			set.add(id);
		}
	}else{
		List<Element> es = e.elements();
		if(es!=null&&!es.isEmpty()){
			for (Element ee : es) {
				recurseOtherExtensionFilesBean(ee,set,abpath,sb);
			}
		}
	}
}
	public void scanNout() {
		instantiate();
		SAXReader reader = this.getUnderlineSaxreader();

		this.fs.forEach(f -> {
			try {
				Document doc = reader.read(f);

				Element sqlmap = doc.getRootElement();
				String ns = sqlmap.attributeValue("namespace");
				// if <sqlMap> hasnot namespace attribute
				if (null == ns) {
					List<Element> crud = sqlmap.elements();
					for (Element e : crud) {
						String id = e.attributeValue("id");
						String ali = e.attributeValue("alias");
						if ((null == id || "".equals(id)) && (null == ali || "".equals(ali))) {
							System.out.println("!!!!!!!" + f.getAbsolutePath() + "!!!!!!!!");
							throw new RuntimeException("crud 's id attribute is null or empty str");
						}
						if (null == id)
							continue;
						String pn = getProjectNameBySqlxml(f);
						if (null == pn || "".equals(pn))
							throw new RuntimeException("cant get project name");
						// if oldmap already has this id
						if (this.m.containsKey(id)) {
							String opn = this.m.get(id);
							System.out.println("conflict: oldprojectname: " + opn);
							System.out.println("oldid: " + id);
							System.out.println("newprojectname: " + pn);
							System.out.println("newid: " + id);
							System.out.println("==========================");
							// if oldmap doesn't have this id
						} else
							this.m.put(id, pn);
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		System.out.println("succeeded,app runs no error");
	}

	private String getProjectNameBySqlxml(File f) {
		if (null == f || !f.exists() || !f.getName().endsWith(".xml"))
			throw new RuntimeException("null file or extension isnot xml");
		String p = f.getAbsolutePath();
		p = p.substring(0, p.lastIndexOf("\\src"));
		return p.substring(p.lastIndexOf("\\") + 1);
	}

	private void instantiate() {
		File f = new File(dir);
		recurseDir(f);
	}

	private void recurseDir(File f) {
		File[] fs = f.listFiles();
		for (int i = 0; i < fs.length; i++) {
			if (fs[i].isDirectory()) {
				if ("src".equalsIgnoreCase(fs[i].getName())) {
					File nf = new File(fs[i], "main/resources/META-INF/config/sql-mapping");
					if (null != nf && nf.isDirectory()) {
						File[] nfs = nf.listFiles();
						for (int j = 0; j < nfs.length; j++) {
							if (!nfs[j].isDirectory() && p.matcher(nfs[j].getName()).matches()) {
								this.fs.add(nfs[j]);
							}
						}
					}
				} else
					recurseDir(fs[i]);
			}
		}
	}
}
