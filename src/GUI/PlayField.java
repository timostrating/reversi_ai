package GUI;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class PlayField {

   private GridPane grid;
   private VBox[][] panes;
   private int paneNr = 0;
   private Scene scene;

    // Images
    private static Image o = new Image("GUI/pictures/o.png", 40, 40, false ,true);
    private static Image x = new Image("GUI/pictures/x.png",40,40,false,true);
    private static Image black = new Image("GUI/pictures/blackPiece.png", 40, 40, false, true);
    private static Image white = new Image("GUI/pictures/whitePiece.png",40 , 40, false, true);


    public PlayField(int rows, int columns) {

        grid = new GridPane();
        panes = new VBox[rows][columns];
        grid.getStyleClass().add("game-grid");

        for(int i = 0; i < columns; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(50);
            grid.getColumnConstraints().add(column);
        }

        for(int i = 0; i < rows; i++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(50);
            grid.getRowConstraints().add(row);
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
                    //TODO player meegeven
                    pane.getChildren().add(Anims.getPicture("white"));

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

        scene = new Scene(grid, (columns * 40) + 100, (rows * 40) + 100, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");

    }

    public static class Anims {

        public static Node getPicture(String player) {

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

            Group group = new Group();
            group.getChildren().add(imageView);
//
            return group;
        }
    }

    public void setPaneNR(int x, int y) { paneNr = x + y; }
    public int getPaneNr() { return paneNr; }
    public void resetPaneNR() { paneNr = -1; }
    public Scene getScene() {return scene; }
    public Pane[][] getPane() {return panes; }
}
