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

import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmSimpleType;
import bingo.odata.ODataErrors;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.model.ODataBuilders;
import bingo.odata.model.ODataKey;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataValue;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.data.PropertyRequestHandlerBase;

public class RetrievePropertyHandler extends PropertyRequestHandlerBase {

	@Override
    protected void doHandleProperty(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
            					   EdmEntityType entityType, ODataKey key, EdmProperty property) throws Throwable {

		ODataValue returnValue = context.getProducer().retrieveProperty(context, entityType, key, property);
		if(null == returnValue){
			throw ODataErrors.internalServerError("return value must be not null");
		}
		write(context, request, response, returnValue);
    }

	@Override
    protected void doHandleNavProperty(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
            EdmEntityType entityType, ODataKey key, EdmNavigationProperty navProperty) throws Throwable {

		ODataValue returnValue = context.getProducer().retrieveNavigationProperty(context, entityType, key, navProperty);
		if(null == returnValue){
			throw ODataErrors.internalServerError("return value must be not null");
		}
		write(context, request, response, returnValue);
	}

	@Override
    protected void doHandleDynaProperty(ODataProducerContext context, 
    									ODataRequest request, ODataResponse response, EdmEntitySet entitySet, EdmEntityType entityType, String name) throws Throwable{

		ODataProperty property = ODataBuilders.dynamicProperty(EdmSimpleType.STRING,name).setRawValue(null).build();
		ODataValue returnValue = ODataBuilders.value().property(property).build();
		write(context, request, response, returnValue);
    }
}