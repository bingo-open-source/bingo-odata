package odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import odata.ODataConstants;
import odata.format.FormatWriter;
import odata.format.SingleLink;
import odata.stax2.QName2;
import odata.stax2.XMLFactoryProvider2;
import odata.stax2.XMLWriter2;


public class AtomSingleLinkFormatWriter extends XmlFormatWriter implements FormatWriter<SingleLink> {

    public void write(UriInfo uriInfo, Writer w, SingleLink link) {
        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();
        writeUri(writer, link, d);
        writer.endDocument();
    }

    static void writeUri(XMLWriter2 writer, SingleLink link, String xmlns) {
        writer.startElement(new QName2("uri"), xmlns);
        writer.writeText(link.getUri());
        writer.endElement("uri");
    }

    public String getContentType() {
        return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
    }

}