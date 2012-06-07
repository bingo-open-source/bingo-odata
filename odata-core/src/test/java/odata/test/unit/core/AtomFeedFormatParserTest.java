package odata.test.unit.core;

import junit.framework.Assert;

import odata.OEntityKey;
import odata.format.xml.AtomFeedFormatParser;

import org.junit.Test;


public class AtomFeedFormatParserTest {

    @Test
    public void parseEntitySetNameTests() {
        Assert.assertEquals("Products", AtomFeedFormatParser.parseEntitySetName("http://services.odata.org/(S(nbyygydfdw1j14nrurehvrex))/OData/OData.svc/Products(0)"));
        Assert
                .assertEquals(
                        "Alphabetical_list_of_products",
                            AtomFeedFormatParser
                                    .parseEntitySetName("http://services.odata.org/northwind/Northwind.svc/Alphabetical_list_of_products(CategoryName='Grains/Cereals',Discontinued=false,ProductID=22,ProductName='Gustaf''s%20Kn%C3%A4ckebr%C3%B6d')"));
        Assert.assertEquals("Modules", AtomFeedFormatParser.parseEntitySetName("http://www.pluralsight-training.net/odata/Modules('HTTP%20Plain%20XML%20(POX)%20Services')"));
    }

    @Test
    public void parseEntityKeyTests() {
        Assert.assertEquals(OEntityKey.create(0), AtomFeedFormatParser.parseEntityKey("http://services.odata.org/(S(nbyygydfdw1j14nrurehvrex))/OData/OData.svc/Products(0)"));
        Assert
                .assertEquals(
                        OEntityKey.create("CategoryName", "Grains/Cereals", "Discontinued", false, "ProductID", 22, "ProductName", "Gustaf's%20Kn%C3%A4ckebr%C3%B6d"),
                            AtomFeedFormatParser
                                    .parseEntityKey("http://services.odata.org/northwind/Northwind.svc/Alphabetical_list_of_products(CategoryName='Grains/Cereals',Discontinued=false,ProductID=22,ProductName='Gustaf''s%20Kn%C3%A4ckebr%C3%B6d')"));
        Assert.assertEquals(OEntityKey.create("HTTP%20Plain%20XML%20(POX)%20Services"), AtomFeedFormatParser
                .parseEntityKey("http://www.pluralsight-training.net/odata/Modules('HTTP%20Plain%20XML%20(POX)%20Services')"));
        Assert
                .assertEquals(
                        OEntityKey.create(
                                "CompanyPublicId",
                                    "is-decisions",
                                    "Customer",
                                    "O.N.U%20(Organisation%20des%20Nations%20Unies)",
                                    "Title",
                                    "L'O.N.U%20s%C3%A9curise%20ses%20r%C3%A9seaux%20avec%20UserLock"),
                            AtomFeedFormatParser
                                    .parseEntityKey(" http://proagora.com/fr/odata/CustomerReferences(CompanyPublicId='is-decisions',Customer='O.N.U%20(Organisation%20des%20Nations%20Unies)',Title='L''O.N.U%20s%C3%A9curise%20ses%20r%C3%A9seaux%20avec%20UserLock')"));
    }
}
