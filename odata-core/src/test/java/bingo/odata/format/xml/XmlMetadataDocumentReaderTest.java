/**
 * file created at 2013-6-5
 */
package bingo.odata.format.xml;

import junit.framework.Assert;

import org.junit.Test;

import bingo.odata.ODataServices;

public class XmlMetadataDocumentReaderTest extends Assert {

	@Test
	public void testReadDemoDataServicesDocument() throws Throwable  {
		ODataServices dataServices = ODataServices.parse(XmlMetadataDocumentReaderTest.class.getResourceAsStream("/edmx/demo.xml"));
		assertNotNull(dataServices);
	}
	
}
