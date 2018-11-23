package com.maesoft.fx.calculator.process.compositepattern;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NumericOperand implements Expression {

    private double value;


    @Override
    public double evaluate() {
        return value;
    }

    @Override
    public String preorder() {
        return  String.valueOf(getValue());
    }

    @Override
    public String postorder() {
        return  String.valueOf(getValue());
    }
}
