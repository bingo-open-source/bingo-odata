package bingo.odata.producer.resources;

import java.io.StringReader;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import bingo.odata.ODataConstants;
import bingo.odata.ODataVersion;
import bingo.odata.OEntity;
import bingo.odata.OEntityKey;
import bingo.odata.edm.EdmDataServices;
import bingo.odata.format.Entry;
import bingo.odata.format.FormatParser;
import bingo.odata.format.FormatParserFactory;
import bingo.odata.format.Settings;
import bingo.odata.producer.exceptions.NotAcceptableException;
import bingo.odata.producer.exceptions.ODataException;
import bingo.odata.zinternal.InternalUtil;

public abstract class BaseResource {

    protected OEntity getRequestEntity(HttpHeaders httpHeaders, UriInfo uriInfo, String payload, EdmDataServices metadata, String entitySetName, OEntityKey entityKey) throws ODataException {
        // TODO validation of MaxDataServiceVersion against DataServiceVersion
        // see spec [ms-odata] section 1.7

        ODataVersion version = InternalUtil.getDataServiceVersion(httpHeaders.getRequestHeaders().getFirst(ODataConstants.Headers.DATA_SERVICE_VERSION));
        return convertFromString(payload, httpHeaders.getMediaType(), version, metadata, entitySetName, entityKey);
    }

    private static OEntity convertFromString(String requestEntity, MediaType type, ODataVersion version, EdmDataServices metadata, String entitySetName, OEntityKey entityKey)
            throws NotAcceptableException {
        FormatParser<Entry> parser = FormatParserFactory.getParser(Entry.class, type, new Settings(version, metadata, entitySetName, entityKey, null, false));
        Entry entry = parser.parse(new StringReader(requestEntity));
        return entry.getEntity();
    }

}