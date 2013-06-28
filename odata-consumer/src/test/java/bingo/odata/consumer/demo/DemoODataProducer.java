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
package bingo.odata.consumer.demo;

import static bingo.meta.edm.EdmUtils.fullQualifiedName;

import java.util.UUID;

import bingo.lang.Assert;
import bingo.lang.Dates;
import bingo.lang.Enumerables;
import bingo.lang.Strings;
import bingo.meta.edm.EdmAssociation;
import bingo.meta.edm.EdmAssociationBuilder;
import bingo.meta.edm.EdmBuilders;
import bingo.meta.edm.EdmCollectionType;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmEntityContainerBuilder;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmEntityTypeBuilder;
import bingo.meta.edm.EdmEntityTypeRef;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmMultiplicity;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmParameter;
import bingo.meta.edm.EdmParameterMode;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmPropertyBuilder;
import bingo.meta.edm.EdmSchemaBuilder;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.meta.edm.EdmTypeKind;
import bingo.meta.edm.EdmTypes;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
import bingo.odata.exceptions.ODataBadRequestException;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataEntitySetBuilder;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataKeyImpl;
import bingo.odata.model.ODataNavigationPropertyImpl;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataPropertyImpl;
import bingo.odata.model.ODataRawValueImpl;
import bingo.odata.model.ODataValue;
import bingo.odata.model.ODataValueBuilder;
import bingo.odata.model.ODataValueImpl;
import bingo.odata.producer.ODataProducer;
import bingo.odata.producer.ODataProducerAdapter;
import bingo.odata.producer.ODataProducerContext;

public class DemoODataProducer extends ODataProducerAdapter implements ODataProducer {
	
	public static final Object INVOKED_FUNCTION = "invoke function successfully!";
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
					 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
								     .addProperty("ID",0)
								     .addProperty("Name","Food")
								     .build());
			
		}
		
		if(entityType.getName().equalsIgnoreCase("Product")){
			builder.addEntity(builder.newEntity()
									 .setKey(new ODataKeyImpl("123456"))
									 .addProperty("ID",0)
									 .addProperty("Name","Bread")
									 .addProperty("Description","Whole grain bread")									 
									 .addProperty("ReleaseDate",Dates.parse("1992-01-01"))
									 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
									 .addProperty("Rating",4)
									 .addProperty("Price",2.5)
									 .build());
			
			builder.addEntity(builder.newEntity()
					 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
					 .addProperty("ID",1)
					 .addProperty("Name","Milk")
					 .addProperty("Description","Low fat milk")								 
					 .addProperty("ReleaseDate",Dates.parse("1995-01-01"))
					 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
					 .addProperty("Rating",3)
					 .addProperty("Price",3.5)
					 .build());

			
			builder.addEntity(builder.newEntity()
					 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
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
	    return 3;
    }

	public ODataEntity retrieveEntity(ODataProducerContext context,EdmEntityType entityType, ODataKey key, ODataQueryInfo queryInfo) {
	    ODataEntityBuilder builder = new ODataEntityBuilder(context.getEntitySet(), entityType);
	    
		if(entityType.getName().equalsIgnoreCase("Category")){
			builder.addProperty("ID",0)
			 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
				   .addProperty("Name","Food");
		}
		
		if(entityType.getName().equalsIgnoreCase("Product")){
			builder.addProperty("ID",0)
			 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
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
	
	public ODataValue invokeFunction(ODataProducerContext context, EdmFunctionImport func, ODataParameters parameters) {
		EdmSchemaBuilder schema = EdmBuilders.schema("ODataDemo","Self");

		EdmEntityTypeBuilder product = EdmBuilders.entityType("Product",fullQualifiedName(schema, "Product"))
										  .addKeyProperty("ID", EdmSimpleType.INT32)
										  .addPropertyForSyndicationTitle("Name",EdmSimpleType.STRING,true)
										  .addPropertyForSyndicationSummary("Description",EdmSimpleType.STRING,true)
										  .addProperty("ReleaseDate", EdmSimpleType.DATETIME, false)
										  .addProperty("DiscontinuedDate",EdmSimpleType.DATETIME,false)
										  .addProperty("Rating",EdmSimpleType.INT32,false)
										  .addProperty("Price",EdmSimpleType.DECIMAL,false);	
		EdmEntityType entityType = product.build();
		EdmEntitySet entitySet = new EdmEntitySet("Products", product.buildRef(schema));
		
		String funcName = func.getName();
		if(Strings.equals(funcName, "getEntity")) {
			
			
			ODataEntityBuilder builder = new ODataEntityBuilder(entitySet, entityType);
			builder.addProperty("ID",0)
			 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
					 .addProperty("Name","Bread")
					 .addProperty("Description","Whole grain bread")									 
					 .addProperty("ReleaseDate",Dates.parse("1992-01-01"))
					 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
					 .addProperty("Rating",4)
					 .addProperty("Price",2.5);
			ODataEntity entity = builder.build();
			return new ODataValueBuilder().entity(entity).build();
		} else if(Strings.equals(funcName, "getEntitySet")) {
			ODataEntitySetBuilder builder = new ODataEntitySetBuilder(context.getEntitySet(),entityType);
			
			if(entityType.getName().equalsIgnoreCase("Category")){
				
				builder.addEntity(builder.newEntity()
						 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
									     .addProperty("ID",0)
									     .addProperty("Name","Food")
									     .build());
				
			}
			
			if(entityType.getName().equalsIgnoreCase("Product")){
				builder.addEntity(builder.newEntity()
										 .setKey(new ODataKeyImpl("123456"))
										 .addProperty("ID",0)
										 .addProperty("Name","Bread")
										 .addProperty("Description","Whole grain bread")									 
										 .addProperty("ReleaseDate",Dates.parse("1992-01-01"))
										 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
										 .addProperty("Rating",4)
										 .addProperty("Price",2.5)
										 .build());
				
				builder.addEntity(builder.newEntity()
						 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
						 .addProperty("ID",1)
						 .addProperty("Name","Milk")
						 .addProperty("Description","Low fat milk")								 
						 .addProperty("ReleaseDate",Dates.parse("1995-01-01"))
						 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
						 .addProperty("Rating",3)
						 .addProperty("Price",3.5)
						 .build());

				
				builder.addEntity(builder.newEntity()
						 .setKey(new ODataKeyImpl(UUID.randomUUID().toString()))
						 .addProperty("ID",2)
						 .addProperty("Name","Vint soda")
						 .addProperty("Description","Americana Variety - Mix of 6 flavors")						 
						 .addProperty("ReleaseDate",Dates.parse("2000-10-01"))
						 .addProperty("DiscontinuedDate",Dates.parse("1999-10-01"))
						 .addProperty("Rating",3)
						 .addProperty("Price",20.9)
						 .build());
			}
			ODataEntitySet entitySet2 = builder.build();
			return new ODataValueBuilder().entitySet(entitySet2).build();
		} else if(Strings.equals(funcName, "getString")) {
			for(EdmParameter param : func.getParameters()){
				if(null == parameters.getParameter(param.getName())){
					throw new ODataBadRequestException(Strings.format("parameter '{0}' required",param.getName()));
				}
			}
			
		    return new ODataValueImpl(ODataObjectKind.Raw, new ODataRawValueImpl(EdmSimpleType.STRING, INVOKED_FUNCTION));
		}
		return null;
    }

	private ODataServices loadDemoMetadata(){
		EdmSchemaBuilder schema = DemoModelAndData.edmSchemaBuilder();
		
		//complex types
		EdmComplexType address = DemoModelAndData.edmComplexTypes().get(0);	
		//entity types
		EdmEntityTypeBuilder product = DemoModelAndData.edmEntityTypeBuilders().get(0);		
		
		EdmEntityTypeBuilder category = DemoModelAndData.edmEntityTypeBuilders().get(1);
		
		EdmEntityTypeBuilder supplier = DemoModelAndData.edmEntityTypeBuilders().get(2);
		
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
		
		demoService.addFunctionImport(EdmBuilders.functionImport("getString")
												 .setEntitySet(products.getName())
												 .setReturnType(EdmCollectionType.of(productRef))
												 .addParameter("rating", EdmSimpleType.INT32, EdmParameterMode.In)
												 .build());
		demoService.addFunctionImport(EdmBuilders.functionImport("getEntity")
				.setEntitySet(products.getName())
//				.setReturnType(EdmCollectionType.of(productRef))
				.setReturnType(EdmEntityTypeRef.of(schema.build(), product.build()))
				.addParameter("rating", EdmSimpleType.INT32, EdmParameterMode.In)
				.build());
		demoService.addFunctionImport(EdmBuilders.functionImport("getEntitySet")
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

	@Override
	public ODataValue retrieveProperty(ODataProducerContext context,
			EdmEntityType entitType, ODataKey key, EdmProperty property) {
	    return new ODataValueImpl(ODataObjectKind.Property, 
	    		new ODataPropertyImpl(
	    				new EdmProperty("name","name", EdmSimpleType.STRING, true, null, false, 9999,
	    				0, 0, null, null, false, null, null)
	    			, INVOKED_FUNCTION));
	}

	@Override
	public ODataValue retrieveNavigationProperty(ODataProducerContext context,
			EdmEntityType entitType, ODataKey key,
			EdmNavigationProperty property) {
	    return null;
	}
	
}
