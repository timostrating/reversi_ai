package Util;

import GUI.GUIPlayer;
import netwerk.RemotePlayer;
import tic_tac_toe.TicTacToe;
import tic_tac_toe.TicTacToeAIMiniMax;
import tic_tac_toe.TicTacToeAIScore;

/**
 * The Arcade class is a Factory for the games and different kind of players
 */
public class Arcade {

    public enum GameFactory {
        TicTacToe;
//        Reversi(Reversi)

        public GameRules toObject(){
            switch (this.ordinal()) {
                case 0: return new TicTacToe();
//                case 1: return new Reversi();
            }
            return null;
        }
    }

    public enum PlayerFactory {
        HumanPlayer,
        GUIPlayer,
        RemotePlayer,
        TicTacToeAIScore,
        TicTacToeAIMiniMax;

        public Player toObject(GameRules game) {
            switch (this.ordinal()) {
                case 0: return new HumanPlayer();
                case 1: return new GUIPlayer();
                case 2: return new RemotePlayer();
                case 3: return new TicTacToeAIScore((TicTacToe) game);
                case 4: return new TicTacToeAIMiniMax((TicTacToe) game);
            }
            return null;
        }
    }

    public GameRules createGame(GameFactory gameType, PlayerFactory... ps) {
        GameRules game = gameType.toObject();
        game.onNextPlayer.register(() -> System.out.println(game));
        game.onGameOver.register(() -> System.err.println(game));

        Player[] players = new Player[ps.length];
        for(int i=0; i<ps.length; i++)
            players[i] = ps[i].toObject(game);

        game.initialize(players);
        return game;
    }

    public static void main(String[] args) {
        Arcade arcade = new Arcade();

        GameRules game = arcade.createGame(GameFactory.TicTacToe, PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIMiniMax);

        game.onValidMovePlayed.register(System.out::println);
        game.onValidMovePlayed.register(System.out::println);

        game.run();
    }
}