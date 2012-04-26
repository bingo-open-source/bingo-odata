package bingo.odata.stax2;

import java.io.Writer;

public interface XMLOutputFactory2 {

    XMLEventWriter2 createXMLEventWriter(Writer writer);

}
