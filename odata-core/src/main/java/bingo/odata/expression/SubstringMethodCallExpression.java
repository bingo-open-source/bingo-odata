package bingo.odata.expression;

public interface SubstringMethodCallExpression extends MethodCallExpression {

    Expression getTarget();

    Expression getStart(); // optional

    Expression getLength(); // optional

}
