package com.maesoft.fx.calculator.process.compositepattern;

import com.maesoft.fx.calculator.process.exception.CalculatorException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CompositeOperand implements Expression {

    private double value;
    private Operator operator;
    private Expression leftOperand, rightOperand;

    /**
     *
     * @param leftOperand
     * @param operator
     * @param rightOperand
     */
    public CompositeOperand(final Expression leftOperand, final Operator operator, final Expression rightOperand) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }



    @Override
    public double evaluate() throws CalculatorException {
        value = operator.apply(leftOperand.evaluate(),rightOperand.evaluate());
        return value;
    }

    public String preorder() {
        return  operator.getType() + "(" + leftOperand.preorder() + " "+ rightOperand.preorder() + ")";
    }

    public String postorder() {
        return   "(" + leftOperand.postorder() +  rightOperand.postorder() + ")" + operator.getType();

    }
}
