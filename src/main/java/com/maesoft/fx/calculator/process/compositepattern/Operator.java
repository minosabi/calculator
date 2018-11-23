package com.maesoft.fx.calculator.process.compositepattern;

import com.maesoft.fx.calculator.process.exception.CalculatorException;
import lombok.Getter;

@Getter
public enum Operator {
    PLUS("+"){
        @Override
        public double apply(final double leftOperand, final double rightOperand) {
            return leftOperand + rightOperand;
        }
    },
    MINUS("-") {
        @Override
        public double apply(final double leftOperand, final double rightOperand) {
            return leftOperand - rightOperand;
        }
    },
    TIMES("*") {
        @Override
        public double apply(final double leftOperand, final double rightOperand) {
            return leftOperand * rightOperand;
        }
     },
    DIVIDE("/"){
            @Override
            public double apply(final double leftOperand, final double rightOperand) throws CalculatorException {
                if(rightOperand > 0.0) {
                    return (leftOperand / rightOperand);
                }
                else {
                    throw new CalculatorException("Can not divide by zero !");
                }
            }
    };

    private String type;

    Operator(String type) {
        this.type = type;
    }

    public abstract double apply(final double leftOperand, final double rightOperand) throws CalculatorException;
}