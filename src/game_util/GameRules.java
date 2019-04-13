package game_util;

import util.Callback;
import util.CallbackWithParam;
import util.Delegate;

import java.util.Arrays;

public abstract class GameRules implements Runnable {
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS  /* PLAYER_3.4.5..N_WINS*/ }

    public Delegate<Callback> onNextPlayer = new Delegate<>();
    public Delegate<Callback> onGameEnded = new Delegate<>();
    public Delegate<CallbackWithParam<Move>> onPermanentMovePlayed = new Delegate<>();
//    public Delegate<CallbackWithParam<Move>> onTemporaryMove = new Delegate<>();
//    public Delegate<CallbackWithParam<Move>> onTemporaryUndoMove = new Delegate<>();

    private Player[] players;
    private Referee referee;

    public void initialize(Referee referee, Player... players) {
        this.referee = referee;
        this.players = players;
        for (int i = 0; i < players.length; i++)
            players[i].setNr(i + 1);

        referee.initialize();
    }

    @Override
    public void run() {
        referee.letTheGameStart(this::gameEnded);
    }

    public void start(boolean newThread) {
        if (newThread) {
            new Thread(this).start();
        } else run();
    }

    public void nextPlayer(Player p) {
        if (canPlay(p)) {
            onNextPlayer.notifyObjects(Callback::callback);
            referee.letPlayerPlay(p);
        }
    }

    protected boolean canPlay(Player p) {
        return true;
    }

    private void gameEnded() {
        onGameEnded.notifyObjects(Callback::callback);
    }

    public Player getPlayer(int i) {
        return players[i];
    }

    public Player getPlayerByName(String name) {
        for (Player p : players) if (p.getName().equals(name)) return p;
        throw new RuntimeException(name + " is not a player. Players are: " + Arrays.toString(players));
    }

    public final GameState getGameState() {
        GameState refState = referee.getGameState();
        if (refState != null) // the referee can override the game state if he wants.
            return refState;
        return getGameSpecificState();
    }

    public abstract GameState getGameSpecificState();
    public boolean playMove(int input, int playerNr) {
        Move m = getMove(input, playerNr);
        if (m != null)
            m.doMove();

        return (m != null);
    }
    public abstract Move getMove(int input, int playerNr);
}
