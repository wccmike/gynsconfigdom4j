package gynsconfigdom4j;

import java.io.IOException;
import java.io.StringBufferInputStream;

import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public interface UnderlineDomRead {
	/** get dom4j's saxreader which avoids validating constraints online
	 * @return
	 */
	default SAXReader getUnderlineSaxreader() {
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
		return reader;
	}
}
