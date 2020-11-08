package com.notsoold;

import com.notsoold.exceptions.IllegalStatementException;

import java.io.Console;

public class Main {
    public static void main(String[] args) {
        Console console = System.console();
        if (console == null) {
            throw new IllegalStateException("Console is not defined, exiting.");
        }

        String userInput = console.readLine("Expression: ");
        while (!userInput.equals("quit")) {
            try {
                /* Actually, the OvercomplicatedCalculator class isn't very useful right now
                 * as you can evaluate InputStamements by hand:
                 *
                 * double result = new InputStatement(userInput).processAndCheck().evaluate();
                 */
                double statementResult = OvercomplicatedCalculator.getInstance().evaluate(new InputStatement(userInput));
                System.out.println("Result: " + statementResult);

            } catch (IllegalStatementException e) {
                System.out.println("Result: " + e.getMessage());

            } catch (Exception e) {
                System.out.println("Error occurred. See the stack below:");
                e.printStackTrace();
            }

            userInput = console.readLine("Expression: ");
        }
    }
}
