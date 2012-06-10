package odata.producer;

import java.math.BigDecimal;
import java.util.Set;

import odata.edm.EdmSimpleType;
import odata.edm.EdmType;
import odata.expression.AddExpression;
import odata.expression.AndExpression;
import odata.expression.BinaryCommonExpression;
import odata.expression.BoolCommonExpression;
import odata.expression.BoolParenExpression;
import odata.expression.BooleanLiteral;
import odata.expression.CastExpression;
import odata.expression.CommonExpression;
import odata.expression.DivExpression;
import odata.expression.EntitySimpleProperty;
import odata.expression.EqExpression;
import odata.expression.Expression;
import odata.expression.GeExpression;
import odata.expression.GtExpression;
import odata.expression.LeExpression;
import odata.expression.LiteralExpression;
import odata.expression.LtExpression;
import odata.expression.ModExpression;
import odata.expression.MulExpression;
import odata.expression.NeExpression;
import odata.expression.NotExpression;
import odata.expression.OrExpression;
import odata.expression.ParenExpression;
import odata.expression.SubExpression;
import odata.expression.SubstringOfMethodCallExpression;
import odata.zinternal.TypeConverter;

import bingo.lang.enumerable.IteratedEnumerable;

/**
 * Evaluate an $filter expression.  The VariableResolver you attach to the
 * evaluator determines the context for evaluation.  The VariableResolver must
 * supply actual values for all EntitySimpleProperties referenced in the $filter.
 *
 * <p>Note: this used to be class InMemoryEvaluation, I just factored out/de-coupled the
 * VariableResolver to make it reusable.
 */
public class ExpressionEvaluator {

    /** Resolves variables during expression evaluation. */
    public interface VariableResolver {
        Object resolveVariable(String path);
    }

    private VariableResolver resolver = null;

    public ExpressionEvaluator(VariableResolver resolver) {
        this.resolver = resolver;
    }

    public Object evaluate(CommonExpression expression) {

        if (expression instanceof LiteralExpression) {
            return Expression.literalValue((LiteralExpression) expression);
        }

        if (expression instanceof BoolCommonExpression) {
            return evaluate((BoolCommonExpression) expression);
        }

        if (expression instanceof EntitySimpleProperty) {
            return resolver.resolveVariable(((EntitySimpleProperty) expression).getPropertyName());
        }

        if (expression instanceof AddExpression) {
            return binaryFunction((BinaryCommonExpression) expression, BinaryFunction.ADD);
        }

        if (expression instanceof SubExpression) {
            return binaryFunction((BinaryCommonExpression) expression, BinaryFunction.SUB);
        }

        if (expression instanceof MulExpression) {
            return binaryFunction((BinaryCommonExpression) expression, BinaryFunction.MUL);
        }

        if (expression instanceof DivExpression) {
            return binaryFunction((BinaryCommonExpression) expression, BinaryFunction.DIV);
        }

        if (expression instanceof ModExpression) {
            return binaryFunction((BinaryCommonExpression) expression, BinaryFunction.MOD);
        }

        if (expression instanceof ParenExpression) {
            return evaluate(((ParenExpression) expression).getExpression());
        }

        if (expression instanceof BoolParenExpression) {
            return evaluate(((BoolParenExpression) expression).getExpression());
        }

        if (expression instanceof CastExpression) {
            CastExpression castExpression = (CastExpression) expression;
            EdmSimpleType<?> t = EdmType.getSimple(castExpression.getType());
            if (t == null) {
                throw new UnsupportedOperationException("Only simple types supported");
            }
            Class<?> javaType = t.getJavaTypes().iterator().next();
            return TypeConverter.convert(evaluate(castExpression.getExpression()), javaType);
        }

        throw new UnsupportedOperationException("unsupported expression " + expression);
    }

    public boolean evaluate(BoolCommonExpression expression) {
        if (expression instanceof EqExpression) {
            return equals((EqExpression) expression);
        }
        if (expression instanceof NeExpression) {
            return !equals((NeExpression) expression);
        }
        if (expression instanceof AndExpression) {
            AndExpression e = (AndExpression) expression;
            return evaluate(e.getLHS()) && evaluate(e.getRHS());
        }
        if (expression instanceof OrExpression) {
            OrExpression e = (OrExpression) expression;
            return evaluate(e.getLHS()) || evaluate(e.getRHS());
        }
        if (expression instanceof BooleanLiteral) {
            return ((BooleanLiteral) expression).getValue();
        }

        if (expression instanceof GtExpression) {
            return compareTo((GtExpression) expression) > 0;
        }
        if (expression instanceof LtExpression) {
            return compareTo((LtExpression) expression) < 0;
        }
        if (expression instanceof GeExpression) {
            return compareTo((GeExpression) expression) >= 0;
        }
        if (expression instanceof LeExpression) {
            return compareTo((LeExpression) expression) <= 0;
        }

        if (expression instanceof NotExpression) {
            NotExpression e = (NotExpression) expression;
            Boolean rt = (Boolean) evaluate(e.getExpression());
            return !rt;
        }
        if (expression instanceof SubstringOfMethodCallExpression) {
            SubstringOfMethodCallExpression e = (SubstringOfMethodCallExpression) expression;
            String targetValue = (String) evaluate(e.getTarget());
            String searchValue = (String) evaluate(e.getValue());
            return targetValue != null && searchValue != null && targetValue.contains(searchValue);
        }
        if (expression instanceof ParenExpression) {
            @SuppressWarnings("unused")
            Object obj = null;
        }
        if (expression instanceof BoolParenExpression) {
            BoolParenExpression e = (BoolParenExpression) expression;
            return evaluate((BoolCommonExpression) e.getExpression());
        }

        throw new UnsupportedOperationException("unsupported expression " + expression);
    }

    private interface BinaryFunction {

        BigDecimal apply(BigDecimal lhs, BigDecimal rhs);

        Double apply(Double lhs, Double rhs);

        Float apply(Float lhs, Float rhs);

        Integer apply(Integer lhs, Integer rhs);

        Long apply(Long lhs, Long rhs);

        public static final BinaryFunction ADD = new BinaryFunction() {

                                                   public BigDecimal apply(BigDecimal lhs, BigDecimal rhs) {
                                                       return lhs.add(rhs);
                                                   }

                                                   public Double apply(Double lhs, Double rhs) {
                                                       return lhs + rhs;
                                                   }

                                                   public Float apply(Float lhs, Float rhs) {
                                                       return lhs + rhs;
                                                   }

                                                   public Integer apply(Integer lhs, Integer rhs) {
                                                       return lhs + rhs;
                                                   }

                                                   public Long apply(Long lhs, Long rhs) {
                                                       return lhs + rhs;
                                                   }
                                               };

        public static final BinaryFunction SUB = new BinaryFunction() {

                                                   public BigDecimal apply(BigDecimal lhs, BigDecimal rhs) {
                                                       return lhs.subtract(rhs);
                                                   }

                                                   public Double apply(Double lhs, Double rhs) {
                                                       return lhs - rhs;
                                                   }

                                                   public Float apply(Float lhs, Float rhs) {
                                                       return lhs - rhs;
                                                   }

                                                   public Integer apply(Integer lhs, Integer rhs) {
                                                       return lhs - rhs;
                                                   }

                                                   public Long apply(Long lhs, Long rhs) {
                                                       return lhs - rhs;
                                                   }
                                               };

        public static final BinaryFunction MUL = new BinaryFunction() {

                                                   public BigDecimal apply(BigDecimal lhs, BigDecimal rhs) {
                                                       return lhs.multiply(rhs);
                                                   }

                                                   public Double apply(Double lhs, Double rhs) {
                                                       return lhs * rhs;
                                                   }

                                                   public Float apply(Float lhs, Float rhs) {
                                                       return lhs * rhs;
                                                   }

                                                   public Integer apply(Integer lhs, Integer rhs) {
                                                       return lhs * rhs;
                                                   }

                                                   public Long apply(Long lhs, Long rhs) {
                                                       return lhs * rhs;
                                                   }
                                               };

        public static final BinaryFunction DIV = new BinaryFunction() {

                                                   public BigDecimal apply(BigDecimal lhs, BigDecimal rhs) {
                                                       return lhs.divide(rhs);
                                                   }

                                                   public Double apply(Double lhs, Double rhs) {
                                                       return lhs / rhs;
                                                   }

                                                   public Float apply(Float lhs, Float rhs) {
                                                       return lhs / rhs;
                                                   }

                                                   public Integer apply(Integer lhs, Integer rhs) {
                                                       return lhs / rhs;
                                                   }

                                                   public Long apply(Long lhs, Long rhs) {
                                                       return lhs / rhs;
                                                   }
                                               };

        public static final BinaryFunction MOD = new BinaryFunction() {

                                                   public BigDecimal apply(BigDecimal lhs, BigDecimal rhs) {
                                                       return lhs.remainder(rhs);
                                                   }

                                                   public Double apply(Double lhs, Double rhs) {
                                                       return lhs % rhs;
                                                   }

                                                   public Float apply(Float lhs, Float rhs) {
                                                       return lhs % rhs;
                                                   }

                                                   public Integer apply(Integer lhs, Integer rhs) {
                                                       return lhs % rhs;
                                                   }

                                                   public Long apply(Long lhs, Long rhs) {
                                                       return lhs % rhs;
                                                   }
                                               };

    }

    private class ObjectPair {

        public Object lhs;
        public Object rhs;

        public ObjectPair(CommonExpression lhs, CommonExpression rhs) {
            this(evaluate(lhs), evaluate(rhs));
        }

        public ObjectPair(Object lhs, Object rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

    }

    private Object binaryFunction(BinaryCommonExpression be, BinaryFunction function) {
        ObjectPair pair = new ObjectPair(be.getLHS(), be.getRHS());
        binaryNumericPromotion(pair);

        // * Edm.Decimal
        // * Edm.Double
        // * Edm.Single
        // * Edm.Int32
        // * Edm.Int64

        if (pair.lhs instanceof BigDecimal) {
            return function.apply((BigDecimal) pair.lhs, (BigDecimal) pair.rhs);
        }
        if (pair.lhs instanceof Double) {
            return function.apply((Double) pair.lhs, (Double) pair.rhs);
        }
        if (pair.lhs instanceof Float) {
            return function.apply((Float) pair.lhs, (Float) pair.rhs);
        }
        if (pair.lhs instanceof Integer) {
            return function.apply((Integer) pair.lhs, (Integer) pair.rhs);
        }
        if (pair.lhs instanceof Long) {
            return function.apply((Long) pair.lhs, (Long) pair.rhs);
        }

        throw new UnsupportedOperationException("unsupported add type " + pair.lhs);
    }

    private boolean equals(BinaryCommonExpression be) {
        ObjectPair pair = new ObjectPair(be.getLHS(), be.getRHS());
        binaryNumericPromotion(pair);
        return (pair.lhs == null ? pair.rhs == null : pair.lhs.equals(pair.rhs));
    }

    @SuppressWarnings( { "unchecked"})
    private int compareTo(BinaryCommonExpression be) {
        ObjectPair pair = new ObjectPair(be.getLHS(), be.getRHS());
        binaryNumericPromotion(pair);
        return ((Comparable) pair.lhs).compareTo(((Comparable) pair.rhs));
    }

    @SuppressWarnings( { "unchecked"})
    private static final Set<Class> SUPPORTED_CLASSES_FOR_BINARY_PROMOTION = IteratedEnumerable.of(BigDecimal.class, Double.class, Float.class, Byte.class, Integer.class, Short.class, Long.class).cast(
                                                                                   Class.class).toSet();

    @SuppressWarnings("unchecked")
    private void binaryNumericPromotion(ObjectPair pair) {

        // * Edm.Decimal
        // * Edm.Double
        // * Edm.Single
        // * Edm.Byte
        // * Edm.Int16
        // * Edm.Int32
        // * Edm.Int64

        if (pair.lhs == null || pair.rhs == null) {
            return;
        }
        Class<?> lhsClass = pair.lhs.getClass();
        Class<?> rhsClass = pair.rhs.getClass();
        if (lhsClass.equals(rhsClass)) {
            return;
        }
        if (!SUPPORTED_CLASSES_FOR_BINARY_PROMOTION.contains(lhsClass) || !SUPPORTED_CLASSES_FOR_BINARY_PROMOTION.contains(rhsClass)) {
            return;
        }

        // If supported, binary numeric promotion SHOULD consist of the
        // application of the following rules in the order specified:

        // * If either operand is of type Edm.Decimal, the other operand is
        // converted to Edm.Decimal unless it is of type Edm.Single or
        // Edm.Double.
        if (lhsClass.equals(BigDecimal.class) && IteratedEnumerable.of(Byte.class, Short.class, Integer.class, Long.class).cast(Class.class).contains(rhsClass)) {
            pair.rhs = BigDecimal.valueOf(((Number) pair.rhs).longValue());
        } else if (rhsClass.equals(BigDecimal.class) && IteratedEnumerable.of(Byte.class, Short.class, Integer.class, Long.class).cast(Class.class).contains(lhsClass)) {
            pair.lhs = BigDecimal.valueOf(((Number) pair.lhs).longValue());
        } // * Otherwise, if either operand is Edm.Double, the other operand is
        // converted to type Edm.Double.
        else if (lhsClass.equals(Double.class)) {
            pair.rhs = ((Number) pair.rhs).doubleValue();
        } else if (rhsClass.equals(Double.class)) {
            pair.lhs = ((Number) pair.lhs).doubleValue();
        } // * Otherwise, if either operand is Edm.Single, the other operand is
        // converted to type Edm.Single.
        else if (lhsClass.equals(Float.class)) {
            pair.rhs = ((Number) pair.rhs).floatValue();
        } else if (rhsClass.equals(Float.class)) {
            pair.lhs = ((Number) pair.lhs).floatValue();
        } // * Otherwise, if either operand is Edm.Int64, the other operand is
        // converted to type Edm.Int64.
        else if (lhsClass.equals(Long.class)) {
            pair.rhs = ((Number) pair.rhs).longValue();
        } else if (rhsClass.equals(Long.class)) {
            pair.lhs = ((Number) pair.lhs).longValue();
        } // * Otherwise, if either operand is Edm.Int32, the other operand is
        // converted to type Edm.Int32
        else if (lhsClass.equals(Integer.class)) {
            pair.rhs = ((Number) pair.rhs).intValue();
        } else if (rhsClass.equals(Integer.class)) {
            pair.lhs = ((Number) pair.lhs).intValue();
        } // * Otherwise, if either operand is Edm.Int16, the other operand is
        // converted to type Edm.Int16.
        else if (lhsClass.equals(Short.class)) {
            pair.rhs = ((Number) pair.rhs).shortValue();
        } else if (rhsClass.equals(Short.class)) {
            pair.lhs = ((Number) pair.lhs).shortValue();
        }

        // * If binary numeric promotion is supported, a data service SHOULD use
        // a castExpression to promote an operand to the target type.

    }

    public static Object cast(Object obj, Class<?> targetType) {
        if (obj == null) {
            return null;
        }
        Class<?> objClass = obj.getClass();
        if (targetType.isAssignableFrom(objClass)) {
            return obj;
        }

        if ((obj instanceof Number) && targetType.equals(Double.class)) {
            return ((Double) obj).doubleValue();
        }
        if ((obj instanceof Number) && targetType.equals(Float.class)) {
            return ((Number) obj).floatValue();
        }
        if ((obj instanceof Number) && targetType.equals(Long.class)) {
            return ((Number) obj).longValue();
        }
        if ((obj instanceof Number) && targetType.equals(Integer.class)) {
            return ((Number) obj).intValue();
        }
        if ((obj instanceof Number) && targetType.equals(Short.class)) {
            return ((Number) obj).shortValue();
        }
        if ((obj instanceof Number) && targetType.equals(Byte.class)) {
            return ((Number) obj).byteValue();
        }

        if (objClass.equals(Integer.class) && targetType.equals(Integer.TYPE)) {
            return obj;
        }

        throw new UnsupportedOperationException("Unable to cast a " + objClass.getSimpleName() + " to a " + targetType.getSimpleName());
    }

}