package bingo.odata.expression;

public interface BoolMethodExpression extends MethodCallExpression, BoolExpression {

    Expression getTarget();

    Expression getValue();

}
