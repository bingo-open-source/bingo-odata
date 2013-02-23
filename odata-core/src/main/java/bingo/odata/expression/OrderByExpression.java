package bingo.odata.expression;

public interface OrderByExpression extends Expression {

    public enum Direction {
        ASCENDING, DESCENDING;
    }

    Expression getExpression();

    Direction getDirection();

}
