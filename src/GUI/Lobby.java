package GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import tic_tac_toe.TicTacToe;

public class Lobby extends Application {


    @Override
    public void start(Stage primaryStage){

        //Pane
        FlowPane pane1 = new FlowPane();

        //Buttons
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");

        //Lobby
        pane1.getChildren().addAll(spel1, spel2);
        Scene scene1 = new Scene(pane1, 200, 250);

        // Games
        Scene ticTacToe = createPlayField(3,3);
        Scene reversi = createPlayField(8,8);

        // Stage
        primaryStage.setTitle("Lobby"); // Set the stage title
        primaryStage.setScene(scene1); // Place the scene in the stage
       // primaryStage.setMaximized(true);
        primaryStage.show(); // Display the stage


        //Register Buttons
        spel1.setOnAction(event -> primaryStage.setScene(ticTacToe));
        spel2.setOnAction(event -> primaryStage.setScene(reversi));

    }

    public static Scene createPlayField(int rows, int columns) {

        //stage.setTitle("Reversi");

        GridPane grid = new GridPane();
        grid.getStyleClass().add("game-grid");

        for(int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints(40);
            grid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints(40);
            grid.getRowConstraints().add(row);
        }

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                Pane pane = new Pane();
                final int X = x + 1;
                final int Y = y * rows;
                pane.setOnMouseReleased(e -> {
                    System.out.println(X + Y );
                    pane.getChildren().add(PlayField.Anims.getAtoms(1));

                });
                pane.getStyleClass().add("game-grid-cell");
                if (x == 0) {
                    pane.getStyleClass().add("first-column");
                }
                if (y == 0) {
                    pane.getStyleClass().add("first-row");
                }
                grid.add(pane, x, y);
            }
        }


        Scene scene = new Scene(grid, (columns * 40) + 100, (rows * 40) + 100, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");

        return scene;
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}
