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
package bingo.odata.edm;

import bingo.lang.Assert;
import bingo.lang.Strings;

public class EdmProperty extends EdmNamedObject {

	private final EdmType type;
	
	private final boolean nullable;
	
	private final String defaultValue;
	
	private final boolean fixedLength;
	
	private final int maxLength;
	
	private final int precision;
	
	private final int scale;
	
	public EdmProperty(String name,EdmType type,
					   boolean nullable,String defaultValue,
					   boolean fixedLength,int maxLength,int precision,int scale) {

		super(name);
		
		this.type = type;
		
		this.nullable     = nullable;
		this.defaultValue = defaultValue;
		this.fixedLength  = fixedLength;
		this.maxLength    = maxLength;
		this.precision    = precision;
		this.scale        = scale;
		
		Assert.notNull(name,"name is required in EdmProperty");
		Assert.notNull(type,"type is required in EdmProperty");
	}
	
	public EdmProperty(String name,EdmType type,
					   boolean nullable,String defaultValue,
					   boolean fixedLength,int maxLength,int precision,int scale,
					   EdmDocumentation documentation) {
		
		this(name,type,nullable,defaultValue,fixedLength,maxLength,precision,scale);
		
		this.documentation = documentation;
	}
	
	public EdmType getType() {
    	return type;
    }

	public boolean isNullable() {
    	return nullable;
    }

	public String getDefaultValue() {
    	return defaultValue;
    }

	public boolean isFixedLength() {
    	return fixedLength;
    }

	public int getMaxLength() {
    	return maxLength;
    }

	public int getPrecision() {
    	return precision;
    }

	public int getScale() {
    	return scale;
    }
	
	@Override
    public String toString() {
		return Strings.format("EdmProperty[name={0},type={1}]", name,type);
    }
}