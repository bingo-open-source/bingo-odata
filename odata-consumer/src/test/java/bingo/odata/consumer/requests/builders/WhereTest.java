package bingo.odata.consumer.requests.builders;

import static org.junit.Assert.*;

import org.junit.Test;

public class WhereTest {

	@Test
	public void testWhere() {
		Where w = null;
		String s = null;
		
		w = new Where("id").eq("123");
		s = "id eq '123'";
		test(s, w);
		
		w = new Where(1).eq(1);
		s = "1 eq 1";
		test(s, w);
		
		w = new Where("name").contain("k");
		s = "substringof('k', name)";
		test(s, w);
		
		w = new Where("name").startsWith("kai");
		s = "startswith(name, 'kai')";
		test(s, w);
		
		w = new Where("name").endsWith("kai");
		s = "endswith(name, 'kai')";
		test(s, w);
		
		w = new Where("name").eq("chenkai")
				.and(new Where("sex").eq("man"))
				.or(new Where("age").gt(23));
		s = "name eq 'chenkai' and (sex eq 'man') or (age gt 23)";
		test(s, w);
		
		w = new Where("name").eq("chenkai")
				.and(new Where("sex").eq("man"))
				.or(new Where("age").gt(23))
				.and(new Where("sex").eq("man"))
				.or(new Where("age").gt(23));
		s = "name eq 'chenkai' and (sex eq 'man') or (age gt 23) and (sex eq 'man') or (age gt 23)";
		test(s, w);
		
		w = new Where("name").eq("chenkai")
				.or(new Where("sex").eq("man"))
				.or(new Where("age").gt(23))
				.or(new Where("sex").eq("man"))
				.or(new Where("age").gt(23));
		s = "name eq 'chenkai' or (sex eq 'man') or (age gt 23) or (sex eq 'man') or (age gt 23)";
		test(s, w);
		
		w = new Where("name").eq("chenkai")
				.and(new Where("sex").eq("man"))
				.and(new Where("age").gt(23))
				.and(new Where("sex").eq("man"))
				.and(new Where("age").gt(23));
		s = "name eq 'chenkai' and (sex eq 'man') and (age gt 23) and (sex eq 'man') and (age gt 23)";
		test(s, w);
		
		w = new Where(new Where("name").eq("chenkai"))
				.or(
						new Where("age").gt(23)
						.and(new Where("sex").eq("man")));
		s = "(name eq 'chenkai') or (age gt 23 and (sex eq 'man'))";
		test(s, w);
		
		w = new Where().not(new Where("sex").eq("girl"));
		s = "not (sex eq 'girl')";
		test(s, w);
		
		w = new Where("name").endsWith("kai").and(new Where("age").gt(23));
		s = "endswith(name, 'kai') and (age gt 23)";
		test(s, w);
		
		w = new Where("age").gt(23).and(new Where("name").endsWith("kai"));
		s = "age gt 23 and (endswith(name, 'kai'))";
		test(s, w);
	}
	
	private void test(String s, Where w) {
		print(s, w);
		assertEquals(s, w.toString());
	}
	
	private void print(String s, Where w) {
		System.out.println(s);
		System.out.println(w);
		System.out.println();
	}
	
	@Test
	public void test1() {
		System.out.println(1);
	}
}
