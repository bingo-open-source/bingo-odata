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
package bingo.odata.format;

import java.io.Writer;
import java.sql.Time;
import java.util.Date;

import bingo.lang.Strings;
import bingo.odata.ODataContext;
import bingo.odata.ODataErrors;
import bingo.odata.ODataObject;
import bingo.odata.ODataWriter;
import bingo.odata.ODataConstants.ContentTypes;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataProperty;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.values.DateTimeOffset;
import bingo.odata.values.UnsignedByte;
import bingo.utils.codec.binary.Base64;
import bingo.utils.json.JSON;
import bingo.utils.json.JSONWriter;

public abstract class ODataJsonWriter<T extends ODataObject> implements ODataWriter<T> {
	
	public String getContentType() {
	    return ContentTypes.APPLICATION_JSON_UTF8;
    }

	public final void write(ODataContext context,Writer out, T target) throws Throwable {
		JSONWriter writer = JSON.createWriter(out);
		
		String callback = context.getUrlInfo().getQueryOptions().getCallback();
		
		if(!Strings.isEmpty(callback)){
			writer.raw(callback + "(");
		}
		
		writer.startObject().name("d");
		
		write(context,JSON.createWriter(out),target);
		
		writer.endObject();
		
		if(!Strings.isEmpty(callback)){
			writer.raw(")");
		}
    }
	
	protected abstract void write(ODataContext context,JSONWriter writer,T target) throws Throwable;
	
	protected static void writeEntity(ODataContext context,JSONWriter writer,ODataEntity entity){
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
	
	protected static void writeEntityMetadata(ODataContext context,JSONWriter writer,ODataEntity entity){
		
		writer.startObject("__metadata");

		writer.property("uri",  ODataAtomUtils.getEntryId(context.getUrlInfo(), entity)).separator()
		      .property("type", entity.getEntitySet().getName());
		
		writer.endObject();
	}
	
	protected static void writeEntityProperties(ODataContext context,JSONWriter writer,ODataEntity entity){
		
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
	
	protected static void writeEntityLinks(ODataContext context,JSONWriter writer,ODataEntity entity){
		String uri = ODataAtomUtils.getEntryId(context.getUrlInfo(), entity);
		
		int i=0;
		for(EdmNavigationProperty np : entity.getEntityType().getDeclaredNavigationProperties()){
			
			if(i==0){
				i=1;
			}else{
				writer.separator();
			}
			
			writer.startObject(np.getName())
			
			      .startObject("__deferred").property("uri", uri + "/" + np.getName()).endObject()
			
				  .endObject();
		}
	}
	
	protected static void writeValue(JSONWriter writer, EdmSimpleType type,Object value){
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