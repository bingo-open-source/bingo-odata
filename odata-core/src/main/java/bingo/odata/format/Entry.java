package bingo.odata.format;

import bingo.odata.OEntity;

/**
 * Building block for OData payload. 
 * 
 * @see Feed
 */
public interface Entry {

    String getUri();

    String getETag();

    OEntity getEntity();

}
