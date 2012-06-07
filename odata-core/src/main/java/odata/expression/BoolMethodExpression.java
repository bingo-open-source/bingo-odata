package odata.expression;

public interface BoolMethodExpression extends MethodCallExpression, BoolCommonExpression {

    CommonExpression getTarget();

    CommonExpression getValue();

}
