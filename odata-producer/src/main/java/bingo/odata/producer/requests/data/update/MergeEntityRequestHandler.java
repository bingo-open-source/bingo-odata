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
package bingo.odata.producer.requests.data.update;

import bingo.odata.ODataObjectKind;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataKey;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.producer.requests.data.EntityRequestHandlerBase;
import bingo.odata.provider.ODataProducerContext;
import bingo.utils.http.HttpStatus;

public class MergeEntityRequestHandler extends EntityRequestHandlerBase {

	@Override
    protected void doHandleEntity(ODataProducerContext context, ODataRequest request, ODataResponse response, EdmEntitySet entitySet,
            						EdmEntityType entityType, ODataKey key) throws Throwable {

		ODataEntity oentity = read(context,request,ODataObjectKind.Entity);
	    
		context.getProducer().mergeEntity(context,entityType,key,oentity);
		
		response.setStatus(HttpStatus.SC_NO_CONTENT);
    }

}