package dynamicref;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import gynsconfigdom4j.UnderlineDomRead;

public class BeanRefChanger implements UnderlineDomRead {
	private static final String REGEX = "dynamicservice\\d*\\.xml";
	public Map<File, List<File>> projectDynamicserviceFileMap = new HashMap<File, List<File>>();
	private SAXReader reader;
	private String dir;

	public BeanRefChanger(String dir) {
		super();
		this.dir = dir;
		this.reader = getUnderlineSaxreader();
	}

	public void scanNoutNotFoundBean() {
		List<File> allBeanXml = getAllBeanXml();
		if (null == allBeanXml || allBeanXml.isEmpty())
			return;
		Map<String, Set<String>> existingBeanNamespaceMap = createExistingBeanNamespaceMapFromAllBeanXml(allBeanXml);
		if (null == existingBeanNamespaceMap || existingBeanNamespaceMap.isEmpty())
			return;
		StringBuffer sb=new StringBuffer();
		allBeanXml.forEach(bx -> {
			try {
				Document doc = this.reader.read(bx);
				Element root = doc.getRootElement();
				List<Element> es = root.elements();
				if (null != es && !es.isEmpty()) {

					String connsp = es.get(0).attributeValue("namespace");
					connsp = (connsp == null) ? "." : connsp;
					recurseScanRefTag(root, existingBeanNamespaceMap, connsp, bx,sb);
					// Iterator<Element> ite = root.elementIterator();
					// while (ite.hasNext()) {
					// Element e = ite.next();
					// if ("ref".equals(e.getName()) && null !=
					// e.attributeValue("name")) {
					// String refName = e.getText();
					// if (null == refName)
					// throw new IOException("ref tag's text null");
					// String nsp = null;
					// if (-1 != refName.indexOf(".")) {
					// nsp = (0 == refName.indexOf(".")) ? "." :
					// refName.substring(0, refName.indexOf("."));
					// refName = refName.substring(refName.indexOf(".") + 1);
					// } else {
					// nsp = connsp;
					// }
					// // if not found
					// if (!existingBeanNamespaceMap.containsKey(refName)
					// || !existingBeanNamespaceMap.get(refName).contains(nsp))
					// {
					// System.out.println("not found ref value: namespace:" +
					// nsp + " name:" + refName);
					// System.out.println("file location: " +
					// bx.getAbsolutePath());
					// if (existingBeanNamespaceMap.containsKey(refName))
					// System.out.println("exist namespace : " +
					// existingBeanNamespaceMap.get(refName));
					// System.out.println("============================================");
					// }
					// }
					//
					// }
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		File txt = new File("D:/1/1.txt");
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

	private void recurseScanRefTag(Element e, Map<String, Set<String>> existingBeanNamespaceMap, String curNsp, File bx,StringBuffer sb)
			throws IOException {
		String tag = e.getName();
		String name = e.attributeValue("name");
		List<Element> child = e.elements();
		if (null != child && !child.isEmpty()) {
			for (Element one : child) {
				recurseScanRefTag(one, existingBeanNamespaceMap, curNsp, bx,sb);
			}
		} else if ("ref".equals(tag) && null != name) {
			String refName = e.getText();
			if (null == refName)
				throw new IOException("ref tag's text null");
			if(-1!=refName.indexOf("${")) return;
			String nsp = null;
			if (-1 != refName.indexOf(".")) {
				nsp = (0 == refName.indexOf(".")) ? "." : refName.substring(0, refName.indexOf("."));
				refName = refName.substring(refName.indexOf(".") + 1);
			} else {
				nsp = curNsp;
			}

			// if not found
			if (!existingBeanNamespaceMap.containsKey(refName)
					|| !existingBeanNamespaceMap.get(refName).contains(nsp)) {
//				sb.append("not found ref value: namespace:" + nsp + " name:" + refName+"\r\n");
//				sb.append("file location: " + bx.getAbsolutePath()+"\r\n");
//				if (existingBeanNamespaceMap.containsKey(refName))
//				sb.append("exist namespace : " + existingBeanNamespaceMap.get(refName)+"\r\n");
//				sb.append("============================================\r\n");
				if (existingBeanNamespaceMap.containsKey(refName)&&existingBeanNamespaceMap.get(refName).size()==1&& existingBeanNamespaceMap.get(refName).contains(".")){
					sb.append("not found ref value: namespace:" + nsp + " name:" + refName+"\r\n");
					sb.append("file location: " + bx.getAbsolutePath()+"\r\n");
					sb.append("exist namespace : " + existingBeanNamespaceMap.get(refName)+"\r\n");
					sb.append("============================================\r\n");
				}
			}

		}

	}

	/**
	 * @return k:bean's id or name; v:namespace collection,if no namespace,fill
	 *         in '.'
	 */
	public Map<String, Set<String>> createExistingBeanNamespaceMapFromAllBeanXml(List<File> allBeanXml) {
		Map<String, Set<String>> map = new HashMap<String, Set<String>>();
		// List<File> allBeanXml = getAllBeanXml();
		if (null != allBeanXml) {
			allBeanXml.forEach(x -> {
				try {
					Document doc = this.reader.read(x);
					Element root = doc.getRootElement();
					List<Element> es = root.elements();
					if (null != es && !es.isEmpty()) {

						String nsp = es.get(0).attributeValue("namespace");
						nsp = (nsp == null) ? "." : nsp;
						recurseCreateExisting(root, map, nsp);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			});
		}
		return map.isEmpty() ? null : map;
	}

	private void recurseCreateExisting(Element es, Map<String, Set<String>> map, String nsp) {
		String tag = es.getName();
		String clazz = es.attributeValue("class");
		List<Element> child = es.elements();
		if ("bean".equals(tag) || "transactionTemplate".equals(tag)||"sharedMap".equals(tag)||"transactionManager".equals(tag)||"executor".equals(tag)||null!=clazz) {
			String id = es.attributeValue("id");
			if (null == id) {
				id = es.attributeValue("name");
			}
			if (null != id) {
				if (map.containsKey(id)) {
					map.get(id).add(nsp);
				} else {
					Set<String> set = new HashSet<String>();
					set.add(nsp);
					map.put(id, set);
				}
			}
		} else if (null != child && !child.isEmpty()) {
			for (Element one : child) {
				recurseCreateExisting(one, map, nsp);
			}
		}
	}

	public List<File> getAllBeanXml() {
		List<File> result = new LinkedList<File>();
		List<File> res = getAllResDir();
		if (res != null) {
			res.forEach(re -> {
				try {
					List<File> oneProjectBeanXml = getAllNeedtochangeXmls(re);
					if (oneProjectBeanXml != null) {
						oneProjectBeanXml.forEach(f -> {
							// System.out.println(result);
							result.add(f);
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		return result.isEmpty() ? null : result;
	}

	public List<File> getAllResDir() {
		List<File> li = new LinkedList<File>();
		recurseGetAllResDir(new File(this.dir), li);
		return li.isEmpty() ? null : li;
	}

	public void recurseGetAllResDir(File d, List<File> li) {
		File[] fs = d.listFiles();
		if (null != fs && fs.length > 0) {

			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					if ("src".equals(fs[i].getName())) {
						File projectRes = new File(fs[i], "main\\resources");
						if (projectRes != null && projectRes.exists()) {
							li.add(projectRes);
						}
						return;
					} else {
						recurseGetAllResDir(fs[i], li);
					}
				}
			}
		}
	}

	public void deal() throws IOException {

		createProjectDynamicserviceFileMap();
		this.projectDynamicserviceFileMap.forEach((proResDir, dys) -> {
			Map<String, String> dic = getSearchMapFromSingleProjectDynamicFiles(dys);
			if (null != dic && !dic.isEmpty()) {
				try {
					List<File> allNeedtochangeXmls = getAllNeedtochangeXmls(proResDir);
					if (null != allNeedtochangeXmls && !allNeedtochangeXmls.isEmpty()) {
						allNeedtochangeXmls.forEach(xml -> {
							try {
								Document doc = this.reader.read(xml);
								String rootNamespace=null;
								if(doc.getRootElement().element("config")!=null) {
//									System.out.println(xml.getName());
									String tmp = doc.getRootElement().element("config").attributeValue("namespace");
									rootNamespace = tmp==null||"".equals(tmp)? null:tmp ;
								}
								String enco = doc.getXMLEncoding();
								if (enco == null)
									enco = "UTF-8";
								List<Boolean> changed = Arrays.asList(false);
								recurseChangeNodeFromDic(doc.getRootElement(), dic, changed,rootNamespace);
								if (changed.get(0)) {
									FileOutputStream out = new FileOutputStream(xml);
									OutputFormat format = OutputFormat.createPrettyPrint();
									format.setEncoding(enco);
									XMLWriter writer = new XMLWriter(out, format);
									writer.write(doc);
									writer.close();
								}
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/** 关于处理namespace的加减修饰全在此方法进行，在创“字典”的方法：如getSearchMapFromSingleProjectDynamicFiles中不对namespace相应加减，只存原样的dynamicservice中的写法
	 * @param e
	 * @param dic
	 * @param changed
	 * @param rootNamespace dom's root's namespace i.e.,"<config namespace='xx'>,if no namespace, this value is null ptr
	 * @throws IOException
	 */
	public void recurseChangeNodeFromDic(Element e, Map<String, String> dic, List<Boolean> changed,String rootNamespace) throws IOException {
		List<Element> es = e.elements();
		if (null != es && !es.isEmpty()) {
			for (Element one : es) {
				recurseChangeNodeFromDic(one, dic, changed,rootNamespace);
			}
		} else if ("ref".equals(e.getName()) && null != e.attributeValue("name")) {
			String dicId = e.getText();
			if (dicId == null)
				throw new IOException("ref tag's text is null");
			// ignore start with '.'
			if (dicId.charAt(0) == '.'){
				dicId = dicId.substring(1);
				// if ref value DONT contains '.' BETWEEN these chars, e.g. >SqlFactory</ref> and namespace has value
			}else if(!dicId.contains(".")&&rootNamespace!=null){
				dicId = rootNamespace+"."+dicId;
			}
			// if this ref's text is referred by my dictionary(id,beanname)
			if (dic.containsKey(dicId)) {
				String finalTargetRef = dic.get(dicId);
				//if finalTargetRef has '.' do nothing,if not add '.' at first char
				finalTargetRef=finalTargetRef.contains(".")?finalTargetRef:"."+finalTargetRef;
				// change it to value in dictionary
				e.setText(finalTargetRef);
				changed.set(0, true);
			}
		}
	}

	/**
	 * @param proResDir
	 *            'resources' dir
	 * @return
	 * @throws Exception
	 */
	public List<File> getAllNeedtochangeXmls(File proResDir) throws Exception {
		List<File> re = new LinkedList<File>();
		File config = new File(proResDir, "META-INF\\config");
		if (null != config && config.exists())
			recurseConfigAddXmls(config, re);
		return re.isEmpty() ? null : re;
	}

	/**
	 * @param d
	 *            'config' dir
	 * @param li
	 * @throws Exception
	 */
	public void recurseConfigAddXmls(File d, List<File> li) throws Exception {
		File[] fs = d.listFiles();
		if (null != fs && fs.length > 0) {

			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					if (!"sql-mapping".equals(fs[i].getName()))
						recurseConfigAddXmls(fs[i], li);
				} else if (fs[i].getName().endsWith(".xml") && !fs[i].getName().matches(REGEX)) {
					Document doc = this.reader.read(fs[i]);
					String namespacePrefix = doc.getRootElement().getNamespacePrefix();
					String rootpost = doc.getRootElement().getName();
					if (!isSegment(doc)&&"spring".equals(namespacePrefix)&&"beans".equals(rootpost))
						li.add(fs[i]);
				}
			}
		}
	}

	public Map<String, String> getSearchMapFromSingleProjectDynamicFiles(List<File> dys) {
		Map<String, String> m = new HashMap<String, String>();
		if (dys != null && !dys.isEmpty()) {
			dys.forEach(dy -> {
				try {
					Document doc = this.reader.read(dy);
					List<Element> ors = doc.getRootElement().elements("reference");
					if (null != ors && !ors.isEmpty()) {

						ors.forEach(or -> {
							String id = or.attributeValue("id");
							String bn = or.attributeValue("bean-name");
							if (null != id && null != bn && !id.equals(bn)) {
								m.put(id, bn);
							}
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		return m.isEmpty() ? null : m;
	}

	public void createProjectDynamicserviceFileMap() throws IOException {
		File bigDir = new File(this.dir);
		recurseBigDir(bigDir);
		// now projectDynamicserviceFileMap finished
		if (this.projectDynamicserviceFileMap.isEmpty()) {
			throw new IOException("projectDynamicserviceFileMap created is empty");
		}
	}

	public void recurseBigDir(File d) {
		File[] fs = d.listFiles();
		if (null != fs && fs.length > 0) {

			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					if ("src".equals(fs[i].getName())) {
						File projectRes = new File(fs[i], "main\\resources");
						if (projectRes != null && projectRes.exists()) {
							List<File> dys = getDynamicFilesInDir(projectRes);
							if (null != dys) {
								this.projectDynamicserviceFileMap.put(projectRes, dys);
							}
						}
						return;
					} else {
						recurseBigDir(fs[i]);
					}
				}
			}
		}
	}

	/**
	 * @param projectRes
	 *            'resources' dir
	 * @return
	 */
	public List<File> getDynamicFilesInDir(File projectRes) {
		if (projectRes != null && projectRes.exists()) {
			List<File> li = new LinkedList<File>();
			recurseAddDynamicFiles(projectRes, li);
			if (li.size() > 0) {
				return li;
			}
		}
		return null;
	}

	/**
	 * @param d
	 *            'resources' dir
	 * @param li
	 */
	public void recurseAddDynamicFiles(File d, List<File> li) {
		File[] fs = d.listFiles();
		if (null != fs && fs.length > 0) {

			for (int i = 0; i < fs.length; i++) {
				if (fs[i].isDirectory()) {
					recurseAddDynamicFiles(fs[i], li);
				} else if (fs[i].getName().matches(REGEX)) {
					li.add(fs[i]);
				}
			}
		}
	}

	private boolean isSegment(Document doc) throws IOException {
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

}
