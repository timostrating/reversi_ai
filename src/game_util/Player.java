package game_util;

import util.CallbackWithParam;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Any player of Ai needs to extend this class
 */
public abstract class Player {

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private String name;
    private int nr;
    private boolean disqualified;

    public final int getNr() {
        return nr;
    }

    final void setNr(int nr) {
        this.nr = nr;
    }

    public final String getName() {
        return name;
    }

    public final void setName(String name) {
        this.name = name;
    }

    public final boolean isDisqualified() {
        return disqualified;
    }

    final void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }

    protected abstract Move getInput();

    public final void yourTurn(CallbackWithParam<Move> inputCallback) {
        EXECUTOR.execute(() -> inputCallback.callback(getInput()));
    }

    @Override
    public String toString() {
        return "Player nr " + nr + (name == null ? "" : " (" + name + ")") + " " + this.getClass().getName();
    }
}
