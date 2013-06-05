/**
 * file created at 2013-6-5
 */
package bingo.odata.format.xml;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;

import bingo.odata.ODataServices;
import bingo.odata.consumer.ODataConsumerContext;

import junit.framework.Assert;

public class XmlMetadataDocumentReaderTest extends Assert {

	@Test
	public void testReadDemoDataServicesDocument() throws Throwable  {
		InputStream       stream       = XmlMetadataDocumentReaderTest.class.getResourceAsStream("/edmx/demo.xml");
		InputStreamReader streamReader = new InputStreamReader(stream);
		
		XmlMetadataDocumentReader reader = new XmlMetadataDocumentReader();
		
		ODataServices dataServices = reader.read(new ODataConsumerContext(), streamReader);
		
		assertNotNull(dataServices);
		
		streamReader.close();
	}
	
}
