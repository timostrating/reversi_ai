package Util;

import static Util.GameRules.GameState.PLAYING;

public abstract class GameRules {
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS  /* PLAYER_3.4.5..N_WINS*/ }

    public Delegate<Callback> onNextPlayer = new Delegate<>();
    public Delegate<Callback> onGameOver = new Delegate<>();
    public Delegate<CallbackWithParam<Integer>> onValidMovePlayed = new Delegate<>();

    private boolean initialized = false;
    private Player[] players;

    public void playGame(Player... players) {
        this.players = players;
        for (int i = 0; i < players.length; i++)
            players[i].setNr(i + 1);
        initialized = true;
    }

    public void nextPlayer(Player p) {
        if (!initialized)
            throw new RuntimeException("Game is not initialized");

        onNextPlayer.notifyObjects(Callback::callback);
        playerPlays(p);

        if (getGameState() != PLAYING) // TODO Fix this
            onGameOver.notifyObjects(Callback::callback);
    }

    public abstract void playerPlays(Player p);
    public abstract GameState getGameState();
    public abstract boolean playMove(int input, int playerNr);
}
