package bingo.odata.test.unit.format.xml;

import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

import org.junit.BeforeClass;
import org.junit.Test;

import bingo.odata.format.FormatType;
import bingo.odata.producer.Responses;
import bingo.odata.test.unit.format.AbstractPropertyFormatWriterTest;

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
