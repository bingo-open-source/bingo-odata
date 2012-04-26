package bingo.odata.expression;

public interface SubstringMethodCallExpression extends MethodCallExpression {

    CommonExpression getTarget();

    CommonExpression getStart(); // optional

    CommonExpression getLength(); // optional

}
