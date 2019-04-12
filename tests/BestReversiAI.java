import game_util.Arcade;
import game_util.Arcade.GameFactory;
import org.junit.jupiter.api.Test;
import reversi.Reversi;
import util.Callback;

import static game_util.Arcade.PlayerFactory.ReversiAIMiniMax;
import static game_util.Arcade.RefereeFactory.DefaultReferee;

public class BestReversiAI {

    Arcade arcade = new Arcade();
    Reversi reversi;

    Callback printBoard = () -> System.out.println(reversi.toString());
//    Callback printBoard = () -> {});

    @Test
    public void playAIvsAI() {
        reversi = (Reversi) arcade.createGame(GameFactory.Reversi, DefaultReferee, ReversiAIMiniMax, ReversiAIMiniMax);
        reversi.getGameSpecificState();
        reversi.onNextPlayer.register(printBoard);
        reversi.onGameEnded.register(printBoard);
        reversi.run();
    }
}
