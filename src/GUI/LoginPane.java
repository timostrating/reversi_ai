package GUI;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.ImageCursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import network.Connection;
import util.CallbackWithParam;
import util.CompositionRoot;

public class LoginPane extends BorderPane {
    public static String username;
    private Connection connection;
    Alert alertInfo;

    public LoginPane(){
        connection = CompositionRoot.getInstance().connection;

        //Adding icon and title to the title bar

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

        hostText.setFocusTraversable(false);
        ipAdressText.setFocusTraversable(false);
        loginTextField.setFocusTraversable(true);

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

        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(loginLabel, HPos.CENTER);

        loginButton.setOnAction(event -> {
            if(connection.connect(hostText.getText(), Integer.parseInt(ipAdressText.getText()))) {
                username = loginTextField.getText();
                connection.getFromServer().onPlayerList.register(onPlayerList);
                connection.getToServer().getPlayerList();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("De server is niet bereikbaar");
                alert.setHeaderText(null);
                alert.setContentText("Verbind met een andere server");
                alert.showAndWait();
            }
        });

        this.setTop(hBox);
        this.setCenter(gridPane);

        //LoginButton with pressing Enter
        loginTextField.setOnKeyPressed(event -> { if(event.getCode() == KeyCode.ENTER){ loginButton.fire(); } });
    }

    private CallbackWithParam<String[]> onPlayerList = this::validatePlayer;

    public void validatePlayer(String[] playerList){
        if(validPlayerName(playerList)) {
            connection.getToServer().setLogin(username);
            GridPane lobby = new LobbyPane();
            Scene scene1 = new Scene(lobby, 550, 300);
            Image cursor = new Image("GUI/pictures/kermitCursor.png");
            scene1.setCursor(new ImageCursor(cursor));
            CompositionRoot.getInstance().lobby.setScene(scene1);
        } else {
            Platform.runLater(() -> {
                alertInfo.setHeaderText(null);
                alertInfo.setContentText(username.equals("") ? "Voer een geldige naam in" : "Deze naam is al in gebruik");
                alertInfo.showAndWait();
            });
        }
        connection.getFromServer().onPlayerList.unregister(onPlayerList);
    }

    private boolean validPlayerName(String[] playerList) {
        for(String player : playerList){
            if(username.toLowerCase().equals(player.toLowerCase())){
                return false;
            }
        }
        return true;
    }
}
