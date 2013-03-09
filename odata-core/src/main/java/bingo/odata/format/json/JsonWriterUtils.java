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

import bingo.lang.Strings;
import bingo.lang.codec.Base64;
import bingo.lang.json.JSON;
import bingo.lang.json.JSONWriter;
import bingo.odata.ODataConverts;
import bingo.odata.ODataErrors;
import bingo.odata.ODataUtils;
import bingo.odata.ODataWriterContext;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataRawValue;
import bingo.odata.values.DateTimeOffset;
import bingo.odata.values.UnsignedByte;

public class JsonWriterUtils {
	
	public JsonWriterUtils(){
		
	}
	
	public static void writeEntitySet(ODataWriterContext context,JSONWriter writer,ODataEntitySet entitySet){
		writer.startObject();
		
		if(entitySet.getInlineCount() != null){
			writer.property("__count", String.valueOf(entitySet.getInlineCount())).separator();
		}
		
		writer.name("results");
		writeEntities(context, writer, entitySet.getEntities());
	
		String nextHref = ODataUtils.nextHref(context, entitySet);
		if(!Strings.isEmpty(nextHref)){
			writer.separator()
			      .property("__next", nextHref);
		}
		
		writer.endObject();
	}
	
	public static void writeEntities(ODataWriterContext context,JSONWriter writer,Iterable<ODataEntity> entities){
		writer.startArray();
		
		int i=0;
		for(ODataEntity entity : entities){
			
			if(i==0){
				i=1;
			}else{
				writer.separator();
			}
			
			writeEntity(context, writer, entity);
		}
		
		writer.endArray();
	}
	
	public static void writeEntity(ODataWriterContext context,JSONWriter writer,ODataEntity entity){
		writer.startObject();
		
		if(!context.isMinimal()){
			writeEntityMetadata(context, writer, entity);
		}
		
		if(!entity.getProperties().isEmpty()){
			if(!context.isMinimal()){
				writer.separator();
			}
			writeEntityProperties(context,writer,entity);
		}
		
		if(!entity.getNavigationProperties().isEmpty()){
			writeNavigationProperties(context, writer, entity);
		}
		
		writer.endObject();
	}
	
	public static void writeEntityMetadata(ODataWriterContext context,JSONWriter writer,ODataEntity entity){
		
		writer.startObject("__metadata");

		writer.property("uri",  ODataUtils.getEntityUrl(context.getUrlInfo(), entity)).separator()
		      .property("type", entity.getEntitySet().getName());
		
		writer.endObject();
	}
	
	public static void writeEntityProperties(ODataWriterContext context,JSONWriter writer,ODataEntity entity){
		
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
	
	public static void writeProperty(ODataWriterContext context,JSONWriter writer,ODataProperty p){
		if(p.getType().isSimple()){
			writer.name(p.getName());
			writeValue(writer,(EdmSimpleType)p.getType(), p.getValue());	
			return;
		}
		throw ODataErrors.notImplemented("ComplexType property not implemented");
	}
	
	public static void writeRawValue(ODataWriterContext context,JSONWriter writer,ODataRawValue rv){
		if(rv.getValue() == null){
			writer.nullValue();
			return;
		}
		
		if(rv.getType().isSimple()){
			writeValue(writer,(EdmSimpleType)rv.getType(), rv.getValue());	
			return;
		}
		
		writer.raw(JSON.encode(rv.getValue()));
	}
	
	private static void writeNavigationProperties(ODataWriterContext context,JSONWriter writer,ODataEntity entity){
//		String uri = ODataUtils.getEntryId(context.getUrlInfo(), entity);
		
		for(ODataNavigationProperty np : entity.getNavigationProperties()){
			
			if(np.isExpanded()){
				writer.separator();
				writer.name(np.getMetadata().getName());

				if(np.isRelatedEntity()){
					ODataEntity oentity = np.getRelatedEntity();
					
					if(null == oentity){
						writer.nullValue();
					}else{
						writeEntity(context, writer, oentity);
					}
				}else{
					ODataEntitySet entitySet = np.getRelatedEntititySet();
					writeEntitySet(context,writer,entitySet);
				}
			}else if(!context.isMinimal()){
				writer.separator();
				writer.startObject(np.getMetadata().getName())
			      .startObject("__deferred").property("uri",ODataUtils.getNavPropertyPath(context.getUrlInfo(),entity,np.getMetadata())).endObject()
				  .endObject();
			}
		}
	}
	
	public static void writeValue(JSONWriter writer, EdmSimpleType type,Object value){
		value = ODataConverts.convert(type, value);
		
		if(null == value){
			writer.nullValue();
		}else if(type == EdmSimpleType.STRING){
			writer.value((String)value);
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
