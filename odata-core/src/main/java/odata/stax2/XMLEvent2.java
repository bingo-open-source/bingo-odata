package odata.stax2;

public interface XMLEvent2 {

    boolean isStartElement();

    StartElement2 asStartElement();

    boolean isEndElement();

    EndElement2 asEndElement();

}