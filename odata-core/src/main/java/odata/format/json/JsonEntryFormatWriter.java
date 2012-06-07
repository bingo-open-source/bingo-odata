package odata.format.json;

import javax.ws.rs.core.UriInfo;

import odata.producer.EntityResponse;


public class JsonEntryFormatWriter extends JsonFormatWriter<EntityResponse> {

    public JsonEntryFormatWriter(String jsonpCallback) {
        super(jsonpCallback);
    }

    @Override
    protected void writeContent(UriInfo uriInfo, JsonWriter jw, EntityResponse target) {
        writeOEntity(uriInfo, jw, target.getEntity(), target.getEntity().getEntitySet(), true);
    }

}
