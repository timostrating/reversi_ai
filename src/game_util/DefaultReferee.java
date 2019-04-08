package game_util;

import util.Callback;

import java.util.concurrent.atomic.AtomicBoolean;

import static game_util.GameRules.GameState.PLAYER_1_WINS;
import static game_util.GameRules.GameState.PLAYER_2_WINS;

public class DefaultReferee implements Referee {

    protected GameRules game;

    public DefaultReferee(GameRules game) {
        this.game = game;
    }

    @Override
    public void letTheGameStart(Callback onEnded) {
        int curPlayer = 0;
        while (game.getGameState() == GameRules.GameState.PLAYING) {
            game.nextPlayer(game.getPlayer(curPlayer % 2));
            curPlayer++;
        }
        onEnded.callback();
    }

    @Override
    public void letPlayerPlay(Player p) {

        AtomicBoolean inputResolved = new AtomicBoolean(false);

        p.yourTurn(input -> {
            if (!p.isDisqualified() && !inputResolved.get()) {
                if (!game.playMove(input, p.getNr())) {
                    disqualify(p);
                }
            }
            inputResolved.set(true); // OP DEZE PLEK LATEN
        });

        long timeout = System.currentTimeMillis() + 1000_000;

        while (!inputResolved.get()) {
            if (System.currentTimeMillis() >= timeout) {
                System.out.println("Player " + p.getNr() + " timed out.");
                disqualify(p);
                break;
            }
        }
    }

    @Override
    public GameRules.GameState getGameState() {
        return game.getPlayer(0).isDisqualified() ?
                PLAYER_2_WINS
                :
                (game.getPlayer(1).isDisqualified() ? PLAYER_1_WINS : null);
    }
}
