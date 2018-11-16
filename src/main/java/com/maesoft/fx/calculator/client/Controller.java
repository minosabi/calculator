package com.maesoft.fx.calculator.client;

import com.maesoft.fx.calculator.process.ServiceDelivery;
import com.maesoft.fx.calculator.process.exception.CalculatorException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

import static com.maesoft.fx.calculator.process.Predicates.isDigitCharacter;
import static com.maesoft.fx.calculator.process.Predicates.startsWithLeftParenthesis;


@Component
public class Controller {

    private final Logger logger = LoggerFactory.getLogger(Controller.class);

    @FXML
    private TextField display;

    @FXML private MenuBar menuBar;

    @Autowired
    ServiceDelivery serviceDelivery;

    private static final  String END_OF_EXPRESSION = "=";
    private static final  String TURN_ON = "ON/C";

    private static final List<String> funtions = Arrays.asList("sqrt","sin","cos","tan");
    private static final List<String> nonDisplayables = Arrays.asList("CE","MU", "MRC", "M-", "M+", "ON/C");

    // Predicates
    private static final Predicate<String> isDisplayable = c -> !nonDisplayables.contains(c);
    private static final Predicate<String> isCalcFunction = funtions::contains;

    private  boolean turnedOn = false;



    /**
     *
     * @param evt
     */
    public void buttonClicked(ActionEvent evt){
        Button btn = (Button)evt.getSource();
        String btnText = btn.getText();

        if(btnText.equals(TURN_ON)){
            turnedOn = true;
            display.setText("0");
        }

        if(turnedOn) {
            CompletableFuture
                    .supplyAsync(() -> assembleExpression(btnText))
                    .thenApply(this::evaluateExpression)
                    .thenAccept( this::showResult);
        }
        else{
            display.setText("Please turn  the calculator on !");
        }
    }

    /**
     *
     * @param input
     */
    private String assembleExpression(final String input)  {
        String expression = display.getText();

        if(isDisplayable.test(input)) {
            if (expression.equals("0") && (startsWithLeftParenthesis.test(input) || isDigitCharacter.test(input.charAt(0)))) {
                expression = input;
            } else {
                expression = isCalcFunction.test(input) ? input + expression : expression + input;
            }
        }

        return expression;
    }

    /**
     *
     * @param expr
     * @return
     */



    private String evaluateExpression(final String expr) {
        String result = "";

        if (expr.contains(END_OF_EXPRESSION)) {
            try {
                result = String.valueOf(serviceDelivery.calculate(expr));

                if (!result.equals("")) {
                    return expr + result;
                }
            } catch (CalculatorException e) {
                result = e.getMessage();
            }
        }
        else {
            result = expr;
        }

        return result;
    }

    /**
     *
     * @param result
     */
    private void showResult(final String result){
            display.clear();
            display.setText(result);
    }


    /**
     *
     * @param
     * @throws IOException
     */
   public void changeUI(Event evt) throws IOException, CalculatorException {

       Parent uiParent=null;
       String uiName = ((MenuItem) evt.getSource()).getText();
       Stage stage = (Stage) menuBar.getScene().getWindow();

        try {
            switch(uiName){
                case "Show Advanced":  uiParent = FXMLLoader.load(getClass().getResource("/fxml/advancedcalculator.fxml")); break;
                case "DatePicker":         uiParent = createDatePicker();break;
                default :                        uiParent = FXMLLoader.load(getClass().getResource("/fxml/simplecalculator.fxml")); break;
            }

            stage.setScene(new Scene(uiParent));
            stage.show();
        }
        catch (IOException ioe){
            if(logger.isDebugEnabled()){
                logger.debug("Error when changing UI: " + ioe.getMessage());
            }

            throw new CalculatorException("Util::changeUI --> " + ioe.getMessage());
        }
    }



 private HBox createDatePicker(){
     HBox datePicker = new HBox(new DatePicker());
     datePicker.setMinWidth(200);
     datePicker.setMinHeight(100);
     return datePicker;
 }
    /**
     *
     * @param input
     */
    private void startCalculationUsingRunLater(String input) {

        Runnable loginTask = () ->

                Platform.runLater(() -> {
                    if(isDisplayable.test(input)) {
                        assembleExpression(input);
                    }
                });

        Thread workerThread = new Thread(loginTask);
        workerThread.start();

    }
}


