package GUI;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BackgroundImage;
import network.Connection;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.CallbackWithParam;
import java.io.FileNotFoundException;


public class LoginView extends Application{
    private TextField loginTextField;
    private String username;
    private Connection connection;

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("Super Mario Bro's");
        connection = new Connection();

        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(10, 50, 50, 50));

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(20, 20, 20, 30));

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label loginLabel = new Label("Gebruikersnaam");
        loginTextField = new TextField();

        Button loginButton = new Button();
        loginButton.setText("Login");

        gridPane.add(loginLabel, 1,0);
        gridPane.add(loginTextField, 1,1);
        gridPane.add(loginButton, 1,2);

        borderPane.setId("borderPane");
        gridPane.setId("gridPane");
        loginButton.setId("loginButton");
        loginTextField.setId("loginTextField");

        borderPane.setStyle("-fx-background-image: url(\"GUI/pictures/kermit.png\");");

        loginButton.setOnAction(event -> {
            username = loginTextField.getText();
            connection.getFromServer().onPlayerList.register(onPlayerList);
            connection.getToServer().getPlayerList();
        });

        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        Scene scene = new Scene(borderPane);
        primaryStage.setScene(scene);

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
