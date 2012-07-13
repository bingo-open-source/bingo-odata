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
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataQueryInfoParser;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataKey;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.data.EntityRequestHandlerBase;

public class RetrieveEntityRequestHandler extends EntityRequestHandlerBase {
	
	@Override
    protected void doHandleEntity(ODataProducerContext context, ODataRequest request, ODataResponse response,
    							   EdmEntitySet entitySet,EdmEntityType entityType,ODataKey key) throws Throwable {
		
		ODataQueryInfo queryInfo = ODataQueryInfoParser.parseForSingleEntity(context.getUrlInfo().getQueryOptions());
		
		ODataEntity data = context.getProducer().findEntity(context, entityType, key, queryInfo);
		
		if(null == data){
			throw ODataErrors.notFound("Resource '{0}' not found",context.getUrlInfo().getResourcePath().getFullPath());
		}else{
			write(context,request,response,ODataObjectKind.Entity,data);	
		}	    
    }

}