package tic_tac_toe;

import Util.*;

import java.util.concurrent.atomic.AtomicBoolean;

public class TicTacToeReferee implements Referee {

    private TicTacToe game;

    public TicTacToeReferee(TicTacToe game) {
        this.game = game;
    }

    @Override
    public void letTheGameStart() {
        game.board = new GameBoard2D(TicTacToe.BOARD_SIZE);
        game.board.reset();
        int curPlayer = 0;
        while (game.board.containsCell(TicTacToe.CellState.EMPTY.ordinal()) && game.getGameState() == GameRules.GameState.PLAYING) {
            game.nextPlayer(game.getPlayer(curPlayer % 2));
            curPlayer++;
        }
    }

    @Override
    public void letPlayerPlay(Player p) {

        AtomicBoolean inputResolved = new AtomicBoolean(false);

        p.yourTurn(input -> {
            inputResolved.set(true);
            if (!p.isDisqualified()) {
                if (!game.playMove(input, p.getNr()))
                    disqualify(p);
            }
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
