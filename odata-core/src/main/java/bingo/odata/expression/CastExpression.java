package bingo.odata.expression;

public interface CastExpression extends CommonExpression {

    CommonExpression getExpression(); // optional

    String getType();

}
