package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.subsystem.Subsystem;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("TLM server");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Subsystem.init();
        launch(args);
    }
}
