package GUI;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PlayField {

   private GridPane grid;
   private Pane[][] panes;
   private int paneNr = 0;
   private Scene scene;

    public PlayField(int rows, int columns) {

        grid = new GridPane();
        panes = new Pane[rows][columns];
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
                panes[x][y] = pane;
                final int X = x + 1;
                final int Y = y * rows;
                pane.setOnMouseReleased(e -> {
                    System.out.println(X + Y );
                    setPaneNR(X, Y);
                    pane.getChildren().add(Anims.getAtoms(1));
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

        public static Node getAtoms(final int number) {
            Circle circle = new Circle(20, 20f, 7);
            circle.setFill(Color.BLACK);
            Group group = new Group();
            group.getChildren().add(circle);
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
