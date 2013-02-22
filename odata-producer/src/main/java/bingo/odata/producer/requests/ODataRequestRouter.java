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

import static bingo.lang.http.HttpMethods.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bingo.odata.ODataContext;
import bingo.odata.ODataException;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.producer.requests.batch.BatchRequestHandler;
import bingo.odata.producer.requests.data.delete.DeleteEntityRequestHandler;
import bingo.odata.producer.requests.data.delete.DeleteLinkRequestHandler;
import bingo.odata.producer.requests.data.insert.InsertEntityRequestHandler;
import bingo.odata.producer.requests.data.insert.InsertLinkRequestHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveCountRequestHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveEntityRequestHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveEntitySetRequestHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveLinkRequestHandler;
import bingo.odata.producer.requests.data.retrieve.RetrievePropertyRequestHandler;
import bingo.odata.producer.requests.data.update.MergeEntityRequestHandler;
import bingo.odata.producer.requests.data.update.UpdateLinkRequestHandler;
import bingo.odata.producer.requests.metadata.MetadataDocumentRequestHandler;
import bingo.odata.producer.requests.metadata.ServiceDocumentRequestHandler;

public class ODataRequestRouter {

	private static final List<ODataRequestRoute> routes = new ArrayList<ODataRequestRoute>(); 
	
	public static final String ENTITY_SET_NAME 		= "entitySetName";
	public static final String ENTITY_KEY_STRING 	= "entityKeyString";
	public static final String ENTITY_NAV_PROP_NAME = "entityNavProperyName";
	public static final String ENTITY_PROP_NAME   	= "entityProperyName";

	static {
		//Retrieve Requests
		add(GET,	"/",							   														new ServiceDocumentRequestHandler());
		add(GET,	"/\\$metadata",					   														new MetadataDocumentRequestHandler());
		add(GET,	"/{entitySetName:[^/()]+?}(?:\\(\\))?",				   									new RetrieveEntitySetRequestHandler());
		add(GET,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",									new RetrieveEntityRequestHandler());
		add(GET,	"/{entitySetName:[^/()]+?}(?:\\(\\))?/\\$count",										new RetrieveCountRequestHandler());
		add(GET,	"/{entitySetName:[^/()]+?}(?:\\(\\))?/\\$links/{entityNavProperyName}",					new RetrieveLinkRequestHandler());
		add(GET,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/{entityProperyName}",				new RetrievePropertyRequestHandler());
		add(GET,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/{entityProperyName}/\\$value",	new RetrievePropertyRequestHandler());
		
		//Insert Requests
		add(POST,	"/{entitySetName:[^/()]+?}(?:\\(\\))?",				   									new InsertEntityRequestHandler());
		add(POST,	"/{entitySetName:[^/()]+?}(?:\\(\\))?/\\$links/{entityNavProperyName}",					new InsertLinkRequestHandler());
		
		//Update Requests
		add(PUT,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new UpdateLinkRequestHandler());
		add(MERGE,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new MergeEntityRequestHandler());
		add(PATCH,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new MergeEntityRequestHandler());		
		add(PUT,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/\\$links/{entityNavProperyName}",	new UpdateLinkRequestHandler());	
		
		//Delete Requests
		add(DELETE,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new DeleteEntityRequestHandler());
		add(DELETE,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/\\$links/{entityNavProperyName}",	new DeleteLinkRequestHandler());		
		
		//Batch Request
		add("*",	"/\\$batch",						   													new BatchRequestHandler());
	}
	
	private static void add(String method,String path,ODataRequestHandler handler) {
		routes.add(ODataRequestRoute.compile(method, path, handler));
	}

	public ODataRequestHandler route(ODataContext context, ODataRequest request, ODataResponse response) throws ODataException {
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