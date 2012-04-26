package bingo.odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import bingo.odata.ODataConstants;
import bingo.odata.format.FormatWriter;
import bingo.odata.producer.PropertyResponse;
import bingo.odata.stax2.XMLFactoryProvider2;
import bingo.odata.stax2.XMLWriter2;

public class XmlPropertyFormatWriter extends XmlFormatWriter implements FormatWriter<PropertyResponse> {

    public String getContentType() {
        return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
    }

    public void write(UriInfo uriInfo, Writer w, PropertyResponse target) {
        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();
        writeProperty(writer, target.getProperty(), true);
        writer.endDocument();
    }

}
