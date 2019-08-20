package dynamicref;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import junit.framework.AssertionFailedError;


public class BeanRefChangerTest {
	private BeanRefChanger b;
	@Before
	public void instantiate(){
		b=new BeanRefChanger("D:/1");
	}
@Test
public void testRecurseAddDynamicFiles(){
	File d = new File("D:/1/pe-gyns-service-cmn-mca-trs/src/main/resources");
	List<File> li=new LinkedList<File>();
	b.recurseAddDynamicFiles(d, li);
	assertEquals(2, li.size());
	File d2 = new File("D:/1/pe-gyns-share-lmt-service/src/main/resources");
	List<File> li2=new LinkedList<File>();
	b.recurseAddDynamicFiles(d2, li2);
	assertEquals(1, li2.size());
	
}
@Test
public void testGetDynamicFilesInDir(){
	File projectRes = new File("D:/1/pe-gyns-service-cmn-mca-trs/src/main/resources");
	List<File> li = b.getDynamicFilesInDir(projectRes);
	assertEquals(2, li.size());
	File projectRes2 = new File("D:/1/pe-gyns-share-lmt-service/src/main/resources");
	List<File> li2 = b.getDynamicFilesInDir(projectRes2);
	assertEquals(1, li2.size());
}
@Test
public void testRecurseBigDir(){
	File d = new File("D:/1");
	b.recurseBigDir(d);
	assertEquals(2, b.projectDynamicserviceFileMap.size());
	
}
@Test
public void testGetSearchMapFromSingleProjectDynamicFiles(){
	File projectRes = new File("D:/1/pe-gyns-service-cmn-mca-trs/src/main/resources");
	List<File> dys = b.getDynamicFilesInDir(projectRes);
	Map<String, String> m = b.getSearchMapFromSingleProjectDynamicFiles(dys);
	assertTrue(m.containsKey("balabala"));
	assertFalse(m.containsKey("balabala2"));
	Map<String, String> ex=new HashMap<String, String>();
	ex.put("balabala", "xiaomoxian");
	ex.put("eappQueryJnlIdFactoryService", "eappQueryJnlIdFactory");
	ex.put("queryTemplate", "mca.queryTemplate");
	ex.put("taskEntryTemplate", "mca.taskEntryTemplateForPer");
	assertEquals(ex, m);
	
}
@Test
public void testRecurseConfigAddXmls() throws Exception{
	File d = new File("D:/1/pe-gyns-service-cmn-mca-trs/src/main/resources/META-INF/config");
	List<File> li=new LinkedList<File>();
	b.recurseConfigAddXmls(d, li);
	assertEquals(10,li.size());
	File d2 = new File("D:/1/pe-gyns-share-lmt-service/src/main/resources/META-INF/config");
	List<File> li2=new LinkedList<File>();
	b.recurseConfigAddXmls(d2, li2);
	assertEquals(5,li2.size());
}
@Test
public void testGetAllNeedtochangeXmls() throws Exception{
	File projectRes = new File("D:/1/pe-gyns-service-cmn-mca-trs/src/main/resources");
	List<File> l = b.getAllNeedtochangeXmls(projectRes);
	assertEquals(10,l.size());
	File projectRes2 = new File("D:/1/pe-gyns-share-lmt-service/src/main/resources");
	List<File> l2 = b.getAllNeedtochangeXmls(projectRes2);
	assertEquals(5,l2.size());
}

}
