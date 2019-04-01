package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;

public class Lobby extends Application {
    @Override
    public void start(Stage primaryStage){

        //Pane
        FlowPane pane1 = new FlowPane();
        FlowPane pane2 = new FlowPane();
        GridPane pane3 = new GridPane();

        pane3.addColumn(4);
        pane3.addRow(4);


        //Buttons
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");

        //Textfield
        TextArea test = new TextArea("Dik bordspel");


        pane1.getChildren().addAll(spel1, spel2);
        pane2.getChildren().addAll(test);
        Scene scene1 = new Scene(pane1, 200, 250);
        Scene scene2 = new Scene(pane3,200, 250);
        primaryStage.setTitle("MyJavaFX"); // Set the stage title
        primaryStage.setScene(scene1); // Place the scene in the stage
        primaryStage.show(); // Display the stage

        //Register Buttons
        spel1.setOnAction(event -> primaryStage.setScene(scene2));
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
