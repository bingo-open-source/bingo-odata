package odata.format.json;

import java.io.Reader;

import odata.format.FormatParser;
import odata.format.Settings;
import odata.format.SingleLink;
import odata.format.SingleLinks;
import odata.format.json.JsonStreamReaderFactory.JsonStreamReader;


public class JsonSingleLinkFormatParser extends JsonFormatParser implements FormatParser<SingleLink> {

    public JsonSingleLinkFormatParser(Settings settings) {
        super(settings);
    }

    public SingleLink parse(Reader reader) {
        // {"uri": "http://host/service.svc/Orders(1)"}
        JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
        try {
            ensureStartObject(jsr.nextEvent());
            ensureStartProperty(jsr.nextEvent(), "uri");
            String uri = jsr.nextEvent().asEndProperty().getValue();
            return SingleLinks.create(uri);
        } finally {
            jsr.close();
        }
    }

}
