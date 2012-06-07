package odata.expression;

import org.joda.time.LocalTime;

public interface TimeLiteral extends LiteralExpression {

    LocalTime getValue();

}
