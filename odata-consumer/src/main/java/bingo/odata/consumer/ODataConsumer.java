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
/*package bingo.odata.consumer;

import java.util.List;
import java.util.Map;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
import bingo.odata.consumer.ext.Page;
import bingo.odata.consumer.requests.builders.EntitySetQuery;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;

public interface ODataConsumer {

	ODataConsumerConfig 		config();
	
	ODataConsumer 				config(ODataConsumerConfig config);
	
	ODataConsumer 				services(ODataServices services);
	
	ODataServices 				cachedGetServiceMetadata();
	
	// meta
	ODataServices 				refreshServiceMetadata();
	
	// create	
	int 						insertEntityByMap(String entityType, Map<String, Object> fields);
	
	int 						insertEntityByObj(String entityType, Object object);
	
	int 						insertEntity(EdmEntityType entityType, ODataEntity entity);
	
	// delete
	int 						deleteEntity(String entityType, Object key);
	
	int 						deleteEntity(EdmEntityType entityType, ODataKey key);
	
	// update
	int 						updateEntityByMap(String entityName, Object key, Map<String, Object> updateFields);
	
	int 						updateEntityByObj(String entityName, Object key, Object updateObject);
	
	int 						updateEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
//	int 						mergeEntity(String entityName, Object key, Map<String, Object> updateFields);
//
//	int 						mergeEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
	// query - entitySet
	EntitySetQuery			 	queryEntitySet(String entitySet);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy, String[] fields);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand, Page page);
	
	<T> List<T>				 	findEntitySet(Class<T> clazz, String entitySet);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy, String[] fields);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand, Page page);
	
	ODataEntitySet 				findEntitySet(EdmEntitySet entitySet);
	
	ODataEntitySet 				findEntitySet(EdmEntitySet entitySet, ODataQueryInfo queryInfo);
	
	// query - entity
	Map<String, Object>			findEntity(String entitySet, Object key);
	
	<T> T 						findEntity(Class<T> clazz, String entitySet, Object key);
	
	ODataEntity 				findEntity(EdmEntitySet entitySet, ODataKey key);
	
	// query - property	
	<T> T						findProperty(String entitType, Object key, String property, Class<T> clazz);
	
	String						findPropertyForString(String entitType, Object key, String property);
	
	ODataProperty 				findProperty(EdmEntityType entitType, ODataKey key, EdmProperty property);
	
	<T> T						findNavigationProperty(String entitType, Object key, String property, Class<T> clazz);
	
	ODataNavigationProperty		findNavigationProperty(EdmEntityType entitType, ODataKey key, EdmNavigationProperty property);

	// query - count
	long 						count(String entitySet);
	
	long 						count(EdmEntitySet entitySet);
	
	long 						count(String entitySet, String where);
	
	long 					 	count(String entitySet, String where, Object paramObj);
	
	long 					 	count(String entitySet, String where, Object paramObj, Page page);
	
	long 						count(EdmEntitySet entitySet, ODataQueryInfo queryInfo);
	
	// function invoke
	String 						invokeFunctionForString(String funcName, Map<String, Object> parameters);

	String 						invokeFunctionForString(String funcName, Map<String, Object> parameters, String entitySet);
	
	String 						invokeFunctionForRawResult(String funcName, Map<String, Object> parameters);
	
	String 						invokeFunctionForRawResult(String funcName, Map<String, Object> parameters, String entitySet);
	
	<T> T	 					invokeFunctionForEntity(String funcName, Map<String, Object> parameters, Class<T> t);
	
	<T> T	 					invokeFunctionForEntity(String funcName, Map<String, Object> parameters, String entitySet, Class<T> t);
	
	<T> T	 					invokeFunctionForType(String funcName, Map<String, Object> parameters, String entitySet, Class<T> t);
	
	<T> T	 					invokeFunctionForType(String funcName, Map<String, Object> parameters, Class<T> t);
	
	<T> List<T>					invokeFunctionForEntityList(String funcName, Map<String, Object> parameters, Class<T> listClass);
	
	<T> List<T>					invokeFunctionForEntityList(String funcName, Map<String, Object> parameters, String entitySet, Class<T> listClass);
	
	<T> T	 					invokeFunctionForEntity(EdmFunctionImport func,ODataParameters parameters, Class<T> t);
	
	ODataValue					invokeFunctionForODataValue(String funcName, Map<String, Object> parameters);
	
	ODataValue					invokeFunctionForODataValue(String funcName, Map<String, Object> parameters, String entitySet);
	
	ODataValue 					invokeFunctionForODataValue(EdmFunctionImport func,ODataParameters parameters);
	
	// action invoke
	// TODO

public interface ODataConsumer {

	ODataConsumerConfig 		config();
	
	ODataConsumer 				config(ODataConsumerConfig config);
	
	ODataConsumer 				services(ODataServices services);
	
	ODataServices 				cachedGetServiceMetadata();
	
	// meta
	ODataServices 				refreshServiceMetadata();
	
	// create	
	int 						insertEntityByMap(String entityType, Map<String, Object> fields);
	
	int 						insertEntityByObj(String entityType, Object object);
	
	int 						insertEntity(EdmEntityType entityType, ODataEntity entity);
	
	// delete
	int 						deleteEntity(String entityType, Object key);
	
	int 						deleteEntity(EdmEntityType entityType, ODataKey key);
	
	// update
	int 						updateEntityByMap(String entityName, Object key, Map<String, Object> updateFields);
	
	int 						updateEntityByObj(String entityName, Object key, Object updateObject);
	
	int 						updateEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
//	int 						mergeEntity(String entityName, Object key, Map<String, Object> updateFields);
//
//	int 						mergeEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
	// query - entitySet
	EntitySetQuery			 	queryEntitySet(String entitySet);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy, String[] fields);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand, Page page);
	
	<T> List<T>				 	findEntitySet(Class<T> clazz, String entitySet);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy, String[] fields);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand);
	
	<T> List<T> 				findEntitySet(Class<T> clazz, String entitySet, String where, Object paramObj, String orderBy, String[] fields, String[] expand, Page page);
	
	ODataEntitySet 				findEntitySet(EdmEntitySet entitySet);
	
	ODataEntitySet 				findEntitySet(EdmEntitySet entitySet, ODataQueryInfo queryInfo);
	
	// query - entity
	Map<String, Object>			findEntity(String entitySet, Object key);
	
	<T> T 						findEntity(Class<T> clazz, String entitySet, Object key);
	
	ODataEntity 				findEntity(EdmEntitySet entitySet, ODataKey key);
	
	// query - property	
	<T> T						findProperty(String entitType, Object key, String property, Class<T> clazz);
	
	String						findPropertyForString(String entitType, Object key, String property);
	
	ODataProperty 				findProperty(EdmEntityType entitType, ODataKey key, EdmProperty property);
	
	<T> T						findNavigationProperty(String entitType, Object key, String property, Class<T> clazz);
	
	ODataNavigationProperty		findNavigationProperty(EdmEntityType entitType, ODataKey key, EdmNavigationProperty property);

	// query - count
	long 						count(String entitySet);
	
	long 						count(EdmEntitySet entitySet);
	
	long 						count(String entitySet, String where);
	
	long 					 	count(String entitySet, String where, Object paramObj);
	
	long 					 	count(String entitySet, String where, Object paramObj, Page page);
	
	long 						count(EdmEntitySet entitySet, ODataQueryInfo queryInfo);
	
	// function invoke
	String 						invokeFunctionForString(String funcName, Map<String, Object> parameters);

	String 						invokeFunctionForString(String funcName, Map<String, Object> parameters, String entitySet);
	
	String 						invokeFunctionForRawResult(String funcName, Map<String, Object> parameters);
	
	String 						invokeFunctionForRawResult(String funcName, Map<String, Object> parameters, String entitySet);
	
	<T> T	 					invokeFunctionForEntity(String funcName, Map<String, Object> parameters, Class<T> t);
	
	<T> T	 					invokeFunctionForEntity(String funcName, Map<String, Object> parameters, String entitySet, Class<T> t);
	
	<T> T	 					invokeFunctionForType(String funcName, Map<String, Object> parameters, String entitySet, Class<T> t);
	
	<T> T	 					invokeFunctionForType(String funcName, Map<String, Object> parameters, Class<T> t);
	
	<T> List<T>					invokeFunctionForEntityList(String funcName, Map<String, Object> parameters, Class<T> listClass);
	
	<T> List<T>					invokeFunctionForEntityList(String funcName, Map<String, Object> parameters, String entitySet, Class<T> listClass);
	
	<T> T	 					invokeFunctionForEntity(EdmFunctionImport func,ODataParameters parameters, Class<T> t);
	
	ODataValue					invokeFunctionForODataValue(String funcName, Map<String, Object> parameters);
	
	ODataValue					invokeFunctionForODataValue(String funcName, Map<String, Object> parameters, String entitySet);
	
	ODataValue 					invokeFunctionForODataValue(EdmFunctionImport func,ODataParameters parameters);
	
	// action invoke
	// TODO
}*
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
package bingo.odata.consumer;

import java.util.Map;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataServices;
import bingo.odata.consumer.requests.builders.EntityQuery;
import bingo.odata.consumer.requests.builders.FunctionInvoker;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataProperty;

public interface ODataConsumer {

	ODataConsumerConfig 		getConfig();
	
	ODataServices 				getServiceMetadata();
	
	ODataServices 				getServiceMetadata(boolean forceRefresh);
	
	// create
	int 						insert(String entityName, Object entity);
	
	int 						insert(ODataEntity entity);
	
	// delete
	int 						delete(String entityName, Object key);
	
	int 						delete(EdmEntityType entityType, ODataKey key);
	
	// update
	int 						update(String entityName, Object key, Object updateEntity);
	
	int 						update(ODataEntity entity);
	
	// query
	EntityQuery			 		query(String entityName);
	
	Map<String, Object>			find(String entityName, Object key);
	
	<T> T 						find(String entityName, Object key, Class<T> clazz);
	
	ODataEntity 				find(EdmEntitySet entitySet, ODataKey key);
	
	// query - property	
	String						findProperty(String entityName, Object key, String property);
	
	<T> T						findProperty(String entityName, Object key, String property, Class<T> clazz);
	
	ODataProperty 				findProperty(EdmEntityType entitType, ODataKey key, EdmProperty property);
	
	// function invoke
	FunctionInvoker				invokeFunction(String funcName);

}