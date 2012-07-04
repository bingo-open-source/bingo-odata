package bingo.odata.expression;

public interface BinaryCommonExpression extends CommonExpression {

    CommonExpression getLHS();

    CommonExpression getRHS();

}
