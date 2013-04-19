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

import bingo.lang.Enumerable;
import bingo.lang.Enumerables;
import bingo.lang.Strings;
import bingo.lang.beans.BeanModel;
import bingo.lang.beans.BeanProperty;
import bingo.lang.codec.Base64;
import bingo.lang.json.JSONWriter;
import bingo.lang.value.DateTimeOffset;
import bingo.lang.value.UnsignedByte;
import bingo.meta.edm.EdmComplexType;
import bingo.meta.edm.EdmProperty;
import bingo.meta.edm.EdmSimpleType;
import bingo.meta.edm.EdmType;
import bingo.odata.ODataConverts;
import bingo.odata.ODataErrors;
import bingo.odata.ODataObject;
import bingo.odata.ODataObjectKind;
import bingo.odata.ODataUtils;
import bingo.odata.ODataWriterContext;
import bingo.odata.model.ODataComplexObject;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;
import bingo.odata.model.ODataNamedValue;
import bingo.odata.model.ODataNavigationProperty;
import bingo.odata.model.ODataProperty;
import bingo.odata.model.ODataRawValue;

public class JsonWriterUtils {
	
	public JsonWriterUtils(){
		
	}
	
	public static void writeEntitySet(ODataWriterContext context,JSONWriter writer,ODataEntitySet entitySet){
		writer.startObject();
		
		if(entitySet.getInlineCount() != null){
			writer.property("__count", entitySet.getInlineCount()).separator();
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
				writeSimpleValue(writer,(EdmSimpleType)p.getType(), p.getValue());	
			}
			
			//TODO : ComplexType
		}
	}
	
	public static void writeProperty(ODataWriterContext context,JSONWriter writer,ODataProperty p){
		writer.name(p.getName());
		writeValue(context, writer, p.getType(), p.getValue());
	}
	
	public static void writeValue(ODataWriterContext context,JSONWriter writer,ODataObjectKind kind,ODataObject value){
		if(null == value){
			writer.nullValue();
			return;
		}
		
		if(value instanceof ODataRawValue){
			ODataRawValue rv = (ODataRawValue)value;
			writeValue(context, writer, rv.getType(), rv.getValue());
			return;
		}
		
		if(value instanceof ODataNamedValue){
			ODataNamedValue nv = (ODataNamedValue)value;
			writeNamedValue(context, writer, nv);
			return;
		}
		
		if(value instanceof ODataEntity){
			ODataEntity entity = (ODataEntity)value;
			writeValue(context, writer, entity.getEntityType(), entity);
			return;
		}
		
		if(value instanceof ODataEntitySet){
			ODataEntitySet entitySet = (ODataEntitySet)value;
			writeEntitySet(context, writer, entitySet);
			return;
		}
		
		if(value instanceof ODataProperty){
			ODataProperty property = (ODataProperty)value;
			writeProperty(context, writer, property);
			return;
		}
		
		if(value instanceof ODataComplexObject) {
			ODataComplexObject complexObject = (ODataComplexObject)value;
			writeComplexObject(context, writer, complexObject.getMetadata(), complexObject);
			return;
		}
		
		throw ODataErrors.notImplemented("unsupported type '{0}'",value.getClass().getName()); 
	}
	
	public static void writeValue(ODataWriterContext context,JSONWriter writer,EdmType type,Object value){
		if(null == value){
			writer.nullValue();
			return;
		}
		
		if(type.isSimple()){
			writeSimpleValue(writer, type.asSimple(), value);
			return;
		}
		
		if(type.isEntity()){
			writeEntity(context, writer, (ODataEntity)value);
			return;
		}
		
		if(type.isComplex()){
			writeComplexObject(context, writer,(EdmComplexType)type, value);
			return;
		}
		
		if(type.isCollection()){
			EdmType elementType = type.asCollection().getElementType();
			Enumerable<Object> enumerable = Enumerables.ofObject(value);
			writer.startArray();
			int i=0;
			for(Object elementValue : enumerable){
				if(i==0){
					i=1;
				}else{
					writer.separator();
				}
				writeValue(context,writer,elementType,elementValue);
			}
			writer.endArray();
			return;
		}
		
		throw ODataErrors.notImplemented("unsupported type '{0}'",type.getTypeKind());
	}
	
	public static void writeNamedValue(ODataWriterContext context,JSONWriter writer,ODataNamedValue nv){
		writer.startObject();
		writer.name(nv.getName());
	    JsonWriterUtils.writeValue(context, writer, nv.getValueKind(), nv.getValue());
	    writer.endObject();
	}
	
	public static void writeComplexObject(ODataWriterContext context,JSONWriter writer,EdmComplexType type, Object object){
		if(null == object){
			writer.nullValue();
			return;
		}
		
		writer.startObject();
		
		if(object instanceof ODataComplexObject){
			ODataComplexObject co = (ODataComplexObject)object;
			int i=0;
			for(ODataProperty p : co.getProperties()){
				if(i==0){
					i = 0;
				}else{
					writer.separator();
				}
				writeProperty(context, writer, p);
			}
		}else{
			BeanModel<?> model = BeanModel.get(object.getClass());
			
			int i=0;
			for(EdmProperty p : type.getDeclaredProperties()){
				BeanProperty bp = model.getProperty(p.getName());
				if(null != bp){
					if(i == 0){
						i = 1;
					}else{
						writer.separator();
					}
					
					writer.name(p.getName());
					writeValue(context, writer, p.getType(), bp.getValue(object));
				}
			}
		}
		
		writer.endObject();
	}
	
	private static void writeNavigationProperties(ODataWriterContext context,JSONWriter writer,ODataEntity entity){
//		String uri = ODataUtils.getEntryId(context.getUrlInfo(), entity);
		
		for(ODataNavigationProperty np : entity.getNavigationProperties()){
			
			if(np.isExpanded()){
				writer.separator();
				writer.name(np.getMetadata().getName());

				if(np.isRelatedEntity()){
					ODataEntity relatedEntity = np.getRelatedEntity();
					
					if(null == relatedEntity){
						writer.nullValue();
					}else{
						writer.startObject();
						
						if(!context.isMinimal()){
							writer.startObject("__deferred").property("uri",ODataUtils.getNavPropertyPath(context.getUrlInfo(),entity,np.getMetadata())).endObject();
						}
						
						if(!relatedEntity.getProperties().isEmpty()){
							if(!context.isMinimal()){
								writer.separator();
							}
							writeEntityProperties(context,writer,relatedEntity);
						}
						
						writer.endObject();
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
	
	public static void writeSimpleValue(JSONWriter writer, EdmSimpleType type,Object value){
		if(value instanceof ODataRawValue){
			value = ((ODataRawValue)value).getValue();
		}
		
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
        	writer.value(formatDateTimeForJson((Date)value));
        } else if (EdmSimpleType.DATETIME_OFFSET.equals(type)) {
            writer.value(((DateTimeOffset)value).getTimestamp());
        } else if (EdmSimpleType.TIME.equals(type)) {
        	writer.value(((Time)value));
        } else{
        	throw ODataErrors.notImplemented("'" + type.getFullQualifiedName() + "' not supported");	
        }
	}
	
	static final String	DATETIME_JSON_SUFFIX = ")\\/";
	static final String	DATETIME_JSON_PREFIX = "\\/Date(";
	
	public static String formatDateTimeForJson(Date date) {
		return DATETIME_JSON_PREFIX + date.getTime() + DATETIME_JSON_SUFFIX;
	}

}