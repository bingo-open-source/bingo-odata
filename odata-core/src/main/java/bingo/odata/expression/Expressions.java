package bingo.odata.expression;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

import bingo.odata.edm.EdmSimpleType;
import bingo.odata.expression.ExpressionParser.AggregateFunction;
import bingo.odata.expression.OrderByExpression.Direction;
import bingo.odata.values.DateTimeOffset;
import bingo.odata.values.Guid;
import bingo.odata.values.UnsignedByte;

public class Expressions {

    private Expressions() {
    }

    public static Expression parse(String expression) {
        return ExpressionParser.parse(expression);
    }

    public static String asPrintString(Expression expr) {
        PrintExpressionVisitor v = new PrintExpressionVisitor();
        expr.visit(v);
        return v.toString();
    }

    public static String asFilterString(Expression expr) {
        FilterExpressionVisitor v = new FilterExpressionVisitor();
        expr.visit(v);
        return v.toString();
    }

    public static NullLiteral null_() {
        return NullLiteralImpl.INSTANCE;
    }

    public static IntegerLiteral integral(int value) {
        return new IntegralLiteralImpl(value);
    }

    public static BoolLiteral boolean_(boolean value) {
        return value ? BooleanLiteralImpl.TRUE : BooleanLiteralImpl.FALSE;
    }

    public static DateTimeLiteral dateTime(Date value) {
        return new DateTimeLiteralImpl(value);
    }

    public static DateTimeOffsetLiteral dateTimeOffset(DateTimeOffset value) {
        return new DateTimeOffsetLiteralImpl(value);
    }

    public static TimeLiteral time(Time value) {
        return new TimeLiteralImpl(value);
    }

    public static StringLiteral string(String value) {
        return new StringLiteralImpl(value);
    }

    public static GuidLiteral guid(Guid value) {
        return new GuidLiteralImpl(value);
    }

    public static DecimalLiteral decimal(BigDecimal value) {
        return new DecimalLiteralImpl(value);
    }

    public static BinaryLiteral binary(byte[] value) {
        return new BinaryLiteralImpl(value);
    }

    public static ByteLiteral byte_(UnsignedByte value) {
        return new ByteLiteralImpl(value);
    }

    public static SByteLiteral sbyte_(byte value) {
        return new SByteLiteralImpl(value);
    }

    public static SingleLiteral single(float value) {
        return new SingleLiteralImpl(value);
    }

    public static DoubleLiteral double_(double value) {
        return new DoubleLiteralImpl(value);
    }

    public static Int64Literal int64(long value) {
        return new Int64LiteralImpl(value);
    }

    public static EntitySimpleProperty simpleProperty(String propertyName) {
        return new EntitySimplePropertyImpl(propertyName);
    }

    public static EqExpression eq(Expression lhs, Expression rhs) {
        return new EqExpressionImpl(lhs, rhs);
    }

    public static NeExpression ne(Expression lhs, Expression rhs) {
        return new NeExpressionImpl(lhs, rhs);
    }

    public static AndExpression and(BoolExpression lhs, BoolExpression rhs) {
        return new AndExpressionImpl(lhs, rhs);
    }

    public static OrExpression or(final BoolExpression lhs, final BoolExpression rhs) {
        return new OrExpressionImpl(lhs, rhs);
    }

    public static LtExpression lt(final Expression lhs, final Expression rhs) {
        return new LtExpressionImpl(lhs, rhs);
    }

    public static GtExpression gt(final Expression lhs, final Expression rhs) {
        return new GtExpressionImpl(lhs, rhs);
    }

    public static LeExpression le(final Expression lhs, final Expression rhs) {
        return new LeExpressionImpl(lhs, rhs);
    }

    public static GeExpression ge(final Expression lhs, final Expression rhs) {
        return new GeExpressionImpl(lhs, rhs);
    }

    public static AddExpression add(final Expression lhs, final Expression rhs) {
        return new AddExpressionImpl(lhs, rhs);
    }

    public static SubExpression sub(final Expression lhs, final Expression rhs) {
        return new SubExpressionImpl(lhs, rhs);
    }

    public static MulExpression mul(final Expression lhs, final Expression rhs) {
        return new MulExpressionImpl(lhs, rhs);
    }

    public static DivExpression div(final Expression lhs, final Expression rhs) {
        return new DivExpressionImpl(lhs, rhs);
    }

    public static ModExpression mod(final Expression lhs, final Expression rhs) {
        return new ModExpressionImpl(lhs, rhs);
    }

    public static ParenExpression paren(final Expression expression) {
        return new ParenExpressionImpl(expression);
    }

    public static BoolParenExpression boolParen(final Expression expression) {
        return new BoolParenExpressionImpl(expression);
    }

    public static NotExpression not(final Expression expression) {
        return new NotExpressionImpl(expression);
    }

    public static NegateExpression negate(final Expression expression) {
        return new NegateExpressionImpl(expression);
    }

    public static CastExpression cast(String type) {
        return cast(null, type);
    }

    public static CastExpression cast(final Expression expression, final String type) {
        return new CastExpressionImpl(expression, type);
    }

    public static IsofExpression isof(String type) {
        return isof(null, type);
    }

    public static IsofExpression isof(final Expression expression, final String type) {
        return new IsofExpressionImpl(expression, type);
    }

    public static EndsWithMethodCallExpression endsWith(final Expression target, final Expression value) {
        return new EndsWithMethodCallExpressionImpl(target, value);
    }

    public static StartsWithMethodCallExpression startsWith(final Expression target, final Expression value) {
        return new StartsWithMethodCallExpressionImpl(target, value);
    }

    public static SubstringOfMethodCallExpression substringOf(Expression value) {
        return substringOf(value, null);
    }

    public static SubstringOfMethodCallExpression substringOf(final Expression value, final Expression target) {
        return new SubstringOfMethodCallExpressionImpl(target, value);
    }

    public static IndexOfMethodCallExpression indexOf(final Expression target, final Expression value) {
        return new IndexOfMethodCallExpressionImpl(target, value);
    }

    public static ReplaceMethodCallExpression replace(final Expression target, final Expression find, final Expression replace) {
        return new ReplaceMethodCallExpressionImpl(target, find, replace);
    }

    public static ToLowerMethodCallExpression toLower(final Expression target) {
        return new ToLowerMethodCallExpressionImpl(target);
    }

    public static ToUpperMethodCallExpression toUpper(final Expression target) {
        return new ToUpperMethodCallExpressionImpl(target);
    }

    public static TrimMethodCallExpression trim(final Expression target) {
        return new TrimMethodCallExpressionImpl(target);
    }

    public static SubstringMethodCallExpression substring(final Expression target, final Expression start) {
        return substring(target, start, null);
    }

    public static SubstringMethodCallExpression substring(final Expression target, final Expression start, final Expression length) {
        return new SubstringMethodCallExpressionImpl(target, start, length);
    }

    public static ConcatMethodCallExpression concat(final Expression lhs, final Expression rhs) {
        return new ConcatMethodCallExpressionImpl(lhs, rhs);
    }

    public static LengthMethodCallExpression length(final Expression target) {
        return new LengthMethodCallExpressionImpl(target);
    }

    public static YearMethodCallExpression year(final Expression target) {
        return new YearMethodCallExpressionImpl(target);
    }

    public static MonthMethodCallExpression month(final Expression target) {
        return new MonthMethodCallExpressionImpl(target);
    }

    public static DayMethodCallExpression day(final Expression target) {
        return new DayMethodCallExpressionImpl(target);
    }

    public static HourMethodCallExpression hour(final Expression target) {
        return new HourMethodCallExpressionImpl(target);
    }

    public static MinuteMethodCallExpression minute(final Expression target) {
        return new MinuteMethodCallExpressionImpl(target);
    }

    public static SecondMethodCallExpression second(final Expression target) {
        return new SecondMethodCallExpressionImpl(target);
    }

    public static RoundMethodCallExpression round(final Expression target) {
        return new RoundMethodCallExpressionImpl(target);
    }

    public static CeilingMethodCallExpression ceiling(final Expression target) {
        return new CeilingMethodCallExpressionImpl(target);
    }

    public static FloorMethodCallExpression floor(final Expression target) {
        return new FloorMethodCallExpressionImpl(target);
    }

    public static OrderByExpression orderBy(final Expression expression, final Direction direction) {
        return new OrderByExpressionImpl(expression, direction);
    }

    public static LiteralExpression literal(Object value) {
        return literal(null, value);
    }

    public static LiteralExpression literal(EdmSimpleType edmType, Object value) {
        if (edmType == null) {
            if (value == null){
            	throw new IllegalArgumentException("Cannot infer literal expression type for a null value");
            }

            edmType = EdmSimpleType.of(value.getClass());
            
            if (edmType == null){
            	throw new IllegalArgumentException("Cannot infer literal expression type for java type: " + value.getClass().getName());
            }
        }

        if (edmType.equals(EdmSimpleType.BINARY))
            return binary((byte[]) value);
        if (edmType.equals(EdmSimpleType.BOOLEAN))
            return boolean_((Boolean) value);
        if (edmType.equals(EdmSimpleType.DATETIME))
            return dateTime((Date) value);
        if (edmType.equals(EdmSimpleType.DATETIME_OFFSET))
            return dateTimeOffset((DateTimeOffset) value);
        if (edmType.equals(EdmSimpleType.DECIMAL))
            return decimal((BigDecimal) value);
        if (edmType.equals(EdmSimpleType.DOUBLE))
            return double_((Double) value);
        if (edmType.equals(EdmSimpleType.STRING))
            return string((String) value);
        if (edmType.equals(EdmSimpleType.GUID))
            return guid((Guid) value);
        if (edmType.equals(EdmSimpleType.INT64))
            return int64((Long) value);
        if (edmType.equals(EdmSimpleType.INT32) || edmType.equals(EdmSimpleType.INT16))
            return integral(Integer.parseInt(value.toString()));
        if (edmType.equals(EdmSimpleType.SINGLE))
            return single((Float) value);
        if (edmType.equals(EdmSimpleType.TIME))
            return time((Time) value);
        if (edmType.equals(EdmSimpleType.BYTE))
            return byte_((UnsignedByte) value);
        if (edmType.equals(EdmSimpleType.SBYTE))
            return sbyte_((Byte) value);
        
        throw new UnsupportedOperationException("Cannot infer literal expression type for edm type: " + edmType);
    }

    public static Object literalValue(LiteralExpression expression) {
        if (expression instanceof BinaryLiteral)
            return ((BinaryLiteral) expression).getValue();
        if (expression instanceof ByteLiteral)
            return ((ByteLiteral) expression).getValue();
        if (expression instanceof SByteLiteral)
            return ((SByteLiteral) expression).getValue();
        if (expression instanceof BoolLiteral)
            return ((BoolLiteral) expression).getValue();
        if (expression instanceof DateTimeLiteral)
            return ((DateTimeLiteral) expression).getValue();
        if (expression instanceof DateTimeOffsetLiteral)
            return ((DateTimeOffsetLiteral) expression).getValue();
        if (expression instanceof DecimalLiteral)
            return ((DecimalLiteral) expression).getValue();
        if (expression instanceof DoubleLiteral)
            return ((DoubleLiteral) expression).getValue();
        if (expression instanceof StringLiteral)
            return ((StringLiteral) expression).getValue();
        if (expression instanceof GuidLiteral)
            return ((GuidLiteral) expression).getValue();
        if (expression instanceof Int64Literal)
            return ((Int64Literal) expression).getValue();
        if (expression instanceof IntegerLiteral)
            return ((IntegerLiteral) expression).getValue();
        if (expression instanceof NullLiteral)
            return null;
        if (expression instanceof SingleLiteral)
            return ((SingleLiteral) expression).getValue();
        if (expression instanceof TimeLiteral)
            return ((TimeLiteral) expression).getValue();

        throw new UnsupportedOperationException("Implement " + expression);
    }

    public static AggregateAnyFunction any(Expression source) {
        return new AggregateAnyFunctionImpl(source, null, null);
    }

    public static AggregateAnyFunction any(Expression source, String var, BoolExpression predicate) {
        return new AggregateAnyFunctionImpl(source, var, predicate);
    }

    public static AggregateAllFunction all(Expression source, String var, BoolExpression predicate) {
        return new AggregateAllFunctionImpl(source, var, predicate);
    }

    public static AggregateBoolFunction aggregate(AggregateFunction function, Expression source, String var, BoolExpression predicate) {
        switch (function) {
        case all:
            return all(source, var, predicate);
        case any:
            return any(source, var, predicate);
        case none:
            return null;
        default:
            throw new RuntimeException("unexpected AggregateFunction: " + function);
        }
    }

    private abstract static class ExpressionImpl implements Expression {
        private final Class<?> interfaceType;

        protected ExpressionImpl(Class<?> interfaceType) {
            this.interfaceType = interfaceType;
        }

        @Override
        public String toString() {
            return interfaceType.getSimpleName();
        }

        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
        }

        abstract void visitThis(ExpressionVisitor visitor);
    }

    private static class NullLiteralImpl extends ExpressionImpl implements NullLiteral {
        static NullLiteral INSTANCE = new NullLiteralImpl();

        private NullLiteralImpl() {
            super(NullLiteral.class);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class BooleanLiteralImpl extends ExpressionImpl implements BoolLiteral {
        static BoolLiteral TRUE  = new BooleanLiteralImpl(true);
        static BoolLiteral FALSE = new BooleanLiteralImpl(false);
        private final boolean value;

        private BooleanLiteralImpl(boolean value) {
            super(BoolLiteral.class);
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class IntegralLiteralImpl extends ExpressionImpl implements IntegerLiteral {
        private final int value;

        public IntegralLiteralImpl(int value) {
            super(IntegerLiteral.class);
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class ByteLiteralImpl extends ExpressionImpl implements ByteLiteral {
        private final UnsignedByte value;

        public ByteLiteralImpl(UnsignedByte value) {
            super(ByteLiteral.class);
            this.value = value;
        }

        public UnsignedByte getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class SByteLiteralImpl extends ExpressionImpl implements SByteLiteral {
        private final byte value;

        public SByteLiteralImpl(byte value) {
            super(SByteLiteral.class);
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class SingleLiteralImpl extends ExpressionImpl implements SingleLiteral {
        private final float value;

        public SingleLiteralImpl(float value) {
            super(SingleLiteral.class);
            this.value = value;
        }

        public float getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class DoubleLiteralImpl extends ExpressionImpl implements DoubleLiteral {
        private final double value;

        public DoubleLiteralImpl(double value) {
            super(DoubleLiteral.class);
            this.value = value;
        }

        public double getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class Int64LiteralImpl extends ExpressionImpl implements Int64Literal {
        private final long value;

        public Int64LiteralImpl(long value) {
            super(Int64Literal.class);
            this.value = value;
        }

        public long getValue() {
            return value;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class ObjectLiteralImpl<T> extends ExpressionImpl {
        private final T value;

        public ObjectLiteralImpl(Class<?> interfaceType, T value) {
            super(interfaceType);
            this.value = value;
        }

        public T getValue() {
            return value;
        }
    }

    private static class DateTimeLiteralImpl extends ObjectLiteralImpl<Date> implements DateTimeLiteral {
        public DateTimeLiteralImpl(Date value) {
            super(DateTimeLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class DateTimeOffsetLiteralImpl extends ObjectLiteralImpl<DateTimeOffset> implements DateTimeOffsetLiteral {
        public DateTimeOffsetLiteralImpl(DateTimeOffset value) {
            super(DateTimeOffsetLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class TimeLiteralImpl extends ObjectLiteralImpl<Time> implements TimeLiteral {
        public TimeLiteralImpl(Time value) {
            super(TimeLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class StringLiteralImpl extends ObjectLiteralImpl<String> implements StringLiteral {
        public StringLiteralImpl(String value) {
            super(StringLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class GuidLiteralImpl extends ObjectLiteralImpl<Guid> implements GuidLiteral {
        public GuidLiteralImpl(Guid value) {
            super(GuidLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class DecimalLiteralImpl extends ObjectLiteralImpl<BigDecimal> implements DecimalLiteral {
        public DecimalLiteralImpl(BigDecimal value) {
            super(DecimalLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class BinaryLiteralImpl extends ObjectLiteralImpl<byte[]> implements BinaryLiteral {
        public BinaryLiteralImpl(byte[] value) {
            super(BinaryLiteral.class, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class EntitySimplePropertyImpl extends ExpressionImpl implements EntitySimpleProperty {
        private final String propertyName;

        protected EntitySimplePropertyImpl(String propertyName) {
            super(EntitySimpleProperty.class);
            this.propertyName = propertyName;
        }

        public String getName() {
            return propertyName;
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class BinaryCommonExpressionImpl extends ExpressionImpl implements BinaryExpression {
        private final Expression lhs;
        private final Expression rhs;

        public BinaryCommonExpressionImpl(Class<?> interfaceType, Expression lhs, Expression rhs) {
            super(interfaceType);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public Expression getLHS() {
            return lhs;
        }

        public Expression getRHS() {
            return rhs;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getLHS().visit(visitor);
            visitor.betweenDescend();
            getRHS().visit(visitor);
            visitor.afterDescend();
        }
    }

    private static class EqExpressionImpl extends BinaryCommonExpressionImpl implements EqExpression {
        public EqExpressionImpl(Expression lhs, Expression rhs) {
            super(EqExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class NeExpressionImpl extends BinaryCommonExpressionImpl implements NeExpression {
        public NeExpressionImpl(Expression lhs, Expression rhs) {
            super(NeExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class BinaryBoolCommonExpressionImpl extends BinaryCommonExpressionImpl implements BinaryBoolExpression {
        private final BoolExpression lhs;
        private final BoolExpression rhs;

        public BinaryBoolCommonExpressionImpl(Class<?> interfaceType, BoolExpression lhs, BoolExpression rhs) {
            super(interfaceType, lhs, rhs);
            this.lhs = lhs;
            this.rhs = rhs;
        }

        @Override
        public BoolExpression getLHS() {
            return lhs;
        }

        @Override
        public BoolExpression getRHS() {
            return rhs;
        }
    }

    private static class AndExpressionImpl extends BinaryBoolCommonExpressionImpl implements AndExpression {
        public AndExpressionImpl(BoolExpression lhs, BoolExpression rhs) {
            super(AndExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class OrExpressionImpl extends BinaryBoolCommonExpressionImpl implements OrExpression {
        public OrExpressionImpl(BoolExpression lhs, BoolExpression rhs) {
            super(OrExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class LtExpressionImpl extends BinaryCommonExpressionImpl implements LtExpression {
        public LtExpressionImpl(Expression lhs, Expression rhs) {
            super(LtExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class GtExpressionImpl extends BinaryCommonExpressionImpl implements GtExpression {
        public GtExpressionImpl(Expression lhs, Expression rhs) {
            super(GtExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class LeExpressionImpl extends BinaryCommonExpressionImpl implements LeExpression {
        public LeExpressionImpl(Expression lhs, Expression rhs) {
            super(LeExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class GeExpressionImpl extends BinaryCommonExpressionImpl implements GeExpression {
        public GeExpressionImpl(Expression lhs, Expression rhs) {
            super(GeExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class AddExpressionImpl extends BinaryCommonExpressionImpl implements AddExpression {
        public AddExpressionImpl(Expression lhs, Expression rhs) {
            super(AddExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class SubExpressionImpl extends BinaryCommonExpressionImpl implements SubExpression {
        public SubExpressionImpl(Expression lhs, Expression rhs) {
            super(SubExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class MulExpressionImpl extends BinaryCommonExpressionImpl implements MulExpression {
        public MulExpressionImpl(Expression lhs, Expression rhs) {
            super(MulExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class DivExpressionImpl extends BinaryCommonExpressionImpl implements DivExpression {
        public DivExpressionImpl(Expression lhs, Expression rhs) {
            super(DivExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class ModExpressionImpl extends BinaryCommonExpressionImpl implements ModExpression {
        public ModExpressionImpl(Expression lhs, Expression rhs) {
            super(ModExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class ConcatMethodCallExpressionImpl extends BinaryCommonExpressionImpl implements ConcatMethodCallExpression {
        public ConcatMethodCallExpressionImpl(Expression lhs, Expression rhs) {
            super(ConcatMethodCallExpression.class, lhs, rhs);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class OneExpressionImpl extends ExpressionImpl {
        private final Expression expression;

        protected OneExpressionImpl(Class<?> interfaceType, Expression expression) {
            super(interfaceType);
            this.expression = expression;
        }

        public Expression getExpression() {
            return expression;
        }
    }

    private abstract static class UnaryExpressionImpl extends OneExpressionImpl {
        protected UnaryExpressionImpl(Class<?> interfaceType, Expression expression) {
            super(interfaceType, expression);
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getExpression().visit(visitor);
            visitor.afterDescend();
        }
    }

    private static class ParenExpressionImpl extends UnaryExpressionImpl implements ParenExpression {
        protected ParenExpressionImpl(Expression expression) {
            super(ParenExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class BoolParenExpressionImpl extends UnaryExpressionImpl implements BoolParenExpression {
        protected BoolParenExpressionImpl(Expression expression) {
            super(BoolParenExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class NotExpressionImpl extends UnaryExpressionImpl implements NotExpression {
        protected NotExpressionImpl(Expression expression) {
            super(NotExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class NegateExpressionImpl extends UnaryExpressionImpl implements NegateExpression {
        protected NegateExpressionImpl(Expression expression) {
            super(NegateExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class ExpressionAndTypeImpl extends OneExpressionImpl {
        private final String type;

        protected ExpressionAndTypeImpl(Class<?> interfaceType, Expression expression, String type) {
            super(interfaceType, expression);
            this.type = type;
        }

        public String getType() {
            return type;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            if (getExpression() != null) {
                getExpression().visit(visitor);
                visitor.betweenDescend();
            }
            visitor.visit(getType());
            visitor.afterDescend();
        }
    }

    private static class CastExpressionImpl extends ExpressionAndTypeImpl implements CastExpression {
        protected CastExpressionImpl(Expression expression, String type) {
            super(CastExpression.class, expression, type);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class IsofExpressionImpl extends ExpressionAndTypeImpl implements IsofExpression {
        protected IsofExpressionImpl(Expression expression, String type) {
            super(IsofExpression.class, expression, type);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class EndsWithMethodCallExpressionImpl extends TargetValueExpressionImpl implements EndsWithMethodCallExpression {
        protected EndsWithMethodCallExpressionImpl(Expression target, Expression value) {
            super(EndsWithMethodCallExpression.class, target, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class StartsWithMethodCallExpressionImpl extends TargetValueExpressionImpl implements StartsWithMethodCallExpression {
        protected StartsWithMethodCallExpressionImpl(Expression target, Expression value) {
            super(StartsWithMethodCallExpression.class, target, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class SubstringOfMethodCallExpressionImpl extends TargetValueExpressionImpl implements SubstringOfMethodCallExpression {
        protected SubstringOfMethodCallExpressionImpl(Expression target, Expression value) {
            super(SubstringOfMethodCallExpression.class, target, value);
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getValue().visit(visitor);
            if (getTarget() != null) {
                visitor.betweenDescend();
                getTarget().visit(visitor);
            }
            visitor.afterDescend();
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class ReplaceMethodCallExpressionImpl extends TargetExpressionImpl implements ReplaceMethodCallExpression {
        private final Expression find;
        private final Expression replace;

        protected ReplaceMethodCallExpressionImpl(Expression target, Expression find, Expression replace) {
            super(ReplaceMethodCallExpression.class, target);
            this.find = find;
            this.replace = replace;
        }

        public Expression getFind() {
            return find;
        }

        public Expression getReplace() {
            return replace;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getTarget().visit(visitor);
            visitor.betweenDescend();
            getFind().visit(visitor);
            visitor.betweenDescend();
            getReplace().visit(visitor);
            visitor.afterDescend();
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class TargetValueExpressionImpl extends TargetExpressionImpl {
        private final Expression value;

        protected TargetValueExpressionImpl(Class<?> interfaceType, Expression target, Expression value) {
            super(interfaceType, target);
            this.value = value;
        }

        public Expression getValue() {
            return value;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getTarget().visit(visitor);
            visitor.betweenDescend();
            getValue().visit(visitor);
            visitor.afterDescend();
        }
    }

    private static class IndexOfMethodCallExpressionImpl extends TargetValueExpressionImpl implements IndexOfMethodCallExpression {
        protected IndexOfMethodCallExpressionImpl(Expression target, Expression value) {
            super(IndexOfMethodCallExpression.class, target, value);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class TargetExpressionImpl extends ExpressionImpl {
        private final Expression target;

        protected TargetExpressionImpl(Class<?> interfaceType, Expression target) {
            super(interfaceType);
            this.target = target;
        }

        public Expression getTarget() {
            return target;
        }
    }

    private static class SubstringMethodCallExpressionImpl extends TargetExpressionImpl implements SubstringMethodCallExpression {
        private final Expression start;
        private final Expression length;

        protected SubstringMethodCallExpressionImpl(Expression target, Expression start, Expression length) {
            super(SubstringMethodCallExpression.class, target);
            this.start = start;
            this.length = length;
        }

        public Expression getStart() {
            return start;
        }

        public Expression getLength() {
            return length;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getTarget().visit(visitor);
            visitor.betweenDescend();
            getStart().visit(visitor);
            if (getLength() != null) {
                visitor.betweenDescend();
                getLength().visit(visitor);
            }
            visitor.afterDescend();
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class OneTargetExpressionImpl extends OneExpressionImpl {
        protected OneTargetExpressionImpl(Class<?> interfaceType, Expression expression) {
            super(interfaceType, expression);
        }

        public Expression getTarget() {
            return getExpression();
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getTarget().visit(visitor);
            visitor.afterDescend();
        }
    }

    private static class LengthMethodCallExpressionImpl extends OneTargetExpressionImpl implements LengthMethodCallExpression {
        protected LengthMethodCallExpressionImpl(Expression expression) {
            super(LengthMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class FloorMethodCallExpressionImpl extends OneTargetExpressionImpl implements FloorMethodCallExpression {
        protected FloorMethodCallExpressionImpl(Expression expression) {
            super(FloorMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class CeilingMethodCallExpressionImpl extends OneTargetExpressionImpl implements CeilingMethodCallExpression {
        protected CeilingMethodCallExpressionImpl(Expression expression) {
            super(CeilingMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class RoundMethodCallExpressionImpl extends OneTargetExpressionImpl implements RoundMethodCallExpression {
        protected RoundMethodCallExpressionImpl(Expression expression) {
            super(RoundMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class SecondMethodCallExpressionImpl extends OneTargetExpressionImpl implements SecondMethodCallExpression {
        protected SecondMethodCallExpressionImpl(Expression expression) {
            super(SecondMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class MinuteMethodCallExpressionImpl extends OneTargetExpressionImpl implements MinuteMethodCallExpression {
        protected MinuteMethodCallExpressionImpl(Expression expression) {
            super(MinuteMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class HourMethodCallExpressionImpl extends OneTargetExpressionImpl implements HourMethodCallExpression {
        protected HourMethodCallExpressionImpl(Expression expression) {
            super(HourMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class DayMethodCallExpressionImpl extends OneTargetExpressionImpl implements DayMethodCallExpression {
        protected DayMethodCallExpressionImpl(Expression expression) {
            super(DayMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class YearMethodCallExpressionImpl extends OneTargetExpressionImpl implements YearMethodCallExpression {
        protected YearMethodCallExpressionImpl(Expression expression) {
            super(YearMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class MonthMethodCallExpressionImpl extends OneTargetExpressionImpl implements MonthMethodCallExpression {
        protected MonthMethodCallExpressionImpl(Expression expression) {
            super(MonthMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class ToLowerMethodCallExpressionImpl extends OneTargetExpressionImpl implements ToLowerMethodCallExpression {
        protected ToLowerMethodCallExpressionImpl(Expression expression) {
            super(ToLowerMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class ToUpperMethodCallExpressionImpl extends OneTargetExpressionImpl implements ToUpperMethodCallExpression {
        protected ToUpperMethodCallExpressionImpl(Expression expression) {
            super(ToUpperMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class TrimMethodCallExpressionImpl extends OneTargetExpressionImpl implements TrimMethodCallExpression {
        protected TrimMethodCallExpressionImpl(Expression expression) {
            super(LengthMethodCallExpression.class, expression);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private static class OrderByExpressionImpl extends OneExpressionImpl implements OrderByExpression {
        private final Direction direction;

        protected OrderByExpressionImpl(Expression expression, Direction direction) {
            super(OrderByExpression.class, expression);
            this.direction = direction;
        }

        public Direction getDirection() {
            return direction;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getExpression().visit(visitor);
            visitor.betweenDescend();
            visitor.visit(getDirection());
            visitor.afterDescend();
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }
    }

    private abstract static class AggregateBoolFunctionImpl extends ExpressionImpl implements AggregateBoolFunction {
        private final Expression     source;
        private final String               variable;
        private final BoolExpression predicate;

        public AggregateBoolFunctionImpl(Expression source, String variable, BoolExpression predicate) {
            super(AggregateAnyFunction.class);
            this.source = source;
            this.variable = variable;
            this.predicate = predicate;
        }

        public Expression getSource() {
            return source;
        }

        public BoolExpression getPredicate() {
            return predicate;
        }

        @Override
        public void visit(ExpressionVisitor visitor) {
            visitThis(visitor);
            visitor.beforeDescend();
            getSource().visit(visitor);
            visitor.betweenDescend();
            if (getPredicate() != null) {
                getPredicate().visit(visitor);
            }
            visitor.afterDescend();
        }

        public String getVariable() {
            return variable;
        }
    }

    private static class AggregateAnyFunctionImpl extends AggregateBoolFunctionImpl implements AggregateAnyFunction {
        public AggregateAnyFunctionImpl(Expression source, String variable, BoolExpression predicate) {
            super(source, variable, predicate);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        public ExpressionParser.AggregateFunction getFunctionType() {
            return ExpressionParser.AggregateFunction.any;
        }
    }

    private static class AggregateAllFunctionImpl extends AggregateBoolFunctionImpl implements AggregateAllFunction {
        public AggregateAllFunctionImpl(Expression source, String variable, BoolExpression predicate) {
            super(source, variable, predicate);
        }

        @Override
        void visitThis(ExpressionVisitor visitor) {
            visitor.visit(this);
        }

        public ExpressionParser.AggregateFunction getFunctionType() {
            return ExpressionParser.AggregateFunction.all;
        }
    }
}
