package bingo.odata.expression;

public interface IsofExpression extends BoolCommonExpression {

    CommonExpression getExpression(); // optional

    String getType();

}
