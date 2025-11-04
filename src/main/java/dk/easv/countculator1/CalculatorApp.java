package dk.easv.countculator1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalculatorApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting application...");
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage.setTitle("Countculator with Memory");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
        System.out.println("Application started successfully!");
    }

    public static void main(String[] args) {
        launch(args);
    }
}