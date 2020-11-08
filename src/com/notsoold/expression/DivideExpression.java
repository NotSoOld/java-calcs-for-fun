package com.notsoold.expression;

import com.notsoold.exceptions.DivisionException;

public class DivideExpression implements BinaryExpression  {

    public static final String EXPR_STR = "/";
    private static final String REGEX_EXPR_STR = "/";

    @Override
    public double evaluate(double leftOperand, double rightOperand) throws Exception {
        if (rightOperand == 0) {
            throw new DivisionException();
        }
        return leftOperand / rightOperand;
    }

    @Override
    public String normalize(String userInput) {
        return userInput.replaceAll(REGEX_EXPR_STR, " " + EXPR_STR + " ").replaceAll("\\s{2,}", " ");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DivideExpression;
    }

    @Override
    public String toString() {
        return EXPR_STR;
    }
}
