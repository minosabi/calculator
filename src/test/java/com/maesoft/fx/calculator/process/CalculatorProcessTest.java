package com.maesoft.fx.calculator.process;

import com.maesoft.fx.calculator.process.exception.CalculatorException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculatorProcessTest {

    @Autowired
    ServiceDelivery serviceDelivery;
    
    @Test
    public void validExpressionTest() throws CalculatorException {
        System.out.println("Test1");

        assertEquals(7.0,serviceDelivery.calculate("(7)="),0.0);
        assertEquals(-81.0,serviceDelivery.calculate("10 - 9 * 9 - (2 + 8)="),0.0);
        assertEquals(-13.0,serviceDelivery.calculate("-16+3="),0.0);
        assertEquals(-19.0,serviceDelivery.calculate("-(16+3)"),0.0);
        assertEquals(-18.0,serviceDelivery.calculate("7+10-(7+7*4)="),0.0);
        assertEquals(3.0,serviceDelivery.calculate("3="),0.0);
        assertEquals(3.0,serviceDelivery.calculate("2+1="),0.0);
        assertEquals(-18.0,serviceDelivery.calculate("7+10-(7+7*4)="), 0.0);
        assertEquals(-36.0,serviceDelivery.calculate("(8 - 7) * 9 - 9 * 5="),0.0);
        assertEquals(52.0,serviceDelivery.calculate("5 + 10 * 5 - 7 + 4="),0.0);
        assertEquals(711.0,serviceDelivery.calculate("4 * 12 * (6 + 9) - 9="),0.0);
        assertEquals(21.0,serviceDelivery.calculate("10 + (9 - 5 + 9) - 2="),0.0);
        assertEquals(8.0,serviceDelivery.calculate("6 * 3 - 4 + (4 - 10)="),0.0);
        assertEquals(20.0,serviceDelivery.calculate("10 - 3 + 10 - 3 + 6="),0.0);
        assertEquals(-277.0,serviceDelivery.calculate("3 + 8 * (10 - 5 * 9)="),0.0);
    }

    //4(6+=4.0

    @Test(expected = CalculatorException.class)
    public void invalidExpressionTest() throws CalculatorException {
      serviceDelivery.calculate("10 - 9 * 9 - (2 + 8=");
      serviceDelivery.calculate("7(=");
      serviceDelivery.calculate("7()=");
      serviceDelivery.calculate("4(6+=");
    }
}
