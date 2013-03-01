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
package bingo.odata.format.atom;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import bingo.lang.Assert;
import bingo.lang.xml.XmlReader;
import bingo.odata.ODataErrors;
import bingo.odata.ODataException;
import bingo.odata.ODataReaderContext;
import bingo.odata.data.ODataEntity;
import bingo.odata.data.ODataEntityBuilder;
import bingo.odata.data.ODataProperty;
import bingo.odata.data.ODataPropertyBuilder;
import bingo.odata.edm.EdmEntitySet;
import bingo.odata.edm.EdmEntityType;
import bingo.odata.edm.EdmProperty;
import bingo.odata.format.ODataAtomReader;

public class AtomEntityReader extends ODataAtomReader<ODataEntity> {

	@Override
    protected ODataEntity read(ODataReaderContext context, XmlReader reader) throws Throwable {
		
		EdmEntitySet  entitySet  = context.getEntitySet();
		EdmEntityType entityType = context.getEntityType();
		
		Assert.notNull(entitySet, "entitySet in context cannot be null");
		Assert.notNull(entityType,"entityType in context cannot be null");
		
		while(reader.next()){
			if(reader.isStartElement(ATOM_QN_ENTRY)){
				return readEntity(entitySet, entityType, reader);
			}
		}
		
	    if(context.isProducer()){
	    	throw ODataErrors.badRequest("Invalid atom entry content");
	    }else{
	    	throw new ODataException("Invalid atom entry content");
	    }
    }
	
	protected static ODataEntity readEntity(EdmEntitySet entitySet,EdmEntityType entityType,XmlReader reader){
		ODataEntityBuilder builder = new ODataEntityBuilder(entitySet, entityType);

		//String etag = reader.getAttributeValue(METADATA_QN_ETAG);
		
		while(reader.next()){
			if(reader.isStartElement(ATOM_QN_TITLE)){
				builder.addTitlePropertyIfExists(reader.getElementText());
			}else if(reader.isStartElement(ATOM_QN_SUMMARY)){
				builder.addSummaryPropertyIfExists(reader.getElementText());
			}else if(reader.isStartElement(ATOM_QN_CONTENT)){
				if(reader.nextToChildElement(METADATA_QN_PROPERTIES)){
					builder.addProperties(readProperties(entitySet, entityType, reader));
				}
			}
		}
		
		return builder.build();
	}
	
	protected static List<ODataProperty> readProperties(EdmEntitySet entitySet,EdmEntityType entityType,XmlReader reader){
		List<ODataProperty> properties = new ArrayList<ODataProperty>();
		
		while(reader.nextIfElementNotEnd(METADATA_QN_PROPERTIES)){
			if(reader.isStartElement()){
				QName  		name 	 = reader.getElementName();
				EdmProperty property = entityType.findDeclaredProperty(name.getLocalPart());
				
				if(null != property){
					String isNull = reader.getAttributeValue(METADATA_QN_NULL);
					
					if("true".equals(isNull)){
						properties.add(ODataPropertyBuilder.nullOf(property));
					}else{
						properties.add(ODataPropertyBuilder.of(property, reader.getElementText()));	
					}
				}
				
				//TODO : support complext type
			}
		}
		
		return properties;
	}
}