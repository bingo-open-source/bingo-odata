package odata.format.xml;

import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import odata.OEntities;
import odata.OEntity;
import odata.OEntityKey;
import odata.OLink;
import odata.OLinks;
import odata.OProperties;
import odata.OProperty;
import odata.edm.EdmComplexType;
import odata.edm.EdmDataServices;
import odata.edm.EdmEntitySet;
import odata.edm.EdmEntityType;
import odata.edm.EdmFunctionImport;
import odata.edm.EdmNavigationProperty;
import odata.edm.EdmType;
import odata.format.Entry;
import odata.format.Feed;
import odata.format.FormatParser;
import odata.stax2.Attribute2;
import odata.stax2.QName2;
import odata.stax2.StartElement2;
import odata.stax2.XMLEvent2;
import odata.stax2.XMLEventReader2;
import odata.stax2.XMLEventWriter2;
import odata.stax2.XMLFactoryProvider2;
import odata.zinternal.FeedCustomizationMapping;
import odata.zinternal.InternalUtil;

import bingo.lang.Func1;
import bingo.lang.enumerable.EnumerableImpl;

public class AtomFeedFormatParser extends XmlFormatParser implements FormatParser<Feed> {

    protected EdmDataServices          metadata;
    protected String                   entitySetName;
    protected OEntityKey               entityKey;
    protected FeedCustomizationMapping fcMapping;

    public AtomFeedFormatParser(EdmDataServices metadata, String entitySetName, OEntityKey entityKey, FeedCustomizationMapping fcMapping) {
        this.metadata = metadata;
        this.entitySetName = entitySetName;
        this.entityKey = entityKey;
        this.fcMapping = fcMapping;
    }

    public static class AtomFeed implements Feed {
        public String          next;
        public Iterable<Entry> entries;

        public Iterable<Entry> getEntries() {
            return entries;
        }

        public String getNext() {
            return next;
        }
    }

    abstract static class AtomEntry implements Entry {
        public String         id;
        public String         title;
        public String         summary;
        public String         updated;
        public String         categoryTerm;
        public String         categoryScheme;
        public String         contentType;

        public List<AtomLink> atomLinks;

        public String getUri() {
            return null;
        }

        public String getETag() {
            return null;
        }

        public String getType() {
            return MediaType.APPLICATION_ATOM_XML;
        }
    }

    static class AtomLink {
        public String    relation;
        public String    title;
        public String    type;
        public String    href;
        public AtomFeed  inlineFeed;
        public AtomEntry inlineEntry;
        public boolean   inlineContentExpected;
    }

    static class BasicAtomEntry extends AtomEntry {
        public String content;

        @Override
        public String toString() {
            return InternalUtil.reflectionToString(this);
        }

        public OEntity getEntity() {
            return null;
        }
    }

    public static class DataServicesAtomEntry extends AtomEntry {
        public String             etag;
        // remove properties and links because they are already in the oentity
        public List<OProperty<?>> properties;
        public List<OLink>        links;

        private OEntity           oentity;

        @Override
        public String toString() {
            return InternalUtil.reflectionToString(this);
        }

        public OEntity getEntity() {
            return this.oentity;
        }

        void setOEntity(OEntity oentity) {
            this.oentity = oentity;
        }
    }

    public AtomFeed parse(Reader reader) {
        return parseFeed(InternalUtil.newXMLEventReader(reader));
    }

    AtomFeed parseFeed(XMLEventReader2 reader) {

        AtomFeed feed = new AtomFeed();
        List<AtomEntry> rt = new ArrayList<AtomEntry>();

        while (reader.hasNext()) {
            XMLEvent2 event = reader.nextEvent();

            if (isStartElement(event, ATOM_ENTRY)) {
                rt.add(parseEntry(reader, event.asStartElement()));
            } else if (isStartElement(event, ATOM_LINK)) {
                if ("next".equals(event.asStartElement().getAttributeByName(new QName2("rel")).getValue())) {
                    feed.next = event.asStartElement().getAttributeByName(new QName2("href")).getValue();
                }
            } else if (isEndElement(event, ATOM_FEED)) {
                // return from a sub feed, if we went down the hierarchy
                break;
            }

        }
        feed.entries = EnumerableImpl.of(rt).cast(Entry.class);

        return feed;

    }

    public static Iterable<OProperty<?>> parseProperties(XMLEventReader2 reader, StartElement2 propertiesElement, EdmDataServices metadata) {
        List<OProperty<?>> rt = new ArrayList<OProperty<?>>();

        while (reader.hasNext()) {
            XMLEvent2 event = reader.nextEvent();

            if (event.isEndElement() && event.asEndElement().getName().equals(propertiesElement.getName())) {
                return rt;
            }

            if (event.isStartElement() && event.asStartElement().getName().getNamespaceUri().equals(NS_DATASERVICES)) {

                String name = event.asStartElement().getName().getLocalPart();
                Attribute2 typeAttribute = event.asStartElement().getAttributeByName(M_TYPE);
                Attribute2 nullAttribute = event.asStartElement().getAttributeByName(M_NULL);
                boolean isNull = nullAttribute != null && "true".equals(nullAttribute.getValue());

                OProperty<?> op = null;

                String type = null;
                EdmType et = null;
                if (typeAttribute != null) {
                    type = typeAttribute.getValue();
                    et = metadata.resolveType(type);
                    if (et == null) {
                        // property arrived with an unknown type
                        throw new RuntimeException("unknown property type: " + type);
                    }
                }

                if (et != null && (!et.isSimple())) {
                    op = OProperties.complex(name, (EdmComplexType) et, isNull ? null : EnumerableImpl.of(parseProperties(reader, event.asStartElement(), metadata)).toList());
                } else {
                    op = OProperties.parseSimple(name, type, isNull ? null : reader.getElementText());
                }
                rt.add(op);

            }

        }

        throw new RuntimeException();
    }

    private AtomLink parseAtomLink(XMLEventReader2 reader, StartElement2 linkElement) {
        AtomLink rt = new AtomLink();
        rt.relation = getAttributeValueIfExists(linkElement, "rel");
        rt.type = getAttributeValueIfExists(linkElement, "type");
        rt.title = getAttributeValueIfExists(linkElement, "title");
        rt.href = getAttributeValueIfExists(linkElement, "href");
        rt.inlineContentExpected = false;

        // expected cases:
        // 1.  </link>                  - no inlined content, i.e. deferred
        // 2.  <m:inline/></link>       - inlined content but null entity or empty feed
        // 3.  <m:inline><feed>...</m:inline></link> - inlined content with 1 or more items
        // 4.  <m:inline><entry>..</m:inline></link> - inlined content 1 an item

        while (reader.hasNext()) {
            XMLEvent2 event = reader.nextEvent();

            if (event.isEndElement() && event.asEndElement().getName().equals(linkElement.getName())) {
                break;
            } else if (isStartElement(event, XmlFormatParser.M_INLINE)) {
                rt.inlineContentExpected = true; // may be null content.
            } else if (isStartElement(event, ATOM_FEED)) {
                rt.inlineFeed = parseFeed(reader);
            } else if (isStartElement(event, ATOM_ENTRY)) {
                rt.inlineEntry = parseEntry(reader, event.asStartElement());
            }
        }
        return rt;
    }

    private DataServicesAtomEntry parseDSAtomEntry(String etag, XMLEventReader2 reader, XMLEvent2 event) {
        DataServicesAtomEntry dsae = new DataServicesAtomEntry();
        dsae.etag = etag;
        dsae.properties = EnumerableImpl.of(parseProperties(reader, event.asStartElement(), this.metadata)).toList();
        return dsae;
    }

    private static String innerText(XMLEventReader2 reader, StartElement2 element) {
        StringWriter sw = new StringWriter();
        XMLEventWriter2 writer = XMLFactoryProvider2.getInstance().newXMLOutputFactory2().createXMLEventWriter(sw);
        while (reader.hasNext()) {

            XMLEvent2 event = reader.nextEvent();
            if (event.isEndElement() && event.asEndElement().getName().equals(element.getName())) {

                return sw.toString();
            } else {
                writer.add(event);
            }

        }
        throw new RuntimeException();
    }

    private static final Pattern ENTITY_SET_NAME = Pattern.compile("\\/([^\\/\\(]+)\\(");

    public static String parseEntitySetName(String atomEntryId) {
        Matcher m = ENTITY_SET_NAME.matcher(atomEntryId);
        if (!m.find())
            throw new RuntimeException("Unable to parse the entity-set name from atom entry id: " + atomEntryId);
        return m.group(1);

    }

    public static OEntityKey parseEntityKey(String atomEntryId) {
        Matcher m = ENTITY_SET_NAME.matcher(atomEntryId);
        if (!m.find())
            throw new RuntimeException("Unable to parse the entity-key from atom entry id: " + atomEntryId);
        return OEntityKey.parse(atomEntryId.substring(m.end() - 1));
    }

    private EdmEntitySet getEntitySet(String atomEntryId) {
        EdmEntitySet entitySet = null;
        String entitySetName = this.entitySetName;
        if (atomEntryId != null && atomEntryId.endsWith(")"))
            entitySetName = parseEntitySetName(atomEntryId);
        if (!metadata.getSchemas().isEmpty()) {
            entitySet = metadata.findEdmEntitySet(entitySetName);
            if (entitySet == null) {
                // panic! could not determine the entity-set, is it a function?
                EdmFunctionImport efi = metadata.findEdmFunctionImport(entitySetName);
                if (efi != null)
                    entitySet = efi.getEntitySet();
            }
        }
        if (entitySet == null)
            throw new RuntimeException("Could not derive the entity-set for entry: " + atomEntryId);
        return entitySet;
    }

    private AtomEntry parseEntry(XMLEventReader2 reader, StartElement2 entryElement) {

        String id = null;
        String categoryTerm = null;
        String categoryScheme = null;
        String title = null;
        String summary = null;
        String updated = null;
        String contentType = null;
        List<AtomLink> atomLinks = new ArrayList<AtomLink>();

        String etag = getAttributeValueIfExists(entryElement, M_ETAG);

        AtomEntry rt = null;

        while (reader.hasNext()) {
            XMLEvent2 event = reader.nextEvent();

            if (event.isEndElement() && event.asEndElement().getName().equals(entryElement.getName())) {
                rt.id = id; //http://localhost:8810/Oneoff01.svc/Comment(1)
                rt.title = title;
                rt.summary = summary;
                rt.updated = updated;
                rt.categoryScheme = categoryScheme; //http://schemas.microsoft.com/ado/2007/08/dataservices/scheme
                rt.categoryTerm = categoryTerm; //NorthwindModel.Customer
                rt.contentType = contentType;
                rt.atomLinks = atomLinks;

                if (rt instanceof DataServicesAtomEntry) {
                    DataServicesAtomEntry dsae = (DataServicesAtomEntry) rt;
                    EdmEntitySet entitySet = getEntitySet(rt.id);
                    OEntity entity = entityFromAtomEntry(metadata, entitySet, dsae, fcMapping);
                    dsae.setOEntity(entity);
                }
                return rt;
            }

            if (isStartElement(event, ATOM_ID)) {
                id = reader.getElementText();
            } else if (isStartElement(event, ATOM_TITLE)) {
                title = reader.getElementText();
            } else if (isStartElement(event, ATOM_SUMMARY)) {
                summary = reader.getElementText();
            } else if (isStartElement(event, ATOM_UPDATED)) {
                updated = reader.getElementText();
            } else if (isStartElement(event, ATOM_CATEGORY)) {
                categoryTerm = getAttributeValueIfExists(event.asStartElement(), "term");
                categoryScheme = getAttributeValueIfExists(event.asStartElement(), "scheme");
            } else if (isStartElement(event, ATOM_LINK)) {
                AtomLink link = parseAtomLink(reader, event.asStartElement());
                atomLinks.add(link);
            } else if (isStartElement(event, M_PROPERTIES)) {
                rt = parseDSAtomEntry(etag, reader, event);
            } else if (isStartElement(event, ATOM_CONTENT)) {
                contentType = getAttributeValueIfExists(event.asStartElement(), "type");
                if (MediaType.APPLICATION_XML.equals(contentType)) {
                    StartElement2 contentElement = event.asStartElement();
                    StartElement2 valueElement = null;
                    while (reader.hasNext()) {
                        XMLEvent2 event2 = reader.nextEvent();
                        if (valueElement == null && event2.isStartElement()) {
                            valueElement = event2.asStartElement();
                            if (isStartElement(event2, M_PROPERTIES)) {
                                rt = parseDSAtomEntry(etag, reader, event2);
                            } else {
                                BasicAtomEntry bae = new BasicAtomEntry();
                                bae.content = innerText(reader, event2.asStartElement());
                                rt = bae;
                            }
                        }
                        if (event2.isEndElement() && event2.asEndElement().getName().equals(contentElement.getName())) {
                            break;
                        }
                    }
                } else {
                    BasicAtomEntry bae = new BasicAtomEntry();
                    bae.content = innerText(reader, event.asStartElement());
                    rt = bae;
                }
            }
        }
        throw new RuntimeException();
    }

    private OEntity entityFromAtomEntry(EdmDataServices metadata, EdmEntitySet entitySet, DataServicesAtomEntry dsae, FeedCustomizationMapping mapping) {

        List<OProperty<?>> props = dsae.properties;
        if (mapping != null) {
            EnumerableImpl<OProperty<?>> properties = EnumerableImpl.of(dsae.properties);
            if (mapping.titlePropName != null)
                properties = properties.concat(OProperties.string(mapping.titlePropName, dsae.title));
            if (mapping.summaryPropName != null)
                properties = properties.concat(OProperties.string(mapping.summaryPropName, dsae.summary));

            props = properties.toList();
        }

        EdmEntityType entityType = entitySet.getType();
        if (dsae.categoryTerm != null) {
            // The type of an entity set is polymorphic...
            entityType = (EdmEntityType) metadata.findEdmEntityType(dsae.categoryTerm);
            if (entityType == null) {
                throw new RuntimeException("Unable to resolve entity type " + dsae.categoryTerm);
            }
        }
        // favor the key we just parsed.

        OEntityKey key = dsae.id != null ? (dsae.id.endsWith(")") ? parseEntityKey(dsae.id) : OEntityKey.infer(entitySet, props)) : null;

        if (key == null) {
            key = entityKey;
        }

        if (key == null)
            return OEntities.createRequest(entitySet, props, toOLinks(metadata, entitySet, dsae.atomLinks, mapping), dsae.title, dsae.categoryTerm);

        return OEntities.create(entitySet, entityType, key, props, toOLinks(metadata, entitySet, dsae.atomLinks, mapping), dsae.title, dsae.categoryTerm);
    }

    private List<OLink> toOLinks(final EdmDataServices metadata, EdmEntitySet fromRoleEntitySet, List<AtomLink> links, final FeedCustomizationMapping mapping) {
        List<OLink> rt = new ArrayList<OLink>(links.size());
        for (final AtomLink link : links) {

            if (link.relation.startsWith(XmlFormatWriter.related)) {
                if (link.type.equals(XmlFormatWriter.atom_feed_content_type)) {

                    if (link.inlineContentExpected) {
                        List<OEntity> relatedEntities = null;

                        if (link.inlineFeed != null && link.inlineFeed.entries != null) {

                            // get the entity set belonging to the from role type
                            EdmNavigationProperty navProperty = fromRoleEntitySet != null ? fromRoleEntitySet.getType().findNavigationProperty(link.title) : null;
                            final EdmEntitySet toRoleEntitySet = metadata != null && navProperty != null ? metadata.getEdmEntitySet(navProperty.getToRole().getType()) : null;

                            // convert the atom feed entries to OEntitys
                            relatedEntities = EnumerableImpl.of(link.inlineFeed.entries).cast(DataServicesAtomEntry.class).select(new Func1<DataServicesAtomEntry, OEntity>() {
                                public OEntity apply(DataServicesAtomEntry input) {
                                    return entityFromAtomEntry(metadata, toRoleEntitySet, input, mapping);
                                }
                            }).toList();
                        } // else empty feed.
                        rt.add(OLinks.relatedEntitiesInline(link.relation, link.title, link.href, relatedEntities));
                    } else {
                        // no inlined entities
                        rt.add(OLinks.relatedEntities(link.relation, link.title, link.href));
                    }
                } else if (link.type.equals(XmlFormatWriter.atom_entry_content_type))
                    if (link.inlineContentExpected) {
                        OEntity relatedEntity = null;
                        if (link.inlineEntry != null) {
                            EdmNavigationProperty navProperty = fromRoleEntitySet != null ? fromRoleEntitySet.getType().findNavigationProperty(link.title) : null;
                            EdmEntitySet toRoleEntitySet = metadata != null && navProperty != null ? metadata.getEdmEntitySet(navProperty.getToRole().getType()) : null;
                            relatedEntity = entityFromAtomEntry(metadata, toRoleEntitySet, (DataServicesAtomEntry) link.inlineEntry, mapping);
                        }
                        rt.add(OLinks.relatedEntityInline(link.relation, link.title, link.href, relatedEntity));
                    } else {
                        // no inlined entity
                        rt.add(OLinks.relatedEntity(link.relation, link.title, link.href));
                    }
            }
        }
        return rt;
    }

}
