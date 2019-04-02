package Util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class Player {

    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private int nr;
    private boolean disqualified;

    public final int getNr() {
        return nr;
    }

    final void setNr(int nr) {
        this.nr = nr;
    }

    public final boolean isDisqualified() {
        return disqualified;
    }

    final void setDisqualified(boolean disqualified) {
        this.disqualified = disqualified;
    }

    protected abstract int getInput();

    public final void yourTurn(CallbackWithParam<Integer> inputCallback) {
        EXECUTOR.execute(() -> inputCallback.callback(getInput()));
    }

}
