package Util;

import GUI.Lobby;
import javafx.application.Application;
import netwerk.Connection;
import sun.applet.Main;
import tic_tac_toe.TicTacToeEngine;

public class CompositionRoot {

    static CompositionRoot compositionRoot = null;

    public Lobby lobby;
    public Connection connection;
    public Arcade arcade;

    private CompositionRoot() {
        this.lobby = new Lobby();
        this.connection = new Connection();
        this.arcade = new Arcade();
    }

    public static CompositionRoot getInstance() {
        if (compositionRoot == null) {
             compositionRoot = new CompositionRoot();
        }
        return  compositionRoot;
    }

    public static void main(String[] args) {
        getInstance();
    }
}

