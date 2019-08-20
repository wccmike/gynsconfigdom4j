package gynsconfigdom4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BundleDependFillerTest {
	private BundleDependFiller filler;
	private List<String[]> actuals;
	private List<String[]> guessResults;
	private List<String> actualsNonArray;
	private List<String> guessResultsNonArray;
	private List<Boolean> actualsBoolean;
	private List<Boolean> guessResultsBoolean;

	@Before
	public void instantiate() {
		filler = new BundleDependFiller();
		actuals = new ArrayList<String[]>();
		guessResults = new ArrayList<String[]>();
		guessResultsNonArray = new ArrayList<String>();
		actualsNonArray = new ArrayList<String>();
		actualsBoolean = new ArrayList<Boolean>();
		guessResultsBoolean = new ArrayList<Boolean>();
	}

	@Test(expected = IOException.class)
	public void testGetReleventProjectPomFileByJarInLibWrongExt() throws IOException {
		File f = new File("src/test/resources/new1.txt").getCanonicalFile();
		File a = filler.getRelevantProjectPomFileByJarInLib(f);
	}

	@Test
	public void testGetReleventProjectPomFileByJarInLib() throws IOException {
		File j = new File(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-ent/pe-gyns-service-ent-transfer-impl/src/main/resources/lib/koal_common_0.1.jar");
		File exp = new File(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-service/pe-gyns-service-ent/pe-gyns-service-ent-transfer-impl/pom.xml");
		File ac = filler.getRelevantProjectPomFileByJarInLib(j);
		Assert.assertEquals(exp, ac);
	}

	@Test
	public void testCreateSymGavMapByMfFiles() throws Exception {
		File[] fs = new File[1];
		fs[0] = new File(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-fsg/pe-gyns-fsg-pweb/src/main/resources/META-INF/MANIFEST.MF")
						.getCanonicalFile();
		Map<String, String[]> expect = new HashMap<String, String[]>();
		String[] v = { "com.csii.gyns", "pe-gyns-fsg-pweb", "1.0.1" };
		expect.put("com.csii.mcs.newibs.creditcard", v);
		Map<String, String[]> ac = filler.createSymGavMapByMfFiles(fs);
		Assert.assertArrayEquals(v, ac.get("com.csii.mcs.newibs.creditcard"));
	}

	@Test
	public void testGetSymbolicNameFromOneMF() throws Exception {
		File f = new File("src/test/resources/getsymbolicnamefromonemf.mf").getCanonicalFile();
		File f2 = new File("src/test/resources/getsymbolicnamefromonemf2.mf").getCanonicalFile();
		File f3 = new File("src/test/resources/getsymbolicnamefromonemf3.mf").getCanonicalFile();
		List<File> fs = new ArrayList<File>();
		fs.add(f);
		fs.add(f2);
		fs.add(f3);
		String s1 = "com.csii.mcs.newibs.creditcard";
		String s2 = "com.csii.mcs.newibs.creditcard";
		String s3 = null;
		guessResultsNonArray.add(s1);
		guessResultsNonArray.add(s2);
		guessResultsNonArray.add(s3);
		for (int i = 0; i < fs.size(); i++) {
			actualsNonArray.add(filler.getSymbolicNameFromOneMF(fs.get(i)));
		}
		for (int i = 0; i < actualsNonArray.size(); i++) {
			Assert.assertEquals(guessResultsNonArray.get(i), actualsNonArray.get(i));
		}

	}

	@Test(expected = IOException.class)
	public void testGetSymbolicNameFromOneMFInvalidFile() throws Exception {
		File f = new File("src/test/resources/getsymbolicnamefromonemfinvalidfile.txt").getCanonicalFile();
		String s = filler.getSymbolicNameFromOneMF(f);
	}

	@Test
	public void testGetRequireValueFromOneMF() throws Exception {
		File f = new File("src/test/resources/getrequirevaluefromonemf.mf").getCanonicalFile();
		File f2 = new File("src/test/resources/getrequirevaluefromonemf2.mf").getCanonicalFile();
		File f3 = new File("src/test/resources/getrequirevaluefromonemf3.mf").getCanonicalFile();
		List<File> fs = new ArrayList<File>();
		fs.add(f);
		fs.add(f2);
		fs.add(f3);
		// pedynamic;bundle-version="6.0.0",
		// pedynamicweb;bundle-version="6.0.0",
		// com.csii.mcs.ibs;bundle-version="6.0.0",
		// com.csii.mcs.newibs.pcommon;bundle-version="1.0.0",
		// com.csii.mcs.constants;bundle-version="1.0.0"
		String[] s1 = { "pedynamic", "pedynamicweb", "com.csii.mcs.ibs", "com.csii.mcs.newibs.pcommon",
				"com.csii.mcs.constants" };
		String[] s2 = { "pedynamic", "pedynamicweb", "com.csii.mcs.ibs", "com.csii.mcs.newibs.pcommon",
				"com.csii.mcs.constants" };
		String[] s3 = null;
		guessResults.add(s1);
		guessResults.add(s2);
		guessResults.add(s3);
		for (int i = 0; i < fs.size(); i++) {
			actuals.add(filler.getRequireValueFromOneMF(fs.get(i)));
		}
		for (int i = 0; i < actuals.size(); i++) {
			Assert.assertArrayEquals(guessResults.get(i), actuals.get(i));
		}
	}

	@Test(expected = IOException.class)
	public void testGetRequireValueFromOneMFInvalidExtension() throws Exception {
		File f = new File("src/test/resources/getrequirevaluefromonemfinvalidextension.txt").getCanonicalFile();
		String[] s = filler.getRequireValueFromOneMF(f);
	}

	@Test
	public void testFindProjectFromMapBySingleImportPackage() {
		// arg
		List<String> args = new ArrayList<String>();
		args.add("com.csii.ecif.constants");
		args.add("1421.gew.bds.yre");
		args.add("com.csii.ecif.constants2.many.fix");
		Map<String, String> map = new HashMap<String, String>();
		map.put("com.csii.ecif.constants", "a");
		map.put("com.csii.ecif.constants2", "b");
		map.put("com.csii", "shouldnevervisted");

		// expect
		guessResultsNonArray.add("a");
		guessResultsNonArray.add(null);
		guessResultsNonArray.add("b");

		for (String one : args) {
			String actual = filler.findProjectFromMapBySingleImportPackage(one, map);
			actualsNonArray.add(actual);
		}

		for (int i = 0; i < actualsNonArray.size(); ++i) {
			Assert.assertEquals(guessResultsNonArray.get(i), actualsNonArray.get(i));
		}
	}

	@Test
	public void testFillDependency() throws Exception {
		File pri = new File("src/test/resources/testfilldependencyprimary.xml").getCanonicalFile();
		File file = new File("src/test/resources/testfilldependency.xml").getCanonicalFile();
		FileUtils.copyFile(pri, file);
		List<String[]> li = new ArrayList<String[]>();
		String[] $1 = { "com.oracle", "ojdbc6", "11.2.0.3" };
		String[] $2 = { "com", "oj", "11.2" };
		String[] $3 = { "orcom", "gww", "16" };
		li.add($1);
		li.add($2);
		li.add($3);
		Assert.assertTrue(filler.haveDependenciesNode(file));
		Assert.assertTrue(filler.dependencyExists(file, $1));
		Assert.assertTrue(filler.dependencyExists(file, $2));
		filler.fillDependency(file, li);
		Assert.assertTrue(filler.haveDependenciesNode(file));
		Assert.assertTrue(filler.dependencyExists(file, $1));
		Assert.assertTrue(filler.dependencyExists(file, $2));
		Assert.assertTrue(filler.dependencyExists(file, $3));
		int c2 = filler.countDependency(file);
		Assert.assertEquals(3, c2);

		File pri2 = new File("src/test/resources/testfilldependencyprimary2.xml").getCanonicalFile();
		File file2 = new File("src/test/resources/testfilldependency2.xml").getCanonicalFile();
		FileUtils.copyFile(pri2, file2);
		List<String[]> li2 = new ArrayList<String[]>();
		String[] $19 = { "commons-beanutils", "commons-beanutils", "1.9.3" };
		String[] $18 = { "wqqambd", "sdbjew", "11.253" };
		li2.add($19);
		li2.add($18);
		Assert.assertTrue(filler.haveDependenciesNode(file2));
		Assert.assertTrue(filler.dependencyExists(file2, $19));
		Assert.assertFalse(filler.dependencyExists(file2, $18));
		filler.fillDependency(file2, li2);
		Assert.assertTrue(filler.haveDependenciesNode(file2));
		Assert.assertTrue(filler.dependencyExists(file2, $19));
		Assert.assertTrue(filler.dependencyExists(file2, $18));
		int c = filler.countDependency(file2);
		Assert.assertEquals(2, c);
	}

	@Test(expected = IOException.class)
	public void testDependencyExistsNonParentNode() throws Exception {
		File file = new File("src/test/resources/testdependencyexists2.xml").getCanonicalFile();
		String[] gav = { "dom4j", "dom4j", "1.6.1" };
		Boolean act = filler.dependencyExists(file, gav);

	}

	@Test
	public void testDependencyExists() throws Exception {
		File file = new File("src/test/resources/testdependencyexists.xml").getCanonicalFile();
		String[] gav = { "com.oracle", "ojdbc6", "11.2.0.3" };
		Boolean act = filler.dependencyExists(file, gav);

		Assert.assertTrue(act);
		String[] gav2 = { "com.oracle222", "ojdbc6", "11.2.0.3" };
		Boolean act2 = filler.dependencyExists(file, gav2);

		Assert.assertFalse(act2);

	}

	@Test
	public void testHaveDependenciesNode() throws Exception {
		String[] testPaths = parseFileCount("src/test/resources/testhavedependenciesnode.xml", 3);
		for (int i = 0; i < testPaths.length; i++) {
			File file = new File(testPaths[i]);
			Boolean actual = filler.haveDependenciesNode(file.getCanonicalFile());
			actualsBoolean.add(actual);
		}
		Boolean $1 = true;
		Boolean $2 = false;
		Boolean $3 = false;
		guessResultsBoolean.add($1);
		guessResultsBoolean.add($2);
		guessResultsBoolean.add($3);
		for (int i = 0; i < actualsBoolean.size(); i++) {
			Assert.assertEquals(guessResultsBoolean.get(i), actualsBoolean.get(i));
		}
	}

	@Test
	public void testGetImportValueFromOneMF() throws Exception {
		String[] testPaths = parseFileCount("src/test/resources/testgetimportvaluefromonemf.mf", 3);
		for (int i = 0; i < testPaths.length; i++) {

			File file = new File(testPaths[i]);
			String[] actual = filler.getImportValueFromOneMF(file.getCanonicalFile());
			actuals.add(actual);
		}
		// guess results
		String[] $1 = { "com.csii.ecif.basicservice", "com.csii.ecif.basicservice.account", "javax.sql",
				"org.apache.commons.logging;version=\"1.1.1\"" };
		String[] $2 = { "com.csii.ecif.basicservice", "com.csii.ecif.basicservice.account", "javax.sql",
				"org.apache.commons.logging;version=\"1.1.1\"" };
		String[] $3 = null;
		guessResults.add($1);
		guessResults.add($2);
		guessResults.add($3);
		for (int i = 0; i < actuals.size(); i++) {
			Assert.assertArrayEquals(guessResults.get(i), actuals.get(i));
		}
	}

	@Test
	public void testGetPomInSameProjectFromMF() throws Exception {
		File fileMF = new File(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-pmobile/pe-gyns-fsg-pmobile/pe-gyns-fsg-mbs-pweb-pibps/src/main/resources/META-INF/MANIFEST.MF");
		File fileExpect = new File(
				"D:/workpla/ee/24thMay/PE10code/pe-gyns-parent/pe-gyns-pmobile/pe-gyns-fsg-pmobile/pe-gyns-fsg-mbs-pweb-pibps/pom.xml");
		Assert.assertEquals(fileExpect, filler.getPomInSameProjectFromMF(fileMF));
	}

	@Test
	public void testGetGAVFromFile() throws Exception {
		// File pom
		String[] testPaths = parseFileCount("src/test/resources/getgavfromfilepom.xml", 2);
		for (int i = 0; i < testPaths.length; i++) {
			File file = new File(testPaths[i]);
			String[] actual = filler.getGAVFromFile(file.getCanonicalFile());
			actuals.add(actual);
		}
		// guess results
		String[] $1 = { "com.csii.gyns", "pe-gyns-fsg-pmobile", "1.0.1" };
		String[] $2 = { "com.csii.gyns", "pe-gyns-pmobile", "1.0.1" };
		guessResults.add($1);
		guessResults.add($2);
		for (int i = 0; i < actuals.size(); i++) {
			Assert.assertArrayEquals(guessResults.get(i), actuals.get(i));
		}
	}

	@Test
	public void testGetExportValueFromOneMF() throws Exception {
		String[] testPaths = parseFileCount("src/test/resources/test.mf", 4);
		// List<String[]> actuals=new ArrayList<String[]>();
		// List<String[]> guessResults=new ArrayList<String[]>();
		for (int i = 0; i < testPaths.length; i++) {

			File file = new File(testPaths[i]);
			String[] actual = filler.getExportValueFromOneMF(file.getCanonicalFile());
			actuals.add(actual);
		}
		// guess results
		String[] $1 = { "com.csii.mca.datamodel.per.loanmgmt", "com.csii.mca.service.per.loanmgmt" };
		String[] $2 = { "com.csii.mca.datamodel.per.loanmgmt", "com.csii.mca.service.per.loanmgmt" };
		String[] $3 = null;
		// CRLF
		String[] $4 = { "com.csii.mca.datamodel.per.loanmgmt", "com.csii.mca.service.per.loanmgmt" };
		guessResults.add($1);
		guessResults.add($2);
		guessResults.add($3);
		guessResults.add($4);
		for (int i = 0; i < actuals.size(); i++) {
			Assert.assertArrayEquals(guessResults.get(i), actuals.get(i));
		}
	}

	private String[] parseFileCount(String firstFile, int count) {
		String[] s = new String[count];
		s[0] = firstFile;
		String front = firstFile.substring(0, firstFile.lastIndexOf("."));
		String rear = firstFile.substring(firstFile.lastIndexOf("."));
		for (int i = 1; i < count; i++) {
			s[i] = front + (i + 1) + rear;
		}
		return s;
	}
}
