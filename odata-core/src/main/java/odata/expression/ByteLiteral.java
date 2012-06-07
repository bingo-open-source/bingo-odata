package odata.expression;

import odata.UnsignedByte;

/** 0 (0x00) to 255 (0xFF) */
public interface ByteLiteral extends LiteralExpression {

    UnsignedByte getValue();

}
