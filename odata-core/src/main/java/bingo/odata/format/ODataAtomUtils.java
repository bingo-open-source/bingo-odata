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

import java.util.Calendar;
import java.util.Date;

import bingo.lang.Dates;
import bingo.odata.ODataUrlInfo;
import bingo.odata.data.ODataEntity;

public final class ODataAtomUtils {
	
	private static final String DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	
	public static String lastUpdated(){
		return formatDateTime(utcTime());
	}

	public static String formatDateTime(Date dt) {
		return Dates.format(dt, DATETIME_FORMAT);
	}
	
	public static String getEntryId(ODataUrlInfo urlInfo,ODataEntity entity){
		return urlInfo.getServiceRootUri() + entity.getKeyString();
	}
	
	private static Date utcTime(){
		Calendar c = Calendar.getInstance();
		
		c.add(Calendar.MILLISECOND, -1 * c.get(Calendar.ZONE_OFFSET));
		
		return c.getTime();
	}
}
