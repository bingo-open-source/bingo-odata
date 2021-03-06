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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bingo.lang.StopWatch;
import bingo.lang.http.HttpStatus;
import bingo.lang.logging.Log;
import bingo.lang.logging.LogFactory;
import bingo.odata.ODataConstants.Headers;
import bingo.odata.ODataContext;
import bingo.odata.ODataError;
import bingo.odata.ODataErrors;
import bingo.odata.ODataFormat;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataProtocol;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.ODataUrlInfo;
import bingo.odata.ODataVersion;
import bingo.odata.ODataWriter;
import bingo.odata.producer.ODataProducer;
import bingo.odata.producer.ODataProducerContext;

public class ODataRequestController {
	
	private static final Log log = LogFactory.get(ODataRequestController.class);
	
	protected ODataProducer producer;
	
	protected ODataProtocol protocol;
	
	protected ODataRequestRouter router = new ODataRequestRouter();
	
	protected List<ODataRequestPlugin> plugins = new ArrayList<ODataRequestPlugin>();
	
	public ODataRequestController(){
		
	}
	
	public ODataRequestController(ODataProducer producer){
		this.producer = producer;
		this.protocol = producer.config().getProtocol();
	}
	
	public void setProducer(ODataProducer producer) {
    	this.producer = producer;
		this.protocol = producer.config().getProtocol();
    }

	public void setRouter(ODataRequestRouter router) {
    	this.router = router;
    }

	public void addPlugin(ODataRequestPlugin plugin){
		this.plugins.add(plugin);
	}
	
	public void addPlugins(Collection<ODataRequestPlugin> plugins){
		this.plugins.addAll(plugins);
	}
	
	public void execute(ODataRequest request, ODataResponse response) {
		ODataVersion version = null;
		ODataFormat  format  = null;
		ODataUrlInfo urlInfo = null;
		
		ODataProducerContext context = null;
		ODataRequestHandler handler = null;
		
		boolean handled = false;
		
		try{
			urlInfo = ODataRequestUtils.createUrlInfo(request);
			version = ODataRequestUtils.getAndCheckVersion(protocol,request);
			format  = ODataRequestUtils.dataServiceFormat(request, version, producer.config().isAutoDetectFormat()); //may be null
			
	        context = new ODataProducerContext(request,producer,protocol,version,format,urlInfo);
	        
	        for(int i=0;i<plugins.size();i++){
	        	ODataRequestPlugin plugin = plugins.get(i);
	        	if(plugin.preHandle(context, request, response)){
	        		endResponse(request,response,context);
	        		return;
	        	}
	        }

	        handler = router.route(context, request, response);
	        
	        if(null != handler){
	        	StopWatch sw = StopWatch.startNew();
	        	
	        	handler.handle(context, request, response);
	        	
	        	if(log.isDebugEnabled()){
	        		log.debug("Found handler '{}', execute used {}ms",handler.getClass().getSimpleName(),sw.stop().getElapsedMilliseconds());
	        	}
	        	
	        	handled = true;
	        	
		        for(int i=0;i<plugins.size();i++){
		        	ODataRequestPlugin plugin = plugins.get(i);
	        		plugin.postHandle(context, request, response);
	        	}
	        	
	        	endResponse(request,response,context);
	        }else{
	        	throw ODataErrors.unsupportedResourcePath(request.getResourcePath());
	        }
		}catch(ODataError error){
			if(log.isInfoEnabled()){
				if(null != handler){
					log.info("[{}] -> Error on request '{}' : {}",handler.getClass().getSimpleName(),request.getResourcePath(),error.getMessage());
				}else{
					log.info("[{}] -> Error on request '{}' : {}","None",request.getResourcePath(),error.getMessage());
				}
			}
			if(null != error.getCause()){
				logError(handler,request,error.getCause());
			}
			error(error,context,version,format,urlInfo,request,response);
		}catch(Throwable e){
			logError(handler,request,e);
			
			if(e.getCause() != null && e.getCause() instanceof ODataError){
				error((ODataError)e.getCause(),context,version,format,urlInfo,request,response);
			}else{
				error(createError(context,e),context,version,format,urlInfo,request,response);	
			}
		}finally{
	        for(int i=0;i<plugins.size();i++){
	        	ODataRequestPlugin plugin = plugins.get(i);
        		try {
	                plugin.doFinally(context, request, response,handled);
                } catch (Throwable e) {
                	log.error("error invoke doFinally of plugin : " + plugin.getClass().getName(),e);
                }
        	}			
		}
	}
	
	protected ODataError createError(ODataProducerContext context, Throwable cause){
		if(cause instanceof IllegalArgumentException){
			return ODataErrors.badRequest(errorMessage(context,cause));
		}
		return ODataErrors.internalServerError(errorMessage(context,cause));
	}
	
	protected void logError(ODataRequestHandler handler,ODataRequest request,Throwable e){
		if(log.isWarnEnabled()){
			if(null != handler){
				log.warn("[{}] -> Error on request '{}' : {}",handler.getClass().getSimpleName(),request.getResourcePath(),e.getMessage(),e);
			}else{
				log.warn("[{}] -> Error on request '{}' : {}","None",request.getResourcePath(),e.getMessage(),e);
			}
		}		
	}
	
	protected void endResponse(ODataRequest request, ODataResponse response, ODataContext context) throws Throwable {
        try {
	        response.setHeader(Headers.DATA_SERVICE_VERSION, context.getVersion().getValue());
	        response.getWriter().close();
        } catch (Exception e) {
        	log.warn("Error close response : {}",e.getMessage(),e);
        }	
	}
	
	protected String errorMessage(ODataProducerContext context, Throwable e){
		if(!context.isPrintStackTrace()){
			return e.getMessage();
		}
		
		StringWriter out    = new StringWriter();
		PrintWriter  writer = new PrintWriter(out);
		
		e.printStackTrace(writer);
		
		return out.toString();
	}
	
	protected void error(ODataError error,ODataProducerContext context,ODataVersion version,ODataFormat format,ODataUrlInfo urlInfo, ODataRequest request, ODataResponse response){
		if(null == version || !protocol.isVersionSupported(version)){
			version = protocol.getDefaultVersion();
		}
		
		if(null == format){
			format = producer.config().getDefaultFormat();
			
			if(null == format){
				format = ODataFormat.Default;
			}
		}
		
		if(null == context){
			context = new ODataProducerContext(request,producer,protocol,version,format,urlInfo);
		}
		
		StringWriter out = new StringWriter();
		
		try {
			ODataWriter<ODataError> writer = protocol.getWriter(version,format,ODataObjectKind.Error);
			
			if(null != writer){
				response.setContentType(writer.getContentType());
				writer.write(context, out, error);
		        response.getWriter().write(out.toString());
			}else{
		        response.setContentType(format.getContentType());
		        response.getWriter().write(error.getMessage());
			}
			
	        response.setStatus(error.getStatus());
	        response.setHeader(Headers.DATA_SERVICE_VERSION, version.getValue());
	        response.getWriter().close();
        } catch (Throwable e) {
        	log.warn("Error writing error response : {}",e.getMessage(),e);
        	try {
	            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception e1) {
            	log.warn("Error setting status : {}",e.getMessage(),e);
            }
        }
	}
}