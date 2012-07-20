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

import bingo.lang.Strings;
import bingo.odata.ODataErrors;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataServices;
import bingo.odata.data.ODataParameterUtils;
import bingo.odata.data.ODataParameters;
import bingo.odata.data.ODataReturnValue;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmFunctionImport;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.ODataRequestHandlerBase;
import bingo.odata.producer.requests.ODataRequestRouter;
import bingo.utils.http.HttpStatus;

public abstract class EntitySetRequestHandlerBase extends ODataRequestHandlerBase {

	@Override
    protected final void doHandle(ODataProducerContext context, ODataRequest request, ODataResponse response) throws Throwable {
	    
		String entitySetName = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_SET_NAME);
		
		if(Strings.isEmpty(entitySetName)){
			throw ODataErrors.badRequest("entitySetName cannot be empty");
		}
		
		ODataServices services = context.getServices();
		
		EdmEntitySet entitySet = services.findEntitySet(entitySetName);
		
		if(null == entitySet){
			EdmFunctionImport functionImport = services.findFunctionImport(entitySetName);
			
			if(null != functionImport){
				
				context.setFunctionImport(functionImport);
				
				doHandleFunctionImport(context,request,response,functionImport);
				
				return;
			}
		}
		
		if(null == entitySet){
			throw ODataErrors.notFound("EntitySet '{0} not found",entitySetName);
		}
		
		EdmEntityType entityType = services.findEntityType(entitySet.getEntityType().getName());
		
		if(null == entityType){
			throw ODataErrors.notFound("EntityType '{0}' not found",entitySet.getEntityType().getName());
		}		
		
		context.setEntitySet(entitySet);
		context.setEntityType(entityType);
		
		doHandleEntitySet(context, request, response, entitySet, entityType);
    } 
	
	protected void doHandleFunctionImport(ODataProducerContext context,ODataRequest request,ODataResponse response,EdmFunctionImport functionImport) throws Throwable {
		
		if(Strings.isEmpty(functionImport.getHttpMethod()) || functionImport.getHttpMethod().equalsIgnoreCase(request.getMethod())){

			String paramsString = context.getUrlInfo().getPathParameter(ODataRequestRouter.ENTITY_KEY_STRING);
			
			ODataParameters params = ODataParameterUtils.parse(functionImport, paramsString, context.getUrlInfo().getQueryOptions());
			
			ODataReturnValue returnValue = context.getProducer().callFunction(context, functionImport, params);
			
			if(functionImport.getReturnType() == null || returnValue == null){
				response.setStatus(HttpStatus.SC_NO_CONTENT);
			}else {
				write(context, request, response, returnValue.getKind(), returnValue.getValue());
			}
		}else{
			throw ODataErrors.badRequest("unsupported http method '{0}' on current resource '{1}'",request.getMethod(),request.getResourcePath());
		}
	}
	
	protected abstract void doHandleEntitySet(ODataProducerContext context,ODataRequest request,ODataResponse response,EdmEntitySet entitySet,EdmEntityType entityType) throws Throwable;
}