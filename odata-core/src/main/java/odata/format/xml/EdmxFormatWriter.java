package odata.format.xml;

import java.io.Writer;

import odata.NamespacedAnnotation;
import odata.PrefixedNamespace;
import odata.edm.EdmAnnotationAttribute;
import odata.edm.EdmAnnotationElement;
import odata.edm.EdmAssociation;
import odata.edm.EdmAssociationSet;
import odata.edm.EdmComplexType;
import odata.edm.EdmDataServices;
import odata.edm.EdmDocumentation;
import odata.edm.EdmEntityContainer;
import odata.edm.EdmEntitySet;
import odata.edm.EdmEntityType;
import odata.edm.EdmFunctionImport;
import odata.edm.EdmFunctionParameter;
import odata.edm.EdmItem;
import odata.edm.EdmNavigationProperty;
import odata.edm.EdmProperty;
import odata.edm.EdmSchema;
import odata.edm.EdmProperty.CollectionKind;
import odata.stax2.QName2;
import odata.stax2.XMLFactoryProvider2;
import odata.stax2.XMLWriter2;


public class EdmxFormatWriter extends XmlFormatWriter {

    public static void write(EdmDataServices services, Writer w) {

        XMLWriter2 writer = XMLFactoryProvider2.getInstance().newXMLWriterFactory2().createXMLWriter(w);
        writer.startDocument();

        writer.startElement(new QName2(edmx, "Edmx", "edmx"));
        writer.writeAttribute("Version", "1.0");
        writer.writeNamespace("edmx", edmx);
        writer.writeNamespace("d", d);
        writer.writeNamespace("m", m);
        writeExtensionNamespaces(services, writer);

        writer.startElement(new QName2(edmx, "DataServices", "edmx"));
        writer.writeAttribute(new QName2(m, "DataServiceVersion", "m"), "1.0");

        // Schema
        for (EdmSchema schema : services.getSchemas()) {

            writer.startElement(new QName2("Schema"), edm);
            writer.writeAttribute("Namespace", schema.getNamespace());
            writeAnnotationAttributes(schema, writer);
            writeDocumentation(schema, writer);

            // ComplexType
            for (EdmComplexType ect : schema.getComplexTypes()) {
                writer.startElement(new QName2("ComplexType"));

                writer.writeAttribute("Name", ect.getName());
                if (ect.getIsAbstract() != null) {
                    writer.writeAttribute("Abstract", ect.getIsAbstract().toString());
                }
                writeAnnotationAttributes(ect, writer);
                writeDocumentation(ect, writer);

                writeProperties(ect.getProperties(), writer);
                writeAnnotationElements(ect, writer);
                writer.endElement("ComplexType");
            }
            // EntityType
            for (EdmEntityType eet : schema.getEntityTypes()) {
                writer.startElement(new QName2("EntityType"));

                writer.writeAttribute("Name", eet.getName());
                if (eet.getIsAbstract() != null) {
                    writer.writeAttribute("Abstract", eet.getIsAbstract().toString());
                }

                if (Boolean.TRUE.equals(eet.getHasStream())) {
                    writer.writeAttribute(new QName2(m, "HasStream", "m"), "true");
                }

                // keys only on base types
                if (eet.isRootType()) {
                    writeAnnotationAttributes(eet, writer);
                    writeDocumentation(eet, writer);
                    writer.startElement(new QName2("Key"));
                    for (String key : eet.getKeys()) {
                        writer.startElement(new QName2("PropertyRef"));
                        writer.writeAttribute("Name", key);
                        writer.endElement("PropertyRef");
                    }

                    writer.endElement("Key");
                } else {
                    writer.writeAttribute("BaseType", eet.getBaseType().getFullyQualifiedTypeName());
                    writeAnnotationAttributes(eet, writer);
                    writeDocumentation(eet, writer);
                }

                writeProperties(eet.getDeclaredProperties(), writer);

                for (EdmNavigationProperty np : eet.getDeclaredNavigationProperties()) {

                    writer.startElement(new QName2("NavigationProperty"));
                    writer.writeAttribute("Name", np.getName());
                    writer.writeAttribute("Relationship", np.getRelationship().getFQNamespaceName());
                    writer.writeAttribute("FromRole", np.getFromRole().getRole());
                    writer.writeAttribute("ToRole", np.getToRole().getRole());
                    writeAnnotationAttributes(np, writer);
                    writeDocumentation(np, writer);
                    writeAnnotationElements(np, writer);
                    writer.endElement("NavigationProperty");

                }

                writeAnnotationElements(eet, writer);
                writer.endElement("EntityType");

            }

            // Association
            for (EdmAssociation assoc : schema.getAssociations()) {
                writer.startElement(new QName2("Association"));

                writer.writeAttribute("Name", assoc.getName());
                writeAnnotationAttributes(assoc, writer);
                writeDocumentation(assoc, writer);

                writer.startElement(new QName2("End"));
                writer.writeAttribute("Role", assoc.getEnd1().getRole());
                writer.writeAttribute("Type", assoc.getEnd1().getType().getFullyQualifiedTypeName());
                writer.writeAttribute("Multiplicity", assoc.getEnd1().getMultiplicity().getSymbolString());
                writer.endElement("End");

                writer.startElement(new QName2("End"));
                writer.writeAttribute("Role", assoc.getEnd2().getRole());
                writer.writeAttribute("Type", assoc.getEnd2().getType().getFullyQualifiedTypeName());
                writer.writeAttribute("Multiplicity", assoc.getEnd2().getMultiplicity().getSymbolString());
                writer.endElement("End");

                writeAnnotationElements(assoc, writer);
                writer.endElement("Association");
            }

            // EntityContainer
            for (EdmEntityContainer container : schema.getEntityContainers()) {
                writer.startElement(new QName2("EntityContainer"));

                writer.writeAttribute("Name", container.getName());
                writer.writeAttribute(new QName2(m, "IsDefaultEntityContainer", "m"), Boolean.toString(container.isDefault()));
                writeAnnotationAttributes(container, writer);
                writeDocumentation(container, writer);

                for (EdmEntitySet ees : container.getEntitySets()) {
                    writer.startElement(new QName2("EntitySet"));
                    writer.writeAttribute("Name", ees.getName());
                    writer.writeAttribute("EntityType", ees.getType().getFullyQualifiedTypeName());
                    writeAnnotationAttributes(ees, writer);
                    writeDocumentation(ees, writer);
                    writeAnnotationElements(ees, writer);
                    writer.endElement("EntitySet");
                }

                for (EdmFunctionImport fi : container.getFunctionImports()) {
                    writer.startElement(new QName2("FunctionImport"));
                    writer.writeAttribute("Name", fi.getName());
                    if (fi.getEntitySet() != null) {
                        writer.writeAttribute("EntitySet", fi.getEntitySet().getName());
                    }
                    if (fi.getReturnType() != null) {
                        // TODO: how to differentiate inline ReturnType vs embedded ReturnType?
                        writer.writeAttribute("ReturnType", fi.getReturnType().getFullyQualifiedTypeName());
                    }
                    writer.writeAttribute(new QName2(m, "HttpMethod", "m"), fi.getHttpMethod());
                    writeAnnotationAttributes(fi, writer);
                    writeDocumentation(fi, writer);

                    for (EdmFunctionParameter param : fi.getParameters()) {
                        writer.startElement(new QName2("Parameter"));
                        writer.writeAttribute("Name", param.getName());
                        writer.writeAttribute("Type", param.getType().getFullyQualifiedTypeName());
                        if (param.getMode() != null)
                            writer.writeAttribute("Mode", param.getMode().toString());
                        writeAnnotationAttributes(param, writer);
                        writeDocumentation(param, writer);
                        writeAnnotationElements(param, writer);
                        writer.endElement("Parameter");
                    }
                    writeAnnotationElements(fi, writer);
                    writer.endElement("FunctionImport");
                }

                for (EdmAssociationSet eas : container.getAssociationSets()) {
                    writer.startElement(new QName2("AssociationSet"));
                    writer.writeAttribute("Name", eas.getName());
                    writer.writeAttribute("Association", eas.getAssociation().getFQNamespaceName());
                    writeAnnotationAttributes(eas, writer);
                    writeDocumentation(eas, writer);

                    writer.startElement(new QName2("End"));
                    writer.writeAttribute("Role", eas.getEnd1().getRole().getRole());
                    writer.writeAttribute("EntitySet", eas.getEnd1().getEntitySet().getName());
                    writer.endElement("End");

                    writer.startElement(new QName2("End"));
                    writer.writeAttribute("Role", eas.getEnd2().getRole().getRole());
                    writer.writeAttribute("EntitySet", eas.getEnd2().getEntitySet().getName());
                    writer.endElement("End");

                    writeAnnotationElements(eas, writer);
                    writer.endElement("AssociationSet");
                }

                writeAnnotationElements(container, writer);
                writer.endElement("EntityContainer");
            }

            writeAnnotationElements(schema, writer);
            writer.endElement("Schema");

        }

        writer.endDocument();
    }

    /**
     * Extensions to CSDL like Annotations appear in an application specific set
     * of namespaces.
     */
    private static void writeExtensionNamespaces(EdmDataServices services, XMLWriter2 writer) {
        if (services.getNamespaces() != null) {
            for (PrefixedNamespace ns : services.getNamespaces()) {
                writer.writeNamespace(ns.getPrefix(), ns.getUri());
            }
        }
    }

    private static void writeProperties(Iterable<EdmProperty> properties, XMLWriter2 writer) {
        for (EdmProperty prop : properties) {
            writer.startElement(new QName2("Property"));

            writer.writeAttribute("Name", prop.getName());
            writer.writeAttribute("Type", prop.getType().getFullyQualifiedTypeName());
            writer.writeAttribute("Nullable", Boolean.toString(prop.isNullable()));
            if (prop.getMaxLength() != null) {
                writer.writeAttribute("MaxLength", Integer.toString(prop.getMaxLength()));
            }
            if (!prop.getCollectionKind().equals(CollectionKind.NONE)) {
                writer.writeAttribute("CollectionKind", prop.getCollectionKind().toString());
            }
            if (prop.getDefaultValue() != null) {
                writer.writeAttribute("DefaultValue", prop.getDefaultValue());
            }
            if (prop.getPrecision() != null) {
                writer.writeAttribute("Precision", Integer.toString(prop.getPrecision()));
            }
            if (prop.getScale() != null) {
                writer.writeAttribute("Scale", Integer.toString(prop.getPrecision()));
            }
            writeAnnotationAttributes(prop, writer);
            writeAnnotationElements(prop, writer);
            writer.endElement("Property");
        }
    }

    private static void writeAnnotationAttributes(EdmItem item, XMLWriter2 writer) {
        if (item.getAnnotations() != null) {
            for (NamespacedAnnotation<?> a : item.getAnnotations()) {
                if (a instanceof EdmAnnotationAttribute) {
                    writer.writeAttribute(new QName2(a.getNamespace().getUri(), a.getName(), a.getNamespace().getPrefix()), a.getValue() == null ? "" : a.getValue().toString());
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void writeAnnotationElements(EdmItem item, XMLWriter2 writer) {
        if (item.getAnnotations() != null) {
            for (NamespacedAnnotation<?> a : item.getAnnotations()) {
                if (a instanceof EdmAnnotationElement) {
                    // TODO: please don't throw an exception here.
                    // this totally breaks ODataConsumer even thought it doesn't rely
                    // on annotations.  A no-op is a interim approach that allows work
                    // to proceed by those using queryable metadata to access annotations.
                    // throw new UnsupportedOperationException("Implement element annotations");
                }
            }
        }
    }

    private static void writeDocumentation(EdmItem item, XMLWriter2 writer) {
        EdmDocumentation doc = item.getDocumentation();
        if (doc != null && (doc.getSummary() != null || doc.getLongDescription() != null)) {
            QName2 d = new QName2(edm, "Documentation");
            writer.startElement(d);
            {
                if (doc.getSummary() != null) {
                    QName2 s = new QName2(edm, "Summary");
                    writer.startElement(s);
                    writer.writeText(doc.getSummary());
                    writer.endElement(s.getLocalPart());
                }
                if (doc.getLongDescription() != null) {
                    QName2 s = new QName2(edm, "LongDescription");
                    writer.startElement(s);
                    writer.writeText(doc.getLongDescription());
                    writer.endElement(s.getLocalPart());
                }
            }
            writer.endElement(d.getLocalPart());
        }
    }

}
