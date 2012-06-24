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
	
	public static EdmSimpleType Binary = add(EdmValueKind.Binary);
	public static EdmSimpleType Boolean = add(EdmValueKind.Boolean);
	public static EdmSimpleType Byte = add(EdmValueKind.Byte);
	public static EdmSimpleType DateTime = add(EdmValueKind.DateTime);
	public static EdmSimpleType DateTimeOffset = add(EdmValueKind.DateTimeOffset);
	public static EdmSimpleType Decimal = add(EdmValueKind.Decimal);
	public static EdmSimpleType Double = add(EdmValueKind.Double);
	public static EdmSimpleType Guid = add(EdmValueKind.Guid);
	public static EdmSimpleType Int16 = add(EdmValueKind.Int16);
	public static EdmSimpleType Int32 = add(EdmValueKind.Int32);
	public static EdmSimpleType Int64 = add(EdmValueKind.Int64);
	public static EdmSimpleType SByte = add(EdmValueKind.SByte);
	public static EdmSimpleType Single = add(EdmValueKind.Single);
	public static EdmSimpleType Stream = add(EdmValueKind.Stream);
	public static EdmSimpleType String = add(EdmValueKind.String);
	public static EdmSimpleType Time = add(EdmValueKind.Time);
	
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