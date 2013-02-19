package bingo.odata.expression;

public interface AggregateBoolFunction extends BoolCommonExpression {

    CommonExpression getSource(); // .NET docs use this terminology

    String getVariable();

    BoolCommonExpression getPredicate();

    ExpressionParser.AggregateFunction getFunctionType();

}
