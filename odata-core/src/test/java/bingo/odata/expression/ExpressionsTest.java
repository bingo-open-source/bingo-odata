package bingo.odata.expression;

import org.junit.Test;

public class ExpressionsTest {

	@Test
	public void testParse() {
		String exprs = "not (name eq 'chenkai') and sex ne 'girl'";
		Expression ex = Expressions.parse(exprs);
		ExpressionVisitor visitor = new FilterExpressionVisitor();
		ex.visit(visitor);
		System.out.println(visitor.toString());
	}

}
