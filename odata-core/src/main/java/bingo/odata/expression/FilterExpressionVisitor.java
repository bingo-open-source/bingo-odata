package bingo.odata.expression;

import bingo.lang.codec.Hex;
import bingo.odata.expression.OrderByExpression.Direction;
import bingo.odata.utils.InternalTypeUtils;

public class FilterExpressionVisitor implements ExpressionVisitor {

	// only literals supported, so this suffices for now
	private String	fragment;

	private void push(String fragment) {
		this.fragment = fragment;
	}

	@Override
	public String toString() {
		return fragment;
	}

	// literals
	public boolean visit(NullLiteral expr) {
		push("null");
		return true;
	}

	public void openParen() {
	    
    }

	public void closeParen() {
	    
    }

	public boolean visit(BoolLiteral expr) {
		push(Boolean.toString(expr.getValue()));
		return true;
	}

	public boolean visit(GuidLiteral expr) {
		push("guid'" + expr.getValue() + "'");
		return true;
	}

	public boolean visit(StringLiteral expr) {
		push("'" + expr.getValue().replace("'", "''") + "'");
		return true;
	}

	public boolean visit(Int64Literal expr) {
		push(expr.getValue() + "L");
		return true;
	}

	public boolean visit(IntegerLiteral expr) {
		push(Integer.toString(expr.getValue()));
		return true;
	}

	public boolean visit(DoubleLiteral expr) {
		push(Double.toString(expr.getValue()));
		return true;
	}

	public boolean visit(SingleLiteral expr) {
		push(expr.getValue() + "f");
		return true;
	}

	public boolean visit(DecimalLiteral expr) {
		push(expr.getValue() + "M");
		return true;
	}

	public boolean visit(BinaryLiteral expr) {
		push("binary'" + Hex.encode(expr.getValue()) + "'");
		return true;
	}

	public boolean visit(DateTimeLiteral expr) {
		push("datetime'" + InternalTypeUtils.formatDateTime(expr.getValue()) + "'");
		return true;
	}

	public boolean visit(DateTimeOffsetLiteral expr) {
		push("datetimeoffset'" + InternalTypeUtils.formatDateTimeOffset(expr.getValue()) + "'");
		return true;
	}

	public boolean visit(TimeLiteral expr) {
		push("time'" + InternalTypeUtils.formatTime(expr.getValue()) + "'");
		return true;
	}

	public boolean visit(ByteLiteral expr) {
		push(Integer.toString(expr.getValue().intValue()));
		return true;
	}

	// non-literals, not supported at the moment

	public void beforeDescend() {
		throw new UnsupportedOperationException();
	}

	public void afterDescend() {
		throw new UnsupportedOperationException();
	}

	public void betweenDescend() {
		throw new UnsupportedOperationException();
	}

	public void visit(String type) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(Direction direction) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(OrderByExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(SByteLiteral expr) {
		push(Byte.toString(expr.getValue()));
		return true;
	}

	public boolean visit(AddExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(AndExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(CastExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(ConcatMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(DivExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(EndsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(EntitySimpleProperty expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(EqExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(GeExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(GtExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(IndexOfMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(IsofExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(LeExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(LengthMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(LtExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(ModExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(MulExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(NeExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(NegateExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(NotExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(OrExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(ParenExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(BoolParenExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(ReplaceMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(StartsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(SubExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(SubstringMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(SubstringOfMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(ToLowerMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(ToUpperMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(TrimMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(YearMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(MonthMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(DayMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(HourMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(MinuteMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(SecondMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(RoundMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(FloorMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(CeilingMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(AggregateAnyFunction expr) {
		throw new UnsupportedOperationException();
	}

	public boolean visit(AggregateAllFunction expr) {
		throw new UnsupportedOperationException();
	}

}
