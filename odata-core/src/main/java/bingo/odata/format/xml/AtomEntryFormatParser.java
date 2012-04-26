package bingo.odata.format.xml;

import java.io.Reader;

import bingo.odata.OEntityKey;
import bingo.odata.edm.EdmDataServices;
import bingo.odata.format.Entry;
import bingo.odata.format.FormatParser;
import bingo.odata.zinternal.FeedCustomizationMapping;
import bingo.odata.zinternal.InternalUtil;

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
