package GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Connection;
import util.CallbackWithParam;

public class LoginView extends Application{
    private TextField loginTextField;
    private String username;
    private Connection connection;

    @Override
    public void start(Stage primaryStage){
        //Adding icon and title to the title bar
        primaryStage.getIcons().add(new Image("/GUI/pictures/kermitIcon.jpg"));
        primaryStage.setTitle("Super Kermit Bro's");

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        connection = new Connection();

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(60, 0, 0, 60));

        HBox hBox = new HBox();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 0, 0, 0));
        gridPane.setHgap(5);
        gridPane.setVgap(20);

        Label loginLabel = new Label("Gebruikersnaam");
        loginTextField = new TextField();
        Button loginButton = new Button();

        loginButton.setText("Login");

        gridPane.add(loginLabel, 0,0);
        gridPane.add(loginTextField, 0,1);
        gridPane.add(loginButton, 0,2);

        borderPane.setId("borderPane");
        gridPane.setId("gridPane");
        loginButton.setId("loginButton");
        loginTextField.setId("text-field");

        borderPane.setStyle("-fx-background-image: url(\"GUI/pictures/kermit.jpg\");");
        loginLabel.setStyle("-fx-font-size: 20; -fx-padding: 5 20 0 20");
        loginTextField.setStyle("-fx-background-color: rgb(255,255,255,0.7); -fx-text-fill: black; -fx-border-color: rgb(0,0,0,0.3); -fx-border-width: 3; -fx-border-radius: 3;");
        loginButton.setStyle("-fx-font-size: 15; -fx-padding: 6 50 6 50;");

        gridPane.setHalignment(loginButton, HPos.CENTER);
        gridPane.setHalignment(loginLabel, HPos.CENTER);

        loginButton.setOnAction(event -> {
            username = loginTextField.getText();
            connection.getFromServer().onPlayerList.register(onPlayerList);
            connection.getToServer().getPlayerList();
        });

        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane, 576, 316);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();
    }

    private CallbackWithParam<String[]> onPlayerList = this::validatePlayer;

    public void validatePlayer(String[] playerList){
        boolean validatePlayer = true;
        for(String player : playerList){
            if(username.toLowerCase().equals(player.toLowerCase())){
                validatePlayer = false;
            }
        }
        if(validatePlayer) {
            System.out.println("Succes!");
            connection.getToServer().setLogin(username);
        } else {
            System.out.println("Username already used!");
        }
        connection.getFromServer().onPlayerList.unregister(onPlayerList);
    }
}
