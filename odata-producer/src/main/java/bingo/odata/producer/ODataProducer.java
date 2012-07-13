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

import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataServices;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataEntitySet;
import bingo.odata.data.ODataKey;
import bingo.odata.data.ODataParameters;
import bingo.odata.data.ODataReturnValue;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmFunctionImport;

public interface ODataProducer {

	ODataServices getServicesMetadata();
	
	ODataEntitySet queryEntities(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo);
	
	long queryEntitiesCount(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo);
	
	ODataEntity findEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key,ODataQueryInfo queryInfo);

	ODataEntity createEntity(ODataProducerContext context,EdmEntityType entityType,ODataEntity entity);
	
	ODataEntity updateEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key,ODataEntity entity);
	
	ODataEntity mergeEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key, ODataEntity entity);
	
	void deleteEntity(ODataProducerContext context,EdmEntityType entityType,ODataKey key);
	
	ODataReturnValue callFunction(ODataProducerContext context,EdmFunctionImport func,ODataParameters parameters);
}