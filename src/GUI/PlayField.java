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
import util.StringUtils;

import java.util.ArrayList;

public class PlayField {

    private static final int GAME_VIEW_SIZE = 600;
    // Images
    private static Image kermit = new Image("GUI/pictures/kermitPiece.gif", 60, 60, false,true),
            o = new Image("GUI/pictures/o.png", 60, 60, false, true),
            x = new Image("GUI/pictures/x.png", 60, 60, false, true),
            black = new Image("GUI/pictures/blackPiece.png", 60, 60, false, true),
            white = new Image("GUI/pictures/whitePiece.png", 60, 60, false, true);
    private VBox[] panes;
    public volatile int guiPlayerInput = -1; // MOET -1 zijn in het begin GuiPlayer heeft infiniate loop op de verandering van deze variable
    private Scene scene;
    private Label player1;
    private Label player2;
    private Label currentPlayer;
    private HBox currentPlayerPane, timerPane;
    private boolean switching = true;
    private GameRules gameRules;
    private boolean guiPlayerIsPlaying = false;
    private VBox winPane;

    private PlayField(int boardSize, GameRules gameRules) { this(boardSize, boardSize, gameRules); }
    private PlayField(int rows, int columns, GameRules gameRules) {
        CompositionRoot.getInstance().lobby.playField = this;
        this.gameRules = gameRules;

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: white;");

        // Current player
        currentPlayer = new Label("");
        currentPlayer.setStyle("-fx-font-size: 3em;");
        currentPlayer.setPadding(new Insets(10, 0,0,40));
        currentPlayerPane = new HBox();
        currentPlayerPane.getChildren().add(currentPlayer);

        // Timer
        timerPane = new HBox();
        timerPane.setPadding(new Insets(10, 350,0,0));
        timerPane.getChildren().add(createTimer());

        BorderPane top = new BorderPane();
        top.setStyle("-fx-background-color: white; -fx-border-width: 0 0 2 0; -fx-border-color: black;");
        top.setLeft(currentPlayerPane);
        top.setRight(timerPane);

        // forfeit button
        Button forfeitButton = new Button("Opgeven");

        // Player List
        player1 = new Label("");
        player2 = new Label("");
        player1.setPadding(new Insets(10, 20, 0, 20));
        player2.setPadding(new Insets(10, 20, 0, 20));
        player1.setStyle("-fx-font-size: 2em;");
        player2.setStyle("-fx-font-size: 2em;");
        ImageView blackGamePiece = new ImageView(black);
        ImageView whiteGamePiece = new ImageView(white);
        VBox scorePane = new VBox();
        gridPane.add(blackGamePiece, 0,0);
        gridPane.add(whiteGamePiece, 0,1);
        gridPane.add(player1, 1,0);
        gridPane.add(player2, 1,1);
        gridPane.add(forfeitButton, 0,4);
        scorePane.getChildren().addAll(gridPane);



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
        totalPane.setStyle("-fx-background-color: white;");
        totalPane.setTop(top);
        totalPane.setCenter(game);
        totalPane.setRight(scorePane);

        scene = new Scene(totalPane, 1000, 700, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");

        forfeitButton.setOnAction(event -> CompositionRoot.getInstance().connection.getToServer().setForfeit());



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


    private void setPicture(GameRules games, int i, int player) {
        if (games instanceof TicTacToe) {
            panes[i].getChildren().add(getPicture((player == 1) ? "x" : "o"));
        }
        else if (games instanceof Reversi) {
            panes[i].getChildren().add(getPicture((player == 1) ? "black" : "white"));
        }
        System.out.println("zet " + i + " voor player " + player);

    }


    private void displayWinScreen(GameState gamestate) {
        winPane = new VBox();
        winPane.getStylesheets().add("GUI/playFieldStyle.css");
        winPane.setAlignment(Pos.CENTER);
        Label winningPlayer;
        if (gamestate == GameState.PLAYER_1_WINS) {
            if (gameRules.getPlayer(0).getName() != null) {
                if(gameRules.getPlayer(0).getName().equals(LoginPane.username)) {
                    winningPlayer = new Label("Je hebt gewonnen!");
                    setWinStyle();
                } else {
                    winningPlayer = new Label("Je hebt verloren!");
                    setLoseStyle();
                }
            } else {
                winningPlayer = new Label("Speler 1 heeft gewonnen!");
            }
        } else if (gamestate == GameState.PLAYER_2_WINS) {
            if (gameRules.getPlayer(1).getName() != null) {
                if (gameRules.getPlayer(1).getName().equals(LoginPane.username)) {
                    winningPlayer = new Label("Je hebt gewonnen!");
                    setWinStyle();
                } else {
                    winningPlayer = new Label("Je hebt verloren!");
                    setLoseStyle();
                }
            } else {
                winningPlayer = new Label("Speler 2 heeft gewonnen!");
            }
        }  else { // else if (gamestate == GameState.DRAW)
            winningPlayer = new Label("Het is gelijk spel geworden!");
            winPane.setStyle("-fx-background-image: url(\"GUI/pictures/pokemonHand.gif\");\n" +
                    "-fx-background-repeat: stretch;   \n" +
                    "-fx-background-size: 1000 700;\n" +
                    "-fx-background-position: center center;\n" +
                    "-fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");
        }

        winningPlayer.setStyle("-fx-font-size: 6em; -fx-text-fill: white;");
        winPane.getChildren().add(winningPlayer);
        Label padLab = new Label("");
        Button continueTournament = new Button("Doorgaan met het toernooi");
        Button back = new Button("Terug naar lobby");


        continueTournament.setOnAction(e -> {
            Connection connection = CompositionRoot.getInstance().connection;
            connection.getToServer().subscribeGame("Reversi");
            BorderPane QueuePane = new QueuePane(true);
            Scene scene = new Scene(QueuePane, 576, 316);
            CompositionRoot.getInstance().lobby.setScene(scene);
        });

        back.setOnAction(e -> {

            GridPane lobby = new LobbyPane();
            Scene scene1 = new Scene(lobby, 576, 316);
            Image cursor = new Image("GUI/pictures/kermitCursor.png");
            scene1.setCursor(new ImageCursor(cursor));
            CompositionRoot.getInstance().lobby.setScene(scene1);

        });
        winPane.getChildren().addAll(continueTournament, padLab, back);

        Scene winScene = new Scene(winPane, 1000, 700);
        CompositionRoot.getInstance().lobby.setScene(winScene);

    }


    public Scene getScene() { return scene; }

    private void setWinStyle(){
        winPane.setStyle("-fx-background-image: url(\"GUI/pictures/kermitDance.gif\");;\n" +
                "    -fx-background-repeat: stretch;   \n" +
                "    -fx-background-size: 1000 700;\n" +
                "    -fx-background-position: center center;\n");
    }

    private void setLoseStyle(){
        winPane.setStyle("-fx-background-image: url(\"GUI/pictures/kermitDark.gif\");\n" +
                "-fx-background-repeat: stretch;   \n" +
                "-fx-background-size: 1000 700;\n" +
                "-fx-background-position: center center;\n" +
                "-fx-effect: dropshadow(three-pass-box, black, 30, 0.5, 0, 0);");

    }

    private ArrayList<Integer> lijstje = new ArrayList<>();
    private void setGuiPlayerInput(int position) {
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

    private void redraw() {
        if (gameRules instanceof Reversi) { // TODO remove if possible
            if (gameRules.getPlayer(0).getName() != null) {
                player1.setText(StringUtils.capitalize(gameRules.getPlayer(0).getName()));
                player2.setText(StringUtils.capitalize(gameRules.getPlayer(1).getName()));
                currentPlayerPane.getChildren().set(0, getCurrentPlayer());
            }

            timerPane.getChildren().set(0,createTimer());

            Reversi reversi = (Reversi) gameRules;
            for (VBox pane : panes) pane.getChildren().clear();
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
       // OFFLINE_PLAYER_VS_AI(RefereeFactory.DefaultReferee, PlayerFactory.GUIPlayer, PlayerFactory.BestAvailableAI),
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

    private Label getCurrentPlayer() {
        if(switching) {
            currentPlayer.setText("Aan zet: " + StringUtils.capitalize(gameRules.getPlayer(0).getName()));
            switching = false;
        } else {
            currentPlayer.setText("Aan zet: " + StringUtils.capitalize(gameRules.getPlayer(1).getName()));
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

        game.onGameEnded.register(() -> Platform.runLater(() -> playField.displayWinScreen(game.getGameState())));
        game.onValidMovePlayed.register(i -> System.out.println(game));
    }
}
