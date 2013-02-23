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

import static bingo.odata.edm.EdmUtils.fullQualifiedName;
import bingo.lang.Assert;
import bingo.lang.Dates;
import bingo.lang.Enumerables;
import bingo.lang.Strings;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataEntityBuilder;
import bingo.odata.data.ODataEntitySet;
import bingo.odata.data.ODataEntitySetBuilder;
import bingo.odata.data.ODataKey;
import bingo.odata.data.ODataParameters;
import bingo.odata.data.ODataReturnValue;
import bingo.odata.edm.EdmAssociation;
import bingo.odata.edm.EdmAssociationBuilder;
import bingo.odata.edm.EdmBuilders;
import bingo.odata.edm.EdmCollectionType;
import bingo.odata.edm.EdmComplexType;
import bingo.odata.edm.EdmEntityContainerBuilder;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmEntityTypeBuilder;
import bingo.odata.edm.EdmEntityTypeRef;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.edm.EdmMultiplicity;
import bingo.odata.edm.EdmParameter;
import bingo.odata.edm.EdmParameterMode;
import bingo.odata.edm.EdmSchemaBuilder;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.exceptions.ODataBadRequestException;
import bingo.odata.producer.ODataProducer;
import bingo.odata.producer.ODataProducerContext;

public class DemoODataProducer implements ODataProducer {
	
	private final ODataServices metadata;
	
	public DemoODataProducer(){
		this.metadata = this.loadDemoMetadata();
	}
	
	public ODataServices retrieveServiceMetadata() {
	    return metadata;
    }
	
	public ODataEntitySet retrieveEntitySet(ODataProducerContext context,EdmEntityType entityType, ODataQueryInfo queryInfo) {
		
		ODataEntitySetBuilder builder = new ODataEntitySetBuilder(context.getEntitySet(),entityType);
		
		if(entityType.getName().equalsIgnoreCase("Category")){
			
			builder.addEntity(builder.newEntity()
								     .addProperty("ID",0)
								     .addProperty("Name","Food")
								     .build());
			
		}
		
		if(entityType.getName().equalsIgnoreCase("Product")){
			builder.addEntity(builder.newEntity()
									 .addProperty("ID",0)
									 .addProperty("Name","Bread")
									 .addProperty("Description","Whole grain bread")									 
									 .addProperty("ReleaseDate",Dates.parse("1992-01-01"))
									 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
									 .addProperty("Rating",4)
									 .addProperty("Price",2.5)
									 .build());
			
			builder.addEntity(builder.newEntity()
					 .addProperty("ID",1)
					 .addProperty("Name","Milk")
					 .addProperty("Description","Low fat milk")								 
					 .addProperty("ReleaseDate",Dates.parse("1995-01-01"))
					 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
					 .addProperty("Rating",3)
					 .addProperty("Price",3.5)
					 .build());

			
			builder.addEntity(builder.newEntity()
					 .addProperty("ID",2)
					 .addProperty("Name","Vint soda")
					 .addProperty("Description","Americana Variety - Mix of 6 flavors")						 
					 .addProperty("ReleaseDate",Dates.parse("2000-10-01"))
					 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
					 .addProperty("Rating",3)
					 .addProperty("Price",20.9)
					 .build());
		}
		
		if(queryInfo.getInlineCount() != null){
			builder.setInlineCount(100l);
		}
		
		return builder.build();
	}

	public long retrieveCount(ODataProducerContext context, EdmEntityType entityType, ODataQueryInfo queryInfo) {
	    return 0;
    }

	public ODataEntity retrieveEntity(ODataProducerContext context,EdmEntityType entityType, ODataKey key, ODataQueryInfo queryInfo) {
	    ODataEntityBuilder builder = new ODataEntityBuilder(context.getEntitySet(), entityType);
	    
		if(entityType.getName().equalsIgnoreCase("Category")){
			builder.addProperty("ID",0)
				   .addProperty("Name","Food");
		}
		
		if(entityType.getName().equalsIgnoreCase("Product")){
			builder.addProperty("ID",0)
					 .addProperty("Name","Bread")
					 .addProperty("Description","Whole grain bread")									 
					 .addProperty("ReleaseDate",Dates.parse("1992-01-01"))
					 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
					 .addProperty("Rating",4)
					 .addProperty("Price",2.5);
		}else{
			return null;
		}
	    
	    return builder.build();
    }

	public void deleteEntity(ODataProducerContext context,EdmEntityType entityType, ODataKey key) {
		Assert.notNull(context.getEntitySet());
		Assert.notNull(entityType);
		Assert.notNull(key);
	}
	
	public ODataEntity insertEntity(ODataProducerContext context, EdmEntityType entityType, ODataEntity entity) {
		Assert.notNull(context.getEntitySet());
		Assert.notNull(entityType);
		Assert.notNull(entity);
		Assert.isFalse(entity.getProperties().isEmpty());
		
	    return entity;
    }
	
	public ODataEntity updateEntity(ODataProducerContext context, EdmEntityType entityType,ODataKey key, ODataEntity entity) {
		Assert.notNull(context.getEntitySet());
		Assert.notNull(entityType);
		Assert.notNull(key);
		Assert.notNull(entity);
		Assert.isFalse(entity.getProperties().isEmpty());
		
	    return entity;
    }
	
	public ODataEntity mergeEntity(ODataProducerContext context, EdmEntityType entityType, ODataKey key, ODataEntity entity) {
		Assert.notNull(context.getEntitySet());
		Assert.notNull(entityType);
		Assert.notNull(key);
		Assert.notNull(entity);
		Assert.isFalse(entity.getProperties().isEmpty());
		
	    return entity;
    }
	
	public ODataReturnValue invokeFunction(ODataProducerContext context, EdmFunctionImport func, ODataParameters parameters) {
		for(EdmParameter param : func.getParameters()){
			if(null == parameters.getParameter(param.getName())){
				throw new ODataBadRequestException(Strings.format("parameter '{0}' required",param.getName()));
			}
		}
		
	    return null;
    }

	private ODataServices loadDemoMetadata(){
		EdmSchemaBuilder schema = EdmBuilders.schema("ODataDemo","Self");
		
		//complex types
		EdmComplexType address = EdmBuilders.complexType("Address")
										    .addProperty("Street", EdmSimpleType.STRING, true)
										    .addProperty("City",EdmSimpleType.STRING,true)
										    .addProperty("State",EdmSimpleType.STRING,true)
										    .addProperty("ZipCode",EdmSimpleType.STRING,true)
										    .addProperty("Country",EdmSimpleType.STRING,true)
										    .build();		
		//entity types
		EdmEntityTypeBuilder product = EdmBuilders.entityType("Product",fullQualifiedName(schema, "Product"))
										  .addKeyProperty("ID", EdmSimpleType.INT32)
										  .addPropertyForSyndicationTitle("Name",EdmSimpleType.STRING,true)
										  .addPropertyForSyndicationSummary("Description",EdmSimpleType.STRING,true)
										  .addProperty("ReleaseDate", EdmSimpleType.DATETIME, false)
										  .addProperty("DiscontinuedDate",EdmSimpleType.DATETIME,false)
										  .addProperty("Rating",EdmSimpleType.INT32,false)
										  .addProperty("Price",EdmSimpleType.DECIMAL,false);		
		
		EdmEntityTypeBuilder category = EdmBuilders.entityType("Category",fullQualifiedName(schema, "Category"))
										    .addKeyProperty("ID", EdmSimpleType.INT32)
											.addPropertyForSyndicationTitle("Name", EdmSimpleType.STRING, true,true);
		
		EdmEntityTypeBuilder supplier = EdmBuilders.entityType("Supplier",fullQualifiedName(schema, "Supplier"))
											.addKeyProperty("ID", EdmSimpleType.INT32)
											.addPropertyForSyndicationTitle("Name", EdmSimpleType.STRING, true,true)
											.addProperty("Address", address, false);
		
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
												 .addParameter("rating", EdmSimpleType.INT32, EdmParameterMode.In)
												 .build());
		
		schema.addEntityTypes(product.build(),category.build(),supplier.build())
		      .addComplexTypes(address)
	      	  .addAssociations(productCategories,productSuppliers)
	      	  .addEntityContainer(demoService.build());
		
		return new ODataServices(Enumerables.of(schema.build()));
	}
}
