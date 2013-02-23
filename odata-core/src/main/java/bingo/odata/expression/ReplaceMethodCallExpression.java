package bingo.odata.expression;

public interface ReplaceMethodCallExpression extends MethodCallExpression {

    Expression getTarget();

    Expression getFind();

    Expression getReplace();

}
