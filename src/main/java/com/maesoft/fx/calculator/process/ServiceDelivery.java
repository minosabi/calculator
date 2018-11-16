package com.maesoft.fx.calculator.process;

import com.maesoft.fx.calculator.client.service.CalculatorService;
import com.maesoft.fx.calculator.process.exception.CalculatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ServiceDelivery implements CalculatorService {

    @Autowired
    CalculatorProcess calculatorProcess;

    @Override
    public double calculate(final String expr) throws CalculatorException {
        return calculatorProcess.calculate(expr);
    }

}
