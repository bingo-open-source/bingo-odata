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

import bingo.lang.http.HttpContentTypes;

public final class ODataConstants {

	public static final class Defaults {
		public static final String DATA_SERVICE_NAME = "Default";
	}

	public static final class ContentTypes extends HttpContentTypes {
		
		public static final String	APPLICATION_JSON_VERBOSE	= APPLICATION_JSON + ";odata=verbose";

		public static final String	APPLICATION_JSON_LIGHT		= APPLICATION_JSON + ";odata=light";

		public static final String	APPLICATION_ATOM_FEED		= "application/atom+xml;type=feed";

		public static final String	APPLICATION_ATOM_ENTRY		= "application/atom+xml;type=entry";	
		
	}
	
	public static final class Headers {
		
		public static final String DATA_SERVICE_VERSION    = "DataServiceVersion";
		
		public static final String MIN_DATA_SERVICE_VERSION = "MinDataServiceVersion";
		
		public static final String MAX_DATA_SERVICE_VERSION = "MaxDataServiceVersion";
	}
	
	public static final class QueryOptions {
		
		public static final String	EXPAND		    = "$expand";

		public static final String	FILTER		    = "$filter";

		public static final String	FORMAT		    = "$format";

		public static final String	ORDER_BY		= "$orderby";

		public static final String	SKIP		    = "$skip";

		public static final String	TOP		        = "$top";

		public static final String	SKIP_TOKEN		= "$skiptoken";

		public static final String	INLINE_COUNT	= "$inlinecount";

		public static final String	SELECT		    = "$select";

		public static final String	CALLBACK		= "$callback";
		
		public static final String    X_HTTP_METHOD   = "$x_http_method";
	}
	
	public static final class ResourcePaths {
		
		public static final String SERVICE_ROOT = "/";
		public static final String METADATA    = "/$metadata";
		public static final String BATCH       = "/$batch";
	}
	
	private ODataConstants() {

	}
}