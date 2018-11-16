package com.maesoft.fx.calculator.client.service;

import com.maesoft.fx.calculator.process.exception.CalculatorException;
import org.springframework.stereotype.Service;

@Service
public interface CalculatorService {
    double calculate( final String expression) throws CalculatorException;
}
