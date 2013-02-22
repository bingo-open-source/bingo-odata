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
package bingo.odata.producer.servlets;

public class ODataServletUtils {

	protected ODataServletUtils(){
		
	}

	public static String getClientAccessPolicyXml() {
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
	
	public static String getCrossDomainXml() {
		return "<?xml version=\"1.0\"?>\n" + 
				"<!DOCTYPE cross-domain-policy SYSTEM \"http://www.adobe.com/xml/dtds/cross-domain-policy.dtd\">\n"  + 
				"<cross-domain-policy>\n" + 
		        "  <allow-access-from domain=\"*\"/>\n" + 
		        "</cross-domain-policy>";
	}
}
