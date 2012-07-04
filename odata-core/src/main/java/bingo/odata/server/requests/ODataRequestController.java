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

import bingo.lang.Objects;
import bingo.lang.StopWatch;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataConstants;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataFormat;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.ODataWriters;
import bingo.odata.ODataConstants.Headers;
import bingo.odata.producer.ODataProducer;
import bingo.utils.http.HttpStatus;

public class ODataRequestController {
	
	private static final Log log = LogFactory.get(ODataRequestController.class);
	
	protected ODataProducer producer;
	
	protected ODataRequestRouter router = new ODataRequestRouter();
	
	protected ODataVersion  defaultDataServiceVersion = ODataConstants.Defaults.DataServiceVersion;
	
	protected ODataFormat defaultDataServiceFormat = ODataConstants.Defaults.DataServiceFormat;
	
	public void setProducer(ODataProducer producer) {
    	this.producer = producer;
    }

	public void setRouter(ODataRequestRouter router) {
    	this.router = router;
    }

	public void setDefaultDataServiceVersion(ODataVersion defaultDataServiceVersion) {
    	this.defaultDataServiceVersion = defaultDataServiceVersion;
    }

	public void setDefaultDataServiceFormat(ODataFormat defaultDataServiceFormat) {
    	this.defaultDataServiceFormat = defaultDataServiceFormat;
    }

	public void execute(ODataRequest request, ODataResponse response) {
		ODataVersion version = null;
		ODataFormat  format  = null;
		ODataUrlInfo     urlInfo = null;
		
		try{
			version = ODataRequestUtils.getAndCheckVersion(request,defaultDataServiceVersion);
			format  = Objects.firstNotNull(ODataRequestUtils.dataServiceFormat(request, version),defaultDataServiceFormat);
			urlInfo = ODataRequestUtils.createUrlInfo(request);
			
	        ODataRequestContext context = new ODataRequestContext(producer,version,format,urlInfo);

	        ODataRequestHandler handler = router.route(context, request, response);
	        
	        if(null != handler){
	        	StopWatch sw = StopWatch.startNew();
	        	
	        	handler.handle(context, request, response);
	        	
	        	if(log.isDebugEnabled()){
	        		log.debug("Found handler '{}', execute used {}ms",handler.getClass().getSimpleName(),sw.stop().getElapsedMilliseconds());
	        	}
	        	
	        	endResponse(request,response,context);
	        }else{
	        	throw ODataErrors.unsupportedResourcePath(request.getResourcePath());
	        }
		}catch(ODataError error){
			log.info(error.getMessage());
			error(error,version,format,request,response);
		}catch(Throwable e){
			log.info(e.getMessage(),e);
			error(ODataErrors.internalServerError(e.getMessage()),version,format,request,response);
		}
	}
	
	private void endResponse(ODataRequest request, ODataResponse response, ODataRequestContext context) throws Throwable {
        response.setContentType(context.getFormat().getContentType());
        response.setHeader(Headers.DataServiceVersion, context.getVersion().getValue());
        response.getWriter().close();	
	}
	
	private void error(ODataError error,ODataVersion version,ODataFormat format,ODataRequest request, ODataResponse response){
		if(null == version){
			version = defaultDataServiceVersion;
		}
		
		if(null == format){
			format = defaultDataServiceFormat;
		}
		
		StringWriter out = new StringWriter();
		
		try {
	        if(format.isAtom() || format.isXml()){
	        	ODataWriters.XML_ERROR_WRITER.write(request, out, error);
	        }else{
	        	//TODO : verbose json 
	        }
	        
	        response.setStatus(error.getStatus());
	        response.setContentType(format.getContentType());
	        response.setHeader(Headers.DataServiceVersion, version.getValue());
	        response.getWriter().write(out.toString());
	        response.getWriter().close();
        } catch (Throwable e) {
        	log.error("Error writing error response : {}",e.getMessage(),e);
        	response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
	}
}