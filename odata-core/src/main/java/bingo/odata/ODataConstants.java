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
package bingo.odata;

import bingo.utils.http.HttpContentTypes;

public final class ODataConstants {

	public static final class Values {

	}
	
	public static final class Defaults {
		public static final String DataServiceName = "Default";
		
		public static final ODataVersion DataServiceVersion = ODataVersion.V2;
		
		public static final ODataFormat DataServiceFormat = ODataFormat.Atom;
	}

	public static final class Versions {
		public static final ODataVersion MinDataServiceVersion = ODataVersion.V1;
		
		public static final ODataVersion MaxDataServiceVersion = ODataVersion.V3;
	}
	
	public static final class ContentTypes extends HttpContentTypes {
		
		public static final String APPLICATION_JSON_VERBOSE = APPLICATION_JSON + ";odata=verbose";

		public static final String APPLICATION_JSON_LIGHT   = APPLICATION_JSON + ";odata=light";
		
	}
	
	public static final class Headers {
		
		public static final String DataServiceVersion    = "DataServiceVersion";
		
		public static final String MinDataServiceVersion = "MinDataServiceVersion";
		
		public static final String MaxDataServiceVersion = "MaxDataServiceVersion";
		
		public static final String Accept = "Accept";
	}
	
	public static final class QueryOptions {
		
		public static final String Format = "$format";
		
	}
	
	public static final class ResourcePaths {
		
		public static final String ServiceRoot = "/";
		public static final String Metadata    = "/$metadata";
		public static final String Batch       = "/$batch";
	}
	
	private ODataConstants() {

	}
}
