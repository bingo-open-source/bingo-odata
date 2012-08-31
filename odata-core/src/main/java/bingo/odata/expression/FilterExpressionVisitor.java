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

	public void visit(NullLiteral expr) {
		push("null");
	}

	public void visit(BooleanLiteral expr) {
		push(Boolean.toString(expr.getValue()));
	}

	public void visit(GuidLiteral expr) {
		push("guid'" + expr.getValue() + "'");
	}

	public void visit(StringLiteral expr) {
		push("'" + expr.getValue().replace("'", "''") + "'");
	}

	public void visit(Int64Literal expr) {
		push(expr.getValue() + "L");
	}

	public void visit(IntegralLiteral expr) {
		push(Integer.toString(expr.getValue()));
	}

	public void visit(DoubleLiteral expr) {
		push(Double.toString(expr.getValue()));
	}

	public void visit(SingleLiteral expr) {
		push(expr.getValue() + "f");
	}

	public void visit(DecimalLiteral expr) {
		push(expr.getValue() + "M");
	}

	public void visit(BinaryLiteral expr) {
		push("binary'" + Hex.encode(expr.getValue()) + "'");
	}

	public void visit(DateTimeLiteral expr) {
		push("datetime'" + InternalTypeUtils.formatDateTime(expr.getValue()) + "'");
	}

	public void visit(DateTimeOffsetLiteral expr) {
		push("datetimeoffset'" + InternalTypeUtils.formatDateTimeOffset(expr.getValue()) + "'");
	}

	public void visit(TimeLiteral expr) {
		push("time'" + InternalTypeUtils.formatTime(expr.getValue()) + "'");
	}

	public void visit(ByteLiteral expr) {
		push(Integer.toString(expr.getValue().intValue()));
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

	public void visit(Direction direction) {
		throw new UnsupportedOperationException();
	}

	public void visit(OrderByExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(SByteLiteral expr) {
		push(Byte.toString(expr.getValue()));
	}

	public void visit(AddExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(AndExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(CastExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(ConcatMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(DivExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(EndsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(EntitySimpleProperty expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(EqExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(GeExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(GtExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(IndexOfMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(IsofExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(LeExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(LengthMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(LtExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(ModExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(MulExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(NeExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(NegateExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(NotExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(OrExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(ParenExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(BoolParenExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(ReplaceMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(StartsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(SubExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(SubstringMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(SubstringOfMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(ToLowerMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(ToUpperMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(TrimMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(YearMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(MonthMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(DayMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(HourMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(MinuteMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(SecondMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(RoundMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(FloorMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(CeilingMethodCallExpression expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(AggregateAnyFunction expr) {
		throw new UnsupportedOperationException();
	}

	public void visit(AggregateAllFunction expr) {
		throw new UnsupportedOperationException();
	}

}
