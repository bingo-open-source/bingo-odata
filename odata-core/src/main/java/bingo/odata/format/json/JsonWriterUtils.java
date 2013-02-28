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
package bingo.odata.format.json;

import java.sql.Time;
import java.util.Date;

import bingo.lang.codec.Base64;
import bingo.lang.json.JSONWriter;
import bingo.odata.ODataContext;
import bingo.odata.ODataErrors;
import bingo.odata.ODataUtils;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataProperty;
import bingo.odata.data.ODataRawValue;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.values.DateTimeOffset;
import bingo.odata.values.UnsignedByte;

public class JsonWriterUtils {
	
	public JsonWriterUtils(){
		
	}
	
	public static void writeEntity(ODataContext context,JSONWriter writer,ODataEntity entity){
		writer.startObject();
		
		writeEntityMetadata(context, writer, entity);
		
		if(!entity.getProperties().isEmpty()){
			writer.separator();
			writeEntityProperties(context,writer,entity);
		}
		
		if(!entity.getEntityType().getDeclaredNavigationProperties().isEmpty()){
			writer.separator();
			writeEntityLinks(context, writer, entity);
		}
		
		writer.endObject();
	}
	
	public static void writeEntityMetadata(ODataContext context,JSONWriter writer,ODataEntity entity){
		
		writer.startObject("__metadata");

		writer.property("uri",  ODataUtils.getEntityUrlWithFormat(context.getUrlInfo(), entity,context.getFormat().getValue())).separator()
		      .property("type", entity.getEntitySet().getName());
		
		writer.endObject();
	}
	
	public static void writeEntityProperties(ODataContext context,JSONWriter writer,ODataEntity entity){
		
		int i=0;
		for(ODataProperty p : entity.getProperties()){
			
			if(i==0){
				i=1;
			}else{
				writer.separator();
			}
			
			if(p.getType().isSimple()){
				writer.name(p.getName());
				writeValue(writer,(EdmSimpleType)p.getType(), p.getValue());	
			}
			
			//TODO : ComplexType
		}
	}
	
	public static void writeProperty(ODataContext context,JSONWriter writer,ODataProperty p){
		if(p.getType().isSimple()){
			writer.name(p.getName());
			writeValue(writer,(EdmSimpleType)p.getType(), p.getValue());	
			return;
		}
		throw ODataErrors.notImplemented("ComplexType property not implemented");
	}
	
	public static void writeRawValue(ODataContext context,JSONWriter writer,ODataRawValue rv){
		if(rv.getValue() == null){
			writer.nullValue();
			return;
		}
		
		if(rv.getType().isSimple()){
			writeValue(writer,(EdmSimpleType)rv.getType(), rv.getValue());	
			return;
		}
		
		throw ODataErrors.notImplemented("ComplexType property not implemented");
	}
	
	public static void writeEntityLinks(ODataContext context,JSONWriter writer,ODataEntity entity){
//		String uri = ODataUtils.getEntryId(context.getUrlInfo(), entity);
		
		int i=0;
		for(EdmNavigationProperty np : entity.getEntityType().getDeclaredNavigationProperties()){
			
			if(i==0){
				i=1;
			}else{
				writer.separator();
			}
			
			writer.startObject(np.getName())
			      .startObject("__deferred").property("uri",ODataUtils.getPropertyPathWithFormat(context.getUrlInfo(), entity,np,context.getFormat().getValue())).endObject()
				  .endObject();
		}
	}
	
	public static void writeValue(JSONWriter writer, EdmSimpleType type,Object value){
		if(null == value){
			writer.nullValue();
		}else if(type == EdmSimpleType.STRING){
			writer.value(value.toString());
		}else if (EdmSimpleType.GUID.equals(type)) {
			writer.value(value.toString());
        } else if (EdmSimpleType.BOOLEAN.equals(type)) {
        	writer.value((Boolean)value);
        } else if (EdmSimpleType.BYTE.equals(type)) {
        	writer.value(((UnsignedByte)value).byteValue());
        } else if (EdmSimpleType.SBYTE.equals(type)) {
        	writer.value(((Byte)value).byteValue());
        } else if (EdmSimpleType.INT16.equals(type)) {
        	writer.value(((Short)value));
        } else if (EdmSimpleType.INT32.equals(type)) {
        	writer.value(((Integer)value));
        } else if (EdmSimpleType.INT64.equals(type)) {
        	writer.value(((Long)value));
        } else if (EdmSimpleType.SINGLE.equals(type)) {
        	writer.value(((Number)value));
        } else if (EdmSimpleType.DOUBLE.equals(type)) {
        	writer.value(((Number)value));
        } else if (EdmSimpleType.DECIMAL.equals(type)) {
        	writer.value(((Number)value));
        } else if (EdmSimpleType.BINARY.equals(type)) {
        	writer.value(Base64.encode((byte[])value));
        } else if (EdmSimpleType.DATETIME.equals(type)) {
        	writer.value((Date)value);
        } else if (EdmSimpleType.DATETIME_OFFSET.equals(type)) {
            writer.value(((DateTimeOffset)value).getTimestamp());
        } else if (EdmSimpleType.TIME.equals(type)) {
        	writer.value(((Time)value));
        } else{
        	throw ODataErrors.notImplemented("'" + type.getFullQualifiedName() + "' not supported");	
        }
	}

}