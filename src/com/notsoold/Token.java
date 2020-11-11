package com.notsoold;

import com.notsoold.expression.*;

import java.util.Arrays;
import java.util.List;

public class Token {

    private final Double doubleValue;
    private final Expression expression;

    public final static Token ADD_EXPR_DUMMY = new Token(new AddExpression());
    public final static Token SUBT_EXPR_DUMMY = new Token(new SubtractExpression());
    public final static Token MULT_EXPR_DUMMY = new Token(new MultiplyExpression());
    public final static Token DIV_EXPR_DUMMY = new Token(new DivideExpression());

    public final static List<Expression> AVAILABLE_EXPRESSIONS =
                    Arrays.asList(ADD_EXPR_DUMMY.getExpression(), SUBT_EXPR_DUMMY.getExpression(),
                                    MULT_EXPR_DUMMY.getExpression(), DIV_EXPR_DUMMY.getExpression());

    /* Only for dummies' construction. */
    private Token(Expression expression) {
        this.expression = expression;
        this.doubleValue = Double.NaN;
    }

    private Token(TokenBuilder builder) {
        this.doubleValue = builder.doubleValue;
        this.expression = builder.expression;
    }

    public boolean isOperator() {
        return expression != null;
    }

    public Double getDoubleValue() {
        return doubleValue;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token another = (Token)obj;
        if (this.isOperator() && !another.isOperator()) {
            return false;
        }
        if (this.isOperator()) {
            return another.getExpression().equals(this.getExpression());

        } else {
            return another.getDoubleValue().equals(this.getDoubleValue());
        }
    }

    @Override
    public String toString() {
        return "[Token: " + (isOperator() ? "operator " + getExpression().toString() : getDoubleValue()) + "]";
    }

    public static class TokenBuilder {

        private Double doubleValue = Double.NaN;
        private Expression expression = null;

        public TokenBuilder setDoubleValue(Double doubleValue) {
            this.doubleValue = doubleValue;
            return this;
        }

        public TokenBuilder setExpression(Expression expression) {
            this.expression = expression;
            return this;
        }

        public Token build() {
            return new Token(this);
        }
    }

}
