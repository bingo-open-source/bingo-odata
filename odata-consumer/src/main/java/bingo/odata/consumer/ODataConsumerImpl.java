/*
 * Copyright 2013 the original author or authors.
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
package bingo.odata.consumer;

import static bingo.odata.consumer.util.ODataConsumerContextHelper.initEntitySetContext;
import static bingo.odata.consumer.util.ODataConsumerContextHelper.initEntityTypeContext;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bingo.lang.Collections;
import bingo.lang.Strings;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.consumer.requests.DeleteEntityRequest;
import bingo.odata.consumer.requests.InsertEntityRequest;
import bingo.odata.consumer.requests.MergeEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.requests.RetrieveCountRequest;
import bingo.odata.consumer.requests.RetrieveEntityRequest;
import bingo.odata.consumer.requests.RetrieveEntitySetRequest;
import bingo.odata.consumer.requests.RetrievePropertyRequest;
import bingo.odata.consumer.requests.UpdateEntityRequest;
import bingo.odata.consumer.requests.behaviors.Behavior;
import bingo.odata.consumer.requests.invoke.FunctionRequest;
import bingo.odata.consumer.requests.metadata.MetadataDocumentRequest;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.exceptions.ODataNotImplementedException;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;

public class ODataConsumerImpl implements ODataConsumer {
	private static final Log log = LogFactory.get(ODataConsumerImpl.class);
	
	private ODataConsumerConfig config = new ODataConsumerConfigImpl();
	
	private String serviceRoot;
	
	private ODataMetadataVerifier verifier;
	
	private ODataServices services;

	/**
	 * instantiate a consumer with the service root of the target odata producer.
	 * like:
	 * http://services.odata.org/V3/OData/OData.svc/
	 * @param serviceRoot
	 */
	public ODataConsumerImpl(String serviceRoot, Behavior... behaviors) {
		this(serviceRoot, true, behaviors);
	}
	
	public ODataConsumerImpl(String serviceRoot, boolean retrieveMetadataImmediately, Behavior... behaviors) {
		this.config.setClientBehaviors(Collections.listOf(behaviors));
		if(!Strings.endsWith(serviceRoot, "/")) {
			serviceRoot += "/";
		}
		this.serviceRoot = serviceRoot;
		if(retrieveMetadataImmediately) {
			ensureMetadata();
		}
	}

	private void ensureMetadata() {
		if(null == services) {
			services = retrieveServiceMetadata();
			verifier = new ODataMetadataVerifier(services);
		}
	}

	/**
	 * get the config of this consumer.
	 */
	public ODataConsumerConfig config() {
		return config;
	}

	/**
	 * config this consumer.
	 * @param config
	 */
	public void config(ODataConsumerConfig config) {
		this.config = config;
	}
	
	/**
	 * get the current services using in consumer.
	 * @return
	 */
	public ODataServices services() {
		return services;
	}

	/**
	 * get the {@link ODataServices} from OData Producer.
	 * 
	 * {@link ODataServices} represents the Metadata Document.
	 */
	public ODataServices retrieveServiceMetadata() throws ConnectFailedException{
		ODataConsumerContext context = new ODataConsumerContext(config);
		
		Request request = new MetadataDocumentRequest(context, this.serviceRoot);
		
		Response resp = request.send();
		
		if(resp.getStatus() == 200) {
			ODataServices services = ODataServices.parse(resp.getInputStream());
			
			if(null != services) {
				this.services = services;
				this.verifier = new ODataMetadataVerifier(services);
			}
			
			log.info("Retrieve Metadata Document successfully!");
			
			return services;
			
		} else throw resp.convertToError(context);
	}

	public int insertEntity(String entityType, Map<String, Object> object) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, object);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new InsertEntityRequest(context, serviceRoot).setEntityType(context.getEntitySet().getName())
				.setEntity(object);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.Created) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}

	public int insertEntity(EdmEntityType entityType, ODataEntity entity) {
		return insertEntity(entityType.getName(), entity.toMap());
	}

	public int deleteEntity(String entityType, String queryString) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * send a delete certain id entity command to producer. 
	 */
	public int deleteEntityByKey(String entityType, Object id) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new DeleteEntityRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(id);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}

	public int deleteEntity(EdmEntityType entityType, ODataKey key) {
		Object id = null;
		if(key.isPrimitiveValue()) {
			id = key.getPrimitiveValue();
			
		} else if(key.isNamedValues()) {
			id = key.getNamedValues();
			
		} else throw new RuntimeException("invalid key.");
		
		deleteEntityByKey(entityType.getName(), id);
		return 1;
	}

	public int deleteEntity(EdmEntityType entityType, ODataQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateEntity(String entityName, String queryString,
			Map<String, Object> updateFields) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateEntity(EdmEntityType entityType, ODataKey key,
			ODataEntity entity) {
		throw new ODataNotImplementedException("");
	}

	public int updateEntity(EdmEntityType entityType, ODataQueryInfo queryInfo,
			ODataEntity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int updateEntityByKey(String entityType, Object id, Map<String, Object> updateFields) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, updateFields);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new UpdateEntityRequest(context, serviceRoot)
					.setEntitySet(context.getEntitySet().getName()).setId(id).setFields(updateFields);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}

	public int mergeEntity(String entityName, String queryString,
			Map<String, Object> updateFields) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int mergeEntity(EdmEntityType entityType, ODataKey key,
			ODataEntity entity) {
		throw new ODataNotImplementedException("");
	}

	public int mergeEntityByKey(String entityType, Object id,
			Map<String, Object> updateFields) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, updateFields);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new MergeEntityRequest(context, serviceRoot)
					.setEntitySet(context.getEntitySet().getName()).setId(id).setFields(updateFields);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}

	public int mergeEntity(EdmEntityType entityType, ODataQueryInfo queryInfo,
			ODataEntity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * get a single entity by key from producer.
	 */
	public ODataEntity findEntity(String entityType, Object key) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType, key);
		
		Request request = new RetrieveEntityRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(key);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToEntity(context);
			
		} else throw response.convertToError(context);
	}

	public ODataEntity findEntity(EdmEntityType entityType, ODataKey key) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * get the entity set from producer.
	 */
	public ODataEntitySet findEntitySet(String entitySet) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet);
		
		ODataConsumerContext context = initEntitySetContext(this, entitySet);
		
		Request request = new RetrieveEntitySetRequest(context, serviceRoot).setEntitySet(entitySet);
		
		Response response = request.send();

		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToEntitySet(context);
			
		} else throw response.convertToError(context);
	}

	public ODataEntitySet findEntitySet(EdmEntityType entityType) {
		// TODO Auto-generated method stub
		return null;
	}

	public ODataEntitySet findEntitySet(String entitySet, String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	public ODataEntitySet findEntitySet(EdmEntityType entityType,
			ODataQueryInfo queryInfo) {
		throw new ODataNotImplementedException("");
	}

	public List<Map<String, Object>> findEntitySetAsList(String entitySetName) {
		
		ODataEntitySet entitySet = findEntitySet(entitySetName);
		
		List<ODataEntity> entities = entitySet.getEntities().toList();
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		for (ODataEntity entity : entities) {
			list.add(entity.toMap());
		}
		return list;
	}
	
	public List<Map<String, Object>> findEntitySetAsList(String entitySet,
			String queryString) {
		// TODO Auto-generated method stub
		return null;
	}

	public ODataProperty findProperty(String entityType, Object key, String property) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType, property);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType, key);
		
		Request request = new RetrievePropertyRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(key).setProperty(property);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToProperty(context);
			
		} else throw response.convertToError(context);
	}

	public ODataProperty findProperty(EdmEntityType entitType, ODataKey key,
			EdmProperty property) {
		throw new ODataNotImplementedException("");
	}

	public ODataNavigationProperty findNavigationProperty(String entitType, Object key,
			String property) {
		// TODO Auto-generated method stub
		return null;
	}

	public ODataNavigationProperty findNavigationProperty(EdmEntityType entitType,
			ODataKey key, EdmNavigationProperty property) {
		throw new ODataNotImplementedException("");
	}


	public long count(EdmEntitySet edmEntitySet) {
		String entitySet = edmEntitySet.getName();
		return count(entitySet);
	}

	public long count(String entitySet) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet);
		
		ODataConsumerContext context = initEntitySetContext(this, entitySet);
		
		Request request = new RetrieveCountRequest(context, serviceRoot).setEntitySet(entitySet);
		
		Response resp = request.send();
		
		if(resp.getStatus() == ODataResponseStatus.OK) {
			
			return resp.convertToLong(context);
			
		} else throw resp.convertToError(context);
	}

	public long count(String entityType, String queryString) {
		// TODO Auto-generated method stub
		return 0;
	}

	public long count(EdmEntityType entityType, ODataQueryInfo queryInfo) {
		EdmEntitySet entitySet = services.findEntitySet(entityType);
		
		if(null == queryInfo) return count(entitySet);
		else {
			throw new ODataNotImplementedException("");
		}
	}

	public String invokeFunction(String entitySet, String funcName, Map<String, Object> parameters) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
		
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = new ODataConsumerContext(config);
		
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.getString();
			
		} else throw response.convertToError(context);
	}

	public <T> T invokeFunction(String entitySet, String funcName, Map<String, Object> parameters, Class<T> clazz) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
	
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = new ODataConsumerContext(config);
	
		context.setEntitySet(services.findEntitySet(entitySet));
		
		context.setEntityType(services.findEntityType(context.getEntitySet().getEntityType()));
		
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToObject(clazz, func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}

	public <T> List<T> invokeFunctionForList(String entitySet, String funcName,
			Map<String, Object> parameters, Class<T> listClass) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
	
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = new ODataConsumerContext(config);
		
		if(Strings.isNotBlank(entitySet)) {
			context.setEntitySet(services.findEntitySet(entitySet));
			
			context.setEntityType(services.findEntityType(context.getEntitySet().getEntityType()));
		}
	
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToObjectList(listClass, func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}

	public <T> T invokeFunction(EdmFunctionImport func,	ODataParameters parameters, Class<T> t) {
		return invokeFunction(func.getEntitySet(), func.getName(), parameters.getParametersMap(), t);
	}

	public ODataValue invokeFunctionForODataValue(String entitySet, String funcName, Map<String, Object> parameters) {
		ensureMetadata();
		
		EdmFunctionImport func = null;

		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = new ODataConsumerContext(config);

		if(Strings.isNotBlank(entitySet)) {
			context.setEntitySet(services.findEntitySet(entitySet));
			
			context.setEntityType(services.findEntityType(context.getEntitySet().getEntityType()));
		}
		
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToODataValue(func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}

	public ODataValue invokeFunctionForODataValue(EdmFunctionImport func, ODataParameters parameters) {
		return invokeFunctionForODataValue(func.getEntitySet(), func.getName(), parameters.getParametersMap());
	}
}
