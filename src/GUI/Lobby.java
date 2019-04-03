package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;


public class Lobby extends Application {

    private static Stage myPrimaryStage;

    @Override
    public void start(Stage primaryStage){

        myPrimaryStage = primaryStage;

        //Pane
        FlowPane lobby = new LobbyPane();
        Scene scene1 = new Scene(lobby, 200, 250);

        // Stage
        primaryStage.setTitle("Lobby"); // Set the stage title
        primaryStage.setScene(scene1); // Place the scene in the stage
        primaryStage.show(); // Display the stage


    }

    public void setScene(Scene scene) {
        myPrimaryStage.setScene(scene);
    }

    public static void main(String[] args) { // TODO This is only for testing
        Application.launch();
    }
}
