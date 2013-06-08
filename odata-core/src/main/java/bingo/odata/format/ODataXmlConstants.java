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

import javax.xml.namespace.QName;

public class ODataXmlConstants {

	public static final String		EDMX_PREFIX	                  = "edmx";
	public static final String		EDMX_NS	                      = "http://schemas.microsoft.com/ado/2007/06/edmx";
	public static final QName		EDMX_QN	                      = new QName(EDMX_NS, "Edmx");
	public static final QName		EDMX_QN_DATASERVICES	      = new QName(EDMX_NS, "DataServices");

	public static final String		DATASERVICES_PREFIX	          = "d";
	public static final String		DATASERVICES_NS	              = "http://schemas.microsoft.com/ado/2007/08/dataservices";
	public static final QName		DATASERVICES_QN_ELEMENT	      = new QName(DATASERVICES_NS, "element");		
	
	public static final String      EXTEND_METADATA_PREFIX        = "em";
	public static final String      EXTEND_METADATA_NS            = "http://schemas.bingosoft.net/odata/metadata/1.0";
	public static final QName       EXTEND_METADATA_QN_TITLE      = new QName(EXTEND_METADATA_NS,"Title");
	public static final QName       EXTEND_METADATA_QN_NULLABLE   = new QName(EXTEND_METADATA_NS,"Nullable");
	public static final QName       EXTEND_METADATA_QN_SERIALIZE_FORMAT = new QName(EXTEND_METADATA_NS,"SerializeFormat");
	public static final QName       EXTEND_METADATA_QN_SERIALIZE_TYPE   = new QName(EXTEND_METADATA_NS,"SerializeType");
	
	public static final String		METADATA_PREFIX	              = "m";
	public static final String		METADATA_NS	                  = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata";
	public static final QName		METADATA_QN_ETAG	          = new QName(METADATA_NS, "etag");
	public static final QName		METADATA_QN_PROPERTIES	      = new QName(METADATA_NS, "properties");
	public static final QName		METADATA_QN_TYPE	          = new QName(METADATA_NS, "type");
	public static final QName		METADATA_QN_NULL	          = new QName(METADATA_NS, "null");
	public static final QName		METADATA_QN_INLINE	          = new QName(METADATA_NS, "inline");
	public static final QName		METADATA_QN_FC_TARGET_PATH	  = new QName(METADATA_NS, "FC_TargetPath");
	public static final QName		METADATA_QN_FC_CONTENT_KIND	  = new QName(METADATA_NS, "FC_ContentKind");
	public static final QName		METADATA_QN_FC_KEEPIN_CONTENT = new QName(METADATA_NS, "FC_KeepInContent");	

	public static final String		EDM_PREFIX	                  = "edm";
	public static final String		EDM2006_NS	                  = "http://schemas.microsoft.com/ado/2006/04/edm";
	public static final QName		EDM2006_QN_SCHEMA	          = new QName(EDM2006_NS, "Schema");
	public static final QName		EDM2006_QN_ENTITY_TYPE	      = new QName(EDM2006_NS, "EntityType");
	public static final QName		EDM2006_QN_ASSOCIATION	      = new QName(EDM2006_NS, "Association");
	public static final QName		EDM2006_QN_COMPLEX_TYPE	      = new QName(EDM2006_NS, "ComplexType");
	public static final QName		EDM2006_QN_ENTITY_CONTAINER	  = new QName(EDM2006_NS, "EntityContainer");
	public static final QName		EDM2006_QN_ENTITY_SET	      = new QName(EDM2006_NS, "EntitySet");
	public static final QName		EDM2006_QN_ASSOCIATION_SET	  = new QName(EDM2006_NS, "AssociationSet");
	public static final QName		EDM2006_QN_FUNCTION_IMPORT	  = new QName(EDM2006_NS, "FunctionImport");
	public static final QName		EDM2006_QN_PARAMETER	      = new QName(EDM2006_NS, "Parameter");
	public static final QName		EDM2006_QN_END	              = new QName(EDM2006_NS, "End");
	public static final QName		EDM2006_QN_PROPERTY_REF	      = new QName(EDM2006_NS, "PropertyRef");
	public static final QName		EDM2006_QN_PROPERTY	          = new QName(EDM2006_NS, "Property");
	public static final QName		EDM2006_QN_NAV_PROPERTY	      = new QName(EDM2006_NS, "NavigationProperty");

	public static final String		EDM2007_NS	                  = "http://schemas.microsoft.com/ado/2007/05/edm";	              
	public static final QName		EDM2007_QN_SCHEMA	          = new QName(EDM2007_NS, "Schema");
	public static final QName		EDM2007_QN_ENTITY_TYPE	      = new QName(EDM2007_NS, "EntityType");
	public static final QName		EDM2007_QN_ASSOCIATION	      = new QName(EDM2007_NS, "Association");
	public static final QName		EDM2007_QN_COMPLEX_TYPE	      = new QName(EDM2007_NS, "ComplexType");
	public static final QName		EDM2007_QN_ENTITY_CONTAINER	  = new QName(EDM2007_NS, "EntityContainer");
	public static final QName		EDM2007_QN_ENTITY_SET	      = new QName(EDM2007_NS, "EntitySet");
	public static final QName		EDM2007_QN_ASSOCIATION_SET	  = new QName(EDM2007_NS, "AssociationSet");
	public static final QName		EDM2007_QN_FUNCTION_IMPORT	  = new QName(EDM2007_NS, "FunctionImport");
	public static final QName		EDM2007_QN_PARAMETER	      = new QName(EDM2007_NS, "Parameter");
	public static final QName		EDM2007_QN_END	              = new QName(EDM2007_NS, "End");
	public static final QName		EDM2007_QN_PROPERTY_REF	      = new QName(EDM2007_NS, "PropertyRef");
	public static final QName		EDM2007_QN_PROPERTY	          = new QName(EDM2007_NS, "Property");
	public static final QName		EDM2007_QN_NAV_PROPERTY	      = new QName(EDM2007_NS, "NavigationProperty");

	public static final String		EDM2008_1_NS	              = "http://schemas.microsoft.com/ado/2008/01/edm";	              
	public static final QName		EDM2008_1_QN_SCHEMA	          = new QName(EDM2008_1_NS, "Schema");
	public static final QName		EDM2008_1_QN_ENTITY_TYPE	  = new QName(EDM2008_1_NS, "EntityType");
	public static final QName		EDM2008_1_QN_ASSOCIATION	  = new QName(EDM2008_1_NS, "Association");
	public static final QName		EDM2008_1_QN_COMPLEX_TYPE	  = new QName(EDM2008_1_NS, "ComplexType");
	public static final QName		EDM2008_1_QN_ENTITY_CONTAINER = new QName(EDM2008_1_NS, "EntityContainer");
	public static final QName		EDM2008_1_QN_ENTITY_SET	      = new QName(EDM2008_1_NS, "EntitySet");
	public static final QName		EDM2008_1_QN_ASSOCIATION_SET  = new QName(EDM2008_1_NS, "AssociationSet");
	public static final QName		EDM2008_1_QN_FUNCTION_IMPORT  = new QName(EDM2008_1_NS, "FunctionImport");
	public static final QName		EDM2008_1_QN_PARAMETER	      = new QName(EDM2008_1_NS, "Parameter");
	public static final QName		EDM2008_1_QN_END	          = new QName(EDM2008_1_NS, "End");
	public static final QName		EDM2008_1_QN_PROPERTY_REF     = new QName(EDM2008_1_NS, "PropertyRef");
	public static final QName		EDM2008_1_QN_PROPERTY	      = new QName(EDM2008_1_NS, "Property");
	public static final QName		EDM2008_1_QN_NAV_PROPERTY     = new QName(EDM2008_1_NS, "NavigationProperty");

	public static final String		EDM2008_9_NS	              = "http://schemas.microsoft.com/ado/2008/09/edm";	              
	public static final QName		EDM2008_9_QN_SCHEMA	          = new QName(EDM2008_9_NS, "Schema");
	public static final QName		EDM2008_9_QN_ENTITY_TYPE	  = new QName(EDM2008_9_NS, "EntityType");
	public static final QName		EDM2008_9_QN_ASSOCIATION	  = new QName(EDM2008_9_NS, "Association");
	public static final QName		EDM2008_9_QN_COMPLEX_TYPE	  = new QName(EDM2008_9_NS, "ComplexType");
	public static final QName		EDM2008_9_QN_ENTITY_CONTAINER = new QName(EDM2008_9_NS, "EntityContainer");
	public static final QName		EDM2008_9_QN_ENTITY_SET	      = new QName(EDM2008_9_NS, "EntitySet");
	public static final QName		EDM2008_9_QN_ASSOCIATION_SET  = new QName(EDM2008_9_NS, "AssociationSet");
	public static final QName		EDM2008_9_QN_FUNCTION_IMPORT  = new QName(EDM2008_9_NS, "FunctionImport");
	public static final QName		EDM2008_9_QN_PARAMETER	      = new QName(EDM2008_9_NS, "Parameter");
	public static final QName		EDM2008_9_QN_END	          = new QName(EDM2008_9_NS, "End");
	public static final QName		EDM2008_9_QN_PROPERTY_REF	  = new QName(EDM2008_9_NS, "PropertyRef");
	public static final QName		EDM2008_9_QN_PROPERTY	      = new QName(EDM2008_9_NS, "Property");
	public static final QName		EDM2008_9_QN_NAV_PROPERTY	  = new QName(EDM2008_9_NS, "NavigationProperty");

	public static final String		EDM2009_NS	                  = "http://schemas.microsoft.com/ado/2009/08/edm";	              	
	public static final QName		EDM2009_QN_SCHEMA	          = new QName(EDM2009_NS, "Schema");
	public static final QName		EDM2009_QN_ENTITY_TYPE	      = new QName(EDM2009_NS, "EntityType");
	public static final QName		EDM2009_QN_ASSOCIATION	      = new QName(EDM2009_NS, "Association");
	public static final QName		EDM2009_QN_COMPLEX_TYPE	      = new QName(EDM2009_NS, "ComplexType");
	public static final QName		EDM2009_QN_ENTITY_CONTAINER	  = new QName(EDM2009_NS, "EntityContainer");
	public static final QName		EDM2009_QN_ENTITY_SET	      = new QName(EDM2009_NS, "EntitySet");
	public static final QName		EDM2009_QN_ASSOCIATION_SET	  = new QName(EDM2009_NS, "AssociationSet");
	public static final QName		EDM2009_QN_FUNCTION_IMPORT	  = new QName(EDM2009_NS, "FunctionImport");
	public static final QName		EDM2009_QN_PARAMETER	      = new QName(EDM2009_NS, "Parameter");
	public static final QName		EDM2009_QN_END	              = new QName(EDM2009_NS, "End");
	public static final QName		EDM2009_QN_PROPERTY_REF	      = new QName(EDM2009_NS, "PropertyRef");
	public static final QName		EDM2009_QN_PROPERTY	          = new QName(EDM2009_NS, "Property");
	public static final QName		EDM2009_QN_NAV_PROPERTY	      = new QName(EDM2009_NS, "NavigationProperty");

	public static final String		ATOM_PREFIX	                  = "atom";
	public static final String		ATOM_NS	                      = "http://www.w3.org/2005/Atom";
	public static final QName		ATOM_QN_FEED	              = new QName(ATOM_NS, "feed");
	public static final QName		ATOM_QN_ENTRY	              = new QName(ATOM_NS, "entry");
	public static final QName		ATOM_QN_ID	                  = new QName(ATOM_NS, "id");
	public static final QName		ATOM_QN_TITLE	              = new QName(ATOM_NS, "title");
	public static final QName		ATOM_QN_SUMMARY	              = new QName(ATOM_NS, "summary");
	public static final QName		ATOM_QN_UPDATED	              = new QName(ATOM_NS, "updated");
	public static final QName		ATOM_QN_CATEGORY	          = new QName(ATOM_NS, "category");
	public static final QName		ATOM_QN_CONTENT	              = new QName(ATOM_NS, "content");
	public static final QName		ATOM_QN_LINK	              = new QName(ATOM_NS, "link");	

	public static final String		APP_PREFIX	                  = "app";
	public static final String		APP_NS	                      = "http://www.w3.org/2007/app";
	public static final QName		APP_QN_WORKSPACE	          = new QName(APP_NS, "workspace");
	public static final QName		APP_QN_SERVICE	              = new QName(APP_NS, "service");
	public static final QName		APP_QN_COLLECTION	          = new QName(APP_NS, "collection");
	public static final QName		APP_QN_ACCEPT	              = new QName(APP_NS, "accept");	

	public static final String		SCHEMA_PREFIX	          	  = "schema";
	public static final String		SCHEMA_NS	              	  = "http://schemas.microsoft.com/ado/2007/08/dataservices/scheme";

	public static final String		RELATED_NS	          		  = "http://schemas.microsoft.com/ado/2007/08/dataservices/related/";
}
