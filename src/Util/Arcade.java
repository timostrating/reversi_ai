package Util;

import GUI.GUIPlayer;
import netwerk.RemotePlayer;
import reversi.Reversi;
import tic_tac_toe.TicTacToe;
import tic_tac_toe.TicTacToeAIMiniMax;
import tic_tac_toe.TicTacToeAIScore;

/**
 * The Arcade class is a Factory for the games and different kind of players
 */
public class Arcade {

    @FunctionalInterface interface China { Object aliExpress(GameRules game); }

    public enum GameFactory {
        TicTacToe(g -> new TicTacToe()),
        Reversi(g -> new Reversi());

        private China china;
        private GameFactory(China china) { this.china = china; }
        public GameRules toObject() { return (GameRules) china.aliExpress(null); }
    }

    public enum PlayerFactory {
        HumanPlayer(g -> new HumanPlayer()),
        GUIPlayer(g -> new GUIPlayer()),
        RemotePlayer(g -> new RemotePlayer()),
        TicTacToeAIScore(g -> new TicTacToeAIScore((TicTacToe) g)),
        TicTacToeAIMiniMax(g -> new TicTacToeAIMiniMax((TicTacToe) g));

        private China china;
        private PlayerFactory(China china) { this.china = china; }
        public Player toObject(GameRules game) { return (Player) china.aliExpress(game); }
    }

    public enum RefereeFactory {
        DefaultReferee(g -> new DefaultReferee(g));

        private China china;
        private RefereeFactory(China china) { this.china = china; }
        public Referee toObject(GameRules game) { return (Referee) china.aliExpress(game); }
    }

    public GameRules createGame(GameFactory gameType, RefereeFactory refereeType, PlayerFactory... ps) {
        GameRules game = gameType.toObject();
//        game.onNextPlayer.register(() -> System.out.println(game));
//        game.onGameEnded.register(() -> System.err.println(game));

        Player[] players = new Player[ps.length];
        for(int i=0; i<ps.length; i++)
            players[i] = ps[i].toObject(game);

        game.initialize(refereeType.toObject(game), players);
        return game;
    }

    public static void main(String[] args) {
        Arcade arcade = new Arcade();

        GameRules game = arcade.createGame(GameFactory.Reversi, RefereeFactory.DefaultReferee, PlayerFactory.HumanPlayer, PlayerFactory.HumanPlayer);

        game.run();
    }
}