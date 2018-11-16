package com.maesoft.fx.calculator;

import com.maesoft.fx.calculator.config.SimpleConfigProperties;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


//@SpringBootApplication(scanBasePackages={"com.maesoft.fx.calc"})// same as @Configuration @EnableAutoConfiguration @ComponentScan
@SpringBootApplication
@Component
public class SimpleCalculator extends Application {

    Logger logger = LoggerFactory.getLogger(SimpleCalculator.class);

    private ConfigurableApplicationContext springContext;

    @Autowired
    private SimpleConfigProperties fxConfigProperties;

    private FXMLLoader fxmlLoader ;
    private Parent rootNode;



    @Override
    public void init() throws Exception {
        springContext = SpringApplication.run(SimpleCalculator.class);
        fxmlLoader= new FXMLLoader(getClass().getResource("/fxml/simplecalculator.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }


    @Override
    public void start(final Stage primaryStage) throws Exception {
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(new Scene(rootNode, 600, 300));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    @Override
    public void stop() {
        springContext.stop();
    }

    /**
     *  main
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

    // Check this -----------------------------
//http://www.greggbolinger.com/let-spring-be-your-javafx-controller-factory/
}
