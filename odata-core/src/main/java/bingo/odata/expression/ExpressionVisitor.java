package bingo.odata.expression;

public interface ExpressionVisitor {

    void beforeDescend();

    void afterDescend();
    
    void openParen();
    
    void closeParen();

    void betweenDescend();

    void visit(String type);

    boolean visit(OrderByExpression expr);

    boolean visit(OrderByExpression.Direction direction);

    boolean visit(AddExpression expr);

    boolean visit(AndExpression expr);

    boolean visit(BoolLiteral expr);

    boolean visit(CastExpression expr);

    boolean visit(ConcatMethodCallExpression expr);

    boolean visit(DateTimeLiteral expr);

    boolean visit(DateTimeOffsetLiteral expr);

    boolean visit(DecimalLiteral expr);

    boolean visit(DivExpression expr);

    boolean visit(EndsWithMethodCallExpression expr);

    boolean visit(EntitySimpleProperty expr);

    boolean visit(EqExpression expr);

    boolean visit(GeExpression expr);

    boolean visit(GtExpression expr);

    boolean visit(GuidLiteral expr);

    boolean visit(BinaryLiteral expr);

    boolean visit(ByteLiteral expr);

    boolean visit(SByteLiteral expr);

    boolean visit(IndexOfMethodCallExpression expr);

    boolean visit(SingleLiteral expr);

    boolean visit(DoubleLiteral expr);

    boolean visit(IntegerLiteral expr);

    boolean visit(Int64Literal expr);

    boolean visit(IsofExpression expr);

    boolean visit(LeExpression expr);

    boolean visit(LengthMethodCallExpression expr);

    boolean visit(LtExpression expr);

    boolean visit(ModExpression expr);

    boolean visit(MulExpression expr);

    boolean visit(NeExpression expr);

    boolean visit(NegateExpression expr);

    boolean visit(NotExpression expr);

    boolean visit(NullLiteral expr);

    boolean visit(OrExpression expr);

    boolean visit(ParenExpression expr);

    boolean visit(BoolParenExpression expr);

    boolean visit(ReplaceMethodCallExpression expr);

    boolean visit(StartsWithMethodCallExpression expr);

    boolean visit(StringLiteral expr);

    boolean visit(SubExpression expr);

    boolean visit(SubstringMethodCallExpression expr);

    boolean visit(SubstringOfMethodCallExpression expr);

    boolean visit(TimeLiteral expr);

    boolean visit(ToLowerMethodCallExpression expr);

    boolean visit(ToUpperMethodCallExpression expr);

    boolean visit(TrimMethodCallExpression expr);

    boolean visit(YearMethodCallExpression expr);

    boolean visit(MonthMethodCallExpression expr);

    boolean visit(DayMethodCallExpression expr);

    boolean visit(HourMethodCallExpression expr);

    boolean visit(MinuteMethodCallExpression expr);

    boolean visit(SecondMethodCallExpression expr);

    boolean visit(RoundMethodCallExpression expr);

    boolean visit(FloorMethodCallExpression expr);

    boolean visit(CeilingMethodCallExpression expr);

    boolean visit(AggregateAnyFunction expr);

    boolean visit(AggregateAllFunction expr);

}
