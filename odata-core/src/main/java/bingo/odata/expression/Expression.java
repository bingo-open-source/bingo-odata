package bingo.odata.expression;

public interface Expression {

    void visit(ExpressionVisitor visitor);

}
