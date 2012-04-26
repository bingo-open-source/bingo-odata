package bingo.odata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import bingo.lang.Strings;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmType;

/**
 * A static factory to create immutable {@link OEntity} instances.
 */
public class OEntities {

    private OEntities() {
    }

    /**
     * Creates a new entity.
     *
     * @param entitySet  the entity-set
     * @param entityKey  the entity-key
     * @param properties  the entity properties, if any
     * @param links  the entity links, if any
     * @return the new entity
     */
    public static OEntity create(EdmEntitySet entitySet, OEntityKey entityKey, List<OProperty<?>> properties, List<OLink> links) {
        return new OEntityImpl(entitySet, null, entityKey, true, properties, links);
    }

    /**
     * Creates a new entity.
     *
     * @param entitySet  the entity-set
     * @param entityKey  the entity-key
     * @param properties  the entity properties, if any
     * @param links  the entity links, if any
     * @param extensions  entity extensions, if any
     * @return the new entity
     */
    public static OEntity create(EdmEntitySet entitySet, OEntityKey entityKey, List<OProperty<?>> properties, List<OLink> links, Object... extensions) {
        return new OEntityImpl(entitySet, null, entityKey, true, properties, links, extensions);
    }

    /**
     * Creates a new entity.
     *
     * @param entitySet  the entity-set
     * @param entityType  the entity type
     * @param entityKey  the entity-key
     * @param properties  the entity properties, if any
     * @param links  the entity links, if any
     * @return the new entity
     */
    public static OEntity create(EdmEntitySet entitySet, EdmEntityType entityType, OEntityKey entityKey, List<OProperty<?>> properties, List<OLink> links) {
        return new OEntityImpl(entitySet, entityType, entityKey, true, properties, links);
    }

    /**
     * Creates a new request-entity.
     * <p>A request-entity is a new entity that has not yet been created in an OData service, and therefore allowed to not have an entity-key.</p>
     *
     * @param entitySet  the entity-set
     * @param properties  the entity properties, if any
     * @param links  the entity links, if any
     * @return the new entity
     */
    public static OEntity createRequest(EdmEntitySet entitySet, List<OProperty<?>> properties, List<OLink> links) {
        return new OEntityImpl(entitySet, null, null, false, properties, links);
    }

    /**
     * Creates a new entity with additional Atom information.
     *
     * @param entitySet  the entity-set
     * @param entityType  the entity type
     * @param entityKey  the entity-key
     * @param properties  the entity properties, if any
     * @param links  the entity links, if any
     * @param title  the Atom title
     * @param categoryTerm  the Atom category term
     * @return the new entity
     */
    public static OEntity create(EdmEntitySet entitySet, EdmEntityType entityType, OEntityKey entityKey, List<OProperty<?>> properties, List<OLink> links, String title, String categoryTerm) {
        return new OEntityAtomImpl(entitySet, entityType, entityKey, true, properties, links, title, categoryTerm);
    }

    /**
     * Creates a new request-entity with additional Atom information.
     * <p>A request-entity is a new entity that has not yet been created in an OData service, and therefore allowed to not have an entity-key.</p>
     *
     * @param entitySet  the entity-set
     * @param properties  the entity properties, if any
     * @param links  the entity links, if any
     * @param title  the Atom title
     * @param categoryTerm  the Atom category term
     * @return the new entity
     */
    public static OEntity createRequest(EdmEntitySet entitySet, List<OProperty<?>> properties, List<OLink> links, String title, String categoryTerm) {
        return new OEntityAtomImpl(entitySet, null, null, false, properties, links, title, categoryTerm);
    }

    private static class OEntityAtomImpl extends OEntityImpl implements AtomInfo {

        private final String title;
        private final String categoryTerm;

        public OEntityAtomImpl(EdmEntitySet entitySet, EdmEntityType entityType, OEntityKey entityKey, boolean entityKeyRequired, List<OProperty<?>> properties, List<OLink> links, String title,
                String categoryTerm) {
            super(entitySet, entityType, entityKey, entityKeyRequired, properties, links);
            this.title = title;
            this.categoryTerm = categoryTerm;
        }

        public String getTitle() {
            return title;
        }

        public String getCategoryTerm() {
            return categoryTerm;
        }
    }

    private static class OEntityImpl implements OEntity {

        private final EdmEntitySet       entitySet;
        private final EdmEntityType      entityType;
        private final OEntityKey         entityKey;
        private final List<OProperty<?>> properties;
        private final List<OLink>        links;
        private final Collection<Object> extensions;

        OEntityImpl(EdmEntitySet entitySet, EdmEntityType entityType, OEntityKey entityKey, boolean entityKeyRequired, List<OProperty<?>> properties, List<OLink> links, Object... extensions) {
            if (entitySet == null)
                throw new IllegalArgumentException("entitySet cannot be null");
            if (entityKeyRequired && entityKey == null)
                throw new IllegalArgumentException("entityKey cannot be null");

            this.entitySet = entitySet;
            this.entityType = entityType;
            this.entityKey = entityKey;
            this.properties = Collections.unmodifiableList(properties);
            this.links = links == null ? Collections.<OLink> emptyList() : Collections.unmodifiableList(links);
            this.extensions = Arrays.asList(extensions);
        }

        @Override
        public String toString() {
            return "OEntity[" + Strings.join(getProperties()) + "]";
        }

        public EdmEntitySet getEntitySet() {
            return entitySet;
        }

        public EdmEntityType getEntityType() {
            return entityType != null ? entityType : entitySet != null ? entitySet.getType() : null;
        }

        public String getEntitySetName() {
            return entitySet.getName();
        }

        public OEntityKey getEntityKey() {
            return entityKey;
        }

        public List<OProperty<?>> getProperties() {
            return properties;
        }

        public OProperty<?> getProperty(String propName) {
            for (OProperty<?> prop : properties) {
                if (prop.getName().equals(propName)) {
                    return prop;
                }
            }
            return null;
        }

        @SuppressWarnings("unchecked")
        public <T> OProperty<T> getProperty(String propName, Class<T> propClass) {
            return (OProperty<T>) getProperty(propName);
        }

        public List<OLink> getLinks() {
            return links;
        }

        @SuppressWarnings("unchecked")
        public <T extends OLink> T getLink(String title, Class<T> linkClass) {
            for (OLink link : getLinks())
                if (link.getTitle().equals(title))
                    return (T) link;
            throw new IllegalArgumentException("No link with title: " + title);
        }

        public EdmType getType() {
            return this.entitySet.getType();
        }

        public <TExtension extends OExtension<OEntity>> TExtension findExtension(Class<TExtension> clazz) {
            for (Object extension : extensions) {
                if (clazz.isInstance(extension)) {
                    return clazz.cast(extension);
                }
            }
            return null;
        }

    }

}
