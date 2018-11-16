package com.maesoft.fx.calculator.process;


import com.maesoft.fx.calculator.process.exception.CalculatorException;
import com.maesoft.fx.calculator.product.CalculatorResult;
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
    private boolean evaluatingFactorExpression;

    @FunctionalInterface
    private  interface Processor<S,T,U,V> {
        V  apply(S s, T t, U u) throws CalculatorException;
    }


    @FunctionalInterface
     private interface Evaluator<T,U>  {
        U  apply(T t) throws CalculatorException;
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

        String expressionWithoutSpaces = input.replaceAll("\\s+","");

        return expressionEvaluator.apply(expressionWithoutSpaces).getValue();
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
    private  final Evaluator<String, CalculatorResult> expressionEvaluator = input ->  this.processor.apply(input, isExpressionOperator, x -> this.termEvaluator.apply(x));


    /**
     * Evaluate Term
     */
    private final Evaluator<String, CalculatorResult> termEvaluator = input ->  this.processor.apply(input, isTermOperator, x -> this.factorEvaluator.apply(x));


    /**
     * Evaluate Factor
     */
    private  final Evaluator<String, CalculatorResult> factorEvaluator = input -> {

        if(startsWithDigit.test(input) ) return evaluateNumber(input);

        if (startsWithMinus.test(input) ) return evaluteMinus(input);

        if (startsWithLeftParenthesis.test(input) )
        {
            evaluatingFactorExpression  = true;

            CalculatorResult result = evaluteFactorExpression(input);

             evaluatingFactorExpression  = false;

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
    private CalculatorResult evaluateNumber(final String input){

            StringBuilder value = new StringBuilder();

            int i = 0;
            while(isDigitCharacter.test(input.charAt(i))){
                value.append(input.charAt(i++));
            }

            CalculatorResult result = new CalculatorResult();
            result.setValue(Double.valueOf(String.valueOf(value)));
            result.setRemainderOfExpression(input.substring(i,input.length()));

            return result;
    }

    /**
     *
     * @param input
     * @return
     * @throws CalculatorException
     */
    private CalculatorResult evaluteMinus (final String input) throws CalculatorException {
        CalculatorResult result =  this.factorEvaluator.apply(input.substring(1));
        result.setValue(result.getValue() * -1.0);
        return result;
    }


    /**
     *
     * @param input
     * @return
     * @throws CalculatorException
     */
    private CalculatorResult evaluteFactorExpression(final String input) throws CalculatorException {
        CalculatorResult result =  expressionEvaluator.apply(input.substring(1));

        String restExpression = result.getRemainderOfExpression();

        if(startsWithRightParenthesis.test(restExpression)){
            result.setValue(result.getValue());
            result.setRemainderOfExpression(restExpression.substring(1));// Remove parenthesis

            return result;
        }
        else{
            //  the expression is invalid
            if(logger.isDebugEnabled()){
                logger.debug(" Expression contains non matching parentheses: '" + originalExpression + "'");
            }

            throw new CalculatorException("Invalid expression: " + originalExpression);

        }
    }


    /**
     *
     */
    private  Processor<String, Predicate, Evaluator<String, CalculatorResult>, CalculatorResult> processor = (input, isOperator, operandEvaluator) -> {
        CalculatorResult result = new CalculatorResult();

        try {
            // Evaluate the  operand
            CalculatorResult evaluatedOperand = operandEvaluator.apply(input);

            // keep the value of the evaluatedOperand
            result.setValue(evaluatedOperand.getValue());

            // Get the rest of the expression after the operator, if any
            String remainder = evaluatedOperand.getRemainderOfExpression();

            if(!isEndOfExpression.test(remainder)) {
                String nextCharacter = remainder.substring(0,1);

                // If the rest of the expression is not an operator while evaluating a term or expression
                // hten the expression is invalid
                if(!evaluatingFactorExpression && !isAnyValidOperator.test(nextCharacter) ){
                    throw new CalculatorException("Invalid expression: " + originalExpression) ;
                }

                while (isOperator.test(nextCharacter) ) {

                    // Evaluate the next evaluatedOperand starting after the operator
                    evaluatedOperand = operandEvaluator.apply(remainder.substring(1));

                    // Determine the operator
                    Operator operator = Operator.getName(nextCharacter);

                    // Determine the value
                    result.setValue(applyOperator(operator, result.getValue(), evaluatedOperand.getValue()));

                    remainder = evaluatedOperand.getRemainderOfExpression();

                    //  If the end of the epression has been reached set the next character to empty
                    nextCharacter = remainder == null? ":":remainder.substring(0,1);
                }

                result.setRemainderOfExpression(remainder);
            }
            else{
                if(logger.isDebugEnabled()){
                    logger.debug("Expression has been evaluated completely");
                }
            }
        }
        catch(CalculatorException ce){
            if(logger.isDebugEnabled()){
                logger.debug("Problem encountered while evaluating the operand: "  + result.getRemainderOfExpression());
                logger.debug(ce.getMessage());
            }

            throw ce;
        }

        return result;
    };
}
