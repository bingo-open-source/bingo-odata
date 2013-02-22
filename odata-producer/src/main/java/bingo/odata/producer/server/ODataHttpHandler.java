/*
 * Copyright 2013 the original author or authors.
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
package bingo.odata.producer.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bingo.lang.http.HttpContentTypes;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.producer.requests.ODataRequestController;
import bingo.odata.producer.servlets.ODataServletRequest;
import bingo.odata.producer.servlets.ODataServletResponse;
import bingo.odata.producer.servlets.ODataServletUtils;

public abstract class ODataHttpHandler extends HttpServlet {

	private static final long serialVersionUID = -355226380427764542L;
	
	@Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uri = request.getRequestURI();
		
		if(uri.equals("/clientaccesspolicy.xml")){
			response.setContentType(HttpContentTypes.TEXT_XML);
			response.getWriter().write(ODataServletUtils.getClientAccessPolicyXml());
			response.getWriter().close();
			
			return;
		}
		
		if(uri.equals("/crossdomain.xml")){
			response.setContentType(HttpContentTypes.TEXT_XML);
			response.getWriter().write(ODataServletUtils.getCrossDomainXml());
			response.getWriter().close();
			
			return;
		}
		
		if(uri.equals("/favicon.ico")){
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		
		if(doHandle(request, response, uri)){
			return;
		}
		
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
	
	protected boolean doHandle(HttpServletRequest request,HttpServletResponse response,String requestUri) throws ServletException,IOException {
		String serviceRootPath = getServiceRootPath(request);
		
		if(requestUri.startsWith(serviceRootPath)){
			
			ODataRequest  orequest  = new ODataServletRequest(request,serviceRootPath);
			ODataResponse oresponse = new ODataServletResponse(response);
			
			getController().execute(orequest, oresponse);
			
			return true;
		}
		
		return false;
	}
	
	protected String getServiceRootPath(HttpServletRequest request){
		return request.getServletPath();
	}
	
	protected abstract ODataRequestController getController();
}
