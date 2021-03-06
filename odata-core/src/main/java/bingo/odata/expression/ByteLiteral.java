package bingo.odata.expression;

import bingo.lang.value.UnsignedByte;

/** 0 (0x00) to 255 (0xFF) */
public interface ByteLiteral extends LiteralExpression {

    UnsignedByte getValue();

}
