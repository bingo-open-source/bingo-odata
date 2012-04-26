package bingo.odata.format.json;

import java.io.Reader;

import bingo.odata.format.FormatParser;
import bingo.odata.format.Settings;
import bingo.odata.format.SingleLink;
import bingo.odata.format.SingleLinks;
import bingo.odata.format.json.JsonStreamReaderFactory.JsonStreamReader;

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
