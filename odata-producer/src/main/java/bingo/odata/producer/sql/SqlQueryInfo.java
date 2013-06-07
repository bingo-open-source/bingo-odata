/**
 * created at 2013-3-4
 */
package bingo.odata.producer.sql;

import java.util.LinkedHashSet;
import java.util.Set;

import bingo.lang.Arrays;
import bingo.lang.Collections;
import bingo.meta.edm.EdmEntityType;

public class SqlQueryInfo implements SqlSelected {
	
	private EdmEntityType     entityType;
	private boolean		      queryTotal;
	private SqlPage			  page;
	private Set<String>       selects = new LinkedHashSet<String>();
	private Set<SqlExpand> expands = new LinkedHashSet<SqlExpand>();
	private String		      where;
	private Object[]          params;
	private String		      orderBy;
	
	public SqlQueryInfo(EdmEntityType entityType){
		this.entityType = entityType;
	}
	
	public SqlPage getPage() {
		return page;
	}

	public void setPage(SqlPage page) {
		this.page = page;
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
	
	public Set<SqlExpand> getExpands() {
		return expands;
	}
	
	public SqlExpand findExpand(String name){
		for(SqlExpand expand : expands){
			if(expand.getName().equalsIgnoreCase(name)){
				return expand;
			}
		}
		return null;
	}

	public void addExpands(SqlExpand... expands){
		Collections.addAll(this.expands, expands);
	}
	
	public void addExpands(Iterable<SqlExpand> expands){
		Collections.addAll(this.expands, expands);
	}
	
	public boolean isQueryTotal() {
		return queryTotal;
	}

	public void setQueryTotal(boolean queryTotal) {
		this.queryTotal = queryTotal;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
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

		for(String key : entityType.getKeys()){
			fields.add(key.toLowerCase());
		}
		
		for(String field : selects){
			fields.add(field.toLowerCase());
		}
		
		return Collections.toStringArray(fields);
	}
}