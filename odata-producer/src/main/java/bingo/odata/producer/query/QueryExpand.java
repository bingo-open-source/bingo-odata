/**
 * created at 2013-3-4
 */
package bingo.odata.producer.query;

import java.util.LinkedHashSet;
import java.util.Set;

import bingo.lang.Arrays;
import bingo.lang.Collections;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmNavigationProperty;

public class QueryExpand implements QuerySelected {

	protected String 				alias;
	protected EdmEntityType 		targetType;
	protected EdmNavigationProperty navProperty;
	protected Set<String>   		selects = new LinkedHashSet<String>();
	
	public QueryExpand(EdmNavigationProperty navProperty,EdmEntityType targetType){
		this.navProperty = navProperty;
		this.targetType = targetType;
	}

	public String getName() {
		return navProperty.getName();
	}
	
	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public EdmNavigationProperty getNavProperty() {
		return navProperty;
	}

	public EdmEntityType getTargetType() {
		return targetType;
	}

	public void setTargetType(EdmEntityType targetEntityType) {
		this.targetType = targetEntityType;
	}

	public Set<String> getSelects() {
		return selects;
	}
	
	public void addSelects(String... fields) {
		Collections.addAll(this.selects, fields);
	}
	
	public void addSelects(Iterable<String> fields) {
		Collections.addAll(this.selects, fields);
	}
	
	public boolean isSelectedField(String name) {
		if(selects.isEmpty()){
			return true;
		}
		for(String field : selects){
			if(field.equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
    }

	public String[] getSelectedFields(){
		if(selects.isEmpty()){
			return Arrays.EMPTY_STRING_ARRAY;
		}
		
		Set<String> fields = new LinkedHashSet<String>();

		for(String key : targetType.getKeys()){
			fields.add(key.toLowerCase());
		}
		
		for(String field : selects){
			fields.add(field.toLowerCase());
		}
		
		return Collections.toStringArray(fields);
	}
}
