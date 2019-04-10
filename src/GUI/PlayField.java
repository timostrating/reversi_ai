package GUI;

import game_util.GameRules;
import game_util.GameRules.GameState;
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
import util.CompositionRoot;

public class PlayField {

   private static final Integer STARTTIME = 10;
    // Images
    private static Image o = new Image("GUI/pictures/o.png", 150, 150, false ,true);
    private static Image x = new Image("GUI/pictures/x.png",150,150,false,true);
    private static Image black = new Image("GUI/pictures/blackPiece.png", 60, 60, false, true);
    private static Image white = new Image("GUI/pictures/whitePiece.png",60, 60, false, true);
    private VBox[] panes;
    private int paneNr = 0;
    private Scene scene;
    private int player = 0;






    PlayField(int rows, int columns) {

        // Current Player
        Label currentPlayer = new Label("Kees");
        currentPlayer.setStyle("-fx-font-size: 3em;");
        HBox playerPane = new HBox();
        playerPane.setSpacing(200);
        playerPane.setAlignment(Pos.CENTER);
        playerPane.getChildren().add(currentPlayer);

        // Player List
        Label player1 = new Label("Player 1 has 10 points");
        Label player2 = new Label("Player 2 has 13 points");
        player1.setPadding(new Insets(50,0,0,0));
        player1.setStyle("-fx-font-size: 2em;");
        player2.setStyle("-fx-font-size: 2em;");
        VBox scorePane = new VBox();
        scorePane.getChildren().addAll(player1, player2);


        // Timer
        // Bind the timerLabel text property to the timeSeconds property
        Label timerLabel = new Label();
        IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 3em;");

        timeSeconds.set(STARTTIME);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(STARTTIME+1),
                        new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        playerPane.getChildren().add(timerLabel);


        GridPane game = new GridPane();
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
                pane.setStyle("-fx-background-color: Chartreuse;");
                pane.setStyle("-fx-background-size: 20px 20px;");
                pane.setOnMouseReleased(e -> {
                    System.out.println(X + Y );
                    setPaneNR(total);


                    //TicTacToe
                    if (player == 0 && rows == 3){
                        pane.getChildren().add(getPicture("x"));
                        pane.setDisable(true);
                        player = 1;
                    }
                    else if (player == 1 && rows == 3) {
                        pane.getChildren().add(getPicture("o"));
                        pane.setDisable(true);
                        player = 0;
                    }

                    //Reversi
                    if (player == 0 && rows == 8){
                        pane.getChildren().add(getPicture("black"));
                        pane.setDisable(true);
                        player = 1;
                    }
                    else if (player == 1 && rows == 8) {
                        pane.getChildren().add(getPicture("white"));
                        pane.setDisable(true);
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

        BorderPane totalPane = new BorderPane();
        totalPane.setTop(playerPane);
        totalPane.setCenter(game);
        totalPane.setRight(scorePane);


        scene = new Scene(totalPane, 1000,700, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");

    }

    private static ImageView getPicture(String player) {

        ImageView imageView = null;
        if (player.equals("x")) {
            imageView = new ImageView(x);
        }
        if (player.equals("o")) {
            imageView = new ImageView(o);
        }
        if (player.equals("black")) {
            imageView = new ImageView(black);
        }
        if (player.equals("white")) {
            imageView = new ImageView(white);
        }

        return imageView;
    }


    void setPicture(GameRules games, int i, int player) {
    i +=1;

    if (games instanceof TicTacToe) {
            if (player == 1) {
                panes[i].getChildren().add(getPicture("x"));
            }
            else if (player == 2) {
                panes[i].getChildren().add(getPicture("o"));
            }
        }
        else if (games instanceof Reversi) {
            if (player == 1) {
                panes[i].getChildren().add(getPicture("black"));
            }
            else if (player == 2) {
                panes[i].getChildren().add(getPicture("white"));
            }
        }
        System.out.println("zet " + i + " voor player " + player);

    }

    void displayWinScreen(GameState gamestate) {
        VBox winPane = new VBox();
        winPane.setAlignment(Pos.CENTER);
        winPane.setStyle("-fx-background-color: Chartreuse;");
        Label winningPlayer;
        if (gamestate == GameState.PLAYER_1_WINS) {
            winningPlayer = new Label("Player 1 has won!");
            winningPlayer.setStyle("-fx-font-size: 10em;");
            winPane.getChildren().add(winningPlayer);
        }
        else if (gamestate == GameState.PLAYER_2_WINS) {
            winningPlayer = new Label("Player 2 has won!");
            winningPlayer.setStyle("-fx-font-size: 10em;");
            winPane.getChildren().add(winningPlayer);
        }
        else if (gamestate == GameState.DRAW) {
            winningPlayer = new Label("It's a draw!");
            winningPlayer.setStyle("-fx-font-size: 10em;");
            winPane.getChildren().add(winningPlayer);
        }
        Scene winScene = new Scene(winPane,1000,700);
        CompositionRoot.getInstance().lobby.setScene(winScene);

    }


    public void setPaneNR(int position) { paneNr = position; }
    public int getPaneNr() { return paneNr; }
    public void resetPaneNR() { paneNr = -1; }
    public Scene getScene() {return scene; }
    public VBox[] getPane() {return panes; }
}
