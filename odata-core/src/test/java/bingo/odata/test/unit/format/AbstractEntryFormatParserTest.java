package bingo.odata.test.unit.format;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.joda.time.LocalDateTime;

import bingo.odata.ODataVersion;
import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmEntityContainer;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmSchema;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.format.Entry;
import bingo.odata.format.FormatParser;
import bingo.odata.format.FormatParserFactory;
import bingo.odata.format.FormatType;
import bingo.odata.format.Settings;

public abstract class AbstractEntryFormatParserTest {

    protected static final String        DATE_TIME       = "DateTime";
    protected static final String        ENTITY_SET_NAME = "EntitySetName";

    protected static FormatParser<Entry> formatParser;

    protected static void createFormatParser(FormatType format) {
        formatParser = FormatParserFactory.getParser(Entry.class, format, getSettings());
    }

    protected void verifyDateTimePropertyValue(Entry entry) {
        assertThat((LocalDateTime) entry.getEntity().getProperty(DATE_TIME).getValue(), is(new LocalDateTime(2003, 7, 1, 0, 0)));
    }

    private static Settings getSettings() {
        return new Settings(ODataVersion.V1, getMetadata(), ENTITY_SET_NAME, null, null);
    }

    private static EdmDataServices getMetadata() {
        EdmProperty.Builder property = EdmProperty.newBuilder(DATE_TIME).setType(EdmSimpleType.DATETIME);
        EdmEntityType.Builder entityType = new EdmEntityType.Builder().setName("EntityType").addKeys("EntityKey").addProperties(property);
        EdmEntitySet.Builder entitySet = new EdmEntitySet.Builder().setName(ENTITY_SET_NAME).setEntityType(entityType);
        EdmEntityContainer.Builder container = new EdmEntityContainer.Builder().addEntitySets(entitySet);
        EdmSchema.Builder schema = new EdmSchema.Builder().addEntityContainers(container).addEntityTypes(entityType);
        return new EdmDataServices.Builder().addSchemas(schema).build();
    }
}
