package sqlmapparamaddnamespaceinjava;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class TestSqlmapFunctionNamespaceAdder {
	private SqlmapFunctionNamespaceAdder sf;

	@Test
	public void testDealDoubleQuoteParam() throws IOException {
		sf.dealDoubleQuoteParam();

	}

	@Before
	public void instantiate() throws IOException {
		sf = new SqlmapFunctionNamespaceAdder("balabala", "D:/workpla/ee/24thMay/gynsconfigdom4j/src/test/resources");
		File init = new File("D:/workpla/ee/24thMay/gynsconfigdom4j/src/test/resources/sqlmapfunctioninit.txt");
		String p = init.getAbsolutePath();
		FileUtils.copyFile(init, new File(p.substring(0, p.lastIndexOf("\\") + 1) + "sqlmapfunction1.java"));
		FileUtils.copyFile(init, new File(p.substring(0, p.lastIndexOf("\\") + 1) + "sqlmapfunction2.java"));
	}
}
