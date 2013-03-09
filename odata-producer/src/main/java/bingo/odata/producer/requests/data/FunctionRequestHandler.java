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
package bingo.odata.producer.requests.data;

import bingo.lang.Strings;
import bingo.meta.edm.EdmEntitySet;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.odata.ODataErrors;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.model.ODataKeyUtils;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.ODataRequestHandlerBase;
import bingo.odata.producer.requests.ODataRequestRouter;

public class FunctionRequestHandler extends ODataRequestHandlerBase {
	
	@Override
    protected void doHandle(ODataProducerContext context, ODataRequest request, ODataResponse response) throws Throwable {
		String entitySetName   = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_SET_NAME);
		String functionName    = context.getUrlInfo().getPathParameter(ODataRequestRouter.FUNCTION_NAME);
		String entityKeyString = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_KEY_STRING);
		
		if(!Strings.isEmpty(entitySetName)){
			EdmEntitySet entitySet = context.getServices().findEntitySet(entitySetName);
			if(null == entitySet){
				throw ODataErrors.notFound("EntitySet '{0}' not found",entitySetName);
			}
			EdmEntityType entityType = context.getServices().findEntityType(entitySet.getEntityType());
			
			context.setEntitySet(entitySet);
			context.setEntityType(entityType);
		}
		
		EdmFunctionImport func = context.getServices().findFunctionImport(functionName);
		if(null == func && !Strings.isEmpty(entitySetName)){
			functionName = entitySetName + "." + functionName;
			func = context.getServices().findFunctionImport(functionName);
		}
		
		if(null == func){
			throw ODataErrors.notFound("Function '{0}' not found",functionName);
		}
		
		if(!Strings.isEmpty(entityKeyString)){
			context.setEntityKey(ODataKeyUtils.parse(entityKeyString));
		}
		
		doHandleFunctionImport(context, request, response, func);
    }
	
}