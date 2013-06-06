/**
 * file created at 2013-6-6
 */
package bingo.odata.producer.query;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import bingo.lang.Collections;
import bingo.lang.Strings;
import bingo.meta.edm.EdmEntityType;
import bingo.meta.edm.EdmNavigationProperty;
import bingo.odata.ODataErrors;
import bingo.odata.ODataQueryInfo;
import bingo.odata.ODataUtils;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.model.ODataKey;
import bingo.odata.producer.ODataProducerContext;
import bingo.odata.producer.sql.SqlExpression;
import bingo.odata.producer.sql.SqlExpressions;

public class QueryUtils {
	
	protected QueryUtils(){
		
	}
	
	public static Object id(ODataKey key){
		if(key.isPrimitiveValue()){
			return key.getPrimitiveValue();
		}else{
			return key.getNamedValues().toMap();
		}
	}
	
	public static QueryInfo parseForEntity(ODataProducerContext context, EdmEntityType entityType,ODataQueryInfo queryInfo){
		QueryInfo query = new QueryInfo(entityType);
		
		if(null != queryInfo){
			//$expand
			parseExpand(context, entityType, query, queryInfo);
			
			//$select
			parseSelectFields(context, entityType, query,queryInfo);
		}
		
		return query;
	}
	
	public static QueryInfo parseForEntitySet(ODataProducerContext context, EdmEntityType entityType,ODataQueryInfo queryInfo){
		QueryInfo query = new QueryInfo(entityType);
		
		if(null != queryInfo){
			//$inlinecount
			parseInlineCount(query, queryInfo);
			
			//$skip,skiptoken,top
			parsePagedQuery(query,queryInfo);
			
			//$expand
			parseExpand(context, entityType, query, queryInfo);
			
			//$select
			parseSelectFields(context, entityType, query,queryInfo);
			
			//$filter
			parseFilter(query,queryInfo);
			
			//orderby
			String orderBy = context.getQueryOptions().getOrderBy();
			if(!Strings.isEmpty(orderBy)){
				query.setOrderBy(orderBy);
			}
		}
		
		return query;
	}
	
	private static void parseInlineCount(QueryInfo query,ODataQueryInfo queryInfo){
		if(queryInfo.isAllPagesInlineCount()){
			query.setQueryTotal(true);
		}
	}
	
	private static void parsePagedQuery(QueryInfo query,ODataQueryInfo queryInfo){
		//skiptoken , top , skip
		String skiptoken = queryInfo.getSkipToken();
		if(!Strings.isEmpty(skiptoken)){
			String[] tokens = Strings.split(skiptoken,"_");

			try {
	            int start = Integer.parseInt(tokens[0]);
	            int size  = Integer.parseInt(tokens[1]);
	            
	            query.setPage(QueryPage.ofStartAndSize(start, size));
            } catch (Exception e) {
            	throw ODataErrors.badRequest("invalid skiptoken");
            }
		}else{
			//paged query
			if(queryInfo.getSkip() != null){
				int start = queryInfo.getSkip() + 1;
				int size  = queryInfo.getTop() == null ? 100 : queryInfo.getTop(); //TODO : HARD CODE MAX SIZE
				
				query.setPage(QueryPage.ofStartAndSize(start, size));
			}else if(queryInfo.getTop() != null){
				query.setPage(new QueryPage(1, queryInfo.getTop()));
			}
		}
	}
	
	private static void parseSelectFields(ODataProducerContext context,EdmEntityType entityType,QueryInfo query,ODataQueryInfo queryInfo){
		//$select
		
		if(!Collections.isEmpty(queryInfo.getSelect()) && !"*".equals(context.getQueryOptions().getSelect())){
			boolean selectall   = false;
			List<String> fields = new ArrayList<String>();
			
			for(String name : ODataUtils.getPropertyNames(queryInfo.getSelect())){
				if(name.equals("*")){
					selectall = true;
					return;
				}
				
				if(name.contains("/")){
					String[] values = Strings.split(name,"/");
					if(values.length != 2){
						throw ODataErrors.badRequest("invalid selected property '{0}'",name);
					}
					String navPropertyName = values[0];
					String navSelectField  = values[1];
					
					QueryExpand expand = query.findExpand(navPropertyName);
					
					if(null == expand){
						expand = addExpand(context, entityType, query.getExpands(), navPropertyName);
					}
					
					expand.addSelects(navSelectField);
					continue;
				}
				
				if(entityType.findNavigationProperty(name) != null){
					continue;
				}
				fields.add(name);
			}
			
			if(!selectall){
				query.addSelects(fields);	
			}
		}		
	}
	
	private static void parseFilter(QueryInfo query,ODataQueryInfo queryInfo){
		//$filter
		if(null != queryInfo.getFilter()){
			SqlExpression whereExpression = SqlExpressions.filter(queryInfo.getFilter());
			query.setWhere(whereExpression.getText());
			query.setParams(whereExpression.getParamValues());
		}
	}
	
	private static void parseExpand(ODataProducerContext context,EdmEntityType entityType, QueryInfo query,ODataQueryInfo queryInfo){
		Set<QueryExpand> list = new LinkedHashSet<QueryExpand>();
		for(EntitySimpleProperty p : queryInfo.getExpand()){
			String path = p.getName();
			if(path.equals("*")){
				list.clear();
				for(EdmNavigationProperty np : entityType.getAllNavigationProperties()){
					if(query.findExpand(np.getName()) == null){
						list.add(new QueryExpand(np,context.getServices().findEntityType(np.getToRole().getType())));	
					}
				}
				break;
			}
			
			if(query.findExpand(path) == null){
				addExpand(context,entityType,list, path);
			}
		}
		query.addExpands(list);
	}
	
	private static QueryExpand addExpand(ODataProducerContext context,EdmEntityType entityType, Set<QueryExpand> list,String name){
		EdmNavigationProperty np = entityType.findNavigationProperty(name);
		if(null == np){
			throw ODataErrors.notFound("expand navigation property '" + name + "' not found");
		}
		QueryExpand expand = new QueryExpand(np,context.getServices().findEntityType(np.getToRole().getType()));
		
		list.add(expand);
		
		return expand;
	}
}
