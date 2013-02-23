package bingo.odata.expression;

public interface CastExpression extends Expression {

    Expression getExpression(); // optional

    String getType();

}
