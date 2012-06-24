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
package bingo.odata.requests;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataException;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.producer.ODataProducer;

public abstract class ODataRequestHandlerBase implements ODataRequestHandler {
	
	private static final Log log = LogFactory.get(ODataRequestHandlerBase.class);

	public final boolean handle(ODataProducer producer, ODataRequest request, ODataResponse response) throws ODataException {
	    try {
	        return doHandle(producer,request,response);
        } catch (Throwable e) {
        	log.error("Error executing handler '{}' on request '{}' : {}",new Object[]{this.getClass().getSimpleName(),request.getUrl(),e.getMessage(),e});
        	
        	if(e instanceof ODataException){
        		throw (ODataException)e;
        	}else{
        		throw new ODataRequestException(e);	
        	}
        }
    }

	protected abstract boolean doHandle(ODataProducer producer,ODataRequest request,ODataResponse response) throws Throwable;
}