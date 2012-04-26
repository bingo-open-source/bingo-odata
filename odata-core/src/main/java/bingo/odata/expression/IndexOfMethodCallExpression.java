package bingo.odata.expression;

public interface IndexOfMethodCallExpression extends MethodCallExpression {

    CommonExpression getTarget();

    CommonExpression getValue();

}
