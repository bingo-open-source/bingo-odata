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
package bingo.odata.requests.metadata;

import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataServices;
import bingo.odata.ODataWriters;
import bingo.odata.producer.ODataProducer;
import bingo.odata.requests.ODataRequestHandlerBase;

public class MetadataDocumentRequestHandler extends ODataRequestHandlerBase {
	
	@Override
    protected boolean doHandle(ODataProducer producer, ODataRequest request, ODataResponse response) throws Throwable {
		ODataServices metadata = producer.getMetadataProducer().getMetadata();
		
		ODataWriters.EDM_METADATA_DOCUMENT_WRITER.write(request,response.getWriter(),metadata);
		
		return true;
    }
}