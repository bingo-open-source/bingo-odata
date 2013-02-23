package bingo.odata.expression;

public interface AggregateBoolFunction extends BoolExpression {

    Expression getSource(); // .NET docs use this terminology

    String getVariable();

    BoolExpression getPredicate();

    ExpressionParser.AggregateFunction getFunctionType();

}
