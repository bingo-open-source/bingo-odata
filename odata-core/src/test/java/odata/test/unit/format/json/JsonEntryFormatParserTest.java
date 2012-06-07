package odata.test.unit.format.json;

import java.io.StringReader;

import odata.format.FormatType;
import odata.test.unit.format.AbstractEntryFormatParserTest;

import org.junit.BeforeClass;
import org.junit.Test;


public class JsonEntryFormatParserTest extends AbstractEntryFormatParserTest {

    @BeforeClass
    public static void setupClass() throws Exception {
        createFormatParser(FormatType.JSON);
    }

    @Test
    public void dateTime() throws Exception {
        verifyDateTimePropertyValue(formatParser.parse(buildJson("\"DateTime\" : \"\\/Date(1057017600000)\\/\"")));
    }

    private StringReader buildJson(String property) {
        return new StringReader("" + "{" + "\"d\" : {" + property + "}" + "}");
    }
}
