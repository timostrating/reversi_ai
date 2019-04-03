package game_util;

import util.Callback;
import util.CallbackWithParam;
import util.Delegate;

public abstract class GameRules implements Runnable {
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS  /* PLAYER_3.4.5..N_WINS*/ }

    public Delegate<Callback> onNextPlayer = new Delegate<>();
    public Delegate<Callback> onGameEnded = new Delegate<>();
    public Delegate<CallbackWithParam<Integer>> onValidMovePlayed = new Delegate<>();

    private Player[] players;
    private Referee referee;

    public void initialize(Referee referee, Player... players) {
        this.referee = referee;
        this.players = players;
        for (int i = 0; i < players.length; i++)
            players[i].setNr(i + 1);
    }

    @Override
    public void run() {
        referee.letTheGameStart();
        gameEnded();
    }

    public void nextPlayer(Player p) {
        onNextPlayer.notifyObjects(Callback::callback);
        referee.letPlayerPlay(p);
    }

    public void gameEnded() {
        onGameEnded.notifyObjects(Callback::callback);
    }

    public Player getPlayer(int i) {
        return players[i];
    }

    public abstract GameState getGameState();
    public abstract boolean playMove(int input, int playerNr);
}