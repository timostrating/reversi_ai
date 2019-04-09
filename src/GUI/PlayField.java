package GUI;

import game_util.GameRules;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import reversi.Reversi;
import tic_tac_toe.TicTacToe;

public class PlayField {

   private GridPane game;
   private HBox playerPane;
   private Label currentPlayer;
   private VBox scorePane;
   private BorderPane totalPane;
   private VBox[] panes;
   private int paneNr = 0;
   private Scene scene;
   private static final Integer STARTTIME = 10;
   private Timeline timeline;
   private Label timerLabel = new Label();
   private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

   private int player = 0;

    // Images
    private static Image o = new Image("GUI/pictures/o.png", 150, 150, false ,true);
    private static Image x = new Image("GUI/pictures/x.png",150,150,false,true);
    private static Image black = new Image("GUI/pictures/blackPiece.png", 60, 60, false, true);
    private static Image white = new Image("GUI/pictures/whitePiece.png",60, 60, false, true);






    public PlayField(int rows, int columns) {

        currentPlayer = new Label("Kees");
        currentPlayer.setStyle("-fx-font-size: 3em;");
        playerPane = new HBox();
        playerPane.setSpacing(200);
        playerPane.setAlignment(Pos.CENTER);
        playerPane.getChildren().add(currentPlayer);

        Label player1 = new Label("Player 1 has 10 points");
        Label player2 = new Label("Player 2 has 13 points");
        player1.setPadding(new Insets(50,0,0,0));
        player1.setStyle("-fx-font-size: 2em;");
        player2.setStyle("-fx-font-size: 2em;");
        scorePane = new VBox();
        scorePane.getChildren().addAll(player1, player2);


        // Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 3em;");

        timeSeconds.set(STARTTIME);
        timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        playerPane.getChildren().add(timerLabel);


        game = new GridPane();
        game.setAlignment(Pos.CENTER);

        int a = rows;
        int b = columns;
        a += 1;
        b = b * b;
        int position = a + b;

        panes = new VBox[position];
        game.getStyleClass().add("game-grid");

        for(int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(200);
            game.getColumnConstraints().add(column);
        }

        for(int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(200);
            game.getRowConstraints().add(row);
        }

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                VBox pane = new VBox();
                pane.setAlignment(Pos.CENTER);
                final int X = x + 1;
                final int Y = y * rows;
                final int total = X + Y;
                panes[total] = pane;
                pane.setOnMouseReleased(e -> {
                    System.out.println(X + Y );
                    setPaneNR(X, Y);

                    //TicTacToe
                    if (player == 0 && rows == 3){
                        pane.getChildren().add(getPicture("x"));
                        player = 1;
                    }
                    else if (player == 1 && rows == 3) {
                        pane.getChildren().add(getPicture("o"));
                        player = 0;
                    }

                    //Reversi
                    if (player == 0 && rows == 8){
                        pane.getChildren().add(getPicture("black"));
                        player = 1;
                    }
                    else if (player == 1 && rows == 8) {
                        pane.getChildren().add(getPicture("white"));
                        player = 0;
                    }


                });
                pane.getStyleClass().add("game-grid-cell");
                if (x == 0) {
                    pane.getStyleClass().add("first-column");
                }
                if (y == 0) {
                    pane.getStyleClass().add("first-row");
                }
                game.add(pane, x, y);
            }
        }

        totalPane = new BorderPane();
        totalPane.setTop(playerPane);
        totalPane.setCenter(game);
        totalPane.setRight(scorePane);


        scene = new Scene(totalPane, 1000,700, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");

    }

        public static ImageView getPicture(String player) {

            ImageView imageView = null;
            if (player == "x") {
                imageView = new ImageView(x);
            }
            if (player == "o") {
                imageView = new ImageView(o);
            }
            if (player == "black") {
                imageView = new ImageView(black);
            }
            if (player == "white") {
                imageView = new ImageView(white);
            }

            return imageView;
        }

        //TODO speler meegeven
        public void setPicture(GameRules games, int i, int player) {
            if (games instanceof TicTacToe) {
                panes[i].getChildren().add(getPicture("x"));
            } else if (games instanceof Reversi) {
                panes[i].getChildren().add(getPicture("black"));
            }
            System.out.println("zet " + i + " voor player " + player);

        }


    public void setPaneNR(int x, int y) { paneNr = x + y; }
    public int getPaneNr() { return paneNr; }
    public void resetPaneNR() { paneNr = -1; }
    public Scene getScene() {return scene; }
    public VBox[] getPane() {return panes; }
}
