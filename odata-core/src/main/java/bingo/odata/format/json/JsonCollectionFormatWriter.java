package bingo.odata.format.json;

import javax.ws.rs.core.UriInfo;

import bingo.odata.OCollection;
import bingo.odata.edm.EdmType;
import bingo.odata.producer.CollectionResponse;

/**
 * Writer for OCollections in JSON
 */
public class JsonCollectionFormatWriter extends JsonFormatWriter<CollectionResponse<?>> {

    public JsonCollectionFormatWriter(String jsonpCallback) {
        super(jsonpCallback);
    }

    @Override
    protected void writeContent(UriInfo uriInfo, JsonWriter jw, CollectionResponse<?> target) {
        OCollection<?> c = target.getCollection();
        EdmType ctype = c.getType();
        jw.startArray();
        {
            boolean isFirst = true;
            for (Object o : c) {
                if (!isFirst) {
                    jw.writeSeparator();
                } else {
                    isFirst = false;
                }
                super.writeValue(jw, ctype, o);
            }
        }
        jw.endArray();
    }

}
