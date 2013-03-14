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
package bingo.odata.producer;

import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataParameters;
import bingo.odata.model.ODataValue;
import bingo.odata.producer.ext.ODataContent;

public interface ODataProducer {
	
	ODataServices retrieveServiceMetadata();
	
	ODataEntitySet retrieveEntitySet(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo);
	
	long retrieveCount(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo);
	
	/**
	 * @return null if not exists
	 */
	ODataEntity retrieveEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key,ODataQueryInfo queryInfo);
	
	/**
	 * must be not null
	 */
	ODataValue retrieveProperty(ODataProducerContext context,EdmEntityType entitType,ODataKey key,EdmProperty property);
	
	/**
	 * must be not null
	 */
	ODataValue retrieveNavigationProperty(ODataProducerContext context,EdmEntityType entitType,ODataKey key,EdmNavigationProperty property);
	
	ODataEntity insertEntity(ODataProducerContext context,EdmEntityType entityType,ODataEntity entity);
	
	ODataEntity updateEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key,ODataEntity entity);
	
	ODataEntity mergeEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key, ODataEntity entity);
	
	void deleteEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key);
	
	ODataValue invokeFunction(ODataProducerContext context,EdmFunctionImport func,ODataParameters parameters);
	
	ODataContent query(ODataProducerContext context,ODataQueryInfo queryInfo);
}