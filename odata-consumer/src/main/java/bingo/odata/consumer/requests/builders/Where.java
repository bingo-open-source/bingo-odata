package bingo.odata.consumer.requests.builders;

import bingo.lang.Strings;

public class Where {
	enum Pattern {
		data1OpData2, opData1, data1, opData1Data2, opData2Data1
	}
	String left;
	String op;
	String right;
	Pattern p = Pattern.data1OpData2;
	
	boolean closed = false;
	
	public Where() {
	}
	public Where(Where w, boolean withQuotes) {
		this(quoted(w));
	}
	public Where(Where w) {
		w.closed = true;
		inner(w.toString(), null, null);
	}
	public Where(String w) {
		inner(w, null, null);
	}
	public Where(Integer w) {
		inner(w + "", null, null);
	}
	
	public Where and(Where w) {
		w.closed = true;
		String left = this.toString();
		String right = w.toString();
		p = Pattern.data1OpData2;
		return inner(left, "and", right);
	}
	
	public Where or(Where w) {
		w.closed = true;
		String left = this.toString();
		String right = w.toString();
		p = Pattern.data1OpData2;
		return inner(left, "or", right);
	}
	
	public Where gt(Where w) {
		return gt(w.toString());
	}
	public Where gt(double w) {
		return inner(null, "gt", w + "");
	}
	public Where gt(int w) {
		p = Pattern.data1OpData2;
		return inner(null, "gt", w + "");
	}
	public Where gt(String w) {
		return inner(null, "gt", quoted(w));
	}
	
	public Where eq(Where w) {
		return eq(w.toString());
	}
	public Where eq(int w) {
		return inner(null, "eq", w + "");
	}
	public Where eq(double w) {
		return inner(null, "eq", w + "");
	}
	public Where eq(String w) {
		return inner(null, "eq", quoted(w));
	}
	
	public Where contain(String w) {
		p = Pattern.opData2Data1;
		return inner(null, "substringof", quoted(w));
	}
	
	public Where not(Where w) {
		w.closed = true;
		return not(w.toString());
	}
	public Where not(String w) {
		p = Pattern.opData1;
		return inner(w, "not", null);
	}
	
	public Where endsWith(String w) {
		p = Pattern.opData1Data2;
		return inner(null, "endswith", quoted(w));
	}
	
	public Where startsWith(String w) {
		p = Pattern.opData1Data2;
		return inner(null, "startswith", quoted(w));
	}
	
	private Where inner(String left, String op, String right) {
		if(null != left) this.left = left;
		if(null != op) this.op = op;
		if(null != right) this.right = right;
		return this;
	}
	
	private static String quoted(Object obj) {
		return "'" + obj + "'";
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		switch (this.p) {
		case data1OpData2:
			str.append(left);
			if(!Strings.isBlank(op)) str.append(" ").append(op);
			if(!Strings.isBlank(right)) str.append(" ").append(right);
			break;
		case opData1Data2:
			str.append(op).append("(").append(left).append(", ").append(right).append(")");
			break;
		case opData2Data1:
			str.append(op).append("(").append(right).append(", ").append(left).append(")");
			break;
		case opData1:
			str.append(op).append(" ").append(left);
			break;
		default:
			break;
		}
		if(closed) str.insert(0, "(").append(")");
		return str.toString();
	}
}
