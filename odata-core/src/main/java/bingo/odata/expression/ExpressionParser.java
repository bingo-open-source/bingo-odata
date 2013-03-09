package bingo.odata.expression;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import bingo.lang.Enumerables;
import bingo.lang.Func1;
import bingo.lang.Func2;
import bingo.lang.codec.Hex;
import bingo.lang.enumerable.IterableEnumerable;
import bingo.lang.time.DateFormats;
import bingo.lang.value.Guid;
import bingo.odata.expression.OrderByExpression.Direction;
import bingo.odata.utils.InternalTypeUtils;

public class ExpressionParser {

    private static class Methods {

        public static final String CAST        = "cast";
        public static final String ISOF        = "isof";
        public static final String ENDSWITH    = "endswith";
        public static final String STARTSWITH  = "startswith";
        public static final String SUBSTRINGOF = "substringof";
        public static final String INDEXOF     = "indexof";
        public static final String REPLACE     = "replace";
        public static final String TOLOWER     = "tolower";
        public static final String TOUPPER     = "toupper";
        public static final String TRIM        = "trim";
        public static final String SUBSTRING   = "substring";
        public static final String CONCAT      = "concat";
        public static final String LENGTH      = "length";
        public static final String YEAR        = "year";
        public static final String MONTH       = "month";
        public static final String DAY         = "day";
        public static final String HOUR        = "hour";
        public static final String MINUTE      = "minute";
        public static final String SECOND      = "second";
        public static final String ROUND       = "round";
        public static final String FLOOR       = "floor";
        public static final String CEILING     = "ceiling";
    }

    private static Set<String> METHODS = Enumerables.of(
                                               	   Methods.CAST,
                                                   Methods.ISOF,
                                                   Methods.ENDSWITH,
                                                   Methods.STARTSWITH,
                                                   Methods.SUBSTRINGOF,
                                                   Methods.INDEXOF,
                                                   Methods.REPLACE,
                                                   Methods.TOLOWER,
                                                   Methods.TOUPPER,
                                                   Methods.TRIM,
                                                   Methods.SUBSTRING,
                                                   Methods.CONCAT,
                                                   Methods.LENGTH,
                                                   Methods.YEAR,
                                                   Methods.MONTH,
                                                   Methods.DAY,
                                                   Methods.HOUR,
                                                   Methods.MINUTE,
                                                   Methods.SECOND,
                                                   Methods.ROUND,
                                                   Methods.FLOOR,
                                                   Methods.CEILING).toSet();

    public enum AggregateFunction {
        none, any, all;
    };

	public static final DateFormat	TIME_FORMATTER	     = DateFormats.getFormat("HH:mm:ss");
	public static boolean	            DUMP_EXPRESSION_INFO = false;

    public static Expression parse(String value) {
        List<Token> tokens = tokenize(value);
        // dump(value,tokens,null);

        Expression rt = readExpression(tokens);
        if (DUMP_EXPRESSION_INFO) {
            dump(value, tokens, rt);
        }

        return rt;
    }

    public static List<OrderByExpression> parseOrderBy(String value) {
        List<Token> tokens = tokenize(value);
        // dump(value,tokens,null);

        List<Expression> expressions = readExpressions(tokens);
        if (DUMP_EXPRESSION_INFO) {
            dump(value, tokens, IterableEnumerable.of(expressions).toArray(Expression.class));
        }

        return IterableEnumerable.of(expressions).select(new Func1<Expression, OrderByExpression>() {
            public OrderByExpression apply(Expression input) {
                if (input instanceof OrderByExpression) {
                    return (OrderByExpression) input;
                }
                return Expressions.orderBy(input, Direction.ASCENDING); // default to asc
            }
        }).toList();
    }

    private static void dump(String value, List<Token> tokens, Expression... expressions) {
        String msg = "[" + value + "] -> " + IterableEnumerable.of(tokens).join("");
        if (expressions != null) {
            msg = msg + " -> " + IterableEnumerable.of(expressions).select(new Func1<Expression, String>() {
                public String apply(Expression input) {
                    return Expressions.asPrintString(input);
                }
            }).join(",");
        }
        System.out.println(msg);
    }

    public static List<EntitySimpleProperty> parseExpand(String value) {
        List<Token> tokens = tokenize(value);
        // dump(value,tokens,null);

        List<Expression> expressions = readExpressions(tokens);

        //  since we support currently simple properties only we have to
        //  confine ourselves to EntitySimpleProperties.
        return IterableEnumerable.of(expressions).select(new Func1<Expression, EntitySimpleProperty>() {
            public EntitySimpleProperty apply(Expression input) {
                if (input instanceof EntitySimpleProperty)
                    return (EntitySimpleProperty) input;
                return null;
            }
        }).toList();
    }

    private static Expression processBinaryExpression(List<Token> tokens, String op, Func2<Expression, Expression, Expression> fn) {

        int ts = tokens.size();
        for (int i = 0; i < ts; i++) {
            Token t = tokens.get(i);
            if (i < ts - 2) {
                if (t.type == TokenType.WHITESPACE && tokens.get(i + 2).type == TokenType.WHITESPACE && tokens.get(i + 1).type == TokenType.WORD) {
                    Token wordToken = tokens.get(i + 1);
                    if (wordToken.value.equals(op)) {
                        Expression lhs = readExpression(tokens.subList(0, i));
                        Expression rhs = readExpression(tokens.subList(i + 3, ts));
                        return fn.evaluate(lhs, rhs);
                    }
                }
            }
        }
        return null;
    }

    private static Expression processUnaryExpression(List<Token> tokens, String op, boolean whitespaceRequired, Func1<Expression, Expression> fn) {

        int ts = tokens.size();
        for (int i = 0; i < ts; i++) {
            Token t = tokens.get(i);
            if (i < ts - 1) {
                if ((t.type == TokenType.WORD || t.type == TokenType.SYMBOL) && (!whitespaceRequired || tokens.get(i + 1).type == TokenType.WHITESPACE)) {
                    Token wordToken = t;
                    if (wordToken.value.equals(op)) {
                        Expression expression = readExpression(tokens.subList(i + (whitespaceRequired ? 2 : 1), ts));
                        return fn.apply(expression);
                    }
                }
            }
        }
        return null;
    }

    private static Expression methodCall(String methodName, List<Expression> methodArguments) {
        if (methodName.equals(Methods.CAST) && methodArguments.size() == 1) {
            Expression arg = methodArguments.get(0);
            assertType(arg, StringLiteral.class);
            String type = ((StringLiteral) arg).getValue();
            return Expressions.cast(type);
        } else if (methodName.equals(Methods.CAST) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            assertType(arg2, StringLiteral.class);
            String type = ((StringLiteral) arg2).getValue();
            return Expressions.cast(arg1, type);
        } else if (methodName.equals(Methods.ISOF) && methodArguments.size() == 1) {
            Expression arg = methodArguments.get(0);
            assertType(arg, StringLiteral.class);
            String type = ((StringLiteral) arg).getValue();
            return Expressions.isof(type);
        } else if (methodName.equals(Methods.ISOF) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            assertType(arg2, StringLiteral.class);
            String type = ((StringLiteral) arg2).getValue();
            return Expressions.isof(arg1, type);
        } else if (methodName.equals(Methods.ENDSWITH) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            return Expressions.endsWith(arg1, arg2);
        } else if (methodName.equals(Methods.STARTSWITH) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            return Expressions.startsWith(arg1, arg2);
        } else if (methodName.equals(Methods.SUBSTRINGOF) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.substringOf(arg1);
        } else if (methodName.equals(Methods.SUBSTRINGOF) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            return Expressions.substringOf(arg1, arg2);
        } else if (methodName.equals(Methods.INDEXOF) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            return Expressions.indexOf(arg1, arg2);
        } else if (methodName.equals(Methods.REPLACE) && methodArguments.size() == 3) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            Expression arg3 = methodArguments.get(2);
            return Expressions.replace(arg1, arg2, arg3);
        } else if (methodName.equals(Methods.TOLOWER) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.toLower(arg1);
        } else if (methodName.equals(Methods.TOUPPER) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.toUpper(arg1);
        } else if (methodName.equals(Methods.TRIM) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.trim(arg1);
        } else if (methodName.equals(Methods.SUBSTRING) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            return Expressions.substring(arg1, arg2);
        } else if (methodName.equals(Methods.SUBSTRING) && methodArguments.size() == 3) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            Expression arg3 = methodArguments.get(2);
            return Expressions.substring(arg1, arg2, arg3);
        } else if (methodName.equals(Methods.CONCAT) && methodArguments.size() == 2) {
            Expression arg1 = methodArguments.get(0);
            Expression arg2 = methodArguments.get(1);
            return Expressions.concat(arg1, arg2);
        } else if (methodName.equals(Methods.LENGTH) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.length(arg1);
        } else if (methodName.equals(Methods.YEAR) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.year(arg1);
        } else if (methodName.equals(Methods.MONTH) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.month(arg1);
        } else if (methodName.equals(Methods.DAY) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.day(arg1);
        } else if (methodName.equals(Methods.HOUR) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.hour(arg1);
        } else if (methodName.equals(Methods.MINUTE) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.minute(arg1);
        } else if (methodName.equals(Methods.SECOND) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.second(arg1);
        } else if (methodName.equals(Methods.CEILING) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.ceiling(arg1);
        } else if (methodName.equals(Methods.FLOOR) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.floor(arg1);
        } else if (methodName.equals(Methods.ROUND) && methodArguments.size() == 1) {
            Expression arg1 = methodArguments.get(0);
            return Expressions.round(arg1);
        } else {
            throw new RuntimeException("Implement method " + methodName);
        }
    }

    private static List<Expression> readExpressions(List<Token> tokens) {
        List<Expression> rt = new ArrayList<Expression>();

        int stack = 0;
        int start = 0;
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.type == TokenType.OPENPAREN) {
                stack++;
            } else if (token.type == TokenType.CLOSEPAREN) {
                stack--;
            } else if (stack == 0 && token.type == TokenType.SYMBOL && token.value.equals(",")) {
                List<Token> tokensInsideComma = tokens.subList(start, i);
                Expression expressionInsideComma = readExpression(tokensInsideComma);
                rt.add(expressionInsideComma);
                start = i + 1;
            } else if (i == tokens.size() - 1) {
                List<Token> tokensInside = tokens.subList(start, i + 1);
                Expression expressionInside = readExpression(tokensInside);
                rt.add(expressionInside);
            }

        }
        return rt;
    }

    public static List<Token> processParentheses(List<Token> tokens) {

        List<Token> rt = new ArrayList<Token>();

        for (int i = 0; i < tokens.size(); i++) {
            Token openToken = tokens.get(i);
            if (openToken.type == TokenType.OPENPAREN) {
                int afterParenIdx = i + 1;
                // is this a method call or any/all aggregate function?
                String methodName = null;
                String aggregateSource = null;
                String aggregateVariable = null;
                AggregateFunction aggregateFunction = AggregateFunction.none;
                int k = i - 1;
                while (k > 0 && tokens.get(k).type == TokenType.WHITESPACE) {
                    k--;
                }
                if (k >= 0) {
                    Token methodNameToken = tokens.get(k);
                    if (methodNameToken.type == TokenType.WORD) {
                        if (METHODS.contains(methodNameToken.value)) {
                            methodName = methodNameToken.value;

                            // this isn't strictly correct.  I think the parser has issues
                            // with sequences of WORD, WHITESPACE, WORD, etc.  I'm not sure I've
                            // ever seen a token type of WHITESPACE producer by a lexer..
                        } else if (methodNameToken.value.endsWith("/any") || methodNameToken.value.endsWith("/all")) {
                            aggregateSource = methodNameToken.value.substring(0, methodNameToken.value.length() - 4);
                            aggregateFunction = Enum.valueOf(AggregateFunction.class, methodNameToken.value.substring(methodNameToken.value.length() - 3));
                            // to get things rolling I'm going to lookahead and require a very strict
                            // sequence of tokens:
                            // i + 1 must be a WORD
                            // i + 2 must be a SYMBOL ':'
                            // or, for any, i + 1 can be CLOSEPAREN
                            int ni = i + 1;
                            Token ntoken = ni < tokens.size() ? tokens.get(ni) : null;
                            if (ntoken == null || (aggregateFunction == AggregateFunction.all && ntoken.type != TokenType.WORD)
                                    || (aggregateFunction == AggregateFunction.any && ntoken.type != TokenType.WORD && ntoken.type != TokenType.CLOSEPAREN)) {
                                throw new RuntimeException("unexpected token: " + (ntoken == null ? "eof" : ntoken.toString()));
                            }
                            if (ntoken.type == TokenType.WORD) {
                                aggregateVariable = ntoken.value;
                                ni += 1;
                                ntoken = ni < tokens.size() ? tokens.get(ni) : null;
                                if (ntoken == null || ntoken.type != TokenType.SYMBOL || !ntoken.value.equals(":")) {
                                    throw new RuntimeException("expected ':', found: " + (ntoken == null ? "eof" : ntoken.toString()));
                                }
                                // now we can parse the predicate, starting after the ':'
                                afterParenIdx = ni + 1;
                            } else {
                                // any(), easiest to early out here
                                List<Token> tokensIncludingParens = tokens.subList(k, ni + 1);
                                Expression any = Expressions.any(Expressions.simpleProperty(aggregateSource));

                                ExpressionToken et = new ExpressionToken(any, tokensIncludingParens);
                                rt.subList(rt.size() - (i - k), rt.size()).clear();
                                rt.add(et);
                                return rt;
                            }

                        }
                    }
                }

                // find matching close paren
                int stack = 0;
                int start = i;
                List<Expression> methodArguments = new ArrayList<Expression>();
                for (int j = afterParenIdx; j < tokens.size(); j++) {
                    Token closeToken = tokens.get(j);
                    if (closeToken.type == TokenType.OPENPAREN) {
                        stack++;
                    } else if (methodName != null && stack == 0 && closeToken.type == TokenType.SYMBOL && closeToken.value.equals(",")) {
                        List<Token> tokensInsideComma = tokens.subList(start + 1, j);
                        Expression expressionInsideComma = readExpression(tokensInsideComma);
                        methodArguments.add(expressionInsideComma);
                        start = j;
                    } else if (closeToken.type == TokenType.CLOSEPAREN) {
                        if (stack > 0) {
                            stack--;
                            continue;
                        }

                        if (methodName != null) {
                            List<Token> tokensIncludingParens = tokens.subList(k, j + 1);
                            List<Token> tokensInsideParens = tokens.subList(start + 1, j);
                            Expression expressionInsideParens = readExpression(tokensInsideParens);
                            methodArguments.add(expressionInsideParens);

                            // method call expression: replace t mn ( t t , t t ) t with t et t
                            Expression methodCall = methodCall(methodName, methodArguments);

                            ExpressionToken et = new ExpressionToken(methodCall, tokensIncludingParens);
                            rt.subList(rt.size() - (i - k), rt.size()).clear();
                            rt.add(et);

                        } else if (aggregateVariable != null) {
                            List<Token> tokensIncludingParens = tokens.subList(k, j + 1);
                            List<Token> tokensInsideParens = tokens.subList(afterParenIdx, j);
                            Expression expressionInsideParens = readExpression(tokensInsideParens);
                            if (!(expressionInsideParens instanceof BoolExpression)) {
                                throw new RuntimeException("illegal any predicate");
                            }
                            Expression any = Expressions
                                    .aggregate(aggregateFunction, Expressions.simpleProperty(aggregateSource), aggregateVariable, (BoolExpression) expressionInsideParens);

                            ExpressionToken et = new ExpressionToken(any, tokensIncludingParens);
                            rt.subList(rt.size() - (i - k), rt.size()).clear();
                            rt.add(et);
                        } else {

                            List<Token> tokensIncludingParens = tokens.subList(i, j + 1);
                            List<Token> tokensInsideParens = tokens.subList(i + 1, j);
                            // paren expression: replace t ( t t t ) t with t et t
                            Expression expressionInsideParens = readExpression(tokensInsideParens);
                            Expression exp = null;
                            if (expressionInsideParens instanceof BoolExpression) {
                                exp = Expressions.boolParen(expressionInsideParens);
                            } else {
                                exp = Expressions.paren(expressionInsideParens);
                            }

                            ExpressionToken et = new ExpressionToken(exp, tokensIncludingParens);
                            rt.add(et);
                        }

                        i = j;
                    }
                }
            } else {
                rt.add(openToken);
            }
        }

        return rt;

    }

    private static <T extends Expression> void assertType(Expression expression, Class<T> type) {
        if (!type.isAssignableFrom(expression.getClass())) {
            throw new RuntimeException("Expected " + type.getSimpleName());
        }
    }

    private static Expression readExpression(List<Token> tokens) {

        Expression rt = null;

        tokens = trimWhitespace(tokens);

        // OrderBy asc, desc
        Token lastToken = tokens.get(tokens.size() - 1);
        if (lastToken.type == TokenType.WORD && (lastToken.value.equals("asc") || lastToken.value.equals("desc"))) {
            return Expressions.orderBy(readExpression(tokens.subList(0, tokens.size() - 1)), lastToken.value.equals("asc") ? Direction.ASCENDING : Direction.DESCENDING);
        }

        // Grouping (highest precedence)
        tokens = processParentheses(tokens);

        // now we have a list of tokens with no explicit parens

        // process literals

        // literals with prefixes
        if (tokens.size() == 2 && tokens.get(0).type == TokenType.WORD && tokens.get(1).type == TokenType.QUOTED_STRING) {
            String word = tokens.get(0).value;
            String value = unquote(tokens.get(1).value);
            
            if (word.equals("datetime")) {
                Date dt = InternalTypeUtils.parseDateTime(value);
                return Expressions.dateTime(dt);
            } else if (word.equals("time")) {
                return Expressions.time(InternalTypeUtils.parseTime(value));
            } else if (word.equals("datetimeoffset")) {
                return Expressions.dateTimeOffset(InternalTypeUtils.parseDateTimeOffset(value));
            } else if (word.equals("guid")) {
                // odata: dddddddd-dddd-dddd-dddddddddddd
                // java: dddddddd-dd-dd-dddd-dddddddddddd
                // value = value.substring(0, 11) + "-" + value.substring(11);
                return Expressions.guid(Guid.fromString(value));
            } else if (word.equals("decimal")) {
                return Expressions.decimal(new BigDecimal(value));
            } else if (word.equals("X") || word.equals("binary")) {
                byte[] bValue = Hex.decode(value.toCharArray());
                return Expressions.binary(bValue);
            }
        }
        
        // long literal: 1234L
        if (tokens.size() == 2 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.WORD && tokens.get(1).value.equals("L")) {
            long longValue = Long.parseLong(tokens.get(0).value);
            return Expressions.int64(longValue);
        }
        // single literal: 2f
        if (tokens.size() == 2 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.WORD && tokens.get(1).value.equals("f")) {
            float floatValue = Float.parseFloat(tokens.get(0).value);
            return Expressions.single(floatValue);
        }
        // single literal: 2.0f
        if (tokens.size() == 4 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.SYMBOL && tokens.get(1).value.equals(".") && tokens.get(2).type == TokenType.NUMBER
                && tokens.get(3).value.equals("f")) {
            float floatValue = Float.parseFloat(tokens.get(0).value + "." + tokens.get(2).value);
            return Expressions.single(floatValue);
        }
        // double literal: 2.0
        if (tokens.size() == 3 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.SYMBOL && tokens.get(1).value.equals(".") && tokens.get(2).type == TokenType.NUMBER) {
            double doubleValue = Double.parseDouble(tokens.get(0).value + "." + tokens.get(2).value);
            return Expressions.double_(doubleValue);
        }
        // double literal: 1E+10
        if (tokens.size() == 4 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.WORD && tokens.get(1).value.equals("E") && tokens.get(2).type == TokenType.SYMBOL
                && tokens.get(2).value.equals("+") && tokens.get(3).type == TokenType.NUMBER) {
            double doubleValue = Double.parseDouble(tokens.get(0).value + "E+" + tokens.get(3).value);
            return Expressions.double_(doubleValue);
        }
        // double literal: 1E-10
        if (tokens.size() == 3 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.WORD && tokens.get(1).value.equals("E") && tokens.get(2).type == TokenType.NUMBER) {
            int e = Integer.parseInt(tokens.get(2).value);
            if (e < 1) {
                double doubleValue = Double.parseDouble(tokens.get(0).value + "E" + tokens.get(2).value);
                return Expressions.double_(doubleValue);
            }
        }
        // double literal: 1.2E+10
        if (tokens.size() == 6 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.SYMBOL && tokens.get(1).value.equals(".") && tokens.get(2).type == TokenType.NUMBER
                && tokens.get(3).type == TokenType.WORD && tokens.get(3).value.equals("E") && tokens.get(4).type == TokenType.SYMBOL && tokens.get(4).value.equals("+")
                && tokens.get(5).type == TokenType.NUMBER) {
            double doubleValue = Double.parseDouble(tokens.get(0).value + "." + tokens.get(2).value + "E+" + tokens.get(5).value);
            return Expressions.double_(doubleValue);
        }
        // double literal: 1.2E-10
        if (tokens.size() == 5 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.SYMBOL && tokens.get(1).value.equals(".") && tokens.get(2).type == TokenType.NUMBER
                && tokens.get(3).type == TokenType.WORD && tokens.get(3).value.equals("E") && tokens.get(4).type == TokenType.NUMBER) {
            int e = Integer.parseInt(tokens.get(4).value);
            if (e < 1) {
                double doubleValue = Double.parseDouble(tokens.get(0).value + "." + tokens.get(2).value + "E" + tokens.get(4).value);
                return Expressions.double_(doubleValue);
            }
        }
        // decimal literal: 1234M or 1234m
        if (tokens.size() == 2 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.WORD && tokens.get(1).value.equalsIgnoreCase("M")) {
            BigDecimal decimalValue = new BigDecimal(tokens.get(0).value);
            return Expressions.decimal(decimalValue);
        }
        // decimal literal: 2.0m
        if (tokens.size() == 4 && tokens.get(0).type == TokenType.NUMBER && tokens.get(1).type == TokenType.SYMBOL && tokens.get(1).value.equals(".") && tokens.get(2).type == TokenType.NUMBER
                && tokens.get(3).value.equalsIgnoreCase("m")) {
            BigDecimal decimalValue = new BigDecimal(tokens.get(0).value + "." + tokens.get(2).value);
            return Expressions.decimal(decimalValue);
        }
        // TODO literals: byteLiteral, sbyteliteral

        // single token expression
        if (tokens.size() == 1) {
            final Token token = tokens.get(0);
            if (token.type == TokenType.QUOTED_STRING) {
                return Expressions.string(unquote(token.value));
            } else if (token.type == TokenType.WORD) {
                if (token.value.equals("null")) {
                    return Expressions.null_();
                }
                if (token.value.equals("true")) {
                    return Expressions.boolean_(true);
                }
                if (token.value.equals("false")) {
                    return Expressions.boolean_(false);
                }
                return Expressions.simpleProperty(token.value);
            } else if (token.type == TokenType.NUMBER) {
                try {
                    int value = Integer.parseInt(token.value);
                    return Expressions.integral(value);
                } catch (NumberFormatException e) {
                    long value = Long.parseLong(token.value);
                    return Expressions.int64(value);
                }
            } else if (token.type == TokenType.EXPRESSION) {
                return ((ExpressionToken) token).expression;
            } else {
                throw new RuntimeException("Unexpected");
            }
        }

        // process operators from least to highest precedence

        // Conditional OR: or
        rt = processBinaryExpression(tokens, "or", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                assertType(lhs, BoolExpression.class);
                assertType(rhs, BoolExpression.class);
                return Expressions.or((BoolExpression) lhs, (BoolExpression) rhs);
            }
        });
        if (rt != null) {
            return rt;
        }

        // Conditional AND: and
        rt = processBinaryExpression(tokens, "and", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                assertType(lhs, BoolExpression.class);
                assertType(rhs, BoolExpression.class);
                return Expressions.and((BoolExpression) lhs, (BoolExpression) rhs);
            }
        });
        if (rt != null) {
            return rt;
        }

        // Equality: eq ne
        rt = processBinaryExpression(tokens, "eq", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.eq(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "ne", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.ne(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }

        // Relational and type testing: lt, gt, le, ge, isof(T) , isof(x,T)
        rt = processBinaryExpression(tokens, "lt", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.lt(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "gt", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.gt(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "le", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.le(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "ge", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.ge(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }

        // Additive: add, sub
        rt = processBinaryExpression(tokens, "add", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.add(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "sub", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.sub(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }

        // Multiplicative: mul, div, mod
        rt = processBinaryExpression(tokens, "mul", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.mul(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "div", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.div(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processBinaryExpression(tokens, "mod", new Func2<Expression, Expression, Expression>() {

            public Expression evaluate(Expression lhs, Expression rhs) {
                return Expressions.mod(lhs, rhs);
            }
        });
        if (rt != null) {
            return rt;
        }

        // Unary: not x, -x, cast(T), cast(x,T)
        rt = processUnaryExpression(tokens, "not", true, new Func1<Expression, Expression>() {

            public Expression apply(Expression expression) {
                return Expressions.not(expression);
            }
        });
        if (rt != null) {
            return rt;
        }
        rt = processUnaryExpression(tokens, "-", false, new Func1<Expression, Expression>() {

            public Expression apply(Expression expression) {
                return Expressions.negate(expression);
            }
        });
        if (rt != null) {
            return rt;
        }

        throw new RuntimeException("Unable to read expression with tokens: " + tokens);

    }

    private static String unquote(String singleQuotedValue) {
        return singleQuotedValue.substring(1, singleQuotedValue.length() - 1).replace("''", "'");
    }

    private static List<Token> trimWhitespace(List<Token> tokens) {
        int start = 0;
        while (tokens.get(start).type == TokenType.WHITESPACE) {
            start++;
        }
        int end = tokens.size() - 1;
        while (tokens.get(end).type == TokenType.WHITESPACE) {
            end--;
        }
        return tokens.subList(start, end + 1);

    }

    // tokenizer
    public static List<Token> tokenize(String value) {
        List<Token> rt = new ArrayList<Token>();
        int current = 0;
        int end = 0;

        while (true) {
            if (current == value.length()) {
                return rt;
            }
            char c = value.charAt(current);
            if (Character.isWhitespace(c)) {
                end = readWhitespace(value, current);
                rt.add(new Token(TokenType.WHITESPACE, value.substring(current, end)));
                current = end;
            } else if (c == '\'') {
                end = readQuotedString(value, current + 1);
                rt.add(new Token(TokenType.QUOTED_STRING, value.substring(current, end)));
                current = end;
            } else if (Character.isLetter(c)) {
                end = readWord(value, current + 1);
                rt.add(new Token(TokenType.WORD, value.substring(current, end)));
                current = end;
            } else if (Character.isDigit(c)) {
                end = readDigits(value, current + 1);
                rt.add(new Token(TokenType.NUMBER, value.substring(current, end)));
                current = end;
            } else if (c == '(') {
                rt.add(new Token(TokenType.OPENPAREN, Character.toString(c)));
                current++;
            } else if (c == ')') {
                rt.add(new Token(TokenType.CLOSEPAREN, Character.toString(c)));
                current++;
            } else if (c == '-') {
                if (Character.isDigit(value.charAt(current + 1))) {
                    end = readDigits(value, current + 1);
                    rt.add(new Token(TokenType.NUMBER, value.substring(current, end)));
                    current = end;
                } else {
                    rt.add(new Token(TokenType.SYMBOL, Character.toString(c)));
                    current++;
                }
            } else if (",.+=:".indexOf(c) > -1) {
                rt.add(new Token(TokenType.SYMBOL, Character.toString(c)));
                current++;
            } else {
                dumpTokens(rt);
                throw new RuntimeException("Unable to tokenize: " + value + " current: " + current + " rem: " + value.substring(current));
            }
        }

    }

    public static void dumpTokens(List<Token> tokens) {
        for (Token t : tokens) {
            System.out.println(t.type.toString() + t.toString());
        }
    }

    private static int readDigits(String value, int start) {
        int rt = start;
        while (rt < value.length() && Character.isDigit(value.charAt(rt))) {
            rt++;
        }
        return rt;
    }

    private static int readWord(String value, int start) {
        int rt = start;
        while (rt < value.length() && (Character.isLetterOrDigit(value.charAt(rt)) || value.charAt(rt) == '/' || value.charAt(rt) == '_')) {
            rt++;
        }
        return rt;
    }

    private static int readQuotedString(String value, int start) {
        int rt = start;
        while (value.charAt(rt) != '\'' || (rt < value.length() - 1 && value.charAt(rt + 1) == '\'')) {
            if (value.charAt(rt) != '\'') {
                rt++;
            } else {
                rt += 2;
            }
        }
        rt++;
        return rt;
    }

    private static int readWhitespace(String value, int start) {
        int rt = start;
        while (rt < value.length() && Character.isWhitespace(value.charAt(rt))) {
            rt++;
        }
        return rt;
    }

    public static enum TokenType {
        UNKNOWN, WHITESPACE, QUOTED_STRING, WORD, SYMBOL, NUMBER, OPENPAREN, CLOSEPAREN, EXPRESSION;
    }

    public static class Token {

        public final TokenType type;
        public final String    value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        @Override
        public String toString() {
            return "[" + value + "]";
        }
    }

    private static class ExpressionToken extends Token {

        public final Expression expression;
        private final List<Token>     tokens;

        public ExpressionToken(Expression expression, List<Token> tokens) {
            super(TokenType.EXPRESSION, null);
            this.expression = expression;
            this.tokens = tokens;
        }

        @Override
        public String toString() {
            return IterableEnumerable.of(tokens).join("");
        }
    }
}
