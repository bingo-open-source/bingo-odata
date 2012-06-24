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

public final class ODataConstants {

	public static final String	DEFAULT_DATA_SERVICES_NAME	= "Default";

	public static final class ContentType {

		public static final String	TEXT_PLAIN		          = "text/plain";
		public static final String	TEXT_PLAIN_UTF8		      = TEXT_PLAIN + ";charset=utf-8";

		public static final String	APPLICATION_ATOM_XML	  = "application/atom+xml";
		public static final String	APPLICATION_ATOM_XML_UTF8 = APPLICATION_ATOM_XML + ";charset=utf-8";

		public static final String	APPLICATION_XML		      = "application/xml;charset=utf-8";
		public static final String	APPLICATION_XML_UTF8	  = "application/xml;charset=utf-8";

		public static final String	APPLICATION_JSON		  = "application/json";
		public static final String	APPLICATION_JSON_UTF8	  = APPLICATION_JSON + ";charset=utf-8";

	}

	private ODataConstants() {

	}

}
