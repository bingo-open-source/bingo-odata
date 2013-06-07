/**
 * created at 2013-2-22
 */
package bingo.odata.producer.sql;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import bingo.lang.Strings;
import bingo.odata.expression.AddExpression;
import bingo.odata.expression.AndExpression;
import bingo.odata.expression.BoolLiteral;
import bingo.odata.expression.ByteLiteral;
import bingo.odata.expression.DateTimeLiteral;
import bingo.odata.expression.DateTimeOffsetLiteral;
import bingo.odata.expression.DecimalLiteral;
import bingo.odata.expression.DivExpression;
import bingo.odata.expression.DoubleLiteral;
import bingo.odata.expression.EndsWithMethodCallExpression;
import bingo.odata.expression.EntitySimpleProperty;
import bingo.odata.expression.EqExpression;
import bingo.odata.expression.Expression;
import bingo.odata.expression.ExpressionVisitorBase;
import bingo.odata.expression.GeExpression;
import bingo.odata.expression.GtExpression;
import bingo.odata.expression.GuidLiteral;
import bingo.odata.expression.Int64Literal;
import bingo.odata.expression.IntegerLiteral;
import bingo.odata.expression.LeExpression;
import bingo.odata.expression.LtExpression;
import bingo.odata.expression.ModExpression;
import bingo.odata.expression.MulExpression;
import bingo.odata.expression.NeExpression;
import bingo.odata.expression.NegateExpression;
import bingo.odata.expression.NotExpression;
import bingo.odata.expression.NullLiteral;
import bingo.odata.expression.OrExpression;
import bingo.odata.expression.SByteLiteral;
import bingo.odata.expression.SingleLiteral;
import bingo.odata.expression.StartsWithMethodCallExpression;
import bingo.odata.expression.StringLiteral;
import bingo.odata.expression.SubExpression;
import bingo.odata.expression.SubstringOfMethodCallExpression;
import bingo.odata.expression.TimeLiteral;

public class SqlExpressionVisitor extends ExpressionVisitorBase {
	
	private final SqlMapping EMPTY_MAPPING = new SqlMapping() {
		public String column(String property) {
			return property;
		}
	};

	private final StringBuilder sb	        = new StringBuilder();
	private final Stack<String>	stack       = new Stack<String>();
	private final List<Object>  paramValues = new ArrayList<Object>();
	private final List<Integer> paramTypes  = new ArrayList<Integer>();
	private final SqlMapping    mapping;
	
	public SqlExpressionVisitor(){
		this.mapping = EMPTY_MAPPING;
	}
	
	public SqlExpressionVisitor(SqlMapping mapping){
		this.mapping = null == mapping ? EMPTY_MAPPING : mapping;
	}
	
	public String getText(){
		return sb.toString();
	}
	
	public List<Object> getParamValues() {
		return paramValues;
	}

	public List<Integer> getParamTypes() {
		return paramTypes;
	}

	private void param(Object value, int type) {
		sb.append("?");
		paramValues.add(value);
		paramTypes.add(type);
	}

	public void openParen() {
		sb.append("(");
	}

	public void closeParen() {
		sb.append(")");
	}

	@Override
	public void betweenDescend() {
		if (!stack.isEmpty()) {
			sb.append(stack.pop());
		}
	}

	@Override
	public boolean visit(BoolLiteral expr) {
		sb.append(expr.getValue() ? "true" : "false");
		return true;
	}

	@Override
	public boolean visit(DateTimeLiteral expr) {
		param(expr.getValue(), Types.TIMESTAMP);
		return true;
	}

	@Override
	public boolean visit(DateTimeOffsetLiteral expr) {
		param(expr.getValue().getTimestamp(), Types.TIMESTAMP);
		return true;
	}

	@Override
	public boolean visit(EntitySimpleProperty expr) {
		sb.append(mapping.column(expr.getName()));
		return true;
	}

	@Override
	public boolean visit(DecimalLiteral expr) {
		param(expr.getValue(), Types.DECIMAL);
		return true;
	}

	@Override
	public boolean visit(AddExpression expr) {
		stack.add(" + ");
		return true;
	}

	@Override
	public boolean visit(DivExpression expr) {
		stack.add(" / ");
		return true;
	}

	@Override
	public boolean visit(EqExpression expr) {
		if(expr.getRHS() instanceof NullLiteral){
			stack.add(" is ");
		}else{
			stack.add(" = ");	
		}
		return true;
	}

	@Override
	public boolean visit(GeExpression expr) {
		stack.add(" >= ");
		return true;
	}

	@Override
	public boolean visit(GtExpression expr) {
		stack.add(" > ");
		return true;
	}

	@Override
	public boolean visit(AndExpression expr) {
		stack.add(" AND ");
		return true;
	}

	@Override
	public boolean visit(GuidLiteral expr) {
		param(expr.getValue().toString(), Types.VARCHAR);
		return true;
	}

	@Override
	public boolean visit(ByteLiteral expr) {
		param(expr.getValue().byteValue(), Types.DECIMAL);//TODO : 
		return true;
	}

	@Override
	public boolean visit(SByteLiteral expr) {
		super.visit(expr);
		return true;
	}

	@Override
	public boolean visit(SingleLiteral expr) {
		param(expr.getValue(), Types.FLOAT);
		return true;
	}

	@Override
	public boolean visit(DoubleLiteral expr) {
		param(expr.getValue(), Types.DOUBLE);
		return true;
	}

	@Override
	public boolean visit(IntegerLiteral expr) {
		param(expr.getValue(), Types.INTEGER);
		return true;
	}

	@Override
	public boolean visit(Int64Literal expr) {
		param(expr.getValue(), Types.BIGINT);
		return true;
	}

	@Override
	public boolean visit(LeExpression expr) {
		stack.add(" <= ");
		return true;
	}

	@Override
	public boolean visit(LtExpression expr) {
		stack.add(" < ");
		return true;
	}

	@Override
	public boolean visit(ModExpression expr) {
		stack.add(" % ");
		return true;
	}

	@Override
	public boolean visit(MulExpression expr) {
		stack.add(" * ");
		return true;
	}

	@Override
	public boolean visit(NeExpression expr) {
		stack.add(" != ");
		return true;
	}

	@Override
	public boolean visit(NegateExpression expr) {
		sb.append("-");
		return true;
	}

	@Override
	public boolean visit(SubExpression expr) {
		stack.add(" - ");
		return true;
	}

	@Override
	public boolean visit(NotExpression expr) {
		stack.add(" NOT ");
		return true;
	}

	@Override
	public boolean visit(NullLiteral expr) {
		sb.append("null");
		return true;
	}

	@Override
	public boolean visit(OrExpression expr) {
		stack.add(" OR ");
		return true;
	}

	@Override
	public boolean visit(StringLiteral expr) {
		param(expr.getValue(), Types.VARCHAR);
		return true;
	}

	@Override
	public boolean visit(TimeLiteral expr) {
		param(expr.getValue(), Types.TIME);
		return true;
	}

	@Override
	public boolean visit(EndsWithMethodCallExpression expr) {
		checkAndBuildLikeExpression(expr.getTarget(),expr.getValue(),"%{0}");
		return false;
	}

	@Override
	public boolean visit(StartsWithMethodCallExpression expr) {
		checkAndBuildLikeExpression(expr.getTarget(),expr.getValue(),"{0}%");
		return false;
	}

	@Override
	public boolean visit(SubstringOfMethodCallExpression expr) {
		checkAndBuildLikeExpression(expr.getTarget(),expr.getValue(),"%{0}%");
		return false;
	}
	
	private void checkAndBuildLikeExpression(Expression p1,Expression p2,String valueTemplate){
		Expression target = p1 instanceof EntitySimpleProperty ? p1 : p2;
		Expression value  = p1 instanceof StringLiteral ? p1 : p2;
		
		if (!(target instanceof EntitySimpleProperty)) {
			throw new UnsupportedOperationException("substringof only supports property target");
		}

		if (!(value instanceof StringLiteral)) {
			throw new UnsupportedOperationException("substringof only supports string value");
		}
		
		sb.append(((EntitySimpleProperty) target).getName()).append(" LIKE ?");
		paramValues.add(Strings.format(valueTemplate,((StringLiteral) value).getValue()));
		paramTypes.add(Types.VARCHAR);
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
