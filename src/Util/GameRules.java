package Util;

public abstract class GameRules {
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS  /* PLAYER_3.4.5..N_WINS*/ }

    public Delegate<Callback> onNextPlayer = new Delegate<>();

    public abstract void playGame(Player[] players);
    public void nextPlayer(Player p) {
        onNextPlayer.notifyObjects(Callback::callback);
        playerPlays(p);
    }
    public abstract void playerPlays(Player p);
    public abstract GameState getGameState();
    public abstract boolean playMove(int input, int playerNr);
}
