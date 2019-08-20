package gynsconfigdom4j;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JarNameSplitterTest {
	private JarNameSplitter js;

	@Before
	public void instantiate() {
		js = new JarNameSplitter();
	}

	@Test
	public void testGetAvByJar() throws Exception {
		File f = new File("src/test/resources/org.eclipse.osgi.services_3.2.0.v20090520-1800.jar").getCanonicalFile();
		String[] av = js.getAvByJar(f);
		String[] exp = { "org-eclipse-osgi-services", "3.2.0.v20090520-1800" };
		assertArrayEquals(exp, av);
	}

	@Test(expected = IOException.class)
	public void testGetAvByJarWrongExten() throws Exception {
		File f = new File("src/test/resources/test.mf").getCanonicalFile();
		String[] av = js.getAvByJar(f);
	}
}
