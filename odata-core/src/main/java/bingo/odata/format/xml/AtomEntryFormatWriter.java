package bingo.odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import bingo.odata.ODataConstants;
import bingo.odata.OEntity;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.format.Entry;
import bingo.odata.format.FormatWriter;
import bingo.odata.producer.EntityResponse;
import bingo.odata.stax2.QName2;
import bingo.odata.stax2.XMLFactoryProvider2;
import bingo.odata.stax2.XMLWriter2;
import bingo.odata.zinternal.InternalUtil;

public class AtomEntryFormatWriter extends XmlFormatWriter implements FormatWriter<EntityResponse> {

    protected String baseUri;

    public void writeRequestEntry(Writer w, Entry entry) {

        DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
        String updated = InternalUtil.toString(utc);

        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();

        writer.startElement(new QName2("entry"), atom);
        writer.writeNamespace("d", d);
        writer.writeNamespace("m", m);

        OEntity entity = entry.getEntity();
        writeEntry(writer, null, entity.getProperties(), entity.getLinks(), null, updated, entity.getEntitySet(), false);
        writer.endDocument();

    }

    public String getContentType() {
        return ODataConstants.APPLICATION_ATOM_XML_CHARSET_UTF8;
    }

    public void write(UriInfo uriInfo, Writer w, EntityResponse target) {
        String baseUri = uriInfo.getBaseUri().toString();
        EdmEntitySet ees = target.getEntity().getEntitySet();

        DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
        String updated = InternalUtil.toString(utc);

        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();

        writer.startElement(new QName2("entry"), atom);
        writer.writeNamespace("m", m);
        writer.writeNamespace("d", d);
        writer.writeAttribute("xml:base", baseUri);

        writeEntry(writer, target.getEntity(), target.getEntity().getProperties(), target.getEntity().getLinks(), baseUri, updated, ees, true);
        writer.endDocument();
    }

}
