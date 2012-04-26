package bingo.odata.format.xml;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import bingo.odata.format.FormatParser;
import bingo.odata.format.SingleLink;
import bingo.odata.format.SingleLinks;
import bingo.odata.stax2.QName2;
import bingo.odata.stax2.XMLEvent2;
import bingo.odata.stax2.XMLEventReader2;
import bingo.odata.zinternal.InternalUtil;

public class AtomSingleLinkFormatParser extends XmlFormatParser implements FormatParser<SingleLink> {

    private static final QName2 URI = new QName2(NS_DATASERVICES, "uri");

    public static Iterable<SingleLink> parseLinks(XMLEventReader2 reader) {
        List<SingleLink> rt = new ArrayList<SingleLink>();
        while (reader.hasNext()) {
            XMLEvent2 event = reader.nextEvent();
            if (isStartElement(event, URI)) {
                rt.add(SingleLinks.create(reader.getElementText()));
            }
        }
        return rt;
    }

    public SingleLink parse(Reader reader) {
        return parseLinks(InternalUtil.newXMLEventReader(reader)).iterator().next();
    }

}
