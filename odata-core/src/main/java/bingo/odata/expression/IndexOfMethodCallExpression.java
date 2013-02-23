package bingo.odata.expression;

public interface IndexOfMethodCallExpression extends MethodCallExpression {

    Expression getTarget();

    Expression getValue();

}
