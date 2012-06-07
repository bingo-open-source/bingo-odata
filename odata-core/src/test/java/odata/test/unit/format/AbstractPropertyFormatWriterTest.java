package odata.test.unit.format;

import java.io.StringWriter;

import odata.OProperties;
import odata.OProperty;
import odata.edm.EdmSimpleType;
import odata.format.FormatType;
import odata.format.FormatWriter;
import odata.format.FormatWriterFactory;
import odata.producer.PropertyResponse;

import org.joda.time.LocalDateTime;
import org.junit.Before;


public abstract class AbstractPropertyFormatWriterTest {

    protected static final OProperty<LocalDateTime> DATE_TIME_PROPERTY = OProperties.simple("DateTime", EdmSimpleType.DATETIME, new LocalDateTime(2003, 7, 1, 0, 0));
    protected static final OProperty<Boolean>       BOOLEAN_PROPERTY   = OProperties.simple("Boolean", EdmSimpleType.BOOLEAN, Boolean.FALSE);

    protected static FormatWriter<PropertyResponse> formatWriter;

    protected StringWriter                          stringWriter;

    protected static void createFormatWriter(FormatType format) {
        formatWriter = FormatWriterFactory.getFormatWriter(PropertyResponse.class, null, format.toString(), null);
    }

    @Before
    public void setup() throws Exception {
        stringWriter = new StringWriter();
    }
}
