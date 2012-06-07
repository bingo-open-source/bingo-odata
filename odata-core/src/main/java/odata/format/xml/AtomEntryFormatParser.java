package odata.format.xml;

import java.io.Reader;

import odata.OEntityKey;
import odata.edm.EdmDataServices;
import odata.format.Entry;
import odata.format.FormatParser;
import odata.zinternal.FeedCustomizationMapping;
import odata.zinternal.InternalUtil;


public class AtomEntryFormatParser implements FormatParser<Entry> {

    protected EdmDataServices          metadata;
    protected String                   entitySetName;
    protected OEntityKey               entityKey;
    protected FeedCustomizationMapping fcMapping;

    public AtomEntryFormatParser(EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping) {
        this.metadata = metadata;
        this.entitySetName = entitySetName;
        this.entityKey = entityKey;
        this.fcMapping = fcMapping;
    }

    public Entry parse(Reader reader) {
        return new AtomFeedFormatParser(metadata, entitySetName, entityKey, fcMapping).parseFeed(InternalUtil.newXMLEventReader(reader)).entries.iterator().next();
    }

}
