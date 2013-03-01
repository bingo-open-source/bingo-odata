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
package bingo.odata;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import bingo.lang.Dates;
import bingo.lang.Strings;
import bingo.lang.uri.QueryStringBuilder;
import bingo.odata.ODataConstants.QueryOptions;
import bingo.odata.edm.EdmNavigationProperty;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.model.ODataEntity;
import bingo.odata.model.ODataEntitySet;

public class ODataUtils {
	
	private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	protected ODataUtils(){
		
	}
	
	public static String[] getPropertyNames(List<EntitySimpleProperty> properties) {
		String[] names = new String[properties.size()];
		
		for(int i=0;i<names.length;i++){
			names[i] = properties.get(i).getName();
		}
		
		return names;
	}
	
	public static String lastUpdated(){
		return formatDateTime(utcTime());
	}

	public static String formatDateTime(Date dt) {
		return Dates.format(dt, DATETIME_FORMAT);
	}
	
	public static String getEntityUrl(ODataUrlInfo urlInfo,ODataEntity entity){
		return urlInfo.getServiceRootUri() + entity.getKeyString();
	}
	
	public static String getEntityUrlWithFormat(ODataUrlInfo urlInfo,ODataEntity entity,String format){
		return getEntityUrl(urlInfo,entity) + "?" + QueryOptions.FORMAT + "=" + format;
	}
	
	public static String getNavPropertyPath(ODataUrlInfo urlInfo,ODataEntity entity,EdmNavigationProperty prop){
		return urlInfo.getServiceRootUri() +  entity.getKeyString() + "/" + prop.getName();
	}
	
	public static String getNavPropertyPathWithFormat(ODataUrlInfo urlInfo,ODataEntity entity,EdmNavigationProperty prop,String format){
		return getNavPropertyPath(urlInfo,entity,prop) + "?" + QueryOptions.FORMAT + "=" + format;
	}
	
	public static String getNavPropertyLinkPath(ODataUrlInfo urlInfo,ODataEntity entity,EdmNavigationProperty prop){
		return urlInfo.getServiceRootUri() +  entity.getKeyString() + "/$links/" + prop.getName();
	}
	
	public static String nextHref(ODataContext context,ODataEntitySet entitySet){
		if(!Strings.isEmpty(entitySet.getSkipToken())){
			QueryStringBuilder qs = new QueryStringBuilder();
			
			Map<String,String> params = context.getUrlInfo().getQueryOptions().getOptionsMap();
			for(Entry<String,String> param : params.entrySet()){
				if(!param.getKey().equals(QueryOptions.TOP) && 
				   !param.getKey().equals(QueryOptions.SKIP) && 
				   !param.getKey().equals(QueryOptions.SKIP_TOKEN)){
					
					qs.add(param.getKey(),param.getValue());
				}
			}
			qs.add(QueryOptions.SKIP_TOKEN,entitySet.getSkipToken());
			
			return context.getUrlInfo().getResourceUri() + "?" + qs.build();
		}
		return Strings.EMPTY;
	}
	
	private static Date utcTime(){
		Calendar c = Calendar.getInstance();
		
		c.add(Calendar.MILLISECOND, -1 * c.get(Calendar.ZONE_OFFSET));
		
		return c.getTime();
	}

}
