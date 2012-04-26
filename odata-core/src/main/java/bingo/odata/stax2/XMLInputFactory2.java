package bingo.odata.stax2;

import java.io.Reader;

public interface XMLInputFactory2 {

    XMLEventReader2 createXMLEventReader(Reader reader);

}
