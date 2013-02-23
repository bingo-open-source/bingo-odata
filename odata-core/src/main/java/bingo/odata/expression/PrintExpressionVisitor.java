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

    
    public void visit(AddExpression expr) {
        append("add");
    }

    
    public void visit(AndExpression expr) {
        append("and");
    }

    
    public void visit(BoolLiteral expr) {
        append("boolean(%s)", expr.getValue());
    }

    
    public void visit(CastExpression expr) {
        append("cast");
    }

    
    public void visit(ConcatMethodCallExpression expr) {
        append("concat");
    }

    
    public void visit(DateTimeLiteral expr) {
        append("datetime(%s)", InternalTypeUtils.formatDateTime(expr.getValue()));
    }

    
    public void visit(DateTimeOffsetLiteral expr) {
        append("datetime(%s)", InternalTypeUtils.formatDateTimeOffset(expr.getValue()));
    }

    
    public void visit(DecimalLiteral expr) {
        append("decimal(%s)", expr.getValue());
    }

    
    public void visit(DivExpression expr) {
        append("div");
    }

    
    public void visit(EndsWithMethodCallExpression expr) {
        append("endswith");
    }

    
    public void visit(EntitySimpleProperty expr) {
        append("simpleProperty(%s)", expr.getName());
    }

    
    public void visit(EqExpression expr) {
        append("eq");
    }

    
    public void visit(GeExpression expr) {
        append("ge");
    }

    
    public void visit(GtExpression expr) {
        append("gt");
    }

    
    public void visit(GuidLiteral expr) {
        append("guid(%s)", expr.getValue());
    }

    
    public void visit(IndexOfMethodCallExpression expr) {
        append("indexof");
    }

    
    public void visit(IntegerLiteral expr) {
        append("integral(%s)", expr.getValue());
    }

    
    public void visit(IsofExpression expr) {
        append("isof");
    }

    
    public void visit(LeExpression expr) {
        append("le");
    }

    
    public void visit(LengthMethodCallExpression expr) {
        append("length");
    }

    
    public void visit(LtExpression expr) {
        append("lt");
    }

    
    public void visit(ModExpression expr) {
        append("mod");
    }

    
    public void visit(MulExpression expr) {
        append("mul");
    }

    
    public void visit(NeExpression expr) {
        append("ne");
    }

    
    public void visit(NegateExpression expr) {
        append("negate");
    }

    
    public void visit(NotExpression expr) {
        append("not");
    }

    
    public void visit(NullLiteral expr) {
        append("null");
    }

    
    public void visit(OrExpression expr) {
        append("or");
    }

    
    public void visit(ParenExpression expr) {
        append("paren");
    }

    
    public void visit(BoolParenExpression expr) {
        append("boolParen");
    }

    
    public void visit(ReplaceMethodCallExpression expr) {
        append("replace");
    }

    
    public void visit(StartsWithMethodCallExpression expr) {
        append("startswith");
    }

    
    public void visit(StringLiteral expr) {
        append("string(%s)", expr.getValue());
    }

    
    public void visit(SubExpression expr) {
        append("sub");
    }

    
    public void visit(SubstringMethodCallExpression expr) {
        append("substring");
    }

    
    public void visit(SubstringOfMethodCallExpression expr) {
        append("substringof");
    }

    
    public void visit(TimeLiteral expr) {
        append("time(%s)", InternalTypeUtils.formatTime(expr.getValue()));
    }

    
    public void visit(ToLowerMethodCallExpression expr) {
        append("tolower");
    }

    
    public void visit(ToUpperMethodCallExpression expr) {
        append("toupper");
    }

    
    public void visit(TrimMethodCallExpression expr) {
        append("trim");
    }

    
    public void visit(YearMethodCallExpression expr) {
        append("year");
    }

    
    public void visit(MonthMethodCallExpression expr) {
        append("month");
    }

    
    public void visit(DayMethodCallExpression expr) {
        append("day");
    }

    
    public void visit(HourMethodCallExpression expr) {
        append("hour");
    }

    
    public void visit(MinuteMethodCallExpression expr) {
        append("minute");
    }

    
    public void visit(SecondMethodCallExpression expr) {
        append("second");
    }

    
    public void visit(RoundMethodCallExpression expr) {
        append("round");
    }

    
    public void visit(FloorMethodCallExpression expr) {
        append("floor");
    }

    
    public void visit(CeilingMethodCallExpression expr) {
        append("ceiling");
    }

    
    public void visit(OrderByExpression expr) {
        append("orderBy");
    }

    
    public void visit(Direction direction) {
        append(direction == Direction.ASCENDING ? "asc" : "desc");
    }

    
    public void visit(Int64Literal expr) {
        append("int64(%s)", expr.getValue());
    }

    
    public void visit(SingleLiteral expr) {
        append("single(%s)", expr.getValue());
    }

    
    public void visit(DoubleLiteral expr) {
        append("double(%s)", expr.getValue());
    }

    
    public void visit(BinaryLiteral expr) {
        append("binary(%s)", Hex.encode(expr.getValue()));
    }

    
    public void visit(ByteLiteral expr) {
        append("byte(%s)", expr.getValue());
    }

    
    public void visit(SByteLiteral expr) {
        append("sbyte(%s)", expr.getValue());
    }

    
    public void visit(AggregateAnyFunction expr) {
        if (expr.getVariable() != null) {
            append("any:(%s =>)", expr.getVariable());
        } else {
            append("any()");
        }
    }

    
    public void visit(AggregateAllFunction expr) {
        append("all:%s =>", expr.getVariable());
    }

}
