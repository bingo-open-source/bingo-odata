package odata.test.unit.format.json;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.util.regex.Pattern;

import odata.format.FormatType;
import odata.producer.Responses;
import odata.test.unit.format.AbstractPropertyFormatWriterTest;

import org.junit.BeforeClass;
import org.junit.Test;


public class JsonPropertyFormatWriterTest extends AbstractPropertyFormatWriterTest {

    @BeforeClass
    public static void setupClass() throws Exception {
        createFormatWriter(FormatType.JSON);
    }

    @Test
    public void dateTime() throws Exception {
        formatWriter.write(null, stringWriter, Responses.property(DATE_TIME_PROPERTY));
        assertThat(stringWriter.toString(), containsString("\\/Date(1057017600000)\\/"));
    }

    @Test
    public void bool() throws Exception {
        formatWriter.write(null, stringWriter, Responses.property(BOOLEAN_PROPERTY));
        assertTrue(Pattern.compile(".+\\{\\s*\"Boolean\"\\s*:\\s*false\\s*\\}.+", Pattern.DOTALL).matcher(stringWriter.toString()).matches());
    }
}
