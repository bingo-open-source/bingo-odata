package bingo.odata.test.unit.format.json;

import java.io.StringReader;

import org.junit.BeforeClass;
import org.junit.Test;

import bingo.odata.format.FormatType;
import bingo.odata.test.unit.format.AbstractEntryFormatParserTest;

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
