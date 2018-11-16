package com.maesoft.fx.calculator.process;

import com.maesoft.fx.calculator.process.exception.CalculatorException;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;

@Getter
public enum Operator {

    ADD("+"),
    SUBTRACT("-"),
    MULTIPLY("*"),
    DIVIDE("/"),
    DIV("div"),
    MOD("mod"),
    AND("and");

    private String type;
    private static final Logger logger = LoggerFactory.getLogger(Operator.class);
    private static final String NOT_SUPPORTED_YET = "Not supported yet.";

    static final Predicate<String> isExpressionOperator = s ->  s!= null && (s.equals(ADD.getType()) ||s.equals(SUBTRACT.getType()));
    static final  Predicate<String> isTermOperator = s -> s != null &&
                                                                       ( s.equals(MULTIPLY.getType())
                                                                      ||s.equals(DIVIDE.getType())
                                                                      ||s.equals(DIV.getType())
                                                                      ||s.equals(MOD.getType())
                                                                      ||s.equals(AND.getType()));

    static final Predicate<String> isAnyValidOperator = s -> s != null &&  ( isExpressionOperator.test(s)|| isTermOperator.test(s));

    /**
     *
     * @param type
     */
    Operator(String type) {
        this.type = type;
    }

    /**
     *
     * @param operator
     * @return
     * @throws CalculatorException
     */
    public static Operator getName(final String operator) throws CalculatorException {
        switch (operator) {
            case "+":
                return ADD;
            case "-":
                return SUBTRACT;
            case "/":
                return DIVIDE;
            case "*":
                return MULTIPLY;
            case "div":
                return DIV;
            case "mod":
                return MOD;
            case "and":
                return AND;

            default:
                if (logger.isDebugEnabled() ){
                    logger.debug("Operator::getOperator --> Invalid operator: " + operator);
                }
                throw new CalculatorException("Invalid operator: '" + operator + "'");
        }
    }


    /**
     *
     * @param operator
     * @param x
     * @param y
     * @return
     * @throws CalculatorException
     */
    public static final double applyOperator(final Operator operator, final double x, final double y) throws CalculatorException {
        switch(operator){
            case ADD:
                return (x + y);

            case SUBTRACT:
                return (x-y );

            case MULTIPLY:
                return (x * y);

            case DIV:
                throw new java.lang.UnsupportedOperationException(NOT_SUPPORTED_YET);

            case MOD:
                throw new java.lang.UnsupportedOperationException(NOT_SUPPORTED_YET);

                case AND:
                    throw new java.lang.UnsupportedOperationException(NOT_SUPPORTED_YET);

            case DIVIDE:
                if(y > 0.0) {
                    return (x / y);
                }
                else {
                    throw new CalculatorException("Can not divide by zero !");
                }

            default:  throw new CalculatorException("Illegal operator !");
        }
    }
}
