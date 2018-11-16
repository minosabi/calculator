package com.maesoft.fx.calculator.process;


import lombok.Getter;
import org.springframework.util.StringUtils;

import java.util.function.Predicate;


public class Predicates {

    @Getter
    private enum Parenthesis {
        LEFT("("),
        RIGHT(")");

        private String type;

        Parenthesis(String type) {
            this.type = type;
        }
    }

    private Predicates(){}

    public static final  Predicate<Character> isDigitCharacter = s -> s != null &&  Character.isDigit(s);
    public static final  Predicate<String> isEndOfExpression  = s -> s == null || s.isEmpty() ||  s.equals("=");

    //
    public static final  Predicate<String> startsWithDigit = s -> s != null &&  isDigitCharacter.test(s.charAt(0));
    public static final  Predicate<String> startsWithLeftParenthesis = s -> s != null && (s.substring(0,1).equals(Parenthesis.LEFT.getType()));
    public static final  Predicate<String> startsWithRightParenthesis = s -> s != null &&  (s.substring(0,1).equals(Parenthesis.RIGHT.getType()));
    public static final  Predicate<String> startsWithMinus = s -> s != null &&  (s.substring(0,1).equals("-"));

    public static final  Predicate<String> hasMatchingParentheses = s -> s != null &&  (StringUtils.countOccurrencesOf(s, "(") == StringUtils.countOccurrencesOf(s, ")"));
    public static final  Predicate<String> isDigitString = s -> s != null &&  (s.matches("\\d+"));

}

//5+7*4-(11+6)