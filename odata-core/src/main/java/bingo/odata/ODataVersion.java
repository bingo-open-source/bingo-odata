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

import bingo.lang.Valued;
import bingo.odata.format.ODataXmlConstants;

public enum ODataVersion implements Valued<String> {
	
	V1(1,0,ODataXmlConstants.EDM2006_NS),
	V2(2,0,ODataXmlConstants.EDM2007_NS),
	V3(3,0,ODataXmlConstants.EDM2009_11_NS);
	
	public static final ODataVersion MAX = ODataVersion.V3;
	
	private final int    major;
	private final int    minor;
	private final String value;
	private final String edmNamespace;
	
	ODataVersion(int major,int minor,String edmNamespace){
		this.major = major;
		this.minor = minor;
		this.value = major + "." + minor;
		this.edmNamespace = edmNamespace;
	}
	
	public int getMajor() {
    	return major;
    }

	public int getMinor() {
    	return minor;
    }

	public String getValue() {
	    return value;
    }
	
	public String getEdmNamespace() {
		return edmNamespace;
	}

	@Override
    public String toString() {
		return value;
    }
	
	public boolean isEqualsTo(ODataVersion v){
		return value.compareTo(v.getValue()) == 0;
	}
	
	public boolean isGreaterThan(ODataVersion v){
		return value.compareTo(v.getValue()) > 0;
	}
	
	public boolean isGreaterThanOrEqualsTo(ODataVersion v){
		return value.compareTo(v.getValue()) >=0;
	}
	
	public boolean isLessThan(ODataVersion v){
		return value.compareTo(v.getValue()) < 0;
	}
	
	public boolean isLessThanOrEqualsTo(ODataVersion v){
		return value.compareTo(v.getValue()) <= 0; 
	}
	
	public static ODataVersion parse(String string){
		if("1.0".equals(string)){
			return V1;
		}
		
		if("2.0".equals(string)){
			return V2;
		}
		
		if("3.0".equals(string)){
			return V3;
		}
		return null;
	}
}