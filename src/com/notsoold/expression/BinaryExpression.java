package com.notsoold.expression;

public interface BinaryExpression extends Expression {

    double evaluate(double leftOperand, double rightOperand) throws Exception;

}
