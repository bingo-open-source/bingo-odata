package odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import odata.ODataConstants;
import odata.edm.EdmDataServices;
import odata.edm.EdmEntitySet;
import odata.format.FormatWriter;
import odata.stax2.QName2;
import odata.stax2.XMLFactoryProvider2;
import odata.stax2.XMLWriter2;


public class AtomServiceDocumentFormatWriter extends XmlFormatWriter implements FormatWriter<EdmDataServices> {

    public String getContentType() {
        return ODataConstants.APPLICATION_XML_CHARSET_UTF8;
    }

    public void write(UriInfo uriInfo, Writer w, EdmDataServices target) {
        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();

        String xmlns = app;

        writer.startElement(new QName2("service"), xmlns);
        writer.writeAttribute(new QName2("xml:base"), uriInfo.getBaseUri().toString());
        writer.writeNamespace("atom", atom);
        writer.writeNamespace("app", app);

        writer.startElement(new QName2("workspace"));
        writeAtomTitle(writer, atom, "Default");

        for (EdmEntitySet ees : target.getEntitySets()) {

            writer.startElement("collection");
            writer.writeAttribute("href", ees.getName());
            writeAtomTitle(writer, atom, ees.getName());

            writer.endElement("collection");

        }

        writer.endElement("workspace");
        writer.endDocument();
    }

    private static void writeAtomTitle(XMLWriter2 writer, String atom, String title) {
        writer.startElement(new QName2(atom, "title", "atom"));
        writer.writeText(title);
        writer.endElement("title");
    }

}
