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
	Map<String, Object>			insert(String entityName, Object entity);
	
	ODataEntity					insert(ODataEntity entity);
	
	// delete
	boolean						delete(String entityName, Object key);
	
	boolean						delete(EdmEntityType entityType, ODataKey key);
	
	// update
	boolean						update(String entityName, Object key, Object updateEntity);
	
	boolean						update(ODataEntity entity);
	
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