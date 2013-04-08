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
package bingo.odata.producer.sql;

import org.junit.Test;

import bingo.odata.expression.Expressions;
import junit.framework.Assert;

public class TestSqlFilter extends Assert {

	private String filter(String odataExpressoin){
		return SqlExpressions.filter(Expressions.parse(odataExpressoin)).getText();
	}
	
	@Test
	public void testArithmeticOperators(){
		assertEquals("a > ?",filter("a gt 100"));
		assertEquals("? > a",filter("100 gt a"));
		assertEquals("a >= ?",filter("a ge 100"));
		assertEquals("a < ?",filter("a lt 100"));
		assertEquals("a <= ?",filter("a le 100"));
		assertEquals("a != ?",filter("a ne 100"));
	}
	
	@Test
	public void testLikeOperator(){
		assertEquals("a LIKE ?",filter("substringof('xx',a)"));
		assertEquals("a LIKE ?",filter("substringof(a,'xx')"));
		assertEquals("a LIKE ?",filter("startswith('xx',a)"));
	}
	
	@Test
	public void testNullEquals(){
		assertEquals("a is null",filter("a eq null"));
		assertEquals("a is null AND b < ?",filter("a eq null and b lt 0"));
	}
}
