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

import java.util.Map;

import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataErrors;
import bingo.odata.ODataFormat;
import bingo.odata.ODataProtocol;
import bingo.odata.ODataProtocols;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataValue;
import bingo.odata.consumer.ext.ODataContent;
import bingo.odata.consumer.requests.builders.QueryBuilder;
import bingo.odata.consumer.requests.builders.QueryFilter;

public class ODataConsumerAdapter implements ODataConsumer {


	@Override
	public int deleteEntity(Object objectWithIdField) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteEntity(String entityName, Object id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEntity(Object objectWithIdField) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updateEntity(String entityName, Object id,
			Map<String, Object> updateFields) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public QueryBuilder queryEntity(String entityName, QueryFilter filter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBuilder queryEntity(String entityName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataConsumerConfig config() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataServices retrieveServiceMetadata() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataEntitySet retrieveEntitySet(EdmEntityType entityType,
			ODataQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long retrieveCount(EdmEntityType entityType, ODataQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ODataEntity retrieveEntity(EdmEntityType entityType, ODataKey key,
			ODataQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataValue retrieveProperty(EdmEntityType entitType, ODataKey key,
			EdmProperty property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataValue retrieveNavigationProperty(EdmEntityType entitType,
			ODataKey key, EdmNavigationProperty property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataEntity insertEntity(EdmEntityType entityType, ODataEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataEntity updateEntity(EdmEntityType entityType, ODataKey key,
			ODataEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataEntity mergeEntity(EdmEntityType entityType, ODataKey key,
			ODataEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEntity(EdmEntityType entityType, ODataKey key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ODataValue invokeFunction(EdmFunctionImport func,
			ODataParameters parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataContent query(ODataQueryInfo queryInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public EdmFunctionImport findFunctionImport(String entitySetName,
			String functionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataEntitySet retrieveEntitySet(String entitySet) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ODataEntity retrieveEntity(String entityType, Object key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int insertEntity(String entityType, ODataEntity entity) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int insertEntity(String entityType, Map<String, Object> object) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
