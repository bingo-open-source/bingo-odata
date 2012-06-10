package odata.format.xml;

import java.io.Writer;

import javax.ws.rs.core.UriInfo;

import odata.ODataConstants;
import odata.format.Entry;
import odata.format.FormatWriter;


public class AtomRequestEntryFormatWriter implements FormatWriter<Entry> {

    public void write(UriInfo uriInfo, Writer w, Entry target) {
        new AtomEntryFormatWriter().writeRequestEntry(w, target);
    }

    public String getContentType() {
        return ODataConstants.APPLICATION_ATOM_XML_CHARSET_UTF8;
    }

}