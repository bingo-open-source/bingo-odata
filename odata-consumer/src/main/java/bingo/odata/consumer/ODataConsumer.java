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
import bingo.odata.consumer.ext.ODataContents;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataValue;
import bingo.odata.producer.ODataProducerConfig;
import bingo.odata.consumer.ext.ODataContent;
import bingo.odata.consumer.requests.behaviors.ClientBehavior;
import bingo.odata.consumer.requests.builders.QueryBuilder;
import bingo.odata.consumer.requests.builders.QueryFilter;

public interface ODataConsumer {

	ODataConsumerConfig config();
	
	ODataServices services();
	
	int insertEntity(String entityType, ODataEntity entity) throws ConnectFailedException;
	
	int insertEntity(String entityType, Map<String, Object> fields);
	
	int deleteEntity(String entityName, Object id);
	
//	int deleteEntity(String entityName, QueryOptions queryOptions);
	
	int updateEntity(String entityName, Object id, Map<String, Object> updateFields);
	
	QueryBuilder queryEntity(String	entityName, QueryFilter filter);
	
	QueryBuilder queryEntity(String entityName);
	
	ODataServices retrieveServiceMetadata() throws ConnectFailedException;

	ODataEntitySet findEntitySet(EdmEntityType entityType,ODataQueryInfo queryInfo);
	
	ODataEntitySet findEntitySet(String entitySet);

	long count(EdmEntityType entityType,ODataQueryInfo queryInfo);
	
	long count(EdmEntitySet entitySet);
	
	long count(String entitySet);
	
	ODataEntity findEntity(EdmEntityType entityType,ODataKey key,ODataQueryInfo queryInfo);
	
	ODataEntity findEntity(String entityType, Object key);
	
	ODataValue findProperty(EdmEntityType entitType,ODataKey key,EdmProperty property);
	
	ODataValue findNavigationProperty(EdmEntityType entitType,ODataKey key,EdmNavigationProperty property);
	
	int insertEntity(EdmEntityType entityType,ODataEntity entity);
	
	int updateEntity(EdmEntityType entityType,ODataKey key,ODataEntity entity);
	
	int mergeEntity(EdmEntityType entityType,ODataKey key, ODataEntity entity);
	
	void deleteEntity(EdmEntityType entityType,ODataKey key);
	
	String invokeFunction(EdmFunctionImport func,ODataParameters parameters);
	
	String invodeFunction(String entitySet, String funcName, Map<String, Object> parameters);
	
	ODataContent query(ODataQueryInfo queryInfo);
	
	EdmFunctionImport findFunctionImport(String entitySetName,String functionName);
	
}