package GUI;

import game_util.GameRules;
import game_util.GameRules.GameState;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import reversi.Reversi;
import tic_tac_toe.TicTacToe;
import util.CompositionRoot;

import java.util.ArrayList;

public class PlayField {

    private static final Integer STARTTIME = 10; // TODO not hardcode
    // Images
    private static Image kermit = new Image("GUI/pictures/Kermitgezicht.jpg", 60, 60, false,true);
    private static Image o = new Image("GUI/pictures/o.png", 60, 60, false, true);
    private static Image x = new Image("GUI/pictures/x.png", 60, 60, false, true);
    private static Image black = new Image("GUI/pictures/blackPiece.png", 60, 60, false, true);
    private static Image white = new Image("GUI/pictures/whitePiece.png", 60, 60, false, true);
    private VBox[] panes;
    public volatile int guiPlayerInput = -1; // MOET -1 zijn in het begin GuiPlayer heeft infiniate loop op de verandering van deze variable
    private Scene scene;
    private int player = 0;

    private GameRules gameRules;
    private boolean guiPlayerIsPlaying = false;


    PlayField(int rows, int columns, GameRules gameRules) {
        CompositionRoot.getInstance().lobby.playField = this;
        this.gameRules = gameRules;

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
        player1.setPadding(new Insets(50, 0, 0, 0));
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
                new KeyFrame(Duration.seconds(STARTTIME + 1),
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

        for (int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(200);
            game.getColumnConstraints().add(column);
        }

        for (int i = 0; i < rows; i++) {
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
                    if (guiPlayerIsPlaying) { // TODO test

//                        System.out.println(X + Y);
                        setGuiPlayerInput(total);

                        if (gameRules instanceof TicTacToe) {
                            pane.getChildren().add(getPicture((player == 0) ? "o" : "x"));
                            pane.setDisable(true);
                        }
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

        //Setting Start board reversi
        if (rows ==8) {
            panes[28].getChildren().add(getPicture("black"));
            panes[28].setDisable(true);
            panes[29].getChildren().add(getPicture("white"));
            panes[29].setDisable(true);
            panes[36].getChildren().add(getPicture("white"));
            panes[36].setDisable(true);
            panes[37].getChildren().add(getPicture("black"));
            panes[37].setDisable(true);
        }


        BorderPane totalPane = new BorderPane();
        totalPane.setTop(playerPane);
        totalPane.setCenter(game);
        totalPane.setRight(scorePane);

        scene = new Scene(totalPane, 1000, 700, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");

    }

    private static ImageView getPicture(String player) {

        ImageView imageView = null;
        if (player.equals("x")) { imageView = new ImageView(x); }
        if (player.equals("o")) { imageView = new ImageView(o); }
        if (player.equals("black")) { imageView = new ImageView(black); }
        if (player.equals("white")) { imageView = new ImageView(white); }
        if (player.equals("kermit")) { imageView = new ImageView(kermit); }

        return imageView;
    }


    void setPicture(GameRules games, int i, int player) {
        i += 1;

        if (games instanceof TicTacToe) {
            panes[i].getChildren().add(getPicture((player == 1) ? "x" : "o"));
        }
        else if (games instanceof Reversi) {
            panes[i].getChildren().add(getPicture((player == 1) ? "black" : "white"));
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
        }
        else if (gamestate == GameState.PLAYER_2_WINS) {
            winningPlayer = new Label("Player 2 has won!");
        }
        else { // else if (gamestate == GameState.DRAW)
            winningPlayer = new Label("It's a draw!");

        }
        winningPlayer.setStyle("-fx-font-size: 8em;");
        winPane.getChildren().add(winningPlayer);

        Scene winScene = new Scene(winPane, 1000, 700);
        CompositionRoot.getInstance().lobby.setScene(winScene);

    }


    public Scene getScene() { return scene; }
    public VBox[] getPane() { return panes; }

    ArrayList<Integer> lijstje = new ArrayList<>();
    public void setGuiPlayerInput(int position) {
        guiPlayerInput = position;
        guiPlayerIsPlaying = false;
        for (int index : lijstje)
            panes[index].getChildren().remove(0);

        lijstje.clear();
    }
    public void currentTurnIsGuiPlayersTurn() {
        Platform.runLater( () -> {
            guiPlayerIsPlaying = true;
            int playerNr = 2; // TODO HARDCODED
            if (gameRules instanceof Reversi) {
                Reversi reversi = (Reversi) gameRules;
                for (int i=0; i<reversi.openPositions.size(playerNr); i++) {
                    int index = reversi.openPositions.get(i, playerNr) + 1;
                    panes[index].getChildren().add(getPicture("kermit"));
                    lijstje.add(index);
                }
            }
        });
    }

    public void resetGuiPlayerInput() {
        guiPlayerInput = -1;
    }
}
