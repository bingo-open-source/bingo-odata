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

import static bingo.lang.http.HttpMethods.DELETE;
import static bingo.lang.http.HttpMethods.GET;
import static bingo.lang.http.HttpMethods.PATCH;
import static bingo.lang.http.HttpMethods.POST;
import static bingo.lang.http.HttpMethods.PUT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bingo.odata.ODataContext;
import bingo.odata.ODataException;
import bingo.odata.ODataRequest;
import bingo.odata.ODataResponse;
import bingo.odata.producer.requests.batch.BatchRequestHandler;
import bingo.odata.producer.requests.data.QueryRequesthandler;
import bingo.odata.producer.requests.data.delete.DeleteEntityHandler;
import bingo.odata.producer.requests.data.delete.DeleteLinksHandler;
import bingo.odata.producer.requests.data.insert.InsertEntityHandler;
import bingo.odata.producer.requests.data.insert.InsertLinksHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveCountHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveEntityHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveEntitySetHandler;
import bingo.odata.producer.requests.data.retrieve.RetrieveLinksHandler;
import bingo.odata.producer.requests.data.retrieve.RetrievePropertyHandler;
import bingo.odata.producer.requests.data.retrieve.RetrievePropertyValueHandler;
import bingo.odata.producer.requests.data.update.MergeEntityHandler;
import bingo.odata.producer.requests.data.update.UpdateEntityHandler;
import bingo.odata.producer.requests.data.update.UpdateLinksHandler;
import bingo.odata.producer.requests.invoke.FunctionRequestHandler;
import bingo.odata.producer.requests.metadata.MetadataDocumentHandler;
import bingo.odata.producer.requests.metadata.ServiceDocumentHandler;

public class ODataRequestRouter {

	private static final List<ODataRequestRoute> routes = new ArrayList<ODataRequestRoute>(); 
	
	public static final String ENTITY_SET_NAME 		= "entitySetName";
	public static final String ENTITY_KEY_STRING 	= "entityKeyString";
	public static final String ENTITY_NAV_PROP_NAME = "entityNavProperyName";
	public static final String ENTITY_PROP_NAME   	= "entityProperyName";
	public static final String FUNCTION_NAME        = "functionName";

	static {
		//Metadata Requests
		add(GET,	"/",							   														new ServiceDocumentHandler());
		add(POST,	"/",							   														new ServiceDocumentHandler());
		add(GET,	"/\\$metadata",					   														new MetadataDocumentHandler());
		add(POST,	"/\\$metadata",					   														new MetadataDocumentHandler());

		//Batch Request
		add("*",	"/\\$batch",						   													new BatchRequestHandler());
		
		//Query Request (not standard odata request )
		add(GET, 	"/\\$query",																			new QueryRequesthandler());
		
		//Reterieve Requests
		add(GET,	"/{entitySetName:[^/()]+?}(?:\\(\\))?",				   									new RetrieveEntitySetHandler());
		add(GET,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",									new RetrieveEntityHandler());
		add(GET,	"/{entitySetName:[^/()]+?}(?:\\(\\))?/\\$count",										new RetrieveCountHandler());
		add(GET,	"/{entitySetName:[^/()]+?}(?:\\(\\))?/\\$links/{entityNavProperyName}",					new RetrieveLinksHandler());
		add(GET,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/{entityProperyName}",				new RetrievePropertyHandler());
		add(GET,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/{entityProperyName}/\\$value",	new RetrievePropertyValueHandler());
		
		//Insert Requests
		add(POST,	"/{entitySetName:[^/()]+?}(?:\\(\\))?",				   									new InsertEntityHandler());
		add(POST,	"/{entitySetName:[^/()]+?}(?:\\(\\))?/\\$links/{entityNavProperyName}",					new InsertLinksHandler());
		
		//Update Requests
		add(PUT,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new UpdateEntityHandler());
		add(PATCH,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new MergeEntityHandler());
		add(PUT,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/\\$links/{entityNavProperyName}",	new UpdateLinksHandler());	
		
		//Delete Requests
		add(DELETE,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}",				   					new DeleteEntityHandler());
		add(DELETE,	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/\\$links/{entityNavProperyName}",	new DeleteLinksHandler());
		
		//Function Requests
		add("*",	"/{functionName:[^/()]+?}",				   												new FunctionRequestHandler());
		add("*",	"/{entitySetName:[^/()]+?}/{functionName:[^/()]+?}",				   					new FunctionRequestHandler());
//		add("*",	"/{entitySetName:[^/()]+?}{entityKeyString:\\(.+?\\)}/{functionName:[^/()]+?}",			new FunctionRequestHandler());

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