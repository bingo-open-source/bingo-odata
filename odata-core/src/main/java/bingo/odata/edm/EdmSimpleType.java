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

import java.util.LinkedHashMap;
import java.util.Map;

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Named;

public class EdmSimpleType extends EdmType implements Named {
	
	private static final Map<EdmValueKind, EdmSimpleType> map = new LinkedHashMap<EdmValueKind, EdmSimpleType>();
	
	public static EdmSimpleType BINARY = add(EdmValueKind.Binary);
	public static EdmSimpleType BOOLEAN = add(EdmValueKind.Boolean);
	public static EdmSimpleType BYTE = add(EdmValueKind.Byte);
	public static EdmSimpleType DATETIME = add(EdmValueKind.DateTime);
	public static EdmSimpleType DATETIME_OFFSET = add(EdmValueKind.DateTimeOffset);
	public static EdmSimpleType DECIMAL = add(EdmValueKind.Decimal);
	public static EdmSimpleType DOUBLE = add(EdmValueKind.Double);
	public static EdmSimpleType GUID = add(EdmValueKind.Guid);
	public static EdmSimpleType INT16 = add(EdmValueKind.Int16);
	public static EdmSimpleType INT32 = add(EdmValueKind.Int32);
	public static EdmSimpleType INT64 = add(EdmValueKind.Int64);
	public static EdmSimpleType SBYTE = add(EdmValueKind.SByte);
	public static EdmSimpleType SINGLE = add(EdmValueKind.Single);
	public static EdmSimpleType STREAM = add(EdmValueKind.Stream);
	public static EdmSimpleType STRING = add(EdmValueKind.String);
	public static EdmSimpleType TIME = add(EdmValueKind.Time);
	
	public static final Enumerable<EdmSimpleType> ALL = Enumerables.of(map.values());
	
	public static EdmSimpleType of(String kindName) {
		return of(EdmValueKind.valueOf(kindName));
	}
	
	public static EdmSimpleType of(EdmValueKind kind) {
		return map.get(kind);
	}

	private final String       name;
	private final String       fullQualifiedName;
	private final EdmValueKind valueKind;
	
	public EdmSimpleType(EdmValueKind kind){
		this.valueKind = kind;
		this.name      = kind.toString();
		this.fullQualifiedName = "Edm." + name;
	}
	
	public String getName() {
    	return name;
    }

	public String getFullQualifiedName() {
    	return fullQualifiedName;
    }

	@Override
    public EdmTypeKind getTypeKind() {
	    return EdmTypeKind.Simple;
    }

	public EdmValueKind getValueKind() {
    	return valueKind;
    }
	
	private static EdmSimpleType add(EdmValueKind kind){
		EdmSimpleType type = new EdmSimpleType(kind);
		map.put(type.getValueKind(), type);
		return type;
	}
}