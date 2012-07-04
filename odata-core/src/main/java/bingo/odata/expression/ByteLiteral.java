package bingo.odata.expression;

import bingo.odata.values.UnsignedByte;

/** 0 (0x00) to 255 (0xFF) */
public interface ByteLiteral extends LiteralExpression {

    UnsignedByte getValue();

}
