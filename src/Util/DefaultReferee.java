package Util;

import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultReferee implements Referee {

    protected GameRules game;

    public DefaultReferee(GameRules game) {
        this.game = game;
    }

    @Override
    public void letTheGameStart(Callback onEnded) {
        int curPlayer = 0;
        while (game.getGameSpecificState() == GameRules.GameState.PLAYING) {
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

        long timeout = System.currentTimeMillis() + 10_000;

        while (!inputResolved.get()) {
            if (System.currentTimeMillis() >= timeout) {
                System.out.println("Player " + p.getNr() + " timed out.");
                disqualify(p);
                break;
            }
        }
    }

}
