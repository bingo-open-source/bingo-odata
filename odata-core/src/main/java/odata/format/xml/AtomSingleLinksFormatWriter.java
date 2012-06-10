package odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import odata.ODataConstants;
import odata.format.FormatWriter;
import odata.format.SingleLink;
import odata.format.SingleLinks;
import odata.stax2.QName2;
import odata.stax2.XMLFactoryProvider2;
import odata.stax2.XMLWriter2;


public class AtomSingleLinksFormatWriter implements FormatWriter<SingleLinks> {

    public void write(UriInfo uriInfo, Writer w, SingleLinks target) {
        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();
        writer.startElement(new QName2("link"), AtomSingleLinkFormatWriter.d);
        for (SingleLink link : target)
            AtomSingleLinkFormatWriter.writeUri(writer, link, null);
        writer.endElement("link");
        writer.endDocument();
    }

    public String getContentType() {
        return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
    }

}