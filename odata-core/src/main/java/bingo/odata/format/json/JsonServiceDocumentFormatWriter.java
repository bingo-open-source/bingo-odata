package bingo.odata.format.json;

import javax.ws.rs.core.UriInfo;

import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmEntitySet;

public class JsonServiceDocumentFormatWriter extends JsonFormatWriter<EdmDataServices> {

    public JsonServiceDocumentFormatWriter(String jsonpCallback) {
        super(jsonpCallback);

    }

    @Override
    public void writeContent(UriInfo uriInfo, JsonWriter jw, EdmDataServices target) {

        jw.startObject();
        {
            jw.writeName("EntitySets");
            jw.startArray();
            {
                boolean isFirst = true;
                for (EdmEntitySet ees : target.getEntitySets()) {
                    if (isFirst)
                        isFirst = false;
                    else
                        jw.writeSeparator();

                    jw.writeString(ees.getName());
                }

            }
            jw.endArray();
        }
        jw.endObject();

    }

}

/*
// jsonp
callback({
"d" : {
"EntitySets": [
"TitleAudioFormats", "TitleAwards", "Titles", "TitleScreenFormats", "Genres", "Languages", "People"
]
}
});

// json
{
"d" : {
"EntitySets": [
"TitleAudioFormats", "TitleAwards", "Titles", "TitleScreenFormats", "Genres", "Languages", "People"
]
}
}
*/