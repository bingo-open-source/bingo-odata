package bingo.odata.zinternal;

import bingo.odata.PrefixedNamespace;
import bingo.odata.edm.EdmAssociation;
import bingo.odata.edm.EdmComplexType;
import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.edm.EdmPropertyBase;
import bingo.odata.edm.EdmSchema;
import bingo.odata.edm.EdmStructuralType;
import bingo.odata.edm.EdmType;
import bingo.odata.zinternal.lang.ImmutableList;

public abstract class EdmDataServicesDecorator extends EdmDataServices {

    protected abstract EdmDataServices getDelegate();

    public EdmDataServicesDecorator() {
        super(null, null, null);
    }

    @Override
    public String getVersion() {
        return getDelegate().getVersion();
    }

    @Override
    public ImmutableList<EdmSchema> getSchemas() {
        return getDelegate().getSchemas();
    }

    @Override
    public EdmEntitySet getEdmEntitySet(String entitySetName) {
        return getDelegate().getEdmEntitySet(entitySetName);
    }

    @Override
    public EdmEntitySet getEdmEntitySet(final EdmEntityType type) {
        return getDelegate().getEdmEntitySet(type);
    }

    @Override
    public EdmEntitySet findEdmEntitySet(String entitySetName) {
        return getDelegate().findEdmEntitySet(entitySetName);
    }

    @Override
    public EdmFunctionImport findEdmFunctionImport(String functionImportName) {
        return getDelegate().findEdmFunctionImport(functionImportName);
    }

    @Override
    public EdmComplexType findEdmComplexType(String complexTypeFQName) {
        return getDelegate().findEdmComplexType(complexTypeFQName);
    }

    @Override
    public EdmPropertyBase findEdmProperty(String propName) {
        return getDelegate().findEdmProperty(propName);
    }

    @Override
    public Iterable<EdmEntityType> getEntityTypes() {
        return getDelegate().getEntityTypes();
    }

    @Override
    public Iterable<EdmComplexType> getComplexTypes() {
        return getDelegate().getComplexTypes();
    }

    @Override
    public Iterable<EdmAssociation> getAssociations() {
        return getDelegate().getAssociations();
    }

    @Override
    public Iterable<EdmEntitySet> getEntitySets() {
        return getDelegate().getEntitySets();
    }

    @Override
    public EdmType findEdmEntityType(String fqName) {
        return getDelegate().findEdmEntityType(fqName);
    }

    @Override
    public EdmSchema findSchema(String namespace) {
        return getDelegate().findSchema(namespace);
    }

    @Override
    public Iterable<EdmStructuralType> getStructuralTypes() {
        return getDelegate().getStructuralTypes();
    }

    @Override
    public ImmutableList<PrefixedNamespace> getNamespaces() {
        return getDelegate().getNamespaces();
    }

    @Override
    public Iterable<EdmStructuralType> getSubTypes(EdmStructuralType t) {
        return getDelegate().getSubTypes(t);
    }

    @Override
    public String toString() {
        return getDelegate().toString();
    }

}
