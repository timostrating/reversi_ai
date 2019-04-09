package GUI;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import network.Connection;
import util.CallbackWithParam;
import util.CompositionRoot;

public class LoginView extends BorderPane{
    private String username;
    private Connection connection;
    Alert alertInfo;

    public LoginView(Stage primaryStage){
        connection = CompositionRoot.getInstance().connection;
        //Adding icon and title to the title bar
        primaryStage.getIcons().add(new Image("/GUI/pictures/kermitIcon.jpg"));
        primaryStage.setTitle("Super henk Bro's");

        alertInfo = new Alert(Alert.AlertType.INFORMATION);

        this.setPadding(new Insets(5, 0, 0, 60));

        HBox hBox = new HBox();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 0, 0, 0));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label hostLabel = new Label("Host");
        Label ipAdressLabel = new Label("Ip adress");
        TextField hostText = new TextField("localhost");
        TextField ipAdressText = new TextField("7789");

        Label loginLabel = new Label("Gebruikersnaam");
        TextField loginTextField = new TextField();
        Button loginButton = new Button();

        loginButton.setText("Login");

        this.setStyle("-fx-background-image: url(\"GUI/pictures/kermit.jpg\");");
        loginLabel.setStyle("-fx-font-size: 20; -fx-padding: 5 20 0 20");
        loginTextField.setStyle("-fx-background-color: rgb(255,255,255,0.7); -fx-text-fill: black; -fx-border-color: rgb(0,0,0,0.3); -fx-border-width: 3; -fx-border-radius: 3;");
        loginButton.setStyle("-fx-font-size: 15; -fx-padding: 6 50 6 50;");
        hostLabel.setStyle("-fx-font-size: 12");
        ipAdressLabel.setStyle("-fx-font-size: 12");
        hostText.setStyle("-fx-font-size: 12");
        ipAdressText.setStyle("-fx-font-size: 12");

        gridPane.add(hostLabel, 0, 0);
        gridPane.add(hostText, 1, 0);
        gridPane.add(ipAdressLabel, 0, 1);
        gridPane.add(ipAdressText, 1, 1);
        gridPane.add(loginLabel, 0,8, 2, 1);
        gridPane.add(loginTextField, 0,9, 2,1);
        gridPane.add(loginButton, 0,10,2,1);

        this.setId("borderPane");
        gridPane.setId("gridPane");
        loginButton.setId("loginButton");
        loginTextField.setId("text-field");

        gridPane.setHalignment(loginButton, HPos.CENTER);
        gridPane.setHalignment(loginLabel, HPos.CENTER);

        loginButton.setOnAction(event -> {
            if(connection.connect(hostText.getText(), Integer.parseInt(ipAdressText.getText()))) {
                username = loginTextField.getText();
                connection.getFromServer().onPlayerList.register(onPlayerList);
                connection.getToServer().getPlayerList();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("De server is niet berijkbaar");
                alert.setHeaderText(null);
                alert.setContentText("Verbind met een andere server");
                alert.showAndWait();
            }
        });

        this.setTop(hBox);
        this.setCenter(gridPane);

        primaryStage.setResizable(false);
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
            connection.getToServer().setLogin(username);
            FlowPane lobby = new LobbyPane();
            Scene scene1 = new Scene(lobby, 600, 600);
            CompositionRoot.getInstance().lobby.setScene(scene1);
        } else {
            if (username.equals("")) {
                Platform.runLater(() -> {
                alertInfo.setTitle("Verkeerde gebruikersnaam");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("Voer een geldige naam in");
                alertInfo.showAndWait();
                });
            } else {
                Platform.runLater(() -> {
                    alertInfo.setTitle("Verkeerde gebruikersnaam");
                    alertInfo.setHeaderText(null);
                    alertInfo.setContentText("Deze naam is al in gebruik");
                    alertInfo.showAndWait();
                });

            }
        }
        connection.getFromServer().onPlayerList.unregister(onPlayerList);
    }
}
