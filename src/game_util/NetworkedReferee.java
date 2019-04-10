package game_util;

import network.Connection;
import network.FromServer;
import network.RemotePlayer;
import network.ToServer;
import util.Callback;
import util.CallbackWithParam;
import util.CompositionRoot;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static game_util.GameRules.GameState.*;

/**
 * A referee that uses the online protocol to play games.
 */
public class NetworkedReferee extends DefaultReferee {

    private FromServer fromServer;
    private ToServer toServer;
    private Callback onEnded;
    private Player localPlayer, remotePlayer;
    private GameRules.GameState gameState;

    public NetworkedReferee(GameRules game) {
        super(game);
        Connection connection = CompositionRoot.getInstance().connection;
        fromServer = connection.getFromServer();
        toServer = connection.getToServer();

    }

    public void initialize() {
        Player p0 = game.getPlayer(0), p1 = game.getPlayer(1);
        remotePlayer = p0 instanceof RemotePlayer ? p0 : p1;
        localPlayer = p0 == remotePlayer ? p1 : p0;

        // register network events
        System.out.println("Register network events");
        fromServer.onTurn.register(onLocalTurn);
        fromServer.onMove.register(onMove);
        fromServer.onResult.register(onResult);
        System.out.println("Register network events done");
    }

    private CallbackWithParam<HashMap<String, String>> onResult = message -> {
        // server says that game has ended.

        switch (message.get("result")) {
            case "DRAW":
                gameState = GameRules.GameState.DRAW;
                break;
            case "WIN":
                gameState = localPlayer.getNr() == 1 ? PLAYER_1_WINS : PLAYER_2_WINS;
                break;
            case "LOSS":
                gameState = remotePlayer.getNr() == 1 ? PLAYER_1_WINS : PLAYER_2_WINS;
                break;
            default:
                throw new RuntimeException("Result: " + message.get("result") + ", from server not recognized.");
        }
        endGame();
    };

    private CallbackWithParam<HashMap<String, String>> onLocalTurn = message -> {
        System.out.println("Local turn");
        System.out.println(game);
        game.nextPlayer(localPlayer);
    };

    private CallbackWithParam<HashMap<String, String>> onMove = message -> {
        Player p = game.getPlayerByName(message.get("PLAYER"));
        System.out.println("Received move for player " + p);
        Move m = game.getMove(Integer.valueOf(message.get("MOVE")), p.getNr());
        if (m != null) {
            m.doMove(true);
            String details = message.get("DETAILS");
            if (details != null && details.equals("Illegal Move"))
                System.err.println("WARNING: Received Move from server, Server thinks it is INVALID, but our GameRules thinks it is valid.");
        } else System.err.println("WARNING: Invalid Move received from server");

        if (p == localPlayer) // the server confirmed the move of localPlayer, now assume that remotePlayer is next:
            game.nextPlayer(remotePlayer);
    };

    @Override
    public void letTheGameStart(Callback onEnded) {
        this.onEnded = onEnded;
    }

    private void endGame() {
        // unregister network events
        fromServer.onTurn.unregister(onLocalTurn);
        fromServer.onResult.unregister(onResult);
        fromServer.onMove.unregister(onMove);
        // tell the game it has ended.
        onEnded.callback();
    }

    @Override
    public void letPlayerPlay(Player p) {
        if (p != localPlayer) return;
        // let local player play:

        AtomicBoolean inputResolved = new AtomicBoolean(false);
        p.yourTurn(input -> {
            if (!p.isDisqualified() && !inputResolved.get() && game.getGameState() == PLAYING)
                toServer.setMove(input == null ? -1 : input.toI());
            inputResolved.set(true);
        });
    }

    @Override
    public GameRules.GameState getGameState() {
        return gameState;
    }

}
