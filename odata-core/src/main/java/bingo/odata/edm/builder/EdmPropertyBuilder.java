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
package bingo.odata.edm.builder;

import bingo.lang.Builder;
import bingo.odata.edm.EdmProperty;
import bingo.odata.edm.EdmType;

public class EdmPropertyBuilder extends EdmNamedBuilder implements Builder<EdmProperty> {
	
	protected EdmType type;
	
	protected boolean nullable;
	
	protected String defaultValue;
	
	protected boolean fixedLength;
	
	protected int maxLength;
	
	protected int precision;
	
	protected int scale;
	
	public EdmPropertyBuilder(){
		
	}
	
	public EdmPropertyBuilder(String name){
		this.name = name;
	}
	
	public EdmPropertyBuilder(String name,EdmType type,boolean nullable){
		this.name     = name;
		this.type     = type;
		this.nullable = nullable;
	}
	
	@Override
    public EdmPropertyBuilder setName(String name) {
	    super.setName(name);
	    return this;
    }

	public EdmType getType() {
    	return type;
    }

	public EdmPropertyBuilder setType(EdmType type) {
    	this.type = type;
    	return this;
    }

	public boolean isNullable() {
    	return nullable;
    }

	public EdmPropertyBuilder setNullable(boolean nullable) {
    	this.nullable = nullable;
    	return this;
    }

	public String getDefaultValue() {
    	return defaultValue;
    }

	public EdmPropertyBuilder setDefaultValue(String defaultValue) {
    	this.defaultValue = defaultValue;
    	return this;
    }

	public boolean isFixedLength() {
    	return fixedLength;
    }

	public EdmPropertyBuilder setFixedLength(boolean fixedLength) {
    	this.fixedLength = fixedLength;
    	return this;
    }

	public int getMaxLength() {
    	return maxLength;
    }

	public EdmPropertyBuilder setMaxLength(int maxLength) {
    	this.maxLength = maxLength;
    	return this;
    }

	public int getPrecision() {
    	return precision;
    }

	public EdmPropertyBuilder setPrecision(int precision) {
    	this.precision = precision;
    	return this;
    }

	public int getScale() {
    	return scale;
    }

	public EdmPropertyBuilder setScale(int scale) {
    	this.scale = scale;
    	return this;
    }

	public EdmProperty build() {
		return new EdmProperty(name, type , nullable, defaultValue, fixedLength, maxLength, precision, scale, documentation);
	}
}
