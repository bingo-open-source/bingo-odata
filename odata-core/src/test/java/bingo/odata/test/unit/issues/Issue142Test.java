package bingo.odata.test.unit.issues;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import org.junit.Test;

import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmEntityContainer;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmSchema;
import bingo.odata.format.xml.AtomFeedFormatParser;
import bingo.odata.format.xml.AtomFeedFormatParser.AtomFeed;

public class Issue142Test {

    @Test
    public void issue142() {
        InputStream xml = getClass().getResourceAsStream("/META-INF/no_content_type.xml");
        EdmDataServices metadata = getMetadata();
        AtomFeed feed = new AtomFeedFormatParser(metadata, null, null, null).parse(new InputStreamReader(xml));
        Assert.assertNotNull(feed);
    }

    private static EdmDataServices getMetadata() {
        EdmDataServices.Builder metadata = new EdmDataServices.Builder();
        EdmSchema.Builder schema = new EdmSchema.Builder();
        EdmEntityContainer.Builder container = new EdmEntityContainer.Builder();
        EdmEntityType.Builder entityType = new EdmEntityType.Builder().addKeys("Key1").setNamespace("WFSERVICE").setName("WorkflowTask");
        EdmEntitySet.Builder entitySet = new EdmEntitySet.Builder().setName("WorkflowTaskCollection").setEntityType(entityType);
        container.addEntitySets(entitySet);
        schema.addEntityContainers(container);
        schema.addEntityTypes(entityType);
        metadata.addSchemas(schema);
        return metadata.build();
    }

}
