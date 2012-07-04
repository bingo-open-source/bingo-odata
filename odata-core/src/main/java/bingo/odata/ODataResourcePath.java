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

import java.util.ArrayList;
import java.util.List;

import bingo.lang.Assert;
import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.NamedValue;
import bingo.lang.Predicates;
import bingo.lang.Strings;
import bingo.lang.tuple.ImmutableNamedValue;
import bingo.odata.ODataConstants.ResourcePaths;

public class ODataResourcePath {
	private static final Enumerable<PathSegment>	        EMPTY_SEGMENTS	= Enumerables.empty();
	private static final Enumerable<NamedValue<Value>>	EMPTY_PARAMS	= Enumerables.empty();
	
	private final String   fullPath;
	private final boolean isServiceRoot;
	private final boolean isServiceMetadata;
	private final boolean isBatch;
	
	private Enumerable<PathSegment> segments;
	
	public ODataResourcePath(String fullPath){
		Assert.notEmpty(fullPath,"resource path can not be empty");
		Assert.isTrue(fullPath.startsWith("/"),"resource path should starts with '/'");
		
		this.fullPath          = fullPath;
		this.isServiceRoot     = fullPath.equals(ResourcePaths.ServiceRoot);
		this.isServiceMetadata = !isServiceRoot & fullPath.equals(ResourcePaths.Metadata);
		this.isBatch		   = fullPath.equals(ResourcePaths.Batch);
	}
	
	public String getFullPath() {
		return fullPath;
	}
	
	public boolean isServiceRoot(){
		return isServiceRoot;
	}
	
	public boolean isServiceMetadata(){
		return isServiceMetadata;
	}
	
	public boolean isBatch() {
    	return isBatch;
    }
	
	public boolean hasSegments(){
		return !getSegments().isEmpty();
	}
	
	public Enumerable<PathSegment> getSegments() {
		if(null == segments){
			parse();
		}
    	return segments;
    }

	public void setSegments(Enumerable<PathSegment> segments) {
    	this.segments = segments;
    }

	private void parse(){
		if(!isServiceRoot && !isServiceMetadata){
			//ResourceName(....)/ResourceName(...)/ResourceName(...)
			String[] parts = Strings.split(fullPath,"/");

			List<PathSegment> list = new ArrayList<PathSegment>();
			
			for(int i=0;i<parts.length;i++){
				String part = parts[i];
				
				int index1 = part.indexOf('(');
				int index2 = part.indexOf(')');
				
				if(index1 < 0 && index2 < 0){
					list.add(new PathSegment(part, null, EMPTY_PARAMS));
					continue;
				}
				
				if(index1 > 0 && index2 == part.length() - 1){
					String   paramString = part.substring(index1 + 1,index2 - 1).trim();
					String[] paramValues = Strings.split(paramString,",");
					
					String name  = part.substring(0,index1);
					Value  value = null;
					List<NamedValue<Value>> params = new ArrayList<NamedValue<Value>>();
					
					if(paramValues.length == 1){
						value = Value.parse(paramString);
					}else{
						for(String param : paramValues){
							int eqIndex = param.indexOf("=");
							
							if(eqIndex > 0){
								params.add(ImmutableNamedValue.<Value>of(param,null));
							}else{
								params.add(ImmutableNamedValue.of(param.substring(0,eqIndex),Value.parse(param.substring(eqIndex + 1))));
							}
						}
					}
					
					list.add(new PathSegment(name, value, Enumerables.of(params)));
					continue;
				}
				
				return ;
			}
			
			this.segments = Enumerables.of(list);
		}else{
			segments = EMPTY_SEGMENTS;
		}
	}
	
	public static final class PathSegment {
		private final String		                   name;
		private final Value		                   value;
		private final Enumerable<NamedValue<Value>>  parameters;
		
		PathSegment(String name,Value value,Enumerable<NamedValue<Value>> parameters) {
			this.name       = name;
			this.value      = value;
			this.parameters = parameters;
        }

		public String getName() {
			return name;
		}

		public Value getValue() {
			return value;
		}
		
		public boolean isSingleValue(){
			return null != value;
		}
		
		public boolean hasParameters(){
			return parameters.isEmpty();
		}
		
		public Enumerable<NamedValue<Value>> getParameters() {
			return parameters;
		}
		
		public Value getParameter(String name) {
			NamedValue<Value> p = parameters.firstOrNull(Predicates.<NamedValue<Value>>nameEqualsIgnoreCase(name));
			
			return null == p ? null : p.getValue();
		}
	}
	
	public static final class Value {
		private static final Value EMPTY = new Value("",false);
		
		private final String   string;
		private final boolean isNumber;
		
		private Value(String string,boolean isNumber){
			this.string   = string;
			this.isNumber = isNumber;
		}

		public String getString() {
        	return string;
        }

		public boolean isNumber() {
        	return isNumber;
        }
		
		private static Value parse(String string){
			if(Strings.isEmpty(string)){
				return EMPTY;
			}else if(string.startsWith("'")){
				if(string.endsWith("'")){
					return new Value(string.substring(1,string.length() - 1),false);
				}
				throw ODataErrors.badRequest(Strings.format("invalid path value : {0}",string));
			}else{
				return new Value(string,true);
			}
		}
	}
}