package bingo.odata.producer.inmemory;

import java.math.BigDecimal;
import java.util.Set;

import bingo.lang.Enumerator;
import bingo.odata.edm.EdmSimpleType;
import bingo.odata.edm.EdmType;
import bingo.odata.expression.AddExpression;
import bingo.odata.expression.AndExpression;
import bingo.odata.expression.BinaryCommonExpression;
import bingo.odata.expression.BoolCommonExpression;
import bingo.odata.expression.BoolMethodExpression;
import bingo.odata.expression.BoolParenExpression;
import bingo.odata.expression.BooleanLiteral;
import bingo.odata.expression.CastExpression;
import bingo.odata.expression.CommonExpression;
import bingo.odata.expression.ConcatMethodCallExpression;
import bingo.odata.expression.DivExpression;
import bingo.odata.expression.EndsWithMethodCallExpression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.EqExpression;
import bingo.odata.expression.Expression;
import bingo.odata.expression.GeExpression;
import bingo.odata.expression.GtExpression;
import bingo.odata.expression.IndexOfMethodCallExpression;
import bingo.odata.expression.LeExpression;
import bingo.odata.expression.LengthMethodCallExpression;
import bingo.odata.expression.LiteralExpression;
import bingo.odata.expression.LtExpression;
import bingo.odata.expression.ModExpression;
import bingo.odata.expression.MulExpression;
import bingo.odata.expression.NeExpression;
import bingo.odata.expression.NotExpression;
import bingo.odata.expression.OrExpression;
import bingo.odata.expression.ParenExpression;
import bingo.odata.expression.ReplaceMethodCallExpression;
import bingo.odata.expression.StartsWithMethodCallExpression;
import bingo.odata.expression.SubExpression;
import bingo.odata.expression.SubstringMethodCallExpression;
import bingo.odata.expression.SubstringOfMethodCallExpression;
import bingo.odata.expression.ToLowerMethodCallExpression;
import bingo.odata.expression.ToUpperMethodCallExpression;
import bingo.odata.expression.TrimMethodCallExpression;
import bingo.odata.zinternal.TypeConverter;

public class InMemoryEvaluation {

    public static Object evaluate(CommonExpression expression, Object target, PropertyModel properties) {
        if (expression instanceof LiteralExpression)
            return Expression.literalValue((LiteralExpression) expression);

        if (expression instanceof EntitySimpleProperty)
            return properties.getPropertyValue(target, ((EntitySimpleProperty) expression).getPropertyName());

        if (expression instanceof BoolCommonExpression)
            return evaluate((BoolCommonExpression) expression, target, properties);

        if (expression instanceof AddExpression)
            return binaryFunction((BinaryCommonExpression) expression, target, properties, BinaryFunction.ADD);

        if (expression instanceof SubExpression)
            return binaryFunction((BinaryCommonExpression) expression, target, properties, BinaryFunction.SUB);

        if (expression instanceof MulExpression)
            return binaryFunction((BinaryCommonExpression) expression, target, properties, BinaryFunction.MUL);

        if (expression instanceof DivExpression)
            return binaryFunction((BinaryCommonExpression) expression, target, properties, BinaryFunction.DIV);

        if (expression instanceof ModExpression)
            return binaryFunction((BinaryCommonExpression) expression, target, properties, BinaryFunction.MOD);

        if (expression instanceof ParenExpression)
            return evaluate(((ParenExpression) expression).getExpression(), target, properties);

        if (expression instanceof BoolParenExpression)
            return evaluate(((BoolParenExpression) expression).getExpression(), target, properties);

        if (expression instanceof CastExpression) {
            CastExpression castExpression = (CastExpression) expression;
            EdmSimpleType<?> t = EdmType.getSimple(castExpression.getType());
            if (t == null)
                throw new UnsupportedOperationException("Only simple types supported");
            Class<?> javaType = t.getJavaTypes().iterator().next();
            return TypeConverter.convert(evaluate(castExpression.getExpression(), target, properties), javaType);
        }

        if (expression instanceof ToLowerMethodCallExpression) {
            ToLowerMethodCallExpression e = (ToLowerMethodCallExpression) expression;
            String value = evaluateToString(e.getTarget(), target, properties);
            return value == null ? null : value.toLowerCase();
        }

        if (expression instanceof ToUpperMethodCallExpression) {
            ToUpperMethodCallExpression e = (ToUpperMethodCallExpression) expression;
            String value = evaluateToString(e.getTarget(), target, properties);
            return value == null ? null : value.toUpperCase();
        }

        if (expression instanceof SubstringMethodCallExpression) {
            SubstringMethodCallExpression e = (SubstringMethodCallExpression) expression;
            String value = evaluateToString(e.getTarget(), target, properties);
            if (value == null || e.getStart() == null)
                return value;

            int start = (Integer) evaluate(e.getStart(), target, properties);

            if (e.getLength() == null) {
                return value.substring(start);
            }

            int length = (Integer) evaluate(e.getLength(), target, properties);
            return length == 0 ? "" : value.substring(start, start + length);
        }

        if (expression instanceof IndexOfMethodCallExpression) {
            IndexOfMethodCallExpression e = (IndexOfMethodCallExpression) expression;
            String text = evaluateToString(e.getTarget(), target, properties);
            String search = evaluateToString(e.getValue(), target, properties);
            return text.indexOf(search);
        }

        if (expression instanceof ReplaceMethodCallExpression) {
            ReplaceMethodCallExpression e = (ReplaceMethodCallExpression) expression;
            String text = evaluateToString(e.getTarget(), target, properties);
            String find = evaluateToString(e.getFind(), target, properties);
            String replace = evaluateToString(e.getReplace(), target, properties);
            return text.replace(find, replace);
        }

        if (expression instanceof ConcatMethodCallExpression) {
            ConcatMethodCallExpression e = (ConcatMethodCallExpression) expression;
            String left = evaluateToString(e.getLHS(), target, properties);
            String right = evaluateToString(e.getRHS(), target, properties);
            return left + right;
        }

        if (expression instanceof TrimMethodCallExpression) {
            TrimMethodCallExpression e = (TrimMethodCallExpression) expression;
            String left = evaluateToString(e.getTarget(), target, properties);
            return left == null ? null : left.trim();
        }

        if (expression instanceof LengthMethodCallExpression) {
            LengthMethodCallExpression e = (LengthMethodCallExpression) expression;
            String left = evaluateToString(e.getTarget(), target, properties);
            return left == null ? 0 : left.length();
        }

        throw new UnsupportedOperationException("unsupported expression " + expression);
    }

    private static String evaluateToString(CommonExpression expression, Object target, PropertyModel properties) {
        Object value = evaluate(expression, target, properties);
        if (value == null)
            return null;
        if (value instanceof String)
            return ((String) value);
        return String.valueOf(value);
    }

    public static boolean evaluate(BoolCommonExpression expression, Object target, PropertyModel properties) {
        if (expression instanceof EqExpression) {
            return equals((EqExpression) expression, target, properties);
        }
        if (expression instanceof NeExpression) {
            return !equals((NeExpression) expression, target, properties);
        }
        if (expression instanceof AndExpression) {
            AndExpression e = (AndExpression) expression;
            return evaluate(e.getLHS(), target, properties) && evaluate(e.getRHS(), target, properties);
        }
        if (expression instanceof OrExpression) {
            OrExpression e = (OrExpression) expression;
            return evaluate(e.getLHS(), target, properties) || evaluate(e.getRHS(), target, properties);
        }
        if (expression instanceof BooleanLiteral) {
            return ((BooleanLiteral) expression).getValue();
        }

        if (expression instanceof GtExpression) {
            return compareTo((GtExpression) expression, target, properties) > 0;
        }
        if (expression instanceof LtExpression) {
            return compareTo((LtExpression) expression, target, properties) < 0;
        }
        if (expression instanceof GeExpression) {
            return compareTo((GeExpression) expression, target, properties) >= 0;
        }
        if (expression instanceof LeExpression) {
            return compareTo((LeExpression) expression, target, properties) <= 0;
        }

        if (expression instanceof NotExpression) {
            NotExpression e = (NotExpression) expression;
            Boolean rt = (Boolean) evaluate(e.getExpression(), target, properties);
            return !rt;
        }
        if (expression instanceof BoolMethodExpression) {
            return evaluate((BoolMethodExpression) expression, target, properties);
        }
        if (expression instanceof ParenExpression) {
            @SuppressWarnings("unused")
            Object obj = null;
        }
        if (expression instanceof BoolParenExpression) {
            BoolParenExpression e = (BoolParenExpression) expression;
            return evaluate((BoolCommonExpression) e.getExpression(), target, properties);
        }

        //Let's try to evaluate generic expressions in hope it evaluates to bool
        Object o = evaluate((CommonExpression) expression, target, properties);
        if (o instanceof Boolean) {
            return (Boolean) o;
        }

        throw new UnsupportedOperationException("unsupported expression " + expression);
    }

    private static boolean evaluate(BoolMethodExpression expression, Object target, PropertyModel properties) {
        String targetValue = (String) evaluate(expression.getTarget(), target, properties);
        String searchValue = (String) evaluate(expression.getValue(), target, properties);

        if (targetValue == null || searchValue == null) {
            return false;
        }
        if (expression instanceof SubstringOfMethodCallExpression) {
            return targetValue.contains(searchValue);
        }
        if (expression instanceof StartsWithMethodCallExpression) {
            return targetValue.startsWith(searchValue);
        }
        if (expression instanceof EndsWithMethodCallExpression) {
            return targetValue.endsWith(searchValue);
        }

        throw new UnsupportedOperationException("unsupported expression " + expression);
    }

    private static interface BinaryFunction {
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

    private static class ObjectPair {
        public Object lhs;
        public Object rhs;

        public ObjectPair(CommonExpression lhs, CommonExpression rhs, Object target, PropertyModel properties) {
            this(evaluate(lhs, target, properties), evaluate(rhs, target, properties));
        }

        public ObjectPair(Object lhs, Object rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
    }

    private static Object binaryFunction(BinaryCommonExpression be, Object target, PropertyModel properties, BinaryFunction function) {
        ObjectPair pair = new ObjectPair(be.getLHS(), be.getRHS(), target, properties);
        binaryNumericPromotion(pair);

        // * Edm.Decimal
        // * Edm.Double
        // * Edm.Single
        // * Edm.Int32
        // * Edm.Int64

        if (pair.lhs instanceof BigDecimal)
            return function.apply((BigDecimal) pair.lhs, (BigDecimal) pair.rhs);
        if (pair.lhs instanceof Double)
            return function.apply((Double) pair.lhs, (Double) pair.rhs);
        if (pair.lhs instanceof Float)
            return function.apply((Float) pair.lhs, (Float) pair.rhs);
        if (pair.lhs instanceof Integer)
            return function.apply((Integer) pair.lhs, (Integer) pair.rhs);
        if (pair.lhs instanceof Long)
            return function.apply((Long) pair.lhs, (Long) pair.rhs);

        throw new UnsupportedOperationException("unsupported add type " + pair.lhs);
    }

    private static boolean equals(BinaryCommonExpression be, Object target, PropertyModel properties) {
        ObjectPair pair = new ObjectPair(be.getLHS(), be.getRHS(), target, properties);
        binaryNumericPromotion(pair);
        return (pair.lhs == null ? pair.rhs == null : pair.lhs.equals(pair.rhs));
    }

    @SuppressWarnings( { "unchecked"})
    private static int compareTo(BinaryCommonExpression be, Object target, PropertyModel properties) {
        ObjectPair pair = new ObjectPair(be.getLHS(), be.getRHS(), target, properties);
        binaryNumericPromotion(pair);
        return ((Comparable) pair.lhs).compareTo(((Comparable) pair.rhs));
    }

    @SuppressWarnings( { "unchecked"})
    private static final Set<Class> SUPPORTED_CLASSES_FOR_BINARY_PROMOTION = Enumerator.create(BigDecimal.class, Double.class, Float.class, Byte.class, Integer.class, Short.class, Long.class).cast(
                                                                                   Class.class).toSet();

    @SuppressWarnings("unchecked")
    private static void binaryNumericPromotion(ObjectPair pair) {

        // * Edm.Decimal
        // * Edm.Double
        // * Edm.Single
        // * Edm.Byte
        // * Edm.Int16
        // * Edm.Int32
        // * Edm.Int64

        if (pair.lhs == null || pair.rhs == null)
            return;
        Class<?> lhsClass = pair.lhs.getClass();
        Class<?> rhsClass = pair.rhs.getClass();
        if (lhsClass.equals(rhsClass))
            return;
        if (!SUPPORTED_CLASSES_FOR_BINARY_PROMOTION.contains(lhsClass) || !SUPPORTED_CLASSES_FOR_BINARY_PROMOTION.contains(rhsClass))
            return;

        // If supported, binary numeric promotion SHOULD consist of the
        // application of the following rules in the order specified:

        // * If either operand is of type Edm.Decimal, the other operand is
        // converted to Edm.Decimal unless it is of type Edm.Single or
        // Edm.Double.
        if (lhsClass.equals(BigDecimal.class) && Enumerator.create(Byte.class, Short.class, Integer.class, Long.class).cast(Class.class).contains(rhsClass))
            pair.rhs = BigDecimal.valueOf(((Number) pair.rhs).longValue());
        else if (rhsClass.equals(BigDecimal.class) && Enumerator.create(Byte.class, Short.class, Integer.class, Long.class).cast(Class.class).contains(lhsClass))
            pair.lhs = BigDecimal.valueOf(((Number) pair.lhs).longValue());

        // * Otherwise, if either operand is Edm.Double, the other operand is
        // converted to type Edm.Double.
        else if (lhsClass.equals(Double.class))
            pair.rhs = ((Number) pair.rhs).doubleValue();
        else if (rhsClass.equals(Double.class))
            pair.lhs = ((Number) pair.lhs).doubleValue();

        // * Otherwise, if either operand is Edm.Single, the other operand is
        // converted to type Edm.Single.
        else if (lhsClass.equals(Float.class))
            pair.rhs = ((Number) pair.rhs).floatValue();
        else if (rhsClass.equals(Float.class))
            pair.lhs = ((Number) pair.lhs).floatValue();

        // * Otherwise, if either operand is Edm.Int64, the other operand is
        // converted to type Edm.Int64.
        else if (lhsClass.equals(Long.class))
            pair.rhs = ((Number) pair.rhs).longValue();
        else if (rhsClass.equals(Long.class))
            pair.lhs = ((Number) pair.lhs).longValue();

        // * Otherwise, if either operand is Edm.Int32, the other operand is
        // converted to type Edm.Int32
        else if (lhsClass.equals(Integer.class))
            pair.rhs = ((Number) pair.rhs).intValue();
        else if (rhsClass.equals(Integer.class))
            pair.lhs = ((Number) pair.lhs).intValue();

        // * Otherwise, if either operand is Edm.Int16, the other operand is
        // converted to type Edm.Int16.
        else if (lhsClass.equals(Short.class))
            pair.rhs = ((Number) pair.rhs).shortValue();
        else if (rhsClass.equals(Short.class))
            pair.lhs = ((Number) pair.lhs).shortValue();

        // * If binary numeric promotion is supported, a data service SHOULD use
        // a castExpression to promote an operand to the target type.

    }

    public static Object cast(Object obj, Class<?> targetType) {
        if (obj == null)
            return null;
        Class<?> objClass = obj.getClass();
        if (targetType.isAssignableFrom(objClass))
            return obj;

        if ((obj instanceof Number) && targetType.equals(Double.class))
            return ((Double) obj).doubleValue();
        if ((obj instanceof Number) && targetType.equals(Float.class))
            return ((Number) obj).floatValue();
        if ((obj instanceof Number) && targetType.equals(Long.class))
            return ((Number) obj).longValue();
        if ((obj instanceof Number) && targetType.equals(Integer.class))
            return ((Number) obj).intValue();
        if ((obj instanceof Number) && targetType.equals(Short.class))
            return ((Number) obj).shortValue();
        if ((obj instanceof Number) && targetType.equals(Byte.class))
            return ((Number) obj).byteValue();

        if (objClass.equals(Integer.class) && targetType.equals(Integer.TYPE))
            return obj;

        throw new UnsupportedOperationException("Unable to cast a " + objClass.getSimpleName() + " to a " + targetType.getSimpleName());
    }

}
