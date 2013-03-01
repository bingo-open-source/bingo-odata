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
package bingo.odata.producer.requests.data.retrieve;

import bingo.odata.ODataErrors;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.model.ODataBuilders;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.ODataRequestRouter;
import bingo.odata.producer.requests.data.EntityRequestHandlerBase;

public class RetrievePropertyValueHandler extends EntityRequestHandlerBase {

	@Override
    protected final void doHandleEntity(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
    							   EdmEntityType entityType, ODataKey key) throws Throwable {

		String entityPropertyName = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_PROP_NAME);

		EdmProperty property = entityType.findProperty(entityPropertyName);
		
		if(null != property){
			doHandleProperty(context, request, response, entitySet, entityType, key, property);
			return;
		}
		
		if(entityType.isOpenType()){
			doHandleDynaProperty(context, request, response, entitySet, entityType, entityPropertyName);
			return;
		}
		
		throw ODataErrors.notFound("Property '{0}' not found in Entity Type '{1}'",entityPropertyName,entityType.getName());
    }
	
    protected void doHandleProperty(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
            					   EdmEntityType entityType, ODataKey key, EdmProperty property) throws Throwable {

		ODataValue returnValue = context.getProducer().retrieveProperty(context, entityType, key, property);
		if(null == returnValue){
			throw ODataErrors.internalServerError("return value must be not null");
		}
		if(!ODataObjectKind.Property.equals(returnValue.getKind())){
			throw ODataErrors.internalServerError("return value must be property");
		}
		ODataProperty p = (ODataProperty)returnValue.getValue();
		if(null == p){
			throw ODataErrors.internalServerError("return value must be not null");
		}
		write(context, request, response,ODataBuilders.value().raw(p.getType(),p.getValue()).build());
    }

    protected void doHandleDynaProperty(ODataProducerContext context, 
    									ODataRequest request, ODataResponse response, EdmEntitySet entitySet, EdmEntityType entityType, String name) throws Throwable{

		ODataProperty property = ODataBuilders.dynamicProperty(EdmSimpleType.STRING,name).setRawValue(null).build();
		ODataValue returnValue = ODataBuilders.value().property(property).build();
		write(context, request, response, returnValue);
    }
}