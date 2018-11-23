package com.maesoft.fx.calculator.process;


import com.maesoft.fx.calculator.process.exception.CalculatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static com.maesoft.fx.calculator.process.Operator.*;
import static com.maesoft.fx.calculator.process.Predicates.*;

@Component
class CalculatorProcess {

    private static final Logger logger = LoggerFactory.getLogger(CalculatorProcess.class);

    private String originalExpression;
    String remainderOfExpression = "";

    @FunctionalInterface
    private  interface Processor<S,T,U,V> {
        V  apply(S s, T t, U u) throws CalculatorException;
    }


    @FunctionalInterface
     private interface Evaluator<T,U>  {
        U evaluate(T t) throws CalculatorException;
    }


    /**
     *
     * @param input
     * @return
     * @throws CalculatorException
     */
    public double calculate(final String input) throws CalculatorException {
        originalExpression = input;

        validate(input);

        return expressionEvaluator.evaluate( input.replaceAll("\\s+",""));
    }

    /**
     *
     * @param input
     * @return
     * @throws CalculatorException
     */
    private void  validate(final String input) throws CalculatorException {
        if(!hasMatchingParentheses.test(input)) {
            throw new CalculatorException("Invalid expression. Expression contains non matching parentheses: '" + originalExpression + "'");
        }
    }


    /**
     * Evaluate Expression
     */
    private  final Evaluator<String, Double> expressionEvaluator = input ->  this.processor.apply(input, isExpressionOperator, x -> this.termEvaluator.evaluate(x));


    /**
     * Evaluate Term
     */
    private final Evaluator<String, Double> termEvaluator = input ->  this.processor.apply(input, isTermOperator, x -> this.factorEvaluator.evaluate(x));


    /**
     * Evaluate Factor
     */
    private  final Evaluator<String, Double> factorEvaluator = input -> {

        // A number
        if(startsWithFraction.test(input) ){
            String result = readNumber(input);
            remainderOfExpression = input.substring(result.length());

            return Double.valueOf(result);
        }

        // A negative factor
        if (startsWithMinus.test(input) ) return -(this.factorEvaluator.evaluate(input.substring(1)));

        // An expression
        if (startsWithLeftParenthesis.test(input) ) {
             Double result =  expressionEvaluator.evaluate(input.substring(1));
            remainderOfExpression = remainderOfExpression.substring(1);

            return result;
        }

        if(logger.isDebugEnabled()){
            logger.debug("Problem encountered while evaluating the factor: "  + input);
        }

        throw new CalculatorException(" Invalid expression:  " + originalExpression );
    } ;


    /**
     *
     * @param input
     * @return
     */
    private String readNumber( String input){

        StringBuilder number = new StringBuilder();

        while(startsWithFraction.test(input) ){
            number.append(input.substring(0,1));
            input = input.substring(1);
        }

        return String.valueOf(number);

    }


    /**
     *
     */
    private  Processor<String, Predicate, Evaluator<String, Double>, Double> processor = (input, isOperator, operandEvaluator) -> {
        Double result;
        try {
            // Evaluate the  operand
            result = operandEvaluator.evaluate(input);

            String token = readNextToken();

            while (isOperator.test(token) ) {
                result = applyOperator(Operator.getName(token),  result,operandEvaluator.evaluate(remainderOfExpression.substring(1)));
                token = readNextToken();
            }
        }
        catch(CalculatorException ce){
            if(logger.isDebugEnabled()){
                logger.debug("Problem encountered while evaluating the operand: '"  + remainderOfExpression + "' " + ce.getMessage());
            }
            throw ce;
        }

        return result;
    };


    /**
     *
     * @return
     */
    private String readNextToken(){
        return (remainderOfExpression.isEmpty()) ?  "" : remainderOfExpression.substring(0,1);
    }
}
