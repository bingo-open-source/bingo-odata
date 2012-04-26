package bingo.odata.format.json;

import java.io.Reader;

import bingo.odata.OCollection;
import bingo.odata.OCollections;
import bingo.odata.OComplexObject;
import bingo.odata.ODataVersion;
import bingo.odata.OObject;
import bingo.odata.OSimpleObjects;
import bingo.odata.edm.EdmCollectionType;
import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.edm.EdmType;
import bingo.odata.format.FormatParser;
import bingo.odata.format.FormatParserFactory;
import bingo.odata.format.FormatType;
import bingo.odata.format.Settings;
import bingo.odata.format.json.JsonStreamReaderFactory.JsonStreamReader;
import bingo.odata.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonEvent;
import bingo.odata.format.json.JsonStreamReaderFactory.JsonStreamReader.JsonValueEvent;
import bingo.odata.producer.exceptions.NotImplementedException;

/**
 * Parses an OCollection in JSON format.
 *
 * <pre>Collection types handled so far:
 * - OComplexObject
 *
 * TODO:
 * - all other types
 * </pre>
 */
public class JsonCollectionFormatParser extends JsonFormatParser implements FormatParser<OCollection<? extends OObject>> {

    private final EdmCollectionType returnType;

    public JsonCollectionFormatParser(Settings s) {
        super(s);
        returnType = (EdmCollectionType) (s == null ? null : s.parseType);
    }

    public JsonCollectionFormatParser(EdmCollectionType collectionType, EdmDataServices md) {
        super(null);
        this.metadata = md;
        returnType = collectionType;
    }

    public OCollection<? extends OObject> parse(Reader reader) {
        JsonStreamReader jsr = JsonStreamReaderFactory.createJsonStreamReader(reader);
        try {
            if (isResponse) {
                ensureNext(jsr);
                ensureStartObject(jsr.nextEvent()); // the response object

                // "d" property
                ensureNext(jsr);
                ensureStartProperty(jsr.nextEvent(), DATA_PROPERTY);

                // "aresult" for DataServiceVersion > 1.0
                if (version.compareTo(ODataVersion.V1) > 0) {
                    ensureNext(jsr);
                    ensureStartObject(jsr.nextEvent());
                    ensureNext(jsr);
                    ensureStartProperty(jsr.nextEvent(), RESULTS_PROPERTY);
                }
            }

            // parse the entry
            OCollection<? extends OObject> o = parseCollection(jsr);

            if (isResponse) {

                // the "d" property was our object...it is also a property.
                ensureNext(jsr);
                ensureEndProperty(jsr.nextEvent());

                if (version.compareTo(ODataVersion.V1) > 0) {
                    ensureNext(jsr);
                    ensureEndObject(jsr.nextEvent());
                    ensureNext(jsr);
                    ensureEndProperty(jsr.nextEvent()); // "results"
                }
                ensureNext(jsr);
                ensureEndObject(jsr.nextEvent()); // the response object
            }

            return o;

        } finally {
            jsr.close();
        }
    }

    protected OCollection<? extends OObject> parseCollection(JsonStreamReader jsr) {
        // an array of objects:
        ensureNext(jsr);
        ensureStartArray(jsr.nextEvent());

        OCollection.Builder<OObject> c = newCollectionBuilder();

        if (this.returnType.getItemType().isSimple()) {
            parseCollectionOfSimple(c, jsr);
        } else {
            FormatParser<? extends OObject> parser = createItemParser(this.returnType.getItemType());

            while (jsr.hasNext()) {
                // this is what I really want to do next:
                // OObject o = parser.parse(jsr);
                // however, the FormatParser api would have to be genericized, we would need an interface for
                // the event-oriented parsers (JsonStreamReader, XMLStreamReader).
                // I just don't have the time at this momement...

                if (parser instanceof JsonComplexObjectFormatParser) {
                    OComplexObject obj = ((JsonComplexObjectFormatParser) parser).parseSingleObject(jsr);
                    // null if not there
                    if (obj != null) {
                        c = c.add(obj);
                    } else {
                        break;
                    }
                } else {
                    throw new NotImplementedException("collections of type: " + this.returnType.getItemType().getFullyQualifiedTypeName() + " not implemented");
                }
            }
        }

        // we should see the end of the array
        ensureEndArray(jsr.previousEvent());

        return c.build();
    }

    protected void parseCollectionOfSimple(OCollection.Builder<OObject> builder, JsonStreamReader jsr) {
        while (jsr.hasNext()) {
            JsonEvent e = jsr.nextEvent();
            if (e.isValue()) {
                JsonValueEvent ve = e.asValue();
                builder.add(OSimpleObjects.parse((EdmSimpleType<?>) this.returnType.getItemType(), ve.getValue()));
            } else if (e.isEndArray()) {
                break;
            } else {
                throw new RuntimeException("invalid JSON content");
            }
        }
    }

    protected OCollection.Builder<OObject> newCollectionBuilder() {
        return OCollections.<OObject> newBuilder(this.returnType.getItemType());
    }

    protected FormatParser<? extends OObject> createItemParser(EdmType edmType) {
        // each item is parsed as a standalone item, not a response item
        Settings s = new Settings(this.version, this.metadata, this.entitySetName, this.entityKey, null, // FeedCustomizationMapping fcMapping,
                    false, // boolean isResponse);
                    edmType); // expected type

        return FormatParserFactory.getParser(EdmType.getInstanceType(edmType), FormatType.JSON, s);
    }
}
