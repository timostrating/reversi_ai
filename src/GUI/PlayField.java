package GUI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class PlayField extends Application{
    @Override
    public void start(final Stage stage) throws Exception {
        int rows = 8;
        int columns = 8;

        stage.setTitle("Reversi");

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

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                Pane pane = new Pane();
                pane.setOnMouseReleased(e -> {
                    pane.getChildren().add(Anims.getAtoms(1));
                });
                pane.getStyleClass().add("game-grid-cell");
                if (i == 0) {
                    pane.getStyleClass().add("first-column");
                }
                if (j == 0) {
                    pane.getStyleClass().add("first-row");
                }
                grid.add(pane, i, j);
            }
        }


        Scene scene = new Scene(grid, (columns * 40) + 100, (rows * 40) + 100, Color.WHITE);
        scene.getStylesheets().add("/GUI/game.css");
        stage.setScene(scene);
        stage.show();
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

    public static void main(final String[] arguments) {
        Application.launch(arguments);
    }
}