package com.maesoft.fx.calculator.process.compositepattern;

import com.maesoft.fx.calculator.process.compositepattern.builder.ExpressionBuilder;
import com.maesoft.fx.calculator.process.exception.CalculatorException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.maesoft.fx.calculator.process.compositepattern.Operator.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AritmethicExpressionTest {
    private Expression one;
    private Expression onePlusTwo;
    private Expression threeTimesFour;
    private Expression onePlusTwoMinusThreeTimesFour;
    private Expression tenMinusZevenPlusZevenTimesFourLambda;
    private Expression tenMinusZevenPlusZevenTimesFour;


    @Before
    public void init() throws CalculatorException {

        one = new NumericOperand(1);

        onePlusTwo  = new ExpressionBuilder()   // 1+2
                .leftoperand(new NumericOperand(1))
                .operator(PLUS)
                .rightoperand(new NumericOperand(2))
                .build();

        threeTimesFour  = new ExpressionBuilder() // 3*4
                .leftoperand(new NumericOperand(3))
                .operator(TIMES)
                .rightoperand(new NumericOperand(4))
                .build();

        onePlusTwoMinusThreeTimesFour = new ExpressionBuilder() // (1+2)-(3*4)
                .leftoperand(onePlusTwo)
                .operator(MINUS)
                .rightoperand(threeTimesFour).build();

        tenMinusZevenPlusZevenTimesFour = new ExpressionBuilder()  // (10-(7+7*4))
                .leftoperand(new NumericOperand(10))
                .operator(MINUS)
                .rightoperand(new ExpressionBuilder()
                        .leftoperand(new NumericOperand(7))
                        .operator(PLUS)
                        .rightoperand(new ExpressionBuilder()
                                .leftoperand(new NumericOperand(7))
                                .operator(TIMES)
                                .rightoperand(new NumericOperand(4))
                                .build())
                        .build())
                .build();

        tenMinusZevenPlusZevenTimesFourLambda = new ExpressionBuilder()// (10-(7+7*4))
                .with( x-> {
                    x.setLeftOperand(new NumericOperand(10));
                    x.setOperator(MINUS);
                    x.setRightOperand(
                            new  ExpressionBuilder()
                            .with( y -> {
                                y.setLeftOperand(new NumericOperand(7));
                                y.setOperator(PLUS);
                                y.setRightOperand(new ExpressionBuilder()
                                        .with(z -> {
                                            z.setLeftOperand(new NumericOperand(7));
                                            z.setOperator(TIMES);
                                            z.rightoperand(new NumericOperand(4));
                                        }).build());
                            }).build());
                }).build();

    }

    @Test
    public void ExpressionsTest() throws CalculatorException {
        assertThat(1.0,is(one.evaluate()));
        assertThat(3.0,is(onePlusTwo.evaluate()));
        assertEquals(12.0,threeTimesFour.evaluate(),0.0);
        assertEquals(-9.0,onePlusTwoMinusThreeTimesFour.evaluate(),0.0);
        assertEquals(-25.0,tenMinusZevenPlusZevenTimesFour.evaluate(),0.0);
        assertEquals(-25.0,tenMinusZevenPlusZevenTimesFourLambda.evaluate(),0.0);
    }

    @Test
    public void orderTest(){
        assertThat("(10.0(7.0(7.04.0)*)+)-", is(tenMinusZevenPlusZevenTimesFour.postorder()));
        assertThat("-(10.0 +(7.0 *(7.0 4.0)))", is(tenMinusZevenPlusZevenTimesFour.preorder()));
    }
}

