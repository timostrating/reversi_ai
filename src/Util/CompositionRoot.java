package Util;

import GUI.Lobby;
import javafx.application.Application;
import sun.applet.Main;
import tic_tac_toe.TicTacToeEngine;

public class CompositionRoot {

    static CompositionRoot compositionRoot = null;

    public Lobby lobby;

    public TicTacToeEngine ticTacToeEngine;

    private CompositionRoot() {

        this.lobby = new Lobby();
        this.ticTacToeEngine = new TicTacToeEngine();

    }

    public static CompositionRoot createCompostionRoot() {
        if (compositionRoot == null) {
             compositionRoot = new CompositionRoot();
        }
        return  compositionRoot;
    }

    public static void main(String[] args) {
        createCompostionRoot();
    }



}

