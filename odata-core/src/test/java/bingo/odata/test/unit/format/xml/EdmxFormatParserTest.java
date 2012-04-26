package bingo.odata.test.unit.format.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Test;

import bingo.odata.edm.EdmDataServices;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.format.xml.EdmxFormatParser;
import bingo.odata.format.xml.EdmxFormatWriter;
import bingo.odata.stax2.XMLEventReader2;
import bingo.odata.zinternal.InternalUtil;

public class EdmxFormatParserTest {

    public EdmxFormatParserTest() {
    }

    // a "unit" edmx
    private String edmxFile            = "/META-INF/edmx.xml";
    // an SAP Data Services sample edmx
    private String sapDsSampleEdmxFile = "/META-INF/sap_ds_sample_edmx.xml";

    @Test
    public void testInheritance() throws FileNotFoundException, InterruptedException {

        // do the raw xml first...
        XMLEventReader2 reader = InternalUtil.newXMLEventReader(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(edmxFile))));
        EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
        assertTrue("parsed", d != null);

        checkTypeHierarchy(d);

        // now take the parsed result, back to xml, re-parse, check that...
        StringWriter sw = new StringWriter();
        EdmxFormatWriter.write(d, sw);

        EdmDataServices d2 = new EdmxFormatParser().parseMetadata(InternalUtil.newXMLEventReader(new StringReader(sw.toString())));
        assertTrue("parsed", d2 != null);

        checkTypeHierarchy(d2);
    }

    @Test
    public void parseSapDsSample() {
        XMLEventReader2 reader = InternalUtil.newXMLEventReader(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(sapDsSampleEdmxFile))));
        EdmDataServices d = new EdmxFormatParser().parseMetadata(reader);
        assertTrue("parsed", d != null);
    }

    private void checkTypeHierarchy(EdmDataServices d) {
        EdmEntityType airport = d.findEdmEntitySet("Airport").getType();
        EdmEntityType badAirport = d.findEdmEntitySet("BadAirport").getType();
        assertTrue(badAirport.getBaseType().equals(airport));
        assertTrue("" + badAirport.getKeys() + ":" + airport.getKeys(), badAirport.getKeys().equals(airport.getKeys()));
        assertTrue(badAirport.getDeclaredNavigationProperties().size() == 0);
        assertEquals(badAirport.getNavigationProperties().size(), airport.getDeclaredNavigationProperties().size() + badAirport.getDeclaredNavigationProperties().size());
        assertTrue(badAirport.getDeclaredProperties().size() == 2);
        assertTrue(badAirport.findDeclaredProperty("rating") != null);
        assertTrue(badAirport.findDeclaredProperty("prop2") != null);
        assertTrue(badAirport.getProperties().size() == airport.getDeclaredProperties().size() + badAirport.getDeclaredProperties().size());
        assertTrue(badAirport.findProperty("name") != null);
        assertTrue(badAirport.findProperty("code") != null);
        assertTrue(badAirport.findProperty("country") != null);
        assertTrue(badAirport.findProperty("rating") != null);
        assertTrue(badAirport.findProperty("prop2") != null);

        EdmEntityType schedule = d.findEdmEntitySet("FlightSchedule").getType();
        EdmEntityType subSchedule = d.findEdmEntitySet("SubFlightSchedule").getType();
        assertTrue(subSchedule.getBaseType().equals(schedule));
        assertTrue(subSchedule.getKeys().equals(schedule.getKeys()));
        assertTrue(subSchedule.getDeclaredNavigationProperties().size() == 0);
        assertEquals(2, subSchedule.getNavigationProperties().size());
        assertTrue(subSchedule.getNavigationProperties().size() == schedule.getDeclaredNavigationProperties().size() + subSchedule.getDeclaredNavigationProperties().size());

        assertTrue(subSchedule.getDeclaredProperties().size() == 3);
        assertTrue(subSchedule.findDeclaredProperty("prop3") != null);
        assertTrue(subSchedule.findDeclaredProperty("prop4") != null);
        assertTrue(subSchedule.findDeclaredProperty("prop5") != null);
        assertTrue(subSchedule.getProperties().size() == schedule.getDeclaredProperties().size() + subSchedule.getDeclaredProperties().size());
        assertTrue(subSchedule.findProperty("arrivalAirportCode") != null);
        assertTrue(subSchedule.findProperty("flightScheduleID") != null);
        assertTrue(subSchedule.findProperty("arrivalTime") != null);
        assertTrue(subSchedule.findProperty("flightNo") != null);
        assertTrue(subSchedule.findProperty("firstDeparture") != null);
        assertTrue(subSchedule.findProperty("departureTime") != null);
        assertTrue(subSchedule.findProperty("departureAirportCode") != null);
        assertTrue(subSchedule.findProperty("lastDeparture") != null);
        assertTrue(subSchedule.findProperty("prop3") != null);
        assertTrue(subSchedule.findProperty("prop4") != null);
        assertTrue(subSchedule.findProperty("prop5") != null);

        EdmEntityType subsubSchedule = d.findEdmEntitySet("SubSubFlightSchedule").getType();
        assertTrue(subsubSchedule.getBaseType().equals(subSchedule));
        assertTrue(subsubSchedule.getKeys().equals(subSchedule.getKeys()));
        assertTrue(subsubSchedule.getDeclaredNavigationProperties().size() == 1);
        assertTrue(subsubSchedule.getNavigationProperties().size() == schedule.getDeclaredNavigationProperties().size() + subSchedule.getDeclaredNavigationProperties().size()
                + subsubSchedule.getDeclaredNavigationProperties().size());

        assertTrue(subsubSchedule.getDeclaredProperties().size() == 4);
        assertTrue(subsubSchedule.getProperties().size() == subsubSchedule.getDeclaredProperties().size() + subSchedule.getDeclaredProperties().size() + schedule.getDeclaredProperties().size());
    }

}