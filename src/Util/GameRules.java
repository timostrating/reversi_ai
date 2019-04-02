package Util;

import static Util.GameRules.GameState.PLAYING;

public abstract class GameRules implements Runnable {
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS  /* PLAYER_3.4.5..N_WINS*/ }

    public Delegate<Callback> onNextPlayer = new Delegate<>();
    public Delegate<Callback> onGameOver = new Delegate<>();
    public Delegate<CallbackWithParam<Integer>> onValidMovePlayed = new Delegate<>();

    protected Player[] players;

    public void initialize(Player... players) {
        this.players = players;
        for (int i = 0; i < players.length; i++)
            players[i].setNr(i + 1);
    }

    public void nextPlayer(Player p) {
        onNextPlayer.notifyObjects(Callback::callback);
        playerPlays(p);

        if (getGameState() != PLAYING) // TODO Fix this
            onGameOver.notifyObjects(Callback::callback);
    }

    private void playerPlays(Player p) {
        playMove(p.getInput(), p.getNr());
    }

    public abstract GameState getGameState();
    public abstract boolean playMove(int input, int playerNr);
}
