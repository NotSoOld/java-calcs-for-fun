package com.notsoold;

public class OvercomplicatedCalculator {

    private static volatile OvercomplicatedCalculator instance;

    private OvercomplicatedCalculator() {}

    public static OvercomplicatedCalculator getInstance() {
        if (instance == null) {
            synchronized (OvercomplicatedCalculator.class) {
                if (instance == null) {
                    instance = new OvercomplicatedCalculator();
                }
            }
        }
        return instance;
    }

    public double evaluate(InputStatement statement) throws Exception {
        return statement.processAndCheck().evaluate();
    }

}
