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
//        Reversi(ReversiBoard)

        public GameRules toObject(){
            switch (this.ordinal()) {
                case 0: return new TicTacToe();
//                case 1: return new ReversiBoard();
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


    public GameRules createGame(GameFactory gameType) {
        return gameType.toObject();
    }

    public void playGame(GameFactory gameType , PlayerFactory... ps) {
        GameRules game = gameType.toObject();
        playGame(game, ps);
    }

    public void playGame(GameRules game , PlayerFactory... ps) {
        game.onNextPlayer.register(() -> System.out.println(game));
        game.onGameOver.register(() -> System.err.println(game));

        Player[] players = new Player[ps.length];
        for(int i=0; i<ps.length; i++)
            players[i] = ps[i].toObject(game);
        game.playGame(players);
    }

    public static void main(String[] args) {
        Arcade arcade = new Arcade();

        GameRules game = arcade.createGame(GameFactory.TicTacToe);

        game.onValidMovePlayed.register((i)->System.out.println(i));
        game.onValidMovePlayed.register((i)->System.out.println(i));

        arcade.playGame(game, PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIMiniMax);
    }
}