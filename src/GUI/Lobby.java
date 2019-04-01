package GUI;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Lobby extends Application {

    private static final Integer STARTTIME = 10;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

    static int paneNr = 0;
    @Override
    public void start(Stage primaryStage){

        //Pane
        FlowPane lobby = new FlowPane();
        VBox ticTacToe = new VBox();
        VBox reversi = new VBox();
      //  GridPane ticTacToeGrid = createPlayField(3,3);
        //GridPane reversiGrid = createPlayField(8,8);
        Pane timer = new Pane();

        //Timer
        createTimer();
        timer.getChildren().add(timerLabel);


        //Buttons
        Button spel1 = new Button("Tic Tac Toe");
        Button spel2 = new Button("Reversi");

        //Lobby
        lobby.getChildren().addAll(spel1, spel2, timer);
        Scene scene1 = new Scene(lobby, 200, 250);

        //TicTacToe
        //ticTacToe.getChildren().addAll(timer, ticTacToeGrid);
        //ticTacToe.setAlignment(Pos.BASELINE_CENTER);

        //Reversi
       // reversi.getChildren().addAll(timer, reversiGrid);

        // Games
        Scene ticTacToeScene = createPlayField(3,3);
        Scene reversiScene = createPlayField(8,8);

        // Stage
        primaryStage.setTitle("Lobby"); // Set the stage title
        primaryStage.setScene(scene1); // Place the scene in the stage
       // primaryStage.setMaximized(true);
        primaryStage.show(); // Display the stage


        //Register Buttons
        spel1.setOnAction(event -> primaryStage.setScene(ticTacToeScene));
        spel2.setOnAction(event -> primaryStage.setScene(reversiScene));

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
                    setPaneNR(X, Y);
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

    public static void setPaneNR(int x, int y) {
        paneNr = x + y;
    }

    public static int getPaneNr() {
        return paneNr;
    }

    public void createTimer() {

        //Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 4em;");

        if (timeline != null) {
            timeline.stop();
        }
        timeSeconds.set(STARTTIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();
    }




    public static void main(String[] args) {
        Application.launch(args);
    }
}
