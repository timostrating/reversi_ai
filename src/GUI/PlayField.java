package GUI;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PlayField {

   private GridPane game;
   private HBox playerPane;
   private HBox timerPane;
   private VBox scorePane;
   private BorderPane totalPane;
   private VBox[][] panes;
   private int paneNr = 0;
   private Scene scene;

   private int player = 0;

    // Images
    private static Image o = new Image("GUI/pictures/o.png", 40, 40, false ,true);
    private static Image x = new Image("GUI/pictures/x.png",40,40,false,true);
    private static Image black = new Image("GUI/pictures/blackPiece.png", 40, 40, false, true);
    private static Image white = new Image("GUI/pictures/whitePiece.png",40 , 40, false, true);






    public PlayField(int rows, int columns) {

        Label currentPlayer = new Label("Kees");
        playerPane = new HBox();
        playerPane.setPrefSize(400, 30);
        playerPane.getChildren().add(currentPlayer);

        Label player1 = new Label("Player 1 has 10 points");
        Label player2 = new Label("Player 2 has 13 points");
        scorePane = new VBox();
        scorePane.getChildren().addAll(player1, player2);


        game = new GridPane();

        panes = new VBox[rows][columns];
        game.getStyleClass().add("game-grid");

        for(int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPrefWidth(50);
            game.getColumnConstraints().add(column);
        }

        for(int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(50);
            game.getRowConstraints().add(row);
        }

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {
                VBox pane = new VBox();
                pane.setAlignment(Pos.CENTER);
                panes[x][y] = pane;
                final int X = x + 1;
                final int Y = y * rows;
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


        scene = new Scene(totalPane, (columns * 40) + 150, (rows * 40) + 150, Color.WHITE);
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


    public void setPaneNR(int x, int y) { paneNr = x + y; }
    public int getPaneNr() { return paneNr; }
    public void resetPaneNR() { paneNr = -1; }
    public Scene getScene() {return scene; }
    public Pane[][] getPane() {return panes; }
}
