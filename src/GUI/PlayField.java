package GUI;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayField {

   private GridPane grid;
   private VBox[][] panes;
   private int paneNr = 0;
   private Scene scene;

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
                    pane.getChildren().add(Anims.getAtoms());

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

        public static Node getAtoms() {
            Image img = new Image("GUI/pictures/x.png",40,40,false,true);
            ImageView imageView = new ImageView(img);
            imageView.autosize();
            Circle circle = new Circle(20);
            circle.setFill(Color.BLACK);
            Group group = new Group();
            group.getChildren().add(imageView);
//            SubScene scene = new SubScene(group, 40, 40);
//            scene.setFill(Color.TRANSPARENT);
            return group;
        }
    }

    public void setPaneNR(int x, int y) { paneNr = x + y; }
    public int getPaneNr() { return paneNr; }
    public void resetPaneNR() { paneNr = -1; }
    public Scene getScene() {return scene; }
    public Pane[][] getPane() {return panes; }
}
