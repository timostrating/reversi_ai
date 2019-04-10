package GUI;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TimerTest extends Application {

    private static final Integer STARTTIME = 10;
    private Timeline timeline;
    private Label timerLabel = new Label();
    private IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("FX Timer with Binding");
        Group root = new Group();
        Scene scene = new Scene(root, 300, 250);

        // Bind the timerLabel text property to the timeSeconds property
        timerLabel.textProperty().bind(timeSeconds.asString());
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 4em;");

        Button button = new Button();
        button.setText("Start Timer");
        button.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {
                if (timeline != null) {
                    timeline.stop();
                }
                timeSeconds.set(STARTTIME);
                timeline = new Timeline();
                timeline.getKeyFrames().add(
                        new KeyFrame(Duration.seconds(STARTTIME+1),
                                new KeyValue(timeSeconds, 0)));
                timeline.playFromStart();
            }
        });

        VBox vb = new VBox(20);             // gap between components is 20
        vb.setAlignment(Pos.CENTER);        // center the components within VBox

        vb.setPrefWidth(scene.getWidth());
        vb.getChildren().addAll(button, timerLabel);
        vb.setLayoutY(30);

        root.getChildren().add(vb);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}