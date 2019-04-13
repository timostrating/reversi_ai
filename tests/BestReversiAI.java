import game_util.Arcade;
import org.junit.jupiter.api.Test;
import reversi.Reversi;
import reversi.ReversiAIMiniMax;
import reversi.ReversiAIMiniMaxOptimized;
import util.Callback;

public class BestReversiAI {

    Reversi reversi = new Reversi();

    Callback printBoard = () -> System.out.println(reversi.toString());
    Callback printErrBoard = () -> {
        System.err.println(reversi.getGameSpecificState());
        System.err.println(reversi.toString());
    };

    @Test
    public void playAIvsAI() {
        reversi = new Reversi();
        reversi.initialize(Arcade.RefereeFactory.DefaultReferee.toObject(reversi), new ReversiAIMiniMaxOptimized(reversi), new ReversiAIMiniMax(reversi));
        reversi.onNextPlayer.register(printBoard);
        reversi.onGameEnded.register(printErrBoard);
        reversi.run();

        reversi = new Reversi();
        reversi.initialize(Arcade.RefereeFactory.DefaultReferee.toObject(reversi), new ReversiAIMiniMax(reversi), new ReversiAIMiniMaxOptimized(reversi));
        reversi.onNextPlayer.register(printBoard);
        reversi.onGameEnded.register(printErrBoard);
        reversi.run();
    }
}
