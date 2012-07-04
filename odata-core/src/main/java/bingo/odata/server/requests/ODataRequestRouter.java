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

import static bingo.utils.http.HttpMethods.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bingo.odata.ODataException;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.server.requests.batch.BatchRequestHandler;
import bingo.odata.server.requests.data.delete.DeleteEntityRequestHandler;
import bingo.odata.server.requests.data.delete.DeleteLinkRequestHandler;
import bingo.odata.server.requests.data.insert.InsertEntityRequestHandler;
import bingo.odata.server.requests.data.insert.InsertLinkRequestHandler;
import bingo.odata.server.requests.data.retrieve.RetrieveCountRequestHandler;
import bingo.odata.server.requests.data.retrieve.RetrieveEntitySetRequestHandler;
import bingo.odata.server.requests.data.retrieve.RetrieveEntityRequestHandler;
import bingo.odata.server.requests.data.retrieve.RetrieveLinkRequestHandler;
import bingo.odata.server.requests.data.retrieve.RetrievePropertyRequestHandler;
import bingo.odata.server.requests.data.update.MergeEntityRequestHandler;
import bingo.odata.server.requests.data.update.UpdateLinkRequestHandler;
import bingo.odata.server.requests.metadata.MetadataDocumentRequestHandler;
import bingo.odata.server.requests.metadata.ServiceDocumentRequestHandler;

public class ODataRequestRouter {

	private static final List<ODataRequestRoute> routes = new ArrayList<ODataRequestRoute>(); 
	
	public static final String ENTITY_SET_NAME 		= "entitySetName";
	public static final String ENTITY_ID 		 		= "entityId";
	public static final String ENTITY_NAV_PROP_NAME   = "entityNavProperyName";
	public static final String ENTITY_PROP_NAME   	= "entityProperyName";
	
	static {
		//Retrieve Requests
		add(GET,	"/",							   							new ServiceDocumentRequestHandler());
		add(GET,	"/$metadata",					   							new MetadataDocumentRequestHandler());
		add(GET,	"/{entitySetName}",				   							new RetrieveEntitySetRequestHandler());
		add(GET,	"/{entitySetName}\\({entityId}\\)",							new RetrieveEntityRequestHandler());
		add(GET,	"/{entitySetName}/$count",									new RetrieveCountRequestHandler());
		add(GET,	"/{entitySetName}/$links/{entityNavProperyName}",			new RetrieveLinkRequestHandler());
		add(GET,	"/{entitySetName}\\({entityId}\\)/{entityProperyName}",		new RetrievePropertyRequestHandler());
		
		//Insert Requests
		add(POST,	"/{entitySetName}",				   							new InsertEntityRequestHandler());
		add(POST,	"/{entitySetName}/$links/{entityNavProperyName}",			new InsertLinkRequestHandler());
		
		//Update Requests
		add(PUT,	"/{entitySetName}",				   							new UpdateLinkRequestHandler());
		add(PUT,	"/{entitySetName}/$links/{entityNavProperyName}",			new UpdateLinkRequestHandler());	
		add(MERGE,	"/{entitySetName}",				   							new MergeEntityRequestHandler());
		add(PATCH,	"/{entitySetName}",				   							new MergeEntityRequestHandler());
		
		//Delete Requests
		add(DELETE,	"/{entitySetName}\\({entityId}\\)",				   			new DeleteEntityRequestHandler());
		add(DELETE,	"/{entitySetName}/$links/{entityNavProperyName}",			new DeleteLinkRequestHandler());		
		
		//Batch Request
		add("*",	"/$batch",						   							new BatchRequestHandler());
	}
	
	private static void add(String method,String path,ODataRequestHandler handler) {
		routes.add(ODataRequestRoute.compile(method, path, handler));
	}

	public ODataRequestHandler route(ODataRequestContext context, ODataRequest request, ODataResponse response) throws ODataException {
		String	            path    = request.getResourcePath();
		Map<String, String> params  = new HashMap<String, String>();
		ODataRequestHandler handler = null;
		
		for(ODataRequestRoute route : routes){
			if(route.matches(request.getMethod(), path, params)){
				handler = route.getHandler();
				context.getUrlInfo().setPathParameters(params);
				break;
			}
		}
		
		return handler;
	}
}