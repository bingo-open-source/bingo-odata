package bingo.odata.consumer.util;

import java.util.Map;

import bingo.lang.Assert;
import bingo.lang.Collections;
import bingo.lang.Strings;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmFunctionImport;
import bingo.meta.edm.EdmProperty;
import bingo.odata.ODataServices;

public class ODataMetadataVerifier {
	private ODataServices services;
	
	public ODataMetadataVerifier(ODataServices services) {
		this.services = services;
	}

	public ODataServices getServices() {
		return services;
	}

	public void setServices(ODataServices services) {
		this.services = services;
	}
	
	public EdmEntityType hasEntityType(String entityType) {
		Assert.notBlank(entityType);
		EdmEntityType type = services.findEntityType(entityType);
		if(null == type) throw new ODataVerifiedFailException("Entity Type", entityType);
		return type;
	}
	
	public void hasFields(String entityType, Map<String, Object> fields) {
		Assert.notNull(fields);
		EdmEntityType type = hasEntityType(entityType);
		Object[] keys = fields.keySet().toArray();
		for (Object key : keys) {
			EdmProperty property = type.findProperty(key.toString());
			if(null == property) throw new ODataVerifiedFailException("Entity Property in " + entityType, key.toString());
		}
	}
	
	public EdmFunctionImport[] hasFunction(String functionName) {
		Assert.notNull(functionName);
		EdmFunctionImport[] funcs = services.findFunctionImport(functionName);
		if(null == funcs || funcs.length == 0) throw new ODataVerifiedFailException("Function", functionName);
		return funcs;
	}
	
	public void hasFunction(String entitySet, String funcName) {
		EdmFunctionImport[] funcs = hasFunction(funcName);
		if(Strings.isNotBlank(entitySet)) {
			for (EdmFunctionImport func : funcs) {
				String enSet = func.getEntitySet();
				if(Strings.equalsIgnoreCase(entitySet, enSet)) return;
			}
			throw new ODataVerifiedFailException("Function belong to Entity Set " + entitySet, funcName);
		}
	}
	

	public class ODataVerifiedFailException extends RuntimeException {

		private static final long serialVersionUID = -1533447768593885495L;
		
		public ODataVerifiedFailException(String name, String value) {
			super("No such object within metadata document: [" + name + " = " + value + "]");
		}
	}
}
