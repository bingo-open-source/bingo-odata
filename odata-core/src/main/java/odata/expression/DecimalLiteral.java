package odata.expression;

import java.math.BigDecimal;

public interface DecimalLiteral extends LiteralExpression {

    BigDecimal getValue();

}
