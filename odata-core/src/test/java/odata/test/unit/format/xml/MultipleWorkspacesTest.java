package odata.test.unit.format.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import junit.framework.Assert;

import odata.format.xml.AtomServiceDocumentFormatParser;
import odata.format.xml.AtomWorkspaceInfo;
import odata.stax2.XMLEventReader2;
import odata.stax2.domimpl.DomXMLFactoryProvider2;

import org.junit.Test;

import bingo.lang.Enumerable;

public class MultipleWorkspacesTest {

    @Test
    public void multipleWorkspacesTest() {
        List<AtomWorkspaceInfo> workspaces = parseWorkspaces("DEMO_FLIGHT.xml");
        Assert.assertEquals(2, workspaces.size());
        assertWorkspaceStructure(workspaces, 0, "Things");
        assertWorkspaceStructure(workspaces, 1, "Data", "flightDataCollection", "zDemoFlightCollection");

        workspaces = parseWorkspaces("DEMO_BANK.xml");
        Assert.assertEquals(2, workspaces.size());
        assertWorkspaceStructure(workspaces, 0, "Things");
        assertWorkspaceStructure(workspaces, 1, "Data", "zDemoBankCollection");
    }

    private void assertWorkspaceStructure(List<AtomWorkspaceInfo> workspaces, int index, String title, String... collections) {
        Assert.assertNotNull(workspaces);
        AtomWorkspaceInfo workspace = workspaces.get(index);
        Assert.assertEquals(title, workspace.getTitle());
        Assert.assertEquals(collections.length, workspace.getCollections().size());
        for (int i = 0; i < collections.length; i++) {
            Assert.assertEquals(collections[i], workspace.getCollections().get(i).getTitle());
        }
    }

    private List<AtomWorkspaceInfo> parseWorkspaces(String resourcePath) {
        InputStream xml = getClass().getResourceAsStream("/META-INF/" + resourcePath);
        XMLEventReader2 reader = DomXMLFactoryProvider2.getInstance().newXMLInputFactory2().createXMLEventReader(new InputStreamReader(xml));
        return Enumerable.of(AtomServiceDocumentFormatParser.parseWorkspaces(reader)).toList();
    }
}
