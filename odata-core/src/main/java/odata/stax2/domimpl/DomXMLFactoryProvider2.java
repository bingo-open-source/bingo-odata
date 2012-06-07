package odata.stax2.domimpl;

import java.io.Reader;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import odata.Throwables;
import odata.stax2.Attribute2;
import odata.stax2.EndElement2;
import odata.stax2.QName2;
import odata.stax2.StartElement2;
import odata.stax2.XMLEvent2;
import odata.stax2.XMLEventReader2;
import odata.stax2.XMLEventWriter2;
import odata.stax2.XMLFactoryProvider2;
import odata.stax2.XMLInputFactory2;
import odata.stax2.XMLOutputFactory2;
import odata.stax2.XMLWriter2;
import odata.stax2.XMLWriterFactory2;
import odata.zinternal.PlatformUtil;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import bingo.lang.Out;
import bingo.lang.iterable.ImmutableIteratorBase;

public class DomXMLFactoryProvider2 extends XMLFactoryProvider2 {

    @Override
    public XMLInputFactory2 newXMLInputFactory2() {
        return new DomXMLInputFactory2();
    }

    @Override
    public XMLOutputFactory2 newXMLOutputFactory2() {
        return new DomXMLOutputFactory2();
    }

    @Override
    public XMLWriterFactory2 newXMLWriterFactory2() {
        return new DomXMLWriterFactory2();
    }

    private static class DomXMLOutputFactory2 implements XMLOutputFactory2 {

        
        public XMLEventWriter2 createXMLEventWriter(Writer writer) {
            return new DomXMLEventWriter2(writer);
        }

    }

    private static class DomXMLEventWriter2 implements XMLEventWriter2 {

        @SuppressWarnings("unused")
        private final Writer writer;

        public DomXMLEventWriter2(Writer writer) {
            this.writer = writer;
        }

        
        public void add(XMLEvent2 event) {
            throw new UnsupportedOperationException();

        }

    }

    private static class DomXMLWriterFactory2 implements XMLWriterFactory2 {

        
        public XMLWriter2 createXMLWriter(Writer writer) {
            return new ManualXMLWriter2(writer);
        }

    }

    private static class DomXMLInputFactory2 implements XMLInputFactory2 {

        
        public XMLEventReader2 createXMLEventReader(Reader reader) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            try {
                DocumentBuilder builder = factory.newDocumentBuilder();

                Document document = builder.parse(new InputSource(reader));
                return new DomXMLEventReader2(document);
            } catch (Exception e) {
                throw Throwables.propagate(e);
            }

        }

    }

    private static class DomXMLEventReader2 implements XMLEventReader2 {
        private final Document document;

        private EventIterator  iterator;

        public DomXMLEventReader2(Document document) {
            this.document = document;
            iterator = new EventIterator();
        }

        
        public String getElementText() {
            return iterator.getElementText();
        }

        
        public boolean hasNext() {
            return iterator.hasNext();
        }

        
        public XMLEvent2 nextEvent() {
            return iterator.next();
        }

        private class EventIterator extends ImmutableIteratorBase<XMLEvent2> {

            private Element current;
            private boolean down = true;

            public EventIterator() {

            }

            public String getElementText() {
                return PlatformUtil.getTextContent(current);
            }

			private boolean startElement2(Out<XMLEvent2> out) {
                Object o = new DomStartElement2(current);
                XMLEvent2 event = new DomXMLEvent2(o);
                out.setValue(event);
                return true;
            }

            private boolean endElement2(Out<XMLEvent2> out) {
                Object o = new DomEndElement2(current);
                XMLEvent2 event = new DomXMLEvent2(o);
                out.setValue(event);
                return true;
            }

            @Override
            protected boolean next(Out<XMLEvent2> out) throws Exception {
                if (current == null) {
                    current = document.getDocumentElement();
                    return startElement2(out);

                } else {

                    if (down) {
                        // traverse down
                        NodeList children = current.getChildNodes();
                        for (int i = 0; i < children.getLength(); i++) {
                            Node childNode = children.item(i);
                            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                                current = (Element) childNode;
                                return startElement2(out);
                            }
                        }
                        down = false;
                        return endElement2(out);

                    } else {
                        // traverse up
                        Node parentNode = current.getParentNode();
                        if (parentNode.getNodeType() == Node.DOCUMENT_NODE) {
                            return false;
                        }
                        Element parentElement = (Element) parentNode;
                        NodeList children = parentElement.getChildNodes();
                        boolean found = false;
                        for (int i = 0; i < children.getLength(); i++) {
                            Node childNode = children.item(i);
                            if (childNode.getNodeType() == Node.ELEMENT_NODE) {

                                if (!found) {
                                    if (childNode == current)
                                        found = true;
                                    continue;
                                }

                                current = (Element) childNode;
                                down = true;
                                return startElement2(out);
                            }
                        }
                        current = parentElement;
                        return endElement2(out);
                    }

                }
            }
        }
    }

    private static class DomStartElement2 implements StartElement2 {

        private final Element element;

        public DomStartElement2(Element element) {
            this.element = element;
        }

        
        public Attribute2 getAttributeByName(String name) {
            return getAttributeByName(new QName2(name));
        }

        
        public Attribute2 getAttributeByName(QName2 qName2) {

            final Attr attr = element.getAttributeNodeNS(qName2.getNamespaceUri(), qName2.getLocalPart());
            if (attr == null)
                return null;

            return new Attribute2() {
                public String getValue() {
                    return attr.getValue();
                }
            };

        }

        
        public QName2 getName() {
            return new QName2(element.getNamespaceURI(), element.getLocalName());
        }

        @Override
        public String toString() {
            return "StartElement " + getName() + " " + PlatformUtil.getTextContent(element);
        }

    }

    private static class DomEndElement2 implements EndElement2 {

        private final Element element;

        public DomEndElement2(Element element) {
            this.element = element;
        }

        
        public QName2 getName() {
            return new QName2(element.getNamespaceURI(), element.getLocalName());
        }

        @Override
        public String toString() {
            return "EndElement " + getName() + " " + PlatformUtil.getTextContent(element);
        }

    }

    private static class DomXMLEvent2 implements XMLEvent2 {

        private final Object event;

        public DomXMLEvent2(Object event) {
            this.event = event;
        }

        
        public EndElement2 asEndElement() {
            return (EndElement2) event;
        }

        
        public StartElement2 asStartElement() {
            return (StartElement2) event;
        }

        
        public boolean isEndElement() {
            return event instanceof EndElement2;
        }

        
        public boolean isStartElement() {
            return event instanceof StartElement2;
        }

        @Override
        public String toString() {
            return event.toString();
        }

    }

}
