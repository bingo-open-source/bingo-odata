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
package bingo.odata.producer.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.producer.requests.ODataRequestController;

public class ODataServlet extends HttpServlet {

	private static final long serialVersionUID = -8501865276357197581L;
	
	protected ODataRequestController controller;
	
	@Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ODataRequest  orequest  = new ODataServletRequest(request,getServiceRootPath(request));
		ODataResponse oresponse = new ODataServletResponse(response);
		
		controller.execute(orequest,oresponse);
    }

	public void setController(ODataRequestController controller) {
    	this.controller = controller;
    }
	
	protected String getServiceRootPath(HttpServletRequest request) throws ServletException, IOException {
		return request.getServletPath();
	}
}