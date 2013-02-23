package bingo.odata.expression;

public interface IsofExpression extends BoolExpression {

    Expression getExpression(); // optional

    String getType();

}
