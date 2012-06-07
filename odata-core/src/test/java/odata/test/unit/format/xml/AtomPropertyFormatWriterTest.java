package odata.test.unit.format.xml;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import odata.format.FormatType;
import odata.producer.Responses;
import odata.test.unit.format.AbstractPropertyFormatWriterTest;

import org.junit.BeforeClass;
import org.junit.Test;


public class AtomPropertyFormatWriterTest extends AbstractPropertyFormatWriterTest {

    @BeforeClass
    public static void setupClass() throws Exception {
        createFormatWriter(FormatType.ATOM);
    }

    @Test
    public void dateTime() throws Exception {
        formatWriter.write(null, stringWriter, Responses.property(DATE_TIME_PROPERTY));
        assertThat(stringWriter.toString(), containsString("2003-07-01T00:00:00"));
    }
}
