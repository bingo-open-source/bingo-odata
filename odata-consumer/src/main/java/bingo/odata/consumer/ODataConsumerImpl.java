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

import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.json.JSON;
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
import bingo.odata.ODataConverts;
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
import bingo.odata.consumer.handlers.CountHandler;
import bingo.odata.consumer.handlers.DeleteEntityHandler;
import bingo.odata.consumer.handlers.FindEntityHandler;
import bingo.odata.consumer.handlers.FindEntitySetHandler;
import bingo.odata.consumer.handlers.FindNavigationPropertyHandler;
import bingo.odata.consumer.handlers.FindPropertyHandler;
import bingo.odata.consumer.handlers.Handlers;
import bingo.odata.consumer.handlers.InsertEntityHandler;
import bingo.odata.consumer.handlers.InvokeFunctionHandler;
import bingo.odata.consumer.handlers.MergeEntityHandler;
import bingo.odata.consumer.handlers.RetrieveServiceMetadataHandler;
import bingo.odata.consumer.handlers.UpdateEntityHandler;
import bingo.odata.consumer.requests.DeleteEntityRequest;
import bingo.odata.consumer.requests.FindNavigationPropertyRequest;
import bingo.odata.consumer.requests.FunctionRequest;
import bingo.odata.consumer.requests.InsertEntityRequest;
import bingo.odata.consumer.requests.MergeEntityRequest;
import bingo.odata.consumer.requests.MetadataRequest;
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
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataConvertor;
import bingo.odata.consumer.util.ODataMetadataVerifier;
import bingo.odata.consumer.util.ODataQueryTranslator;
import bingo.odata.exceptions.ODataNotImplementedException;
import bingo.odata.expression.BoolExpression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.OrderByExpression;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntityBuilder;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;

public class ODataConsumerImpl implements ODataConsumer {
	
	private ODataConsumerConfig 	config 		= new ODataConsumerConfigImpl();
	
	private ODataServices 			services;

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
		Assert.notBlank(serviceRoot);
		this.config.setClientBehaviors(Collections.listOf(behaviors));
		if(!Strings.endsWith(serviceRoot, "/")) {
			serviceRoot += "/";
		}
		this.config.setProducerUrl(serviceRoot);
		if(retrieveMetadataImmediately) {
			consumerWithMetadata();
		}
	}

	private ODataConsumer consumerWithMetadata() {
		if(null == services) services = retrieveServiceMetadata();
		return this;
	}

	public ODataConsumerConfig config() {
		return config;
	}

	public ODataConsumer config(ODataConsumerConfig config) {
		this.config = config;
		return this;
	}
	
	public ODataServices services() {
		return services;
	}
	
	public ODataConsumer services(ODataServices services) {
		this.services = services;
		return this;
	}

	/**
	 * get the {@link ODataServices} from OData Producer.
	 * 
	 * {@link ODataServices} represents the Metadata Document.
	 */
	public ODataServices retrieveServiceMetadata() throws ConnectFailedException{
		return new RetrieveServiceMetadataHandler(this)
				.retrieveServiceMetadata();
	}
	
	public int insertEntity(EdmEntityType entityType, ODataEntity entity) {
		return insertEntityByMap(entityType.getName(), entity.toMap());
	}
	
	public int insertEntityByObj(String entityType, Object object) {
		return insertEntityByMap(entityType, JSON.decodeToMap(JSON.encode(object)));
	}

	public int insertEntityByMap(String entityType, Map<String, Object> object) {
		return Handlers.get(InsertEntityHandler.class, consumerWithMetadata())
					.insertEntityByMap(entityType, object);
	}

	/**
	 * send a delete certain id entity command to producer. 
	 */
	public int deleteEntity(String entityType, Object key) {
		return Handlers.get(DeleteEntityHandler.class, consumerWithMetadata()).deleteEntity(entityType, key);
	}

	public int deleteEntity(EdmEntityType entityType, ODataKey key) {
		return deleteEntity(entityType.getName(), key.toKeyString(false));
	}

	public int updateEntity(EdmEntityType entityType, ODataKey key,	ODataEntity entity) {
		return updateEntityByMap(entityType.getName(), key.toKeyString(false), entity.toMap());
	}

	public int updateEntityByMap(String entityType, Object key, Map<String, Object> updateFields) {
		return Handlers.get(UpdateEntityHandler.class, consumerWithMetadata())
				.updateEntity(entityType, key, updateFields);
	}

	public int updateEntityByObj(String entityType, Object key, Object updateObject) {
		return updateEntityByMap(entityType, key, JSON.decodeToMap(JSON.encode(updateObject)));
	}
	

	public int mergeEntity(String entityType, Object key, Map<String, Object> updateFields) {
		return Handlers.get(MergeEntityHandler.class, consumerWithMetadata())
				.mergeEntity(entityType, key, updateFields);
	}
	
	public ODataEntity findEntity(EdmEntitySet entitySet, ODataKey key) {
		Map<String, Object> entityMap = findEntity(entitySet.getName(), key.toKeyString(false));
		ODataConsumerContext context = ODataConsumerContextHelper.initContext(
								this, entitySet.getName(), null, key.toKeyString(false));
		return ODataConvertor.convertTo(ODataEntity.class, context, entityMap);
	}
	
	public <T> T findEntity(Class<T> clazz, String entityType, Object key) {
		Map<String, Object> entityMap = findEntity(entityType, key);
		return Converts.convert(entityMap, clazz);
	}

	public Map<String, Object> findEntity(String entityType, Object key) {
		return Handlers.get(FindEntityHandler.class, consumerWithMetadata())
				.findEntity(entityType, key);
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
		return Handlers.get(FindEntitySetHandler.class, consumerWithMetadata())
				.findEntitySet(entitySet, where, params, orderBy, fields, expand, page);
	}
	public <T> List<T> findEntitySet(Class<T> clazz, String entitySet) {
		return findEntitySet(clazz, entitySet, null, null, null, null, null, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String queryString) {
		return findEntitySet(clazz, entitySet, queryString, null, null, null, null, null);
	}
	
	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Map<String, Object> params) {
		return findEntitySet(clazz, entitySet, where, params, null, null, null, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Map<String, Object> params, String orderBy) {
		return findEntitySet(clazz, entitySet, where, params, orderBy, null, null, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Map<String, Object> params, String orderBy, String[] fields) {
		return findEntitySet(clazz, entitySet, where, params, orderBy, fields, null, null);
	}
	
	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Map<String, Object> params, String orderBy, String[] fields, String[] expand) {
		return findEntitySet(clazz, entitySet, where, params, orderBy, fields, expand, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Map<String, Object> params, String orderBy, String[] fields, String[] expand, Page page) {
		return Handlers.get(FindEntitySetHandler.class, consumerWithMetadata())
				.findEntitySet(clazz, entitySet, where, params, orderBy, fields, expand, page);
	}

	public ODataEntitySet findEntitySet(EdmEntitySet entitySet) {
		return findEntitySet(entitySet, null);
	}

	public ODataEntitySet findEntitySet(EdmEntitySet entitySet,	ODataQueryInfo queryInfo) {
		return Handlers.get(FindEntitySetHandler.class, consumerWithMetadata())
				.findEntitySet(entitySet, queryInfo);
	}

	public ODataProperty findProperty(EdmEntityType entityType, ODataKey key, EdmProperty property) {
		return Handlers.get(FindPropertyHandler.class, consumerWithMetadata())
				.findProperty(entityType, key, property);
	}
	
	public <T> T findProperty(String entityType, Object key, String property, Class<T> clazz) {
		return Handlers.get(FindPropertyHandler.class, consumerWithMetadata())
				.findProperty(entityType, key, property, clazz);
	}
	
	public String findPropertyForString(String entityType, Object key, String property) {
		return Handlers.get(FindPropertyHandler.class, consumerWithMetadata())
				.findPropertyForString(entityType, key, property);
	}

	public ODataNavigationProperty findNavigationProperty(EdmEntityType entityType, 
			ODataKey key, EdmNavigationProperty property) {
		return Handlers.get(FindNavigationPropertyHandler.class, consumerWithMetadata())
				.findNavigationProperty(entityType, key, property);
	}
	
	public <T> T findNavigationProperty(String entityType, Object key, 
			String naviProperty, Class<T> clazz) {
		return Handlers.get(FindNavigationPropertyHandler.class, consumerWithMetadata())
				.findNavigationProperty(entityType, key, naviProperty, clazz);
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
		return Handlers.get(CountHandler.class, consumerWithMetadata())
				.count(entitySet, queryString, resolveQueryString);
	}

	public String invokeFunctionForRawResult(String funcName, Map<String, Object> parameters) {
		return invokeFunctionForRawResult(funcName, parameters, (String)null);
	}
	public String invokeFunctionForRawResult(String funcName, Map<String, Object> parameters, String entitySet) {
		return Handlers.get(InvokeFunctionHandler.class, consumerWithMetadata())
				.forRawResult(funcName, parameters, entitySet);
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
		return Handlers.get(InvokeFunctionHandler.class, consumerWithMetadata())
				.forEntity(funcName, parameters, entitySet, clazz);
	}

	public <T> List<T> invokeFunctionForEntityList(String funcName,
			Map<String, Object> parameters, Class<T> listClass) {
		return invokeFunctionForEntityList(funcName, parameters, null, listClass);
	}
	public <T> List<T> invokeFunctionForEntityList(String funcName,
			Map<String, Object> parameters, String entitySet, Class<T> listClass) {
		return Handlers.get(InvokeFunctionHandler.class, consumerWithMetadata())
				.forEntityList(funcName, parameters, entitySet, listClass);
	}

	public <T> T invokeFunctionForEntity(EdmFunctionImport func,	ODataParameters parameters, Class<T> t) {
		return invokeFunctionForEntity(func.getName(), parameters.getParametersMap(), func.getEntitySet(), t);
	}

	public ODataValue invokeFunctionForODataValue(String funcName, Map<String, Object> parameters) {
		return invokeFunctionForODataValue(funcName, parameters, null);
	}
	public ODataValue invokeFunctionForODataValue(String funcName, Map<String, Object> parameters, String entitySet) {
		return Handlers.get(InvokeFunctionHandler.class, consumerWithMetadata())
				.forODataValue(funcName, parameters, entitySet);
	}

	public ODataValue invokeFunctionForODataValue(EdmFunctionImport func, ODataParameters parameters) {
		return invokeFunctionForODataValue(func.getName(), parameters.getParametersMap(), func.getEntitySet());
	}

	public String invokeFunctionForString(String funcName,
			Map<String, Object> parameters) {
		return invokeFunctionForString(funcName, parameters, null);
	}

	public String invokeFunctionForString(String funcName, Map<String, Object> parameters, String entitySet) {
		return Handlers.get(InvokeFunctionHandler.class, consumerWithMetadata())
				.forString(funcName, parameters, entitySet);
	}
}
