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
package bingo.odata.producer.requests.data;

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataKeyUtils;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.ODataRequestRouter;

public abstract class EntityRequestHandlerBase extends EntitySetRequestHandlerBase {

	@Override
    protected final void doHandleEntitySet(ODataProducerContext context, ODataRequest request, ODataResponse response, 
    										 EdmEntitySet entitySet,EdmEntityType entityType) throws Throwable {

		String entityKeyString = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_KEY_STRING);
		
		ODataKey key = ODataKeyUtils.parse(entityKeyString);
		
		context.setEntityKey(key);
		
		doHandleEntity(context, request, response, entitySet,entityType, key);
	}
	
	protected abstract void doHandleEntity(ODataProducerContext context, ODataRequest request, ODataResponse response,
											 EdmEntitySet entitySet,EdmEntityType entityType,ODataKey key) throws Throwable;
}
