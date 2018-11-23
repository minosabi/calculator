package com.maesoft.fx.calculator.process.compositepattern.builder;

import com.maesoft.fx.calculator.process.compositepattern.CompositeOperand;
import com.maesoft.fx.calculator.process.compositepattern.Expression;
import com.maesoft.fx.calculator.process.compositepattern.NumericOperand;
import com.maesoft.fx.calculator.process.compositepattern.Operator;
import com.maesoft.fx.calculator.process.exception.CalculatorException;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

import static com.maesoft.fx.calculator.process.compositepattern.Operator.*;


@Getter
@Setter
public class ExpressionBuilder {
    private Expression leftOperand;
    private Operator operator;
    private Expression rightOperand;

    public ExpressionBuilder leftoperand(Expression leftOperand) {
        this.leftOperand = leftOperand;
        return this;
    }

    public ExpressionBuilder operator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public ExpressionBuilder rightoperand(Expression rightOperand) {
        this.rightOperand = rightOperand;
        return this;
    }

    public Expression build() {
        return new CompositeOperand(leftOperand, operator, rightOperand);
    }

    //  ---------------------------------------   use lambda function
    public ExpressionBuilder with(Consumer<ExpressionBuilder> builderFunction) {
        builderFunction.accept(this);
        return this;
    }

    public void exampleLambaFunction() throws CalculatorException {

        new ExpressionBuilder()
                .with( s-> {
                            s.leftOperand = new ExpressionBuilder()
                                    .leftoperand(new NumericOperand(1))
                                    .operator(PLUS)
                                    .rightoperand(new NumericOperand(2))
                                    .build();
                        })
                .with( t  -> {
                    t.rightOperand = new ExpressionBuilder()
                            .leftoperand(new NumericOperand(3))
                            .operator(TIMES)
                            .rightoperand(new NumericOperand(4))
                            .build();
                } )
                .with( o -> o.operator = MINUS).build().evaluate();
    }
}