/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.odata.producer.demo;

import bingo.lang.Enumerables;
import bingo.odata.ODataServices;
import bingo.odata.ODataVersion;
import bingo.odata.edm.EdmAssociation;
import bingo.odata.edm.EdmCollectionType;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityTypeRef;
import bingo.odata.edm.EdmMultiplicity;
import bingo.odata.edm.EdmParameterMode;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.edm.builder.EdmAssociationBuilder;
import bingo.odata.edm.builder.EdmBuilders;
import bingo.odata.edm.builder.EdmEntityContainerBuilder;
import bingo.odata.edm.builder.EdmEntityTypeBuilder;
import bingo.odata.edm.builder.EdmSchemaBuilder;
import bingo.odata.producer.MetadataProducer;

public class DemoMetadataProducer implements MetadataProducer {
	
	private ODataServices metadata = null;
	
	public DemoMetadataProducer(){
		this.loadDemoMetadata();
	}

	public ODataServices getMetadata() {
	    return metadata;
    }
	
	private void loadDemoMetadata(){
		EdmSchemaBuilder schema = EdmBuilders.schema("ODataDemo","Self");
		
		//entity types
		EdmEntityTypeBuilder product = EdmBuilders.entityType("Product")
										  .addKeyProperty("ID", EdmSimpleType.Int32)
										  .addProperty("Name",EdmSimpleType.String,true)
										  .addProperty("Description",EdmSimpleType.String,true)
										  .addProperty("ReleaseDate", EdmSimpleType.DateTime, false)
										  .addProperty("DiscontinuedDate",EdmSimpleType.DateTime,false)
										  .addProperty("Rating",EdmSimpleType.Int32,false)
										  .addProperty("Price",EdmSimpleType.Decimal,false);		
		
		EdmEntityTypeBuilder category = EdmBuilders.entityType("Category")
										    .addKeyProperty("ID", EdmSimpleType.Int32)
											.addProperty("Name", EdmSimpleType.String, true);
		
		EdmEntityTypeBuilder supplier = EdmBuilders.entityType("Supplier")
											.addKeyProperty("ID", EdmSimpleType.Int32)
											.addProperty("Name", EdmSimpleType.String, true);
		
		//entity type ref
		EdmEntityTypeRef productRef  = product.buildRef(schema);
		EdmEntityTypeRef categoryRef = category.buildRef(schema);
		EdmEntityTypeRef supplierRef = supplier.buildRef(schema);
		
		//associations
		EdmAssociation productCategories = 
			new EdmAssociationBuilder("Product_Category_Category_Products")
					.setEnd1("Product_Category",productRef,EdmMultiplicity.Many)
					.setEnd2("Category_Products", categoryRef, EdmMultiplicity.ZeroOrOne)
					.build();
		
		EdmAssociation productSuppliers = 
			new EdmAssociationBuilder("Product_Supplier_Supplier_Products")
					.setEnd1("Product_Supplier",productRef,EdmMultiplicity.Many)
					.setEnd2("Supplier_Products",supplierRef,EdmMultiplicity.ZeroOrOne)
					.build();
		
		product.addNavigationProperty("Category",productCategories,productCategories.getEnd1(),productCategories.getEnd2());
		product.addNavigationProperty("Supplier",productSuppliers,productSuppliers.getEnd1(),productSuppliers.getEnd2());
		
		category.addNavigationProperty("Products",productCategories,productCategories.getEnd2(),productCategories.getEnd1());
		
		supplier.addNavigationProperty("Products",productSuppliers,productSuppliers.getEnd2(),productSuppliers.getEnd1());
		
		
		//entity containers
		EdmEntityContainerBuilder demoService = EdmBuilders.entityContainer("DemoService",true);
		
		//entity sets
		EdmEntitySet products   = new EdmEntitySet("Products",   productRef);
		EdmEntitySet categories = new EdmEntitySet("Categories", categoryRef);
		EdmEntitySet suppliers  = new EdmEntitySet("Suppliers",  supplierRef);
		
		demoService.addEntitySets(products,categories,suppliers);
		
		demoService.addAssociationSet(EdmBuilders.associationSet("Products_Category_Categories")
												 .setAssociation(productCategories)
												 .setEnd1("Product_Category",products.getName())
												 .setEnd2("Category_Products",categories.getName())
												 .build());
												 
		demoService.addAssociationSet(EdmBuilders.associationSet("Products_Supplier_Suppliers")
												 .setAssociation(productSuppliers)
												 .setEnd1("Product_Supplier",products.getName())
												 .setEnd2("Supplier_Products",suppliers.getName())
												 .build());
		
		demoService.addFunctionImport(EdmBuilders.functionImport("GetProductsByRating")
												 .setEntitySet(products.getName())
												 .setReturnType(EdmCollectionType.of(productRef))
												 .addParameter("rating", EdmSimpleType.Int32, EdmParameterMode.In)
												 .build());
		
		schema.addEntityTypes(product.build(),category.build(),supplier.build())
	      	  .addAssociations(productCategories,productSuppliers)
	      	  .addEntityContainer(demoService.build());
		
		metadata = new ODataServices(ODataVersion.V3,Enumerables.of(schema.build()));
	}
}