package bingo.odata.expression;

public interface BinaryBoolCommonExpression extends CommonExpression {

    BoolCommonExpression getLHS();

    BoolCommonExpression getRHS();

}
