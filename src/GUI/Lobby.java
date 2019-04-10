package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.CompositionRoot;

public class Lobby extends Application {

    private static Stage myPrimaryStage;
    public PlayField playField;

    @Override
    public void start(Stage primaryStage){
        CompositionRoot.getInstance().setLobby(this);

        myPrimaryStage = primaryStage;

        //Pane
        BorderPane loginView = new LoginPane(primaryStage);
        Scene scene = new Scene(loginView, 576, 316);

        // Stage
        //primaryStage.setTitle("Super Kermit Bro's"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        myPrimaryStage.show(); // Display the stage
        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public void setScene(Scene scene) {
        Platform.runLater(() -> {
            myPrimaryStage.setScene(scene);
        });
    }

    public static void main(String[] args) { // TODO This is only for testing
        Application.launch();
    }
}
