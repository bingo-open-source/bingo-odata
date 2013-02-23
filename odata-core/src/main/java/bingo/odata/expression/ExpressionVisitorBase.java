/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package bingo.odata.expression;

import bingo.odata.expression.OrderByExpression.Direction;

public abstract class ExpressionVisitorBase implements ExpressionVisitor {

	public void beforeDescend() {
		throw new UnsupportedOperationException("visit beforeDescend");
	}

	public void afterDescend() {
		throw new UnsupportedOperationException("visit afterDescend");
    }

	public void betweenDescend() {
		throw new UnsupportedOperationException("visit betweenDescend");
    }

	public void visit(String type) {
		throw new UnsupportedOperationException("visit String type");
	}

	public void visit(OrderByExpression expr) {
		throw new UnsupportedOperationException("visit OrderByExpression");
    }

	public void visit(Direction direction) {
		throw new UnsupportedOperationException("visit Direction");	    
    }

	public void visit(AddExpression expr) {
		throw new UnsupportedOperationException("visit AddExpression");	    
    }

	public void visit(AndExpression expr) {
		throw new UnsupportedOperationException("visit AndExpression");
    }

	public void visit(BoolLiteral expr) {
		throw new UnsupportedOperationException("visit BoolLiteral");	    
    }

	public void visit(CastExpression expr) {
		throw new UnsupportedOperationException("visit CastExpression");	    
    }

	public void visit(ConcatMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ConcatMethodCallExpression");	    
    }

	public void visit(DateTimeLiteral expr) {
		throw new UnsupportedOperationException("visit DateTimeLiteral");	    
    }

	public void visit(DateTimeOffsetLiteral expr) {
		throw new UnsupportedOperationException("visit DateTimeOffsetLiteral");	    
    }

	public void visit(DecimalLiteral expr) {
		throw new UnsupportedOperationException("visit DecimalLiteral");	    
    }

	public void visit(DivExpression expr) {
		throw new UnsupportedOperationException("visit DivExpression");	    
    }

	public void visit(EndsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit EndsWithMethodCallExpression");	    
    }

	public void visit(EntitySimpleProperty expr) {
		throw new UnsupportedOperationException("visit EntitySimpleProperty");	    
    }

	public void visit(EqExpression expr) {
		throw new UnsupportedOperationException("visit EqExpression");	    
    }

	public void visit(GeExpression expr) {
		throw new UnsupportedOperationException("visit GeExpression");	    
    }

	public void visit(GtExpression expr) {
		throw new UnsupportedOperationException("visit GtExpression");	    
    }

	public void visit(GuidLiteral expr) {
		throw new UnsupportedOperationException("visit GuidLiteral");	    
    }

	public void visit(BinaryLiteral expr) {
		throw new UnsupportedOperationException("visit BinaryLiteral");	    
    }

	public void visit(ByteLiteral expr) {
		throw new UnsupportedOperationException("visit ByteLiteral");	    
    }

	public void visit(SByteLiteral expr) {
		throw new UnsupportedOperationException("visit SByteLiteral");	    
    }

	public void visit(IndexOfMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit IndexOfMethodCallExpression");	    
    }

	public void visit(SingleLiteral expr) {
		throw new UnsupportedOperationException("visit SingleLiteral");	    
    }

	public void visit(DoubleLiteral expr) {
		throw new UnsupportedOperationException("visit DoubleLiteral");	    
    }

	public void visit(IntegerLiteral expr) {
		throw new UnsupportedOperationException("visit IntegerLiteral");	    
    }

	public void visit(Int64Literal expr) {
		throw new UnsupportedOperationException("visit Int64Literal");	    
    }

	public void visit(IsofExpression expr) {
		throw new UnsupportedOperationException("visit IsofExpression");	    
    }

	public void visit(LeExpression expr) {
		throw new UnsupportedOperationException("visit LeExpression");	    
    }

	public void visit(LengthMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit LengthMethodCallExpression");	    
    }

	public void visit(LtExpression expr) {
		throw new UnsupportedOperationException("visit LtExpression");	    
    }

	public void visit(ModExpression expr) {
		throw new UnsupportedOperationException("visit ModExpression");	    
    }

	public void visit(MulExpression expr) {
		throw new UnsupportedOperationException("visit MulExpression");	    
    }

	public void visit(NeExpression expr) {
		throw new UnsupportedOperationException("visit NeExpression");	    
    }

	public void visit(NegateExpression expr) {
		throw new UnsupportedOperationException("visit NegateExpression");	    
    }

	public void visit(NotExpression expr) {
		throw new UnsupportedOperationException("visit NotExpression");	    
    }

	public void visit(NullLiteral expr) {
		throw new UnsupportedOperationException("visit NullLiteral");	    
    }

	public void visit(OrExpression expr) {
		throw new UnsupportedOperationException("visit OrExpression");
    }

	public void visit(ParenExpression expr) {
		throw new UnsupportedOperationException("visit ParenExpression");
    }

	public void visit(BoolParenExpression expr) {
		throw new UnsupportedOperationException("visit BoolParenExpression");
    }

	public void visit(ReplaceMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ReplaceMethodCallExpression");
    }

	public void visit(StartsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit StartsWithMethodCallExpression");
    }

	public void visit(StringLiteral expr) {
		throw new UnsupportedOperationException("visit StringLiteral");
    }

	public void visit(SubExpression expr) {
		throw new UnsupportedOperationException("visit SubExpression");
    }

	public void visit(SubstringMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit SubstringMethodCallExpression");
    }

	public void visit(SubstringOfMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit SubstringOfMethodCallExpression");
    }

	public void visit(TimeLiteral expr) {
		throw new UnsupportedOperationException("visit TimeLiteral");
    }

	public void visit(ToLowerMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ToLowerMethodCallExpression");
    }

	public void visit(ToUpperMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ToUpperMethodCallExpression");
    }

	public void visit(TrimMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit TrimMethodCallExpression");
    }

	public void visit(YearMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit YearMethodCallExpression");
    }

	public void visit(MonthMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit MonthMethodCallExpression");
    }

	public void visit(DayMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit DayMethodCallExpression");
    }

	public void visit(HourMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit HourMethodCallExpression");
    }

	public void visit(MinuteMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit MinuteMethodCallExpression");
    }

	public void visit(SecondMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit SecondMethodCallExpression");
    }

	public void visit(RoundMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit RoundMethodCallExpression");
    }

	public void visit(FloorMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit FloorMethodCallExpression");
    }

	public void visit(CeilingMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit CeilingMethodCallExpression");
    }

	public void visit(AggregateAnyFunction expr) {
		throw new UnsupportedOperationException("visit AggregateAnyFunction");
    }

	public void visit(AggregateAllFunction expr) {
		throw new UnsupportedOperationException("visit AggregateAllFunction");
    }
}
