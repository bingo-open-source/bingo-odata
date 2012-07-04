package bingo.odata.expression;

public interface OrderByExpression extends CommonExpression {

    public enum Direction {
        ASCENDING, DESCENDING
    }

    CommonExpression getExpression();

    Direction getDirection();

}
