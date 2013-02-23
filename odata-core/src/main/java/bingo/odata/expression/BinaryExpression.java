package bingo.odata.expression;

public interface BinaryExpression extends Expression {

    Expression getLHS();

    Expression getRHS();

}
