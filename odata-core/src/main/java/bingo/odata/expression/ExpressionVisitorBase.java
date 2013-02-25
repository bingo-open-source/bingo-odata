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
		
	}

	public void afterDescend() {

    }

	public void betweenDescend() {

    }

	public void visit(String type) {
		throw new UnsupportedOperationException("visit String type");
	}

	public boolean visit(OrderByExpression expr) {
		throw new UnsupportedOperationException("visit OrderByExpression");
    }

	public boolean visit(Direction direction) {
		throw new UnsupportedOperationException("visit Direction");	    
    }

	public boolean visit(AddExpression expr) {
		throw new UnsupportedOperationException("visit AddExpression");	    
    }

	public boolean visit(AndExpression expr) {
		throw new UnsupportedOperationException("visit AndExpression");
    }

	public boolean visit(BoolLiteral expr) {
		throw new UnsupportedOperationException("visit BoolLiteral");	    
    }

	public boolean visit(CastExpression expr) {
		throw new UnsupportedOperationException("visit CastExpression");	    
    }

	public boolean visit(ConcatMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ConcatMethodCallExpression");	    
    }

	public boolean visit(DateTimeLiteral expr) {
		throw new UnsupportedOperationException("visit DateTimeLiteral");	    
    }

	public boolean visit(DateTimeOffsetLiteral expr) {
		throw new UnsupportedOperationException("visit DateTimeOffsetLiteral");	    
    }

	public boolean visit(DecimalLiteral expr) {
		throw new UnsupportedOperationException("visit DecimalLiteral");	    
    }

	public boolean visit(DivExpression expr) {
		throw new UnsupportedOperationException("visit DivExpression");	    
    }

	public boolean visit(EndsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit EndsWithMethodCallExpression");	    
    }

	public boolean visit(EntitySimpleProperty expr) {
		throw new UnsupportedOperationException("visit EntitySimpleProperty");	    
    }

	public boolean visit(EqExpression expr) {
		throw new UnsupportedOperationException("visit EqExpression");	    
    }

	public boolean visit(GeExpression expr) {
		throw new UnsupportedOperationException("visit GeExpression");	    
    }

	public boolean visit(GtExpression expr) {
		throw new UnsupportedOperationException("visit GtExpression");	    
    }

	public boolean visit(GuidLiteral expr) {
		throw new UnsupportedOperationException("visit GuidLiteral");	    
    }

	public boolean visit(BinaryLiteral expr) {
		throw new UnsupportedOperationException("visit BinaryLiteral");	    
    }

	public boolean visit(ByteLiteral expr) {
		throw new UnsupportedOperationException("visit ByteLiteral");	    
    }

	public boolean visit(SByteLiteral expr) {
		throw new UnsupportedOperationException("visit SByteLiteral");	    
    }

	public boolean visit(IndexOfMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit IndexOfMethodCallExpression");	    
    }

	public boolean visit(SingleLiteral expr) {
		throw new UnsupportedOperationException("visit SingleLiteral");	    
    }

	public boolean visit(DoubleLiteral expr) {
		throw new UnsupportedOperationException("visit DoubleLiteral");	    
    }

	public boolean visit(IntegerLiteral expr) {
		throw new UnsupportedOperationException("visit IntegerLiteral");	    
    }

	public boolean visit(Int64Literal expr) {
		throw new UnsupportedOperationException("visit Int64Literal");	    
    }

	public boolean visit(IsofExpression expr) {
		throw new UnsupportedOperationException("visit IsofExpression");	    
    }

	public boolean visit(LeExpression expr) {
		throw new UnsupportedOperationException("visit LeExpression");	    
    }

	public boolean visit(LengthMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit LengthMethodCallExpression");	    
    }

	public boolean visit(LtExpression expr) {
		throw new UnsupportedOperationException("visit LtExpression");	    
    }

	public boolean visit(ModExpression expr) {
		throw new UnsupportedOperationException("visit ModExpression");	    
    }

	public boolean visit(MulExpression expr) {
		throw new UnsupportedOperationException("visit MulExpression");	    
    }

	public boolean visit(NeExpression expr) {
		throw new UnsupportedOperationException("visit NeExpression");	    
    }

	public boolean visit(NegateExpression expr) {
		throw new UnsupportedOperationException("visit NegateExpression");	    
    }

	public boolean visit(NotExpression expr) {
		throw new UnsupportedOperationException("visit NotExpression");	    
    }

	public boolean visit(NullLiteral expr) {
		throw new UnsupportedOperationException("visit NullLiteral");	    
    }

	public boolean visit(OrExpression expr) {
		throw new UnsupportedOperationException("visit OrExpression");
    }

	public boolean visit(ParenExpression expr) {
		return true;
	}

	public boolean visit(BoolParenExpression expr) {
		return true;
	}

	public boolean visit(ReplaceMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ReplaceMethodCallExpression");
    }

	public boolean visit(StartsWithMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit StartsWithMethodCallExpression");
    }

	public boolean visit(StringLiteral expr) {
		throw new UnsupportedOperationException("visit StringLiteral");
    }

	public boolean visit(SubExpression expr) {
		throw new UnsupportedOperationException("visit SubExpression");
    }

	public boolean visit(SubstringMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit SubstringMethodCallExpression");
    }

	public boolean visit(SubstringOfMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit SubstringOfMethodCallExpression");
    }

	public boolean visit(TimeLiteral expr) {
		throw new UnsupportedOperationException("visit TimeLiteral");
    }

	public boolean visit(ToLowerMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ToLowerMethodCallExpression");
    }

	public boolean visit(ToUpperMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit ToUpperMethodCallExpression");
    }

	public boolean visit(TrimMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit TrimMethodCallExpression");
    }

	public boolean visit(YearMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit YearMethodCallExpression");
    }

	public boolean visit(MonthMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit MonthMethodCallExpression");
    }

	public boolean visit(DayMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit DayMethodCallExpression");
    }

	public boolean visit(HourMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit HourMethodCallExpression");
    }

	public boolean visit(MinuteMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit MinuteMethodCallExpression");
    }

	public boolean visit(SecondMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit SecondMethodCallExpression");
    }

	public boolean visit(RoundMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit RoundMethodCallExpression");
    }

	public boolean visit(FloorMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit FloorMethodCallExpression");
    }

	public boolean visit(CeilingMethodCallExpression expr) {
		throw new UnsupportedOperationException("visit CeilingMethodCallExpression");
    }

	public boolean visit(AggregateAnyFunction expr) {
		throw new UnsupportedOperationException("visit AggregateAnyFunction");
    }

	public boolean visit(AggregateAllFunction expr) {
		throw new UnsupportedOperationException("visit AggregateAllFunction");
    }
}
