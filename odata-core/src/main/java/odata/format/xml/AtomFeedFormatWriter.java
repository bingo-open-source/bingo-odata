package odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import odata.ODataConstants;
import odata.OEntity;
import odata.edm.EdmEntitySet;
import odata.format.FormatWriter;
import odata.producer.EntitiesResponse;
import odata.stax2.QName2;
import odata.stax2.XMLFactoryProvider2;
import odata.stax2.XMLWriter2;
import odata.zinternal.InternalUtil;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


public class AtomFeedFormatWriter extends XmlFormatWriter implements FormatWriter<EntitiesResponse> {

    public String getContentType() {
        return ODataConstants.APPLICATION_ATOM_XML_CHARSET_UTF8;
    }

    public void write(UriInfo uriInfo, Writer w, EntitiesResponse response) {

        String baseUri = uriInfo.getBaseUri().toString();

        EdmEntitySet ees = response.getEntitySet();
        String entitySetName = ees.getName();
        DateTime utc = new DateTime().withZone(DateTimeZone.UTC);
        String updated = InternalUtil.toString(utc);

        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();

        writer.startElement(new QName2("feed"), atom);
        writer.writeNamespace("m", m);
        writer.writeNamespace("d", d);
        writer.writeAttribute("xml:base", baseUri);

        writeElement(writer, "title", entitySetName, "type", "text");
        writeElement(writer, "id", baseUri + uriInfo.getPath());

        writeElement(writer, "updated", updated);

        writeElement(writer, "link", null, "rel", "self", "title", entitySetName, "href", entitySetName);

        Integer inlineCount = response.getInlineCount();
        if (inlineCount != null) {
            writeElement(writer, "m:count", inlineCount.toString());
        }

        for (OEntity entity : response.getEntities()) {
            writer.startElement("entry");
            writeEntry(writer, entity, entity.getProperties(), entity.getLinks(), baseUri, updated, ees, true);
            writer.endElement("entry");
        }

        if (response.getSkipToken() != null) {
            //<link rel="next" href="https://odata.sqlazurelabs.com/OData.svc/v0.1/rp1uiewita/StackOverflow/Tags/?$filter=TagName%20gt%20'a'&amp;$skiptoken=52" />
            String nextHref = uriInfo.getRequestUriBuilder().replaceQueryParam("$skiptoken", response.getSkipToken()).build().toString();
            writeElement(writer, "link", null, "rel", "next", "href", nextHref);
        }

        writer.endDocument();

    }

}
