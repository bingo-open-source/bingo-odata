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

import java.io.StringWriter;

import bingo.lang.Strings;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataErrors;
import bingo.odata.ODataException;
import bingo.odata.ODataObject;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataWriter;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataWriters;

public abstract class ODataRequestHandlerBase implements ODataRequestHandler {
	
	private static final Log log = LogFactory.get(ODataRequestHandlerBase.class);
	
	public final void handle(ODataRequestContext context, ODataRequest request, ODataResponse response) throws ODataException {
	    try {
	        doHandle(context,request,response);
        } catch (Throwable e) {
        	log.error("Error executing handler '{}' on request '{}' : {}",new Object[]{this.getClass().getSimpleName(),request.getUrl(),e.getMessage(),e});
        	
        	if(e instanceof ODataException){
        		throw (ODataException)e;
        	}else{
        		throw new ODataException(e);	
        	}
        }
    }
	
	protected static <T extends ODataObject> void write(ODataRequestContext context,ODataRequest request, ODataResponse response,ODataObjectKind kind,T target) throws Throwable {
		ODataWriter<T> writer = getWriter(context, kind);
		
		StringWriter out = new StringWriter();
		
		writer.write(request, out, target);
		
		String content = out.toString();
		
		if(log.isDebugEnabled()){
			log.debug("response content type : {}",	     writer.getContentType());
			log.trace("response content text : \n\n{}\n",content);
		}
		
		response.getWriter().write(content);
		response.setContentType(writer.getContentType());
	}
	
	protected static <T extends ODataObject> ODataWriter<T> getWriter(ODataRequestContext context,ODataObjectKind kind) {
		ODataWriter<T> writer = ODataWriters.of(context.getFormat(), kind);
		
		if(null == writer){
			throw ODataErrors.badRequest(Strings.format("cannot found writer of '{0}'",kind));
		}
		
		return writer;
	}
	
	protected abstract void doHandle(ODataRequestContext context,ODataRequest request,ODataResponse response) throws Throwable;
}