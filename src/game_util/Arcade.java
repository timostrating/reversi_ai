package game_util;

import GUI.GUIPlayer;
import network.RemotePlayer;
import reversi.Reversi;
import reversi.ReversiAIMiniMax;
import reversi.ReversiAIMiniMaxThreaded;
import reversi.ReversiAIRandom;
import tic_tac_toe.TicTacToe;
import tic_tac_toe.TicTacToeAIMiniMax;
import tic_tac_toe.TicTacToeAIScore;

/**
 * The Arcade class is a Factory for the games and different kind of players
 */
public class Arcade {

    @FunctionalInterface interface China { Object aliExpress(GameRules game); }

    public enum GameFactory {
        TicTacToe(3, g -> new TicTacToe()),
        Reversi(8, g -> new Reversi());

        public final int boardSize;
        private China china;
        private GameFactory(int boardSize, China china) {
            this.boardSize = boardSize;
            this.china = china; }
        public GameRules toObject() { return (GameRules) china.aliExpress(null); }
    }

    public enum PlayerFactory {
        HumanPlayer(g -> new HumanPlayer(g)),
        GUIPlayer(g -> new GUIPlayer(g)),
        RemotePlayer(g -> new RemotePlayer()),
        TicTacToeAIScore(g -> new TicTacToeAIScore((TicTacToe) g)),
        TicTacToeAIMiniMax(g -> new TicTacToeAIMiniMax((TicTacToe) g)),
        ReversiAIRandom(g -> new ReversiAIRandom((Reversi) g)),
        ReversiAIMiniMax(g -> new ReversiAIMiniMax((Reversi) g)),
        ReversiAIMiniMaxThreaded(g -> new ReversiAIMiniMaxThreaded((Reversi) g)),
        BestAvailableAI(game -> {
            if (game instanceof TicTacToe)
                return new TicTacToeAIMiniMax((TicTacToe) game);
            else if (game instanceof Reversi)
                return new ReversiAIMiniMaxThreaded((Reversi) game);
            else
                throw new RuntimeException("Unfortunately humans are better than the best AI");
        });

        private China china;
        private PlayerFactory(China china) { this.china = china; }
        public Player toObject(GameRules game) { return (Player) china.aliExpress(game); }
    }

    public enum RefereeFactory {
        DefaultReferee(game_util.DefaultReferee::new),
        NetworkedReferee(game_util.NetworkedReferee::new);

        private China china;
        private RefereeFactory(China china) { this.china = china; }
        public Referee toObject(GameRules game) { return (Referee) china.aliExpress(game); }
    }

    public GameRules createGame(GameFactory gameType, RefereeFactory refereeType, PlayerFactory... ps) {
        GameRules game = gameType.toObject();
//        game_util.onNextPlayer.register(() -> System.out.println(game_util));
//        game_util.onGameEnded.register(() -> System.err.println(game_util));

        Player[] players = new Player[ps.length];
        for(int i=0; i<ps.length; i++)
            players[i] = ps[i].toObject(game);

        game.initialize(refereeType.toObject(game), players);
        return game;
    }

    public static void main(String[] args) {
        Arcade arcade = new Arcade();

//        GameRules game_util = arcade.createGame(GameFactory.TicTacToe, RefereeFactory.DefaultReferee, PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.TicTacToeAIMiniMax);
        GameRules game = arcade.createGame(GameFactory.Reversi, RefereeFactory.DefaultReferee, PlayerFactory.ReversiAIMiniMaxThreaded, PlayerFactory.ReversiAIMiniMaxThreaded);
        game.onNextPlayer.register(() -> System.out.println(game));
//        game.onValidMovePlayed.register((i) -> System.out.println(game));
        game.onGameEnded.register(() -> {
            System.err.println(game.getGameState());
            System.err.println(game);
        });

        long beginTime = System.currentTimeMillis();

        game.run();

        System.out.println("Game took " + (System.currentTimeMillis() - beginTime) / 1000 + "s");


//        Connection c = CompositionRoot.getInstance().connection;
//        c.connect("145.33.225.170", 7789);
//        String localName = "PizzaLover321";
//        c.getToServer().setLogin(localName);
//        c.getToServer().subscribeGame("Tic-tac-toe");
//
//        c.getFromServer().onMatch.register(match -> {
//
//            GameRules game = arcade.createGame(GameFactory.TicTacToe, RefereeFactory.NetworkedReferee, PlayerFactory.TicTacToeAIMiniMax, PlayerFactory.RemotePlayer);
//            game.getPlayer(0).setName(localName);
//            game.getPlayer(1).setName(match.get("OPPONENT"));
//            game.onNextPlayer.register(() -> System.out.println(game));
//            game.onGameEnded.register(() -> System.err.println(game));
//
//            game.run();
//        });
    }
}