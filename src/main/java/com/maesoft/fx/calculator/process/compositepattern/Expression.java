package com.maesoft.fx.calculator.process.compositepattern;

import com.maesoft.fx.calculator.process.exception.CalculatorException;

public interface Expression {
    double evaluate() throws CalculatorException;

    String preorder();
    String postorder();
}
