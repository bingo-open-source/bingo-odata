package bingo.odata.expression;

public interface ConcatMethodCallExpression extends MethodCallExpression {

    CommonExpression getLHS();

    CommonExpression getRHS();

}
