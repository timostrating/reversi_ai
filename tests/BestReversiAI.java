import game_util.Arcade;
import org.junit.jupiter.api.Test;
import reversi.Reversi;
import reversi.ReversiAIMiniMax;
import util.Callback;

public class BestReversiAI {

    Reversi reversi = new Reversi();

    Callback printBoard = () -> System.out.println(reversi.toString());
//    Callback printBoard = () -> {});

    @Test
    public void playAIvsAI() {
        reversi = new Reversi();
        reversi.initialize(Arcade.RefereeFactory.DefaultReferee.toObject(reversi), new ReversiAIMiniMax(reversi), new ReversiAIMiniMax(reversi));
        reversi.getGameSpecificState();
        reversi.onNextPlayer.register(printBoard);
        reversi.onGameEnded.register(printBoard);
        reversi.run();
    }
}
