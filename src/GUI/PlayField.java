package GUI;

import game_util.Arcade;
import game_util.Arcade.GameFactory;
import game_util.Arcade.PlayerFactory;
import game_util.Arcade.RefereeFactory;
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
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import network.Connection;
import reversi.Reversi;
import tic_tac_toe.TicTacToe;
import util.CallbackWithParam;
import util.CompositionRoot;
import util.Delegate;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayField {

    private static final int GAME_VIEW_SIZE = 600;
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
    Label player1, player2;
    Label currentPlayer;
    HBox playerPane;
    boolean switching = true;

    private GameRules gameRules;
    private boolean guiPlayerIsPlaying = false;
    LoginPane loginPane;
    String username;


    PlayField(int boardSize, GameRules gameRules) { this(boardSize, boardSize, gameRules); }
    PlayField(int rows, int columns, GameRules gameRules) {
        CompositionRoot.getInstance().lobby.playField = this;
        this.gameRules = gameRules;

        // Current
        currentPlayer = new Label("");
        currentPlayer.setStyle("-fx-font-size: 3em;");
        playerPane = new HBox();
        playerPane.setSpacing(200);
        playerPane.setAlignment(Pos.CENTER);
        playerPane.getChildren().addAll(currentPlayer, createTimer());


        // Player List
        player1 = new Label("Player 1");
        player2 = new Label("Player 2");
        player1.setPadding(new Insets(50, 0, 0, 0));
        player1.setStyle("-fx-font-size: 2em;");
        player2.setStyle("-fx-font-size: 2em;");
        VBox scorePane = new VBox();
        scorePane.getChildren().addAll(player1, player2);


        GridPane game = new GridPane();
        game.setAlignment(Pos.CENTER);

        panes = new VBox[rows * columns];
        game.getStyleClass().add("game-grid");

        for (int x = 0; x < columns; x++)
            game.getColumnConstraints().add(new ColumnConstraints((GAME_VIEW_SIZE / columns)));

        for (int y = 0; y < rows; y++)
            game.getRowConstraints().add(new RowConstraints((GAME_VIEW_SIZE / rows)));


        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                VBox pane = new VBox();
                pane.setAlignment(Pos.CENTER);
                final int total = y * rows + x; // This is required to be final so that the setOnMouseReleased lambda can use the memory
                panes[total] = pane;

                pane.setOnMouseReleased(e -> {
                    if (guiPlayerIsPlaying) {
                        System.out.println("GuiPlayer clicked on: ("+total % rows+", " +total / columns+") i = "+total);
                        setGuiPlayerInput(total);
                    }
                });
                pane.getStyleClass().add("game-grid-cell");
                if (x == 0) { pane.getStyleClass().add("first-column"); }
                if (y == 0) { pane.getStyleClass().add("first-row"); }
                game.add(pane, x, y);
            }
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
            if (gameRules.getPlayer(0).getName() != null) {
                winningPlayer = new Label(gameRules.getPlayer(0).getName() + " has won!");
            }
            else {
                winningPlayer = new Label("Player 1 has won!");
            }
        }
        else if (gamestate == GameState.PLAYER_2_WINS) {
            if (gameRules.getPlayer(1).getName() != null) {
                winningPlayer = new Label(gameRules.getPlayer(1).getName() + " has won!");
            }
            else {
                winningPlayer = new Label("Player 2 has won!");
            }
        }
        else { // else if (gamestate == GameState.DRAW)
            winningPlayer = new Label("It's a draw!");

        }
        winningPlayer.setStyle("-fx-font-size: 8em;");
        winPane.getChildren().add(winningPlayer);
        Button continueTournament = new Button("Door gaan met het toernooi");
        Button back = new Button("Terug naar lobby");

        continueTournament.setOnAction(e -> {
            Connection connection = CompositionRoot.getInstance().connection;
            connection.getToServer().subscribeGame("Reversi");
            BorderPane QueuePane = new QueuePane(true);
            Scene scene = new Scene(QueuePane, 500, 400);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });

        back.setOnAction(e -> {

            GridPane lobby = new LobbyPane();
            Scene scene1 = new Scene(lobby, 550, 300);
            Image cursor = new Image("GUI/pictures/kermitCursor.png");
            scene1.setCursor(new ImageCursor(cursor));
            CompositionRoot.getInstance().lobby.setScene(scene1);

        });
        winPane.getChildren().addAll(back, continueTournament);

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
                    int index = reversi.openPositions.get(i, playerNr);
                    panes[index].getChildren().add(getPicture("kermit"));
                    lijstje.add(index);
                }
            }
        });
    }

    public void resetGuiPlayerInput() {
        guiPlayerInput = -1;
    }

    public void redraw() {
        if (gameRules instanceof Reversi) { // TODO remove if possible
            if (gameRules.getPlayer(0).getName() != null) {
                player1.setText("Player 1: " + gameRules.getPlayer(0).getName());
                player2.setText("Player 2: " + gameRules.getPlayer(1).getName());
            }

            playerPane.getChildren().set(1,createTimer());
            playerPane.getChildren().set(0, getCurrentPlayer());

            Reversi reversi = (Reversi) gameRules;
            for (int i=0; i<panes.length; i++)
                panes[i].getChildren().clear();

            for (int i=0; i<panes.length; i++) {
                if (reversi.board.get(i) == 1)
                    panes[i].getChildren().add(getPicture("black"));
                if (reversi.board.get(i) == 2)
                    panes[i].getChildren().add(getPicture("white"));
            }
        }
    }

    public enum StandardGameType {
        OFFLINE_AI_VS_PLAYER(RefereeFactory.DefaultReferee, PlayerFactory.BestAvailableAI, PlayerFactory.GUIPlayer),
        OFFLINE_PLAYER_VS_AI(RefereeFactory.DefaultReferee, PlayerFactory.GUIPlayer, PlayerFactory.BestAvailableAI),
        ONLINE_AI_VS_REMOTE(RefereeFactory.NetworkedReferee, PlayerFactory.BestAvailableAI, PlayerFactory.RemotePlayer),
        ONLINE_HUMAN_VS_REMOTE(RefereeFactory.NetworkedReferee, PlayerFactory.BestAvailableAI, PlayerFactory.HumanPlayer),
        ONLINE_REMOTE_VS_AI(RefereeFactory.NetworkedReferee, PlayerFactory.RemotePlayer, PlayerFactory.BestAvailableAI),
        ONLINE_REMOTE_VS_HUMAN(RefereeFactory.NetworkedReferee, PlayerFactory.RemotePlayer, PlayerFactory.HumanPlayer);

        public final RefereeFactory refereeFactory;

        public final PlayerFactory first;
        public final PlayerFactory second;
        StandardGameType(RefereeFactory refereeFactory, PlayerFactory first, PlayerFactory second) {
            this.refereeFactory = refereeFactory;
            this.first = first;
            this.second = second;
        }

    }
    public static PlayField createGameAndPlayField(GameFactory gameFactory, StandardGameType standardGameType) {
        return createGameAndPlayField(gameFactory, standardGameType, (e)->{} );
    }

    public static PlayField createGameAndPlayField(GameFactory gameFactory, StandardGameType standardGameType, CallbackWithParam<GameRules> BeforeGameStart) {

        Arcade arcade = CompositionRoot.getInstance().arcade;
        GameRules game = arcade.createGame(gameFactory, standardGameType.refereeFactory, standardGameType.first, standardGameType.second);
        PlayField playField = new PlayField(gameFactory.boardSize, game);

        BeforeGameStart.callback(game);

        registerDefaultCallBacks(game, playField);
        new Thread(game).start();

        return playField;
    }
    private Label createTimer() {
        Integer startTime = 10;
        Label timerLabel = new Label();
        Timeline timeline = new Timeline();
        IntegerProperty timeSeconds = new SimpleIntegerProperty(startTime);
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 3em;");
        timeSeconds.set(startTime);
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(startTime + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        return timerLabel;
    }

    public Label getCurrentPlayer() {
        if(switching) {
            currentPlayer.setText(gameRules.getPlayer(0).getName());
            switching = false;
        } else if (!switching){
            currentPlayer.setText(gameRules.getPlayer(1).getName());
            switching = true;
        }
        return currentPlayer;
    }

    private static void registerDefaultCallBacks(GameRules game, PlayField playField) {
        Platform.runLater(playField::redraw); // TODO this is a hack

        game.onValidMovePlayed.register((pair0 -> {
            Platform.runLater(() -> playField.setPicture(game, pair0.getKey(), pair0.getValue()));
            if (game instanceof Reversi) {
                Platform.runLater(playField::redraw); // TODO this is a hack
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }));

        game.onGameEnded.register(() -> {
            Platform.runLater(() -> playField.displayWinScreen(game.getGameState()));
        });

        game.onValidMovePlayed.register(i -> {
            System.out.println(game);
        });
    }
}
