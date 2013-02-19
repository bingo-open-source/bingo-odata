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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bingo.lang.http.HttpContentTypes;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.producer.requests.ODataRequestController;
import bingo.odata.producer.servlets.ODataServletRequest;
import bingo.odata.producer.servlets.ODataServletResponse;

public abstract class ODataHttpHandlerBase implements ODataHttpHandler {

	public void handle(HttpServletRequest request, HttpServletResponse response) throws Throwable {
		String uri = request.getRequestURI();
		
		if(uri.equals("/clientaccesspolicy.xml")){
			
			response.setContentType(HttpContentTypes.TEXT_XML);
			response.getWriter().write(getClientAccessPolicyXml());
			response.getWriter().close();
			
		}else if(uri.equals("/crossdomain.xml")){
			
			response.setContentType(HttpContentTypes.TEXT_XML);
			response.getWriter().write(getCrossDomainXml());
			response.getWriter().close();
			
		}else if(uri.startsWith(this.getServiceRootPath())){
			
			ODataRequest  orequest  = new ODataServletRequest(request,getServiceRootPath());
			ODataResponse oresponse = new ODataServletResponse(response);
			
			getController().execute(orequest, oresponse);
			
		}else{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
    }
	
	protected String getServiceRootPath(){
		return "/";
	}
	
	protected abstract ODataRequestController getController();
	
	private static String getClientAccessPolicyXml() {
		return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" + 
			    "<access-policy>\n" + 
				"  <cross-domain-access>\n" + 
				"    <policy>\n" + 
				"      <allow-from http-request-headers=\"*\">\n" + 
				"        <domain uri=\"*\"/>\n" + 
				"      </allow-from>\n" + 
				"      <grant-to>\n" + 
				"        <resource path=\"/\" include-subpaths=\"true\"/>\n" + 
				"      </grant-to>\n" + 
				"    </policy>\n" + 
				"  </cross-domain-access>\n" + 
				"</access-policy>";
	}	
	
	private static String getCrossDomainXml() {
		return "<?xml version=\"1.0\"?>\n" + 
				"<!DOCTYPE cross-domain-policy SYSTEM \"http://www.adobe.com/xml/dtds/cross-domain-policy.dtd\">\n"  + 
				"<cross-domain-policy>\n" + 
		        "  <allow-access-from domain=\"*\"/>\n" + 
		        "</cross-domain-policy>";
	}
}
