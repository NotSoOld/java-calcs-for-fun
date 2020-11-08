package com.notsoold.expression;

public class SubtractExpression implements BinaryExpression {

    public static final String EXPR_STR = "-";
    private static final String REGEX_EXPR_STR = "-";

    @Override
    public double evaluate(double leftOperand, double rightOperand) {
        return leftOperand - rightOperand;
    }

    @Override
    public String normalize(String userInput) {
        return userInput.replaceAll(REGEX_EXPR_STR, " " + EXPR_STR + " ").replaceAll("\\s{2,}", " ");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SubtractExpression;
    }

    @Override
    public String toString() {
        return EXPR_STR;
    }
}
