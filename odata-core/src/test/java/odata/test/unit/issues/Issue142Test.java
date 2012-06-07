package odata.test.unit.issues;

import java.io.InputStream;
import java.io.InputStreamReader;

import junit.framework.Assert;

import odata.edm.EdmDataServices;
import odata.edm.EdmEntityContainer;
import odata.edm.EdmEntitySet;
import odata.edm.EdmEntityType;
import odata.edm.EdmSchema;
import odata.format.xml.AtomFeedFormatParser;
import odata.format.xml.AtomFeedFormatParser.AtomFeed;

import org.junit.Test;


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
