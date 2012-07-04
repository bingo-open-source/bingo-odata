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
package bingo.odata.server.requests;

import java.util.Map;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataException;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;

public abstract class ODataRequestHandlerBase implements ODataRequestHandler {
	
	private static final Log log = LogFactory.get(ODataRequestHandlerBase.class);
	
	public boolean matches(ODataRequestContext context, ODataRequest request, Map<String, String> params) throws ODataException {
	    return true;
    }

	public final void handle(ODataRequestContext context, ODataRequest request, ODataResponse response) throws ODataException {
	    try {
	        doHandle(context,request,response);
	        	
        	String contentType = getContentType(context,request);
        	
        	if(null != contentType){
        		response.setContentType(contentType);
        	}
        } catch (Throwable e) {
        	log.error("Error executing handler '{}' on request '{}' : {}",new Object[]{this.getClass().getSimpleName(),request.getUrl(),e.getMessage(),e});
        	
        	if(e instanceof ODataException){
        		throw (ODataException)e;
        	}else{
        		throw new ODataException(e);	
        	}
        }
    }
	
	protected String getContentType(ODataRequestContext context,ODataRequest request) {
		return context.getFormat().getContentType();
	}
	
	protected abstract void doHandle(ODataRequestContext context,ODataRequest request,ODataResponse response) throws Throwable;
}