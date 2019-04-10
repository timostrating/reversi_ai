package util;

import GUI.Lobby;
import game_util.Arcade;
import network.Connection;

public class CompositionRoot {

    static CompositionRoot compositionRoot = null;

    public Lobby lobby;
    public Connection connection;
    public Arcade arcade;

    private CompositionRoot() {
        this.lobby = null; // This is the intended way
        this.connection = new Connection();
        this.arcade = new Arcade();
    }

    public static CompositionRoot getInstance() {
        if (compositionRoot == null) {
             compositionRoot = new CompositionRoot();
        }
        return  compositionRoot;
    }

    public void setLobby (Lobby lobby) {
        this.lobby = lobby;
    }
}

