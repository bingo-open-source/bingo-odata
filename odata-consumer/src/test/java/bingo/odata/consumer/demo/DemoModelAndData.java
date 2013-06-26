package bingo.odata.consumer.demo;

import static bingo.meta.edm.EdmUtils.fullQualifiedName;

import java.util.ArrayList;
import java.util.List;

import bingo.meta.edm.EdmBuilders;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmEntityTypeBuilder;
import bingo.meta.edm.EdmSchemaBuilder;
import bingo.meta.edm.EdmSimpleType;

public class DemoModelAndData {
	public static EdmSchemaBuilder edmSchemaBuilder() {
		 return EdmBuilders.schema("ODataDemo","Self");
	}
	
	public static List<EdmComplexType> edmComplexTypes() {
		List<EdmComplexType> list = new ArrayList<EdmComplexType>();
		list.add(EdmBuilders.complexType("Address")
		    .addProperty("Street", EdmSimpleType.STRING, true)
		    .addProperty("City",EdmSimpleType.STRING,true)
		    .addProperty("State",EdmSimpleType.STRING,true)
		    .addProperty("ZipCode",EdmSimpleType.STRING,true)
		    .addProperty("Country",EdmSimpleType.STRING,true)
		    .build());	
		return list;
	}
	
	public static List<EdmEntityTypeBuilder> edmEntityTypeBuilders() {
		EdmSchemaBuilder schema = edmSchemaBuilder();
		List<EdmEntityTypeBuilder> list = new ArrayList<EdmEntityTypeBuilder>();
		
		list.add(EdmBuilders.entityType("Product",fullQualifiedName(schema, "Product"))
		  .addKeyProperty("ID", EdmSimpleType.INT32)
		  .addPropertyForSyndicationTitle("Name",EdmSimpleType.STRING,true)
		  .addPropertyForSyndicationSummary("Description",EdmSimpleType.STRING,true)
		  .addProperty("ReleaseDate", EdmSimpleType.DATETIME, false)
		  .addProperty("DiscontinuedDate",EdmSimpleType.DATETIME,false)
		  .addProperty("Rating",EdmSimpleType.INT32,false)
		  .addProperty("Price",EdmSimpleType.DECIMAL,false));
		
		list.add(EdmBuilders.entityType("Category",fullQualifiedName(schema, "Category"))
		    .addKeyProperty("ID", EdmSimpleType.INT32)
			.addPropertyForSyndicationTitle("Name", EdmSimpleType.STRING, true,true));
		
		list.add(EdmBuilders.entityType("Supplier",fullQualifiedName(schema, "Supplier"))
			.addKeyProperty("ID", EdmSimpleType.INT32)
			.addPropertyForSyndicationTitle("Name", EdmSimpleType.STRING, true,true)
			.addProperty("Address", edmComplexTypes().get(0), false));
		return list;
	}
}
