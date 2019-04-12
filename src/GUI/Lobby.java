package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.CompositionRoot;

public class Lobby extends Application {

    private static Stage myPrimaryStage;
    public PlayField playField;
    Image cursor;

    @Override
    public void start(Stage primaryStage){
        CompositionRoot.getInstance().setLobby(this);

        myPrimaryStage = primaryStage;
        cursor = new Image("GUI/pictures/kermitCursor.png");

        //Pane
        BorderPane loginView = new LoginPane();
        primaryStage.setTitle("Super Kermit Bro's");
        primaryStage.getIcons().add(new Image("/GUI/pictures/kermitPiece.gif"));
        primaryStage.setResizable(false);
        Scene scene = new Scene(loginView, 576, 316);
        scene.setCursor(new ImageCursor(cursor));

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
            scene.setCursor(new ImageCursor(cursor));
            myPrimaryStage.setScene(scene);
        });
    }

    public Stage getPrimaryStage() {
        return myPrimaryStage;
    }

    public static void main(String[] args) { // TODO This is only for testing
        Application.launch();
    }
}
