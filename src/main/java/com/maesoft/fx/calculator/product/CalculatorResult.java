package com.maesoft.fx.calculator.product;

import com.maesoft.design.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalculatorResult implements Product {
        private String evaluatorName;
        private String remainderOfExpression;
        private double value;
}
