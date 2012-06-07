package odata.consumer;

import java.util.ArrayList;
import java.util.List;

import odata.OEntity;
import odata.OEntityKey;
import odata.OLink;
import odata.OLinks;
import odata.OProperty;
import odata.edm.EdmDataServices;
import odata.edm.EdmEntitySet;
import odata.edm.EdmMultiplicity;
import odata.edm.EdmNavigationProperty;
import odata.format.xml.XmlFormatWriter;
import odata.zinternal.InternalUtil;


public abstract class AbstractConsumerEntityPayloadRequest {

    protected final List<OProperty<?>> props = new ArrayList<OProperty<?>>();
    protected final List<OLink>        links = new ArrayList<OLink>();

    protected final String             entitySetName;
    protected final String             serviceRootUri;
    protected final EdmDataServices    metadata;

    protected AbstractConsumerEntityPayloadRequest(String entitySetName, String serviceRootUri, EdmDataServices metadata) {
        this.entitySetName = entitySetName;
        this.serviceRootUri = serviceRootUri;
        this.metadata = metadata;
    }

    protected <T> T properties(T rt, OProperty<?>... props) {
        for (OProperty<?> prop : props)
            this.props.add(prop);
        return rt;
    }

    protected <T> T properties(T rt, Iterable<OProperty<?>> props) {
        for (OProperty<?> prop : props)
            this.props.add(prop);
        return rt;
    }

    protected <T> T link(T rt, String navProperty, OEntity target) {
        return link(rt, navProperty, target.getEntitySet(), target.getEntityKey());
    }

    protected <T> T link(T rt, String navProperty, OEntityKey targetKey) {
        return link(rt, navProperty, null, targetKey);
    }

    private <T> T link(T rt, String navProperty, EdmEntitySet targetEntitySet, OEntityKey targetKey) {
        EdmEntitySet entitySet = metadata.getEdmEntitySet(entitySetName);
        EdmNavigationProperty navProp = entitySet.getType().findNavigationProperty(navProperty);
        if (navProp == null)
            throw new IllegalArgumentException("unknown navigation property " + navProperty);

        if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY)
            throw new IllegalArgumentException("many associations are not supported");

        StringBuilder href = new StringBuilder(serviceRootUri);
        if (!serviceRootUri.endsWith("/"))
            href.append("/");

        if (targetEntitySet == null)
            targetEntitySet = metadata.getEdmEntitySet(navProp.getToRole().getType());

        href.append(InternalUtil.getEntityRelId(targetEntitySet, targetKey));

        // TODO get rid of XmlFormatWriter
        // We may need to rethink the rel property on a link
        // since it adds no new information. The title is
        // already there and rel has only a fixed prefix valid for
        // the atom format.
        String rel = XmlFormatWriter.related + navProperty;

        this.links.add(OLinks.relatedEntity(rel, navProperty, href.toString()));
        return rt;
    }

}
