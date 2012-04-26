package bingo.odata.expression;

import org.joda.time.DateTime;

public interface DateTimeOffsetLiteral extends LiteralExpression {

    DateTime getValue();

}
