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

public enum ODataVersion implements Valued<String> {

	V1(1,0),
	V2(2,0),
	V3(3,0);
	
	private final int    major;
	private final int    minor;
	private final String value;
	
	ODataVersion(int major,int minor){
		this.major = major;
		this.minor = minor;
		this.value = major + "." + minor;
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

	@Override
    public String toString() {
		return value;
    }
}