package Util;

import java.util.ArrayList;

public abstract class GameRules {
    public enum GameState {PLAYING, DRAW, PLAYER_1_WINS, PLAYER_2_WINS  /* PLAYER_3.4.5..N_WINS*/ }

    public ArrayList<Callback> onNextPlayer = new ArrayList<>();

    public abstract void playGame(Player[] players);
    public void nextPlayer(Player p) {
        for (Callback c : onNextPlayer)
            c.callback();
        playerPlays(p);
    }
    public abstract void playerPlays(Player p);
    public abstract GameState getGameState();
    public abstract boolean playMove(int input, int playerNr);
}
