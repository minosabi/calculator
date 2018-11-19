package com.maesoft.fx.calculator.client;

import com.maesoft.fx.calculator.SimpleCalculator;
import com.maesoft.fx.calculator.config.SimpleConfigProperties;
import com.maesoft.fx.calculator.process.ServiceDelivery;
import com.maesoft.fx.calculator.process.exception.CalculatorException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.Arrays;
import java.util.List;

import static org.testfx.assertions.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableAutoConfiguration
public class ITControllerTest extends ApplicationTest{

    private static final  String TURN_ON = "ON/C";
    private ConfigurableApplicationContext springContext;

    @Autowired
    private SimpleConfigProperties fxConfigProperties;

    private FXMLLoader fxmlLoader ;

    @Autowired
    ServiceDelivery serviceDelivery;

    private int exprId = -1;

    @Override
    public void start (Stage stage) throws Exception {
        springContext = SpringApplication.run(SimpleCalculator.class);
        fxmlLoader= new FXMLLoader(getClass().getResource("/fxml/simplecalculator.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);

        Parent mainNode = fxmlLoader.load();//SimpleCalculator.class.getResource("/fxml/simplecalculator.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();
    }

    @Before
    public void setUp () throws Exception {
        exprId++;
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void validExpressionTest() throws CalculatorException {
         List<String> expressionsAndAnswers = Arrays.asList(
                 "(7);7.0",
                 "10 - 9 * 9 - (2 + 8);-81.0",
                 "-16+3;-13.0",
                 "7+10-(7+7*4);-18.0",
                 "3;3.0",
                 "2+1;3.0",
                 "7+10-(7+7*4);-18.0",
                 "(8 - 7) * 9 - 9 * 5;-36.0",
                 "5 + 10 * 5 - 7 + 4;52.0",
                 "4 * 12 * (6 + 9) - 9;711.0",
                 "10 + (9 - 5 + 9) - 2;21.0",
                 "6 * 3 - 4 + (4 - 10);8.0",
                 "10 - 3 + 10 - 3 + 6;20.0",
                 "3 + 8 * (10 - 5 * 9);-277.0"
         );

        for(int i=0; i< expressionsAndAnswers.size(); i++) {
            final String[] expressionAndAnswer = expressionsAndAnswers.get(i).split(";");

            clickOn("#btnOn").clickOn("#display").eraseText(1).write(expressionAndAnswer[0]).clickOn("#endOfExpression");

            assertThat(lookup("#display").queryAs(TextField.class)).hasText(expressionAndAnswer[0] + "="+expressionAndAnswer[1]);
        }

    }

}
