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

import java.util.List;
import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.Converts;
import bingo.lang.Strings;
import bingo.lang.json.JSON;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
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
import bingo.odata.consumer.requests.behaviors.Behavior;
import bingo.odata.consumer.requests.builders.EntityQuery;
import bingo.odata.consumer.requests.builders.EntityQueryImpl;
import bingo.odata.consumer.requests.builders.FunctionInvoker;
import bingo.odata.consumer.requests.builders.FunctionInvokerImpl;
import bingo.odata.consumer.util.ODataConsumerContextHelper;
import bingo.odata.consumer.util.ODataConvertor;
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
		if(null == services) services = getServiceMetadata();
		return this;
	}

	public ODataConsumerConfig getConfig() {
		return config;
	}

	public ODataConsumer config(ODataConsumerConfig config) {
		this.config = config;
		return this;
	}

	/**
	 * get the {@link ODataServices} from OData Producer.
	 * 
	 * {@link ODataServices} represents the Metadata Document.
	 */
	public ODataServices getServiceMetadata() {
		if(null == this.services) {
			this.services = new RetrieveServiceMetadataHandler(this)
			.retrieveServiceMetadata();
		}
		return this.services;
	}
	
	public ODataServices getServiceMetadata(boolean forceRefresh) {
		if(forceRefresh) {
			this.services = new RetrieveServiceMetadataHandler(this)
				.retrieveServiceMetadata();
		}
		return getServiceMetadata();
	}
	
	public ODataConsumer services(ODataServices services) {
		this.services = services;
		return this;
	}

	
	public ODataEntity insertEntity(EdmEntityType entityType, ODataEntity entity) {
		Map<String, Object> properties = insertEntityByMap(entityType.getName(), entity.toMap());
		EdmEntitySet entitySet = getServiceMetadata().findEntitySet(entityType);
		ODataEntity returnEntity = new ODataEntityBuilder(entitySet, entityType)
									.addProperties(properties).build();
		return returnEntity;
	}
	
	public Map<String, Object> insertEntityByObj(String entityType, Object object) {
		return insertEntityByMap(entityType, JSON.decodeToMap(JSON.encode(object)));
	}

	public Map<String, Object> insertEntityByMap(String entityType, Map<String, Object> object) {
		return Handlers.get(InsertEntityHandler.class, consumerWithMetadata())
					.insertEntityByMap(entityType, object);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> insert(String entityName, Object entity) {
		Assert.notBlank(entityName);
		Assert.notNull(entity);
		if(entity instanceof Map) {
			return insertEntityByMap(entityName, (Map<String, Object>)entity);
		} else {
			return insertEntityByObj(entityName, entity);
		}
	}
	
	public ODataEntity insert(ODataEntity entity) {
		return insertEntity(entity.getEntityType(), entity);
	}

	/**
	 * send a delete certain id entity command to producer. 
	 */
	public boolean delete(String entityName, Object key) {
		return Handlers.get(DeleteEntityHandler.class, consumerWithMetadata()).deleteEntity(entityName, key);
	}

	public boolean delete(EdmEntityType entityType, ODataKey key) {
		return delete(entityType.getName(), key.toKeyString(false));
	}

	public boolean update(ODataEntity entity) {
		return updateEntityByMap(entity.getEntityType().getName(), 
				entity.getKey().toKeyString(false), entity.toMap());
	}

	public boolean updateEntityByMap(String entityType, Object key, Map<String, Object> updateFields) {
		return Handlers.get(UpdateEntityHandler.class, consumerWithMetadata())
				.updateEntity(entityType, key, updateFields);
	}

	public boolean updateEntityByObj(String entityType, Object key, Object updateObject) {
		return updateEntityByMap(entityType, key, JSON.decodeToMap(JSON.encode(updateObject)));
	}
	
	@SuppressWarnings("unchecked")
	public boolean update(String entityName, Object key, Object updateEntity) {
		Assert.notBlank(entityName);
		Assert.notNull(key);
		Assert.notNull(updateEntity);
		if(updateEntity instanceof Map) {
			return updateEntityByMap(entityName, key, (Map<String, Object>)updateEntity);
		} else {
			return updateEntityByObj(entityName, key, updateEntity);
		}
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
	
	public <T> T findEntity(String entityType, Object key, Class<T> clazz) {
		Map<String, Object> entityMap = findEntity(entityType, key);
		return Converts.convert(entityMap, clazz);
	}

	public Map<String, Object> findEntity(String entityType, Object key) {
		return Handlers.get(FindEntityHandler.class, consumerWithMetadata())
				.findEntity(entityType, key);
	}

	public Map<String, Object>	find(String entityName, Object key) {
		return findEntity(entityName, key);
	}

	public <T> T find(String entityName, Object key, Class<T> clazz) {
		return findEntity(entityName, key, clazz);
	}
	
	public ODataEntity find(EdmEntitySet entitySet, ODataKey key) {
		return findEntity(entitySet, key);
	}
	
	/**
	 * get the entity set from producer.
	 */

	public EntityQuery	query(String entityName) {
		return new EntityQueryImpl(this).entity(entityName);
	}
	
	public List<Map<String, Object>> findEntitySet(String entitySet) {
		return findEntitySet(entitySet, null, null, null, null, null, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String queryString) {
		return findEntitySet(entitySet, queryString, null, null, null, null, null);
	}
	
	public List<Map<String, Object>> findEntitySet(String entitySet, String where, Object params) {
		return findEntitySet(entitySet, where, params, null, null, null, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Object params, String orderBy) {
		return findEntitySet(entitySet, where, params, orderBy, null, null, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Object params, String orderBy, String[] fields) {
		return findEntitySet(entitySet, where, params, orderBy, fields, null, null);
	}
	
	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Object params, String orderBy, String[] fields, String[] expand) {
		return findEntitySet(entitySet, where, params, orderBy, fields, expand, null);
	}

	public List<Map<String, Object>> findEntitySet(String entitySet, String where,
			Object params, String orderBy, String[] fields, String[] expand, Page page) {
		return Handlers.get(FindEntitySetHandler.class, consumerWithMetadata())
				.findEntitySet(entitySet, where, params, orderBy, fields, expand, page);
	}
	public <T> List<T> findEntitySet(Class<T> clazz, String entitySet) {
		return findEntitySet(clazz, entitySet, null, null, null, null, null, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String queryString) {
		return findEntitySet(clazz, entitySet, queryString, null, null, null, null, null);
	}
	
	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where, Object params) {
		return findEntitySet(clazz, entitySet, where, params, null, null, null, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Object params, String orderBy) {
		return findEntitySet(clazz, entitySet, where, params, orderBy, null, null, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Object params, String orderBy, String[] fields) {
		return findEntitySet(clazz, entitySet, where, params, orderBy, fields, null, null);
	}
	
	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Object params, String orderBy, String[] fields, String[] expand) {
		return findEntitySet(clazz, entitySet, where, params, orderBy, fields, expand, null);
	}

	public <T> List<T>	findEntitySet(Class<T> clazz, String entitySet, String where,
			Object params, String orderBy, String[] fields, String[] expand, Page page) {
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
	
//	public <T> T findProperty(String entityType, Object key, String property, Class<T> clazz) {
//		return Handlers.get(FindPropertyHandler.class, consumerWithMetadata())
//				.findProperty(entityType, key, property, clazz);
//	}
	
	public String findPropertyForString(String entityType, Object key, String property) {
		return Handlers.get(FindPropertyHandler.class, consumerWithMetadata())
				.findPropertyForString(entityType, key, property);
	}
	
	public String findProperty(String entityName, Object key, String property) {
		return findPropertyForString(entityName, key, property);
	}
	
	public <T> T findProperty(String entityName, Object key, String property,
			Class<T> clazz) {
		return findProperty(entityName, key, property, clazz);
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
	
	public long count(String entitySet, String where) {
		return count(entitySet, where, null, null);
	}
	
	public long count(String entitySet, String where, Object params) {
		return count(entitySet, where, params, null);
	}
	
	public long count(String entitySet, String where, Object params, Page page) {
		return Handlers.get(CountHandler.class, consumerWithMetadata())
				.count(entitySet, where, params, page);
	}

	public long count(EdmEntitySet entitySet, ODataQueryInfo queryInfo) {
		return Handlers.get(CountHandler.class, consumerWithMetadata()).count(entitySet, queryInfo);
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
	
	public FunctionInvoker invokeFunction(String funcName) {
		return new FunctionInvokerImpl(this).invoke(funcName);
	}
}
