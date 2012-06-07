package odata.format.json;

import javax.ws.rs.core.UriInfo;

import odata.producer.ComplexObjectResponse;


/**
 * Writer for OComplexObjects in JSON
 */
public class JsonComplexObjectFormatWriter extends JsonFormatWriter<ComplexObjectResponse> {

    public JsonComplexObjectFormatWriter(String jsonpCallback) {
        super(jsonpCallback);
    }

    @Override
    protected void writeContent(UriInfo uriInfo, JsonWriter jw, ComplexObjectResponse target) {
        super.writeComplexObject(jw, target.getObject().getType().getFullyQualifiedTypeName(), target.getObject().getProperties());
    }

}
