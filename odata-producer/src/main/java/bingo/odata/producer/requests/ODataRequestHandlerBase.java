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
package bingo.odata.producer.requests;

import java.io.StringWriter;

import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataErrors;
import bingo.odata.ODataException;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataReader;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataWriter;
import bingo.odata.data.ODataValue;
import bingo.odata.producer.ODataProducerContext;

public abstract class ODataRequestHandlerBase implements ODataRequestHandler {
	
	private static final Log log = LogFactory.get(ODataRequestHandlerBase.class);
	
	public final void handle(ODataProducerContext context, ODataRequest request, ODataResponse response) throws ODataException {
	    try {
	        doHandle(context,request,response);
        } catch (Throwable e) {
        	if(e instanceof ODataException){
        		throw (ODataException)e;
        	}else{
        		throw new ODataException(e);	
        	}
        }
    }
	
	protected static void write(ODataProducerContext context,ODataRequest request,ODataResponse response,ODataValue returnValue) throws Throwable{
		write(context,request,response,returnValue.getKind(),returnValue.getValue());
	}
	
	protected static <T extends ODataObject> void write(ODataProducerContext context,ODataRequest request, ODataResponse response,ODataObjectKind kind,T target) throws Throwable {
		ODataWriter<T> writer = getWriter(context, kind);

		StringWriter out = new StringWriter();
		
		writer.write(context, out, target);
		
		String content = out.toString();
		
		if(log.isDebugEnabled()){
			log.debug("response content type : {}",	     writer.getContentType());
			log.trace("response content text : \n\n{}\n",content);
		}
		
		//The response's character encoding is only set from the given content type if this method is called before getWriter is called
		response.setContentType(writer.getContentType());
		response.getWriter().write(content);
	}
	
	protected static <T extends ODataObject> T read(ODataProducerContext context,ODataRequest request,ODataObjectKind kind) throws Throwable {
		ODataReader<T> reader = getReader(context, kind);

		return reader.read(context, request.getReader());
	}
	
	protected static <T extends ODataObject> ODataReader<T> getReader(ODataProducerContext context,ODataObjectKind kind) {
		ODataReader<T> reader = context.getProtocol().getReader(context.getVersion(),context.getFormat(), kind);
		
		if(null == reader){
			throw ODataErrors.unsupportedDataServiceFormat(context.getFormat().getValue());
		}
		
		return reader;
	}
	
	protected static <T extends ODataObject> ODataWriter<T> getWriter(ODataProducerContext context,ODataObjectKind kind) {
		ODataWriter<T> writer = context.getProtocol().getWriter(context.getVersion(),context.getFormat(), kind);
		
		if(null == writer){
			throw ODataErrors.unsupportedDataServiceFormat(context.getFormat().getValue());
		}
		
		return writer;
	}
	
	protected abstract void doHandle(ODataProducerContext context,ODataRequest request,ODataResponse response) throws Throwable;
}