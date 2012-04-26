package bingo.odata.format.json;

import java.io.Reader;

import bingo.odata.ODataVersion;
import bingo.odata.format.Entry;
import bingo.odata.format.FormatParser;
import bingo.odata.format.Settings;
import bingo.odata.format.json.JsonStreamReaderFactory.JsonStreamReader;

public class JsonEntryFormatParser extends JsonFormatParser implements FormatParser<Entry> {

    public JsonEntryFormatParser(Settings settings) {
        super(settings);
    }

    public Entry parse(Reader reader) {
        JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
        try {
            ensureNext(jsr);

            // skip the StartObject event
            ensureStartObject(jsr.nextEvent());

            if (isResponse) {
                // "d" property
                ensureNext(jsr);
                ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

                // skip the StartObject event
                ensureStartObject(jsr.nextEvent());
            }

            // "result" for DataServiceVersion > 1.0
            if (version.compareTo(ODataVersion.V1) > 0) {
                ensureNext(jsr);
                ensureStartObject(jsr.nextEvent());
                ensureNext(jsr);
                ensureStartProperty(jsr.nextEvent(), RESULTS_PROPERTY);

                // skip the StartObject event
                ensureStartObject(jsr.nextEvent());
            }

            // parse the entry
            return parseEntry(metadata.getEdmEntitySet(entitySetName), jsr);

            // no interest in the closing events
        } finally {
            jsr.close();
        }
    }

}
