package odata.format;

import odata.ODataVersion;
import odata.OEntityKey;
import odata.edm.EdmDataServices;
import odata.edm.EdmType;
import odata.zinternal.FeedCustomizationMapping;

public class Settings {

    public final ODataVersion             version;
    public final EdmDataServices          metadata;
    public final String                   entitySetName;
    public final OEntityKey               entityKey;
    public final FeedCustomizationMapping fcMapping;
    public final boolean                  isResponse;
    public final EdmType                  parseType;

    public Settings(ODataVersion version, EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping) {
        this(version, metadata, entitySetName, entityKey, fcMapping, true, null);
    }

    public Settings(ODataVersion version, EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping, boolean isResponse) {
        this(version, metadata, entitySetName, entityKey, fcMapping, isResponse, null);
    }

    public Settings(ODataVersion version, EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping, boolean isResponse, EdmType parseType) {
        this.version = version;
        this.metadata = metadata;
        this.entitySetName = entitySetName;
        this.entityKey = entityKey;
        this.fcMapping = fcMapping;
        this.isResponse = isResponse;
        this.parseType = parseType;
    }

}