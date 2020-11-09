package com.notsoold;

import com.notsoold.exceptions.IllegalStatementException;
import com.notsoold.expression.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputStatement {

    private String userInput;
    private List<Token> processedTokens = new LinkedList<>();

    public InputStatement(String userInput) {
        this.userInput = userInput;
    }

    public double evaluate() throws Exception {
        evaluateExpressionsOfSamePriority(Arrays.asList(Token.MULT_EXPR_DUMMY, Token.DIV_EXPR_DUMMY));
        evaluateExpressionsOfSamePriority(Arrays.asList(Token.ADD_EXPR_DUMMY, Token.SUBT_EXPR_DUMMY));
        return processedTokens.get(0).getDoubleValue();
    }

    private void evaluateExpressionsOfSamePriority(List<Token> expressionsToFindAndEvaluate) throws Exception {
        Optional<Integer> leftmostOperatorPosition = findLeftmostOperatorPosition(expressionsToFindAndEvaluate);

        while (leftmostOperatorPosition.isPresent()) {
            int operatorPosition = leftmostOperatorPosition.get();

            System.out.println(processedTokens + ", eval operator at " + operatorPosition);
            List<Token> expressionToProcess = processedTokens.subList(operatorPosition - 1, operatorPosition + 2);
            double leftOperand = expressionToProcess.get(0).getDoubleValue();
            double rightOperand = expressionToProcess.get(2).getDoubleValue();
            // We're sure that it will be a binary expression.
            double result = ((BinaryExpression)expressionToProcess.get(1).getExpression()).evaluate(leftOperand, rightOperand);
            // Remove left operand, the operator, and right operand.
            expressionToProcess.clear();
            // Insert calculated value.
            processedTokens.add(operatorPosition - 1, new Token.TokenBuilder().setDoubleValue(result).build());

            leftmostOperatorPosition = findLeftmostOperatorPosition(expressionsToFindAndEvaluate);
        }
    }

    private Optional<Integer> findLeftmostOperatorPosition(List<Token> expressionsToFind) {
        return expressionsToFind.stream()
                        .map(expr -> processedTokens.indexOf(expr))
                        .filter(position -> position != -1)
                        .sorted().findFirst();

    }

    public InputStatement processAndCheck() throws IllegalStatementException {
        List<String> meaningfulTokens = tokenize();

        Token lastTokenObject = null;
        boolean firstNumberIsNegative = false;
        for (int i = 0; i < meaningfulTokens.size(); i++) {
            String tokenStr = meaningfulTokens.get(i);

            Token currentTokenObject;
            try {
                double tokenValue = Double.parseDouble(tokenStr);
                if (lastTokenObject != null && !lastTokenObject.isOperator()) {
                    throw new IllegalStatementException("At token #" + i + ", second consecutive number '" + tokenStr + "' found. This is illegal.");
                }
                // Creating new number token.
                if (firstNumberIsNegative) {
                    tokenValue *= -1;
                    firstNumberIsNegative = false;
                }
                currentTokenObject = new Token.TokenBuilder().setDoubleValue(tokenValue).build();

            } catch (NumberFormatException ignored) {
                // It's not a valid number. Than maybe it's an operator (expression)?
                // In addition, check if the first token is not a number (includes a hack for the negative first number).
                if (lastTokenObject == null) {
                    if (SubtractExpression.EXPR_STR.equals(tokenStr)) {
                        if (i != 0) {
                            // More than two minus signs at the front of the statement.
                            throw new IllegalStatementException("At token #" + i + ", illegal combination is found.");
                        }
                        firstNumberIsNegative = true;
                        continue;
                    }
                    throw new IllegalStatementException("At token #" + i + ", first token should be a number.");
                }

                Expression currentTokenExpression;
                switch (tokenStr) {
                case AddExpression.EXPR_STR:
                    currentTokenExpression = new AddExpression();
                    break;
                case SubtractExpression.EXPR_STR:
                    currentTokenExpression = new SubtractExpression();
                    break;
                case MultiplyExpression.EXPR_STR:
                    currentTokenExpression = new MultiplyExpression();
                    break;
                case DivideExpression.EXPR_STR:
                    currentTokenExpression = new DivideExpression();
                    break;
                default:
                    throw new IllegalStatementException("At token #" + i + ", illegal symbol '" + tokenStr + "' found.");
                }

                if (lastTokenObject.isOperator()) {
                    throw new IllegalStatementException("At token #" + i + ", second consecutive operator '" + tokenStr + "' found. This is illegal.");
                }

                // Creating new expression token.
                currentTokenObject = new Token.TokenBuilder().setExpression(currentTokenExpression).build();
            }

            processedTokens.add(currentTokenObject);
            lastTokenObject = currentTokenObject;
        }

        if (processedTokens.isEmpty()) {
            // Statement "-" (containing only one minus sign) will also trigger this exception.
            throw new IllegalStatementException("Input statement was empty or was not a valid statement.");
        }
        if (lastTokenObject != null && lastTokenObject.isOperator()) {
            throw new IllegalStatementException("Last token should be a number, not an operator.");
        }

        return this;
    }

    private List<String> tokenize() {
        userInput = Token.ADD_EXPR_DUMMY.getExpression().normalize(userInput);
        userInput = Token.SUBT_EXPR_DUMMY.getExpression().normalize(userInput);
        userInput = Token.MULT_EXPR_DUMMY.getExpression().normalize(userInput);
        userInput = Token.DIV_EXPR_DUMMY.getExpression().normalize(userInput);

        return Stream.of(userInput.split(" ")).filter(tok -> !tok.isEmpty()).collect(Collectors.toList());
    }
}
