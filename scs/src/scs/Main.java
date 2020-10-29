package scs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // load the fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scs.fxml"));

        // create new scs screen controller and assign it into fxml file loader
        ScsScreenController scsScreenController = new ScsScreenController();
        loader.setController(scsScreenController);

        // load scs screen and start it's Controller
        Parent root = loader.load();
        scsScreenController.onStart();


        primaryStage.setTitle("SCS");
        primaryStage.setScene(new Scene(root, 800, 500));

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
