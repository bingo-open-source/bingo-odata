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
import bingo.meta.edm.EdmCollectionType;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataReader;
import bingo.odata.ODataReaderContext;
import bingo.odata.ODataResponseStatus;
import bingo.odata.ODataServices;
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.consumer.exceptions.ResolveFailedException;
import bingo.odata.consumer.ext.Page;
import bingo.odata.consumer.requests.DeleteEntityRequest;
import bingo.odata.consumer.requests.InsertEntityRequest;
import bingo.odata.consumer.requests.MergeEntityRequest;
import bingo.odata.consumer.requests.Request;
import bingo.odata.consumer.requests.Response;
import bingo.odata.consumer.requests.CountRequest;
import bingo.odata.consumer.requests.FindEntityRequest;
import bingo.odata.consumer.requests.FindEntitySetRequest;
import bingo.odata.consumer.requests.FindPropertyRequest;
import bingo.odata.consumer.requests.UpdateEntityRequest;
import bingo.odata.consumer.requests.behaviors.Behavior;
import bingo.odata.consumer.requests.builders.QueryBuilder;
import bingo.odata.consumer.requests.builders.QueryBuilderImpl;
import bingo.odata.consumer.requests.invoke.FunctionRequest;
import bingo.odata.consumer.requests.metadata.MetadataDocumentRequest;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.consumer.util.ODataQueryTranslator;
import bingo.odata.exceptions.ODataNotImplementedException;
import bingo.odata.expression.BoolExpression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.OrderByExpression;
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

	private String qualifiedKey(Object key) {
		if(Strings.startsWith(key.toString(), "'") && Strings.endsWith(key.toString(), "'")) return key.toString();
		return "'" + key + "'";
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
		
		request.setLog(false);
		
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

	/**
	 * send a delete certain id entity command to producer. 
	 */
	public int deleteEntity(String entityType, Object key) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new DeleteEntityRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(qualifiedKey(key));
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}

	public int deleteEntity(EdmEntityType entityType, ODataKey key) {
		deleteEntity(entityType.getName(), key.toKeyString(false));
		return 1;
	}

	public int updateEntity(EdmEntityType entityType, ODataKey key,	ODataEntity entity) {
		return updateEntity(entityType.getName(), key.toKeyString(false), entity.toMap());
	}

	public int updateEntity(String entityType, Object key, Map<String, Object> updateFields) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, updateFields);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new UpdateEntityRequest(context, serviceRoot)
					.setEntitySet(context.getEntitySet().getName()).setId(qualifiedKey(key)).setFields(updateFields);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}

	public int mergeEntity(String entityType, Object key, Map<String, Object> updateFields) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasFields(entityType, updateFields);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType);
		
		Request request = new MergeEntityRequest(context, serviceRoot)
					.setEntitySet(context.getEntitySet().getName()).setId(qualifiedKey(key)).setFields(updateFields);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.NoContent) {
			return 1;
		} else {
			throw response.convertToError(context);
		}
	}
	
	public ODataEntity findEntity(EdmEntityType entityType, ODataKey key) {
		return findEntity(entityType.getName(), key.toKeyString(false));
	}

	/**
	 * get a single entity by key from producer.
	 */
	public ODataEntity findEntity(String entityType, Object key) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType, key);
		
		Request request = new FindEntityRequest(context, serviceRoot)
								.setEntitySet(context.getEntitySet().getName()).setId(qualifiedKey(key));
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToEntity(context);
			
		} else throw response.convertToError(context);
	}

	/**
	 * get the entity set from producer.
	 */

	public QueryBuilder	query(String entitySet) {
		return new QueryBuilderImpl(this).entitySet(entitySet);
	}
	
	public List<Map<String, Object>> findEntitySet(String entitySet) {
		return findEntitySet(entitySet, null, null, null, null, null, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String queryString) {
		return findEntitySet(entitySet, queryString, null, null, null, null, null);
	}
	
	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Map<String, Object> params) {
		return findEntitySet(entitySet, where, params, null, null, null, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Map<String, Object> params, String orderBy) {
		return findEntitySet(entitySet, where, params, orderBy, null, null, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Map<String, Object> params, String orderBy, String[] fields) {
		return findEntitySet(entitySet, where, params, orderBy, fields, null, null);
	}
	
	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Map<String, Object> params, String orderBy, String[] fields, String[] expand) {
		return findEntitySet(entitySet, where, params, orderBy, fields, expand, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Map<String, Object> params, String orderBy, String[] fields, String[] expand, Page page) {
		ensureMetadata();
		
		String whereParamed = Strings.isBlank(where)? 
									null : ODataQueryTranslator.translateFilter(where, params, false);
		
		BoolExpression filter = Strings.isBlank(whereParamed)? 
									null : ODataQueryInfoParser.parseFilter(whereParamed);
		
		List<OrderByExpression> orderByExpressions = Strings.isBlank(orderBy)?
									null : ODataQueryInfoParser.parseOrderBy(orderBy);
		 
		List<EntitySimpleProperty> select = null == fields || fields.length == 0?
									null : ODataQueryInfoParser.parseSelect(Strings.join(fields, ","));
		
		List<EntitySimpleProperty> expandList = null == expand || expand.length == 0?
									null : ODataQueryInfoParser.parseExpand(Strings.join(expand, ","));
		
		ODataQueryInfo queryInfo = new ODataQueryInfo(expandList, filter, orderByExpressions
				, null == page? null : page.getSkip(), null == page? null : page.getTop()
				, null, null, select, null);
		
		ODataEntitySet oDataEntitySet = findEntitySet(services.findEntitySet(entitySet), queryInfo);
		
		List<ODataEntity> entities = oDataEntitySet.getEntities().toList();
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		
		for (ODataEntity entity : entities) {
			list.add(entity.toMap());
		}
		
		return list;
	}

	public ODataEntitySet findEntitySet(EdmEntitySet entitySet) {
		return findEntitySet(entitySet, null);
	}

	public ODataEntitySet findEntitySet(EdmEntitySet entitySet,	ODataQueryInfo queryInfo) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet.getName());
		
		ODataConsumerContext context = initEntitySetContext(this, entitySet.getName());
		
		Request request = new FindEntitySetRequest(context, serviceRoot).setEntitySet(entitySet.getName());
		
		if(null != queryInfo) request.addAdditionalQueryString(ODataQueryInfoParser.toQueryString(queryInfo));
		
		Response response = request.send();

		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToEntitySet(context);
			
		} else throw response.convertToError(context);
	}

	public ODataProperty findProperty(EdmEntityType entitType, ODataKey key, EdmProperty property) {
		return findProperty(entitType.getName(), key.toKeyString(false), property.getName());
	}
	
	public ODataProperty findProperty(String entityType, Object key, String property) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityType(entityType, property);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType, key);
		
		Request request = new FindPropertyRequest(context, serviceRoot)
			.setEntitySet(context.getEntitySet().getName()).setId(qualifiedKey(key)).setProperty(property);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToProperty(context);
			
		} else throw response.convertToError(context);
	}

	public ODataNavigationProperty findNavigationProperty(EdmEntityType entitType, 
			ODataKey key, EdmNavigationProperty property) {
		return findNavigationProperty(entitType.getName(), key.toKeyString(false), property.getName());
	}
	
	public ODataNavigationProperty findNavigationProperty(String entityType, Object key, String naviProperty) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntityTypeWithNavigationProp(entityType, naviProperty);
		
		ODataConsumerContext context = initEntityTypeContext(this, entityType, key);
		
		Request request = new FindPropertyRequest(context, serviceRoot)
			.setEntitySet(context.getEntitySet().getName()).setId(qualifiedKey(key)).setProperty(naviProperty);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToNavigationProperty(context);
			
		} else throw response.convertToError(context);
	}

	public long count(EdmEntitySet edmEntitySet) {
		return count(edmEntitySet.getName());
	}

	public long count(String entitySet) {
		return count(entitySet, null);
	}

	public long count(EdmEntitySet entitySet, ODataQueryInfo queryInfo) {
		return count(entitySet.getName(), ODataQueryInfoParser.toQueryString(queryInfo), false);
	}
	
	public long count(String entitySet, String queryString) {
		return count(entitySet, queryString, true);
	}

	private long count(String entitySet, String queryString, boolean resolveQueryString) {
		ensureMetadata();
		
		if(config.isVerifyMetadata()) verifier.hasEntitySet(entitySet);
		
		ODataConsumerContext context = initEntitySetContext(this, entitySet);
		
		Request request = new CountRequest(context, serviceRoot).setEntitySet(entitySet);

		if(resolveQueryString) {
			queryString = ODataQueryTranslator.translateFilter(queryString);
		}
			
		request.addAdditionalQueryString(queryString);
		
		Response resp = request.send();
		
		if(resp.getStatus() == ODataResponseStatus.OK) {
			
			return resp.convertToRawLong(context);
			
		} else throw resp.convertToError(context);
	}

	public String invokeFunctionForRawResult(String funcName, Map<String, Object> parameters) {
		return invokeFunctionForRawResult(funcName, parameters, (String)null);
	}
	public String invokeFunctionForRawResult(String funcName, Map<String, Object> parameters, String entitySet) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
		
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper.initFunctionContext(this, func, entitySet);
		
		Request request = new FunctionRequest(context, serviceRoot)
					.setHttpMethod(null == func? null:func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.getString();
			
		} else throw response.convertToError(context);
	}

	public <T> T invokeFunctionForEntity(String funcName, Map<String, Object> parameters, Class<T> clazz) {
		return invokeFunctionForEntity(funcName, parameters, null, clazz);
	}
	
	public <T> T invokeFunctionForType(String funcName, Map<String, Object> parameters, Class<T> clazz) {
		return invokeFunctionForEntity(funcName, parameters, null, clazz);
	}
	
	public <T> T invokeFunctionForType(String funcName, Map<String, Object> parameters, String entitySet, Class<T> clazz) {
		return invokeFunctionForEntity(funcName, parameters, entitySet, clazz);
	}
	
	public <T> T invokeFunctionForEntity(String funcName, Map<String, Object> parameters, String entitySet, Class<T> clazz) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
	
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper.initFunctionContext(this, func, entitySet);
		
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToObject(clazz, func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}

	public <T> List<T> invokeFunctionForEntityList(String funcName,
			Map<String, Object> parameters, Class<T> listClass) {
		return invokeFunctionForEntityList(funcName, parameters, null, listClass);
	}
	public <T> List<T> invokeFunctionForEntityList(String funcName,
			Map<String, Object> parameters, String entitySet, Class<T> listClass) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
	
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper.initFunctionContext(this, func, entitySet);
	
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToObjectList(listClass, func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}

	public <T> T invokeFunctionForEntity(EdmFunctionImport func,	ODataParameters parameters, Class<T> t) {
		return invokeFunctionForEntity(func.getName(), parameters.getParametersMap(), func.getEntitySet(), t);
	}

	public ODataValue invokeFunctionForODataValue(String funcName, Map<String, Object> parameters) {
		return invokeFunctionForODataValue(funcName, parameters, null);
	}
	public ODataValue invokeFunctionForODataValue(String funcName, Map<String, Object> parameters, String entitySet) {
		ensureMetadata();
		
		EdmFunctionImport func = null;

		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);

		ODataConsumerContext context = ODataConsumerContextHelper.initFunctionContext(this, func, entitySet);
		
		Request request = new FunctionRequest(context, serviceRoot).setHttpMethod(func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToODataValue(func.getReturnType(), context);
			
		} else throw response.convertToError(context);
	}

	public ODataValue invokeFunctionForODataValue(EdmFunctionImport func, ODataParameters parameters) {
		return invokeFunctionForODataValue(func.getName(), parameters.getParametersMap(), func.getEntitySet());
	}

	public String invokeFunctionForString(String funcName,
			Map<String, Object> parameters) {
		return invokeFunctionForString(funcName, parameters, null);
	}

	public String invokeFunctionForString(String funcName, Map<String, Object> parameters, String entitySet) {
		ensureMetadata();
		
		EdmFunctionImport func = null;
		
		if(config.isVerifyMetadata()) func = verifier.hasFunction(entitySet, funcName);
		
		ODataConsumerContext context = ODataConsumerContextHelper.initFunctionContext(this, func, entitySet);
		
		Request request = new FunctionRequest(context, serviceRoot)
					.setHttpMethod(null == func? null:func.getHttpMethod())
					.setEntitySet(entitySet).setFunction(funcName).setParams(parameters);
		
		Response response = request.send();
		
		if(response.getStatus() == ODataResponseStatus.OK) {
			
			return response.convertToString(context);
			
		} else throw response.convertToError(context);
	}
}
