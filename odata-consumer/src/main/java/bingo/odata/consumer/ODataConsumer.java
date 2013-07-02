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
package bingo.odata.consumer;

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
import bingo.odata.consumer.requests.builders.QueryBuilder;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;

public interface ODataConsumer {

	ODataConsumerConfig 		config();
	
	void 						config(ODataConsumerConfig config);
	
	ODataServices 				services();
	
	// meta
	ODataServices 				retrieveServiceMetadata();
	
	// create	
	int 						insertEntity(String entityType, Map<String, Object> fields);
	
	int 						insertEntity(EdmEntityType entityType, ODataEntity entity);
	
	// delete
	int 						deleteEntity(String entityType, Object key);
	
	int 						deleteEntity(EdmEntityType entityType, ODataKey key);
	
	// update
	int 						updateEntity(String entityName, Object key, Map<String, Object> updateFields);
	
	int 						updateEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
//	int 						mergeEntity(String entityName, Object key, Map<String, Object> updateFields);
//
//	int 						mergeEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
	// query - entitySet
	QueryBuilder			 	query(String entitySet);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Map<String, Object> params);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Map<String, Object> params, String orderBy);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Map<String, Object> params, String orderBy, String[] fields);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Map<String, Object> params, String orderBy, String[] fields, String[] expand);
	
	List<Map<String, Object>> 	findEntitySet(String entitySet, String where, Map<String, Object> params, String orderBy, String[] fields, String[] expand, Page page);
	
	ODataEntitySet 				findEntitySet(EdmEntitySet entitySet);
	
	ODataEntitySet 				findEntitySet(EdmEntitySet entitySet, ODataQueryInfo queryInfo);
	
	// query - entity
	ODataEntity 				findEntity(String entityType, Object key);
	
	ODataEntity 				findEntity(EdmEntityType entityType, ODataKey key);
	
	// query - property	
	ODataProperty				findProperty(String entitType, Object key, String property);
	
	ODataProperty 				findProperty(EdmEntityType entitType, ODataKey key, EdmProperty property);
	
	ODataNavigationProperty		findNavigationProperty(String entitType, Object key, String property);
	
	ODataNavigationProperty		findNavigationProperty(EdmEntityType entitType, ODataKey key, EdmNavigationProperty property);

	// query - count
	long 						count(String entitySet);
	
	long 						count(EdmEntitySet entitySet);
	
	long 						count(String entitySet, String where);
	
	long 						count(EdmEntitySet entitySet, ODataQueryInfo queryInfo);
	
	// function invoke
	String 						invokeFunctionForString(String funcName, Map<String, Object> parameters);

	String 						invokeFunctionForString(String funcName, Map<String, Object> parameters, String entitySet);
	
	String 						invokeFunctionForRawString(String funcName, Map<String, Object> parameters);
	
	String 						invokeFunctionForRawString(String funcName, Map<String, Object> parameters, String entitySet);
	
	<T> T	 					invokeFunctionForEntity(String funcName, Map<String, Object> parameters, Class<T> t);
	
	<T> T	 					invokeFunctionForEntity(String funcName, Map<String, Object> parameters, String entitySet, Class<T> t);
	
	<T> List<T>					invokeFunctionForEntityList(String funcName, Map<String, Object> parameters, Class<T> listClass);
	
	<T> List<T>					invokeFunctionForEntityList(String funcName, Map<String, Object> parameters, String entitySet, Class<T> listClass);
	
	<T> T	 					invokeFunctionForEntity(EdmFunctionImport func,ODataParameters parameters, Class<T> t);
	
	ODataValue					invokeFunctionForODataValue(String funcName, Map<String, Object> parameters);
	
	ODataValue					invokeFunctionForODataValue(String funcName, Map<String, Object> parameters, String entitySet);
	
	ODataValue 					invokeFunctionForODataValue(EdmFunctionImport func,ODataParameters parameters);
	
	// action invoke
	// TODO
}