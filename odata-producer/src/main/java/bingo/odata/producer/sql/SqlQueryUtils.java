/**
 * file created at 2013-6-6
 */
package bingo.odata.producer.sql;

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
import bingo.odata.expression.OrderByExpression;
import bingo.odata.model.ODataKey;
import bingo.odata.producer.ODataProducerContext;

public class SqlQueryUtils {
	
	protected SqlQueryUtils(){
		
	}
	
	public static Object id(ODataKey key){
		if(key.isPrimitiveValue()){
			return key.getPrimitiveValue();
		}else{
			return key.getNamedValues().toMap();
		}
	}
	
	public static SqlQueryInfo parseForEntity(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo){
		SqlQueryInfo query = new SqlQueryInfo(entityType);
		
		if(null != queryInfo){
			//$expand
			parseExpand(context, entityType, query, queryInfo);
			
			//$select
			parseSelectFields(context, entityType, query,queryInfo);
		}
		
		return query;
	}
	
	public static SqlQueryInfo parseForEntitySet(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo){
		return parseForEntitySet(context, entityType, queryInfo, null);
	}
	
	public static SqlQueryInfo parseForEntitySet(ODataProducerContext context,EdmEntityType entityType,ODataQueryInfo queryInfo,SqlMapping mapping){
		SqlQueryInfo query = new SqlQueryInfo(entityType);
		
		if(null != queryInfo){
			//$inlinecount
			parseInlineCount(query, queryInfo);
			
			//$skip,skiptoken,top
			parsePagedQuery(context,query,queryInfo);
			
			//$expand
			parseExpand(context, entityType, query, queryInfo);
			
			//$select
			parseSelectFields(context, entityType, query,queryInfo);
			
			//$filter
			parseFilter(query,queryInfo,mapping);
			
			//orderby
			parseOrderBy(query,queryInfo,mapping);
		}
		
		return query;
	}
	
	private static void parseInlineCount(SqlQueryInfo query,ODataQueryInfo queryInfo){
		if(queryInfo.isAllPagesInlineCount()){
			query.setQueryTotal(true);
		}
	}
	
	private static void parsePagedQuery(ODataProducerContext context, SqlQueryInfo query,ODataQueryInfo queryInfo){
		//skiptoken , top , skip
		String skiptoken = queryInfo.getSkipToken();
		if(!Strings.isEmpty(skiptoken)){
			String[] tokens = Strings.split(skiptoken,"_");

			try {
	            int start = Integer.parseInt(tokens[0]);
	            int size  = Integer.parseInt(tokens[1]);
	            
	            query.setPage(SqlPage.ofStartAndSize(start, size));
            } catch (Exception e) {
            	throw ODataErrors.badRequest("invalid skiptoken");
            }
		}else{
			//paged query
			if(queryInfo.getSkip() != null){
				int start = queryInfo.getSkip() + 1;
				int size  = queryInfo.getTop() == null ? context.getProducer().config().getMaxResults() : queryInfo.getTop();
				
				query.setPage(SqlPage.ofStartAndSize(start, size));
			}else if(queryInfo.getTop() != null){
				query.setPage(new SqlPage(1, queryInfo.getTop()));
			}else{
				query.setPage(new SqlPage(1, context.getProducer().config().getMaxResults()));
			}
		}
		
		if(query.getPage().size() > context.getProducer().config().getMaxResults()){
			throw ODataErrors.badRequest("results size can not exceed " + context.getProducer().config().getMaxResults());
		}
	}
	
	private static void parseSelectFields(ODataProducerContext context,EdmEntityType entityType,SqlQueryInfo query,ODataQueryInfo queryInfo){
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
					
					SqlExpand expand = query.findExpand(navPropertyName);
					
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
	
	private static void parseFilter(SqlQueryInfo query,ODataQueryInfo queryInfo,SqlMapping mapping){
		//$filter
		if(null != queryInfo.getFilter()){
			SqlExpression whereExpression = SqlExpressions.filter(queryInfo.getFilter(),mapping);
			query.setWhere(whereExpression.getText());
			query.setParams(whereExpression.getParamValues());
		}
	}
	
	private static void parseOrderBy(SqlQueryInfo query,ODataQueryInfo queryInfo,SqlMapping mapping){
		//$orderby
		List<OrderByExpression> orderByExpressions = queryInfo.getOrderBy();
		if(null != orderByExpressions && !orderByExpressions.isEmpty()){
			StringBuilder orderBy = new StringBuilder();
			
			for(int i=0;i<orderByExpressions.size();i++){
				OrderByExpression orderByExpression = orderByExpressions.get(i);
				
				if(i > 0){
					orderBy.append(",");
				}
				
				EntitySimpleProperty property = (EntitySimpleProperty)orderByExpression.getExpression();
				orderBy.append(null == mapping ? property.getName() : mapping.column(property.getName()));
				
				if(null != orderByExpression.getDirection()){
					orderBy.append(" ");
					orderBy.append(orderByExpression.getDirection().equals(OrderByExpression.Direction.ASCENDING) ? "asc" : "desc");
				}
			}
			
			query.setOrderBy(orderBy.toString());
		}
	}	
	
	private static void parseExpand(ODataProducerContext context,EdmEntityType entityType, SqlQueryInfo query,ODataQueryInfo queryInfo){
		Set<SqlExpand> list = new LinkedHashSet<SqlExpand>();
		for(EntitySimpleProperty p : queryInfo.getExpand()){
			String path = p.getName();
			if(path.equals("*")){
				list.clear();
				for(EdmNavigationProperty np : entityType.getAllNavigationProperties()){
					if(query.findExpand(np.getName()) == null){
						list.add(new SqlExpand(np,context.getServices().findEntityType(np.getToRole().getType())));	
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
	
	private static SqlExpand addExpand(ODataProducerContext context,EdmEntityType entityType, Set<SqlExpand> list,String name){
		EdmNavigationProperty np = entityType.findNavigationProperty(name);
		if(null == np){
			throw ODataErrors.notFound("expand navigation property '" + name + "' not found");
		}
		SqlExpand expand = new SqlExpand(np,context.getServices().findEntityType(np.getToRole().getType()));
		
		list.add(expand);
		
		return expand;
	}
}
