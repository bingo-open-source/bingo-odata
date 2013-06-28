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
import bingo.odata.consumer.exceptions.ConnectFailedException;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;
import bingo.odata.producer.ODataProducerConfig;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.consumer.requests.behaviors.Behavior;
import bingo.odata.consumer.requests.builders.QueryBuilder;
import bingo.odata.consumer.requests.builders.QueryFilter;

public interface ODataConsumer {

	ODataConsumerConfig 		config();
	
	ODataServices 				services();
	
	// meta
	ODataServices 				retrieveServiceMetadata();
	
	// create	
	int 						insertEntity(String entityType, Map<String, Object> fields);
	
	int 						insertEntity(EdmEntityType entityType, ODataEntity entity);
	
	// delete
	int 						deleteEntityByKey(String entityType, Object key);
	
	int 						deleteEntity(String entityType, String queryString);

	int 						deleteEntity(EdmEntityType entityType, ODataKey key);
	
	int 						deleteEntity(EdmEntityType entityType, ODataQueryInfo queryInfo);
	
	// update
	int 						updateEntityByKey(String entityName, Object key, Map<String, Object> updateFields);
	
	int 						updateEntity(String entityName, String queryString, Map<String, Object> updateFields);
	
	int 						updateEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
	
	int 						updateEntity(EdmEntityType entityType, ODataQueryInfo queryInfo, ODataEntity entity);
	
//	int 						mergeEntityByKey(String entityName, Object key, Map<String, Object> updateFields);
//
//	int 						mergeEntity(String entityName, String queryString, Map<String, Object> updateFields);
//
//	int 						mergeEntity(EdmEntityType entityType, ODataKey key, ODataEntity entity);
//
//	int 						mergeEntity(EdmEntityType entityType, ODataQueryInfo queryInfo, ODataEntity entity);
	
	// query - entitySet
	ODataEntitySet 				findEntitySet(String entitySet);
	
	ODataEntitySet 				findEntitySet(String entitySet, String queryString);
	
	ODataEntitySet 				findEntitySet(EdmEntityType entityType);
	
	ODataEntitySet 				findEntitySet(EdmEntityType entityType, ODataQueryInfo queryInfo);
	
	List<Map<String, Object>> 	findEntitySetAsList(String entitySet);
	
	List<Map<String, Object>> 	findEntitySetAsList(String entitySet, String queryString);
	
	// query - entity
	ODataEntity 				findEntity(String entityType, Object key);
	
	ODataEntity 				findEntity(EdmEntityType entityType, ODataKey key);
	
	// query - property	
	ODataProperty				findProperty(String entitType, Object key, String property);
	
	ODataProperty 				findProperty(EdmEntityType entitType, ODataKey key, EdmProperty property);
	
	ODataNavigationProperty		findNavigationProperty(String entitType, Object key, String property);
	
	ODataNavigationProperty		findNavigationProperty(EdmEntityType entitType, ODataKey key, EdmNavigationProperty property);

	// query - count
	long 						count(String entityType, String queryString);
	
	long 						count(EdmEntityType entityType, ODataQueryInfo queryInfo);

	long 						count(String entitySet);
	
	long 						count(EdmEntitySet entitySet);
	
	// function invoke
	String 						invokeFunction(String entitySet, String funcName, Map<String, Object> parameters);
	
	<T> T	 					invokeFunction(String entitySet, String funcName, Map<String, Object> parameters, Class<T> t);
	
	<T> List<T>					invokeFunctionForList(String entitySet, String funcName, Map<String, Object> parameters, Class<T> listClass);
	
	<T> T	 					invokeFunction(EdmFunctionImport func,ODataParameters parameters, Class<T> t);
	
	ODataValue					invokeFunctionForODataValue(String entitySet, String funcName, Map<String, Object> parameters);
	
	ODataValue 					invokeFunctionForODataValue(EdmFunctionImport func,ODataParameters parameters);
	
	// action invoke
	// TODO
}