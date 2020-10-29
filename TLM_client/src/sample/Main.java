package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.subsystem.SubSystem;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("tlm client");
        primaryStage.setScene(new Scene(root, 2000, 2000));
        primaryStage.show();

    }


    public static void main(String[] args) {
        SubSystem.init();

        launch(args);
    }
}
