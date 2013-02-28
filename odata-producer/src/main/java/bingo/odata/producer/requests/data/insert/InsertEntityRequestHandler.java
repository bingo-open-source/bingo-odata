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
package bingo.odata.producer.requests.data.insert;

import bingo.lang.http.HttpHeaders;
import bingo.lang.http.HttpStatus;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataUtils;
import bingo.odata.data.ODataEntity;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.requests.data.EntitySetRequestHandlerBase;

public class InsertEntityRequestHandler extends EntitySetRequestHandlerBase {

	@Override
    protected void doHandleEntitySet(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
            						  EdmEntityType entityType) throws Throwable {

		ODataEntity oentity = read(context,request,ODataObjectKind.Entity);
	    
		ODataEntity created = context.getProducer().insertEntity(context, entityType, oentity);
		
		response.setStatus(HttpStatus.SC_CREATED);
		response.setHeader(HttpHeaders.LOCATION, ODataUtils.getEntityUrl(context.getUrlInfo(), created));
		
		write(context, request, response, ODataObjectKind.Entity, created);
    }
}