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
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataErrors;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.model.ODataKey;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.ODataRequestRouter;

public abstract class PropertyRequestHandlerBase extends EntityRequestHandlerBase {

	@Override
    protected final void doHandleEntity(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
    							   EdmEntityType entityType, ODataKey key) throws Throwable {

		String entityPropertyName = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_PROP_NAME);

		EdmProperty property = entityType.findProperty(entityPropertyName);
		
		if(null != property){
			doHandleProperty(context, request, response, entitySet, entityType, key, property);
			return;
		}
		
		EdmNavigationProperty navProperty = entityType.findNavigationProperty(entityPropertyName);
		if(null != navProperty){
			doHandleNavProperty(context, request, response, entitySet, entityType, key, navProperty);
			return;
		}
		
		EdmFunctionImport func = context.getProducer().findFunctionImport(context,entitySet.getName(),entityPropertyName);
		if(null != func){
			doHandleFunctionImport(context, request, response, func);
			return;
		}
		
		if(entityType.isOpenType()){
			doHandleDynaProperty(context, request, response, entitySet, entityType, entityPropertyName);
			return;
		}
		
		throw ODataErrors.notFound("[Navigation] Property '{0}' not found in Entity Type '{1}'",entityPropertyName,entityType.getName());
    }

	protected void doHandleProperty(ODataProducerContext context, ODataRequest request, ODataResponse response,
							      EdmEntitySet entitySet,EdmEntityType entityType,ODataKey key,EdmProperty property) throws Throwable {
		throw ODataErrors.notImplemented();
	}

	protected void doHandleNavProperty(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
	        					  EdmEntityType entityType, ODataKey key, EdmNavigationProperty navProperty) throws Throwable {
		throw ODataErrors.notImplemented();
	}
	
	protected void doHandleDynaProperty(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,EdmEntityType entityType,String name) throws Throwable {
		throw ODataErrors.notImplemented();
	}
}