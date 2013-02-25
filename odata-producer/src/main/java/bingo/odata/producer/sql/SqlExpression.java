/**
 * created at 2013-2-22
 */
package bingo.odata.producer.sql;


public class SqlExpression {

	private final String   text;
	private final Object[] paramValues;
	private final Object[] paramTypes;
	
	public SqlExpression(String expression,Object[] paramValues,Object[] paramTypes) {
	    this.text  = expression;
	    this.paramValues = paramValues;
	    this.paramTypes  = paramTypes;
    }
	
	public String getText() {
		return text;
	}

	public Object[] getParamValues() {
		return paramValues;
	}

	public Object[] getParamTypes() {
		return paramTypes;
	}
}