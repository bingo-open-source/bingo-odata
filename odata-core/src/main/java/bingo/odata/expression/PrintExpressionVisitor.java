package bingo.odata.expression;

import bingo.lang.codec.Hex;
import bingo.odata.expression.OrderByExpression.Direction;
import bingo.odata.utils.InternalTypeUtils;

public class PrintExpressionVisitor implements ExpressionVisitor {

    private final StringBuilder sb = new StringBuilder();

    public static String asString(Expression expr) {
        PrintExpressionVisitor v = new PrintExpressionVisitor();
        expr.visit(v);
        return v.toString();
    }

    @Override
    public String toString() {
        return sb.toString();
    }

    private void append(String value) {
        sb.append(value);
    }

    private void append(String format, Object... args) {
        sb.append(String.format(format, args));
    }
    
    
    public void openParen() {
	    
    }

	public void closeParen() {
	    
    }

	public void visit(String type) {
        append(type);
    }
    
    public void afterDescend() {
        append(")");
    }

    public void beforeDescend() {
        append("(");
    }
    
    public void betweenDescend() {
        append(",");
    }
    
    public boolean visit(AddExpression expr) {
        append("add");
        return true;
    }
    
    public boolean visit(AndExpression expr) {
        append("and");
        return true;
    }

    
    public boolean visit(BoolLiteral expr) {
        append("boolean(%s)", expr.getValue());
        return true;
    }

    public boolean visit(CastExpression expr) {
        append("cast");
        return true;
    }

    public boolean visit(ConcatMethodCallExpression expr) {
        append("concat");
        return true;
    }
    
    public boolean visit(DateTimeLiteral expr) {
        append("datetime(%s)", InternalTypeUtils.formatDateTime(expr.getValue()));
        return true;
    }
    
    public boolean visit(DateTimeOffsetLiteral expr) {
        append("datetime(%s)", InternalTypeUtils.formatDateTimeOffset(expr.getValue()));
        return true;
    }
    
    public boolean visit(DecimalLiteral expr) {
        append("decimal(%s)", expr.getValue());
        return true;
    }

    public boolean visit(DivExpression expr) {
        append("div");
        return true;
    }

    public boolean visit(EndsWithMethodCallExpression expr) {
        append("endswith");
        return true;
    }
    
    public boolean visit(EntitySimpleProperty expr) {
        append("simpleProperty(%s)", expr.getName());
        return true;
    }
    
    public boolean visit(EqExpression expr) {
        append("eq");
        return true;
    }
    
    public boolean visit(GeExpression expr) {
        append("ge");
        return true;
    }
    
    public boolean visit(GtExpression expr) {
        append("gt");
        return true;
    }

    public boolean visit(GuidLiteral expr) {
        append("guid(%s)", expr.getValue());
        return true;
    }
    
    public boolean visit(IndexOfMethodCallExpression expr) {
        append("indexof");
        return true;
    }
    
    public boolean visit(IntegerLiteral expr) {
        append("integral(%s)", expr.getValue());
        return true;
    }
    
    public boolean visit(IsofExpression expr) {
        append("isof");
        return true;
    }
    
    public boolean visit(LeExpression expr) {
        append("le");
        return true;
    }
    
    public boolean visit(LengthMethodCallExpression expr) {
        append("length");
        return true;
    }
    
    public boolean visit(LtExpression expr) {
        append("lt");
        return true;
    }
    
    public boolean visit(ModExpression expr) {
        append("mod");
        return true;
    }
    
    public boolean visit(MulExpression expr) {
        append("mul");
        return true;
    }
    
    public boolean visit(NeExpression expr) {
        append("ne");
        return true;
    }
    
    public boolean visit(NegateExpression expr) {
        append("negate");
        return true;
    }
    
    public boolean visit(NotExpression expr) {
        append("not");
        return true;
    }
    
    public boolean visit(NullLiteral expr) {
        append("null");
        return true;
    }

    public boolean visit(OrExpression expr) {
        append("or");
        return true;
    }

    public boolean visit(ParenExpression expr) {
        append("paren");
        return true;
    }
    
    public boolean visit(BoolParenExpression expr) {
        append("boolParen");
        return true;
    }

    public boolean visit(ReplaceMethodCallExpression expr) {
        append("replace");
        return true;
    }

    public boolean visit(StartsWithMethodCallExpression expr) {
        append("startswith");
        return true;
    }
    
    public boolean visit(StringLiteral expr) {
        append("string(%s)", expr.getValue());
        return true;
    }
    
    public boolean visit(SubExpression expr) {
        append("sub");
        return true;
    }
    
    public boolean visit(SubstringMethodCallExpression expr) {
        append("substring");
        return true;
    }

    public boolean visit(SubstringOfMethodCallExpression expr) {
        append("substringof");
        return true;
    }

    public boolean visit(TimeLiteral expr) {
        append("time(%s)", InternalTypeUtils.formatTime(expr.getValue()));
        return true;
    }

    public boolean visit(ToLowerMethodCallExpression expr) {
        append("tolower");
        return true;
    }
    
    public boolean visit(ToUpperMethodCallExpression expr) {
        append("toupper");
        return true;
    }
    
    public boolean visit(TrimMethodCallExpression expr) {
        append("trim");
        return true;
    }

    public boolean visit(YearMethodCallExpression expr) {
        append("year");
        return true;
    }
    
    public boolean visit(MonthMethodCallExpression expr) {
        append("month");
        return true;
    }
    
    public boolean visit(DayMethodCallExpression expr) {
        append("day");
        return true;
    }

    public boolean visit(HourMethodCallExpression expr) {
        append("hour");
        return true;
    }
    
    public boolean visit(MinuteMethodCallExpression expr) {
        append("minute");
        return true;
    }
    
    public boolean visit(SecondMethodCallExpression expr) {
        append("second");
        return true;
    }
    
    public boolean visit(RoundMethodCallExpression expr) {
        append("round");
        return true;
    }
    
    public boolean visit(FloorMethodCallExpression expr) {
        append("floor");
        return true;
    }
    
    public boolean visit(CeilingMethodCallExpression expr) {
        append("ceiling");
        return true;
    }
    
    public boolean visit(OrderByExpression expr) {
        append("orderBy");
        return true;
    }
    
    public boolean visit(Direction direction) {
        append(direction == Direction.ASCENDING ? "asc" : "desc");
        return true;
    }
    
    public boolean visit(Int64Literal expr) {
        append("int64(%s)", expr.getValue());
        return true;
    }

    public boolean visit(SingleLiteral expr) {
        append("single(%s)", expr.getValue());
        return true;
    }

    public boolean visit(DoubleLiteral expr) {
        append("double(%s)", expr.getValue());
        return true;
    }

    public boolean visit(BinaryLiteral expr) {
        append("binary(%s)", Hex.encode(expr.getValue()));
        return true;
    }
    
    public boolean visit(ByteLiteral expr) {
        append("byte(%s)", expr.getValue());
        return true;
    }
    
    public boolean visit(SByteLiteral expr) {
        append("sbyte(%s)", expr.getValue());
        return true;
    }
    
    public boolean visit(AggregateAnyFunction expr) {
        if (expr.getVariable() != null) {
            append("any:(%s =>)", expr.getVariable());
        } else {
            append("any()");
        }
        return true;
    }

    public boolean visit(AggregateAllFunction expr) {
        append("all:%s =>", expr.getVariable());
        return true;
    }
}
