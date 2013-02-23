package bingo.odata.expression;

public interface BinaryBoolExpression extends Expression {

    BoolExpression getLHS();

    BoolExpression getRHS();

}
