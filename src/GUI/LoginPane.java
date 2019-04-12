package GUI;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import network.Connection;
import util.CallbackWithParam;
import util.CompositionRoot;

public class LoginPane extends BorderPane {
    public static String username;
    private Connection connection;
    private Alert alertInfo;

    LoginPane(){
        connection = CompositionRoot.getInstance().connection; // get the connection from the compositionroot

        alertInfo = new Alert(Alert.AlertType.INFORMATION); // creating new Alert for information

        this.setPadding(new Insets(5, 0, 0, 60)); //set padding of the Borderpane

        this.getStylesheets().add("GUI/loginPaneStyle.css");

        HBox hBox = new HBox(); //create hbox

        // create gridpane and set padding and v, h gap
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0, 0, 0, 0));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        Label marLab = new Label("");

        // create labels and textfields for the borderpane (connection)
        Label hostLabel = new Label("Host");
        Label ipAdressLabel = new Label("Ip adress");
        TextField hostText = new TextField("localhost");
        TextField ipAdressText = new TextField("7789");

        //create label, textfield and button to log in
        Label loginLabel = new Label("Gebruikersnaam");
        TextField loginTextField = new TextField();
        Button loginButton = new Button("Login");

        loginLabel.setStyle("-fx-font: 24 arial;");
        GridPane.setHalignment(loginTextField, HPos.CENTER);

        // set focus on the login text field
        hostText.setFocusTraversable(false);
        ipAdressText.setFocusTraversable(false);
        loginTextField.setFocusTraversable(true);

        // set style to all the elements of the borderpane
        this.setStyle("-fx-background-image: url(\"GUI/pictures/kermit.jpg\");");
        this.setTop(hBox);
        this.setCenter(gridPane);

        // add all nodes to the borderpane
        gridPane.add(hostLabel, 0, 0);
        gridPane.add(hostText, 1, 0);
        gridPane.add(ipAdressLabel, 0, 1);
        gridPane.add(ipAdressText, 1, 1);
        gridPane.add(loginLabel, 0,8, 2, 1);
        gridPane.add(loginTextField, 0,10, 2,1);
        gridPane.add(marLab, 0,11,2,1);
        gridPane.add(loginButton, 0,12,2,1);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        GridPane.setHalignment(loginLabel, HPos.CENTER);

        loginLabel.setPadding(new Insets(0, 0,20,0));

        // login button action
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

        loginTextField.setOnKeyPressed(event -> { if(event.getCode() == KeyCode.ENTER){ loginButton.fire(); } }); //LoginButton with pressing the enter key
    }

    // check if name is valid
    private void validateUsername(String[] playerList){
        if(availableUsername(playerList)) {
            connection.getToServer().setLogin(username);
            GridPane lobby = new LobbyPane();
            Scene scene1 = new Scene(lobby, 576, 316);
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

    // check if name is available
    private boolean availableUsername(String[] playerList) {
        for(String player : playerList){
            if(username.toLowerCase().equals(player.toLowerCase())){
                return false;
            }
        }
        return true;
    }

    //action on getting notified from the callback
    private CallbackWithParam<String[]> onPlayerList = this::validateUsername;
}
