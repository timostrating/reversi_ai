package Util;

import netwerk.Connection;
import netwerk.FromServer;
import netwerk.RemotePlayer;
import netwerk.ToServer;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static Util.GameRules.GameState.PLAYER_1_WINS;
import static Util.GameRules.GameState.PLAYER_2_WINS;

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
        Player p0 = game.getPlayer(0), p1 = game.getPlayer(1);
        remotePlayer = p0 instanceof RemotePlayer ? p0 : p1;
        localPlayer = p0 == remotePlayer ? p1 : p0;
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
        letPlayerPlay(localPlayer);
    };

    private CallbackWithParam<HashMap<String, String>> onMove = message -> {
        Player p = game.getPlayerByName(message.get("PLAYER"));
        if (p == remotePlayer)
            game.playMove(Integer.valueOf(message.get("MOVE")), p.getNr());
    };

    @Override
    public void letTheGameStart(Callback onEnded) {
        // register network events
        this.onEnded = onEnded;
        fromServer.onTurn.register(onLocalTurn);
        fromServer.onMove.register(onMove);
        fromServer.onResult.register(onResult);
    }

    private void endGame() {
        // unregister network events
        fromServer.onTurn.unregister(onLocalTurn);
        fromServer.onResult.unregister(onResult);
        // tell the game it has ended.
        onEnded.callback();
    }

    @Override
    public void letPlayerPlay(Player p) {
        if (p != localPlayer) return;
        // let local player play:

        AtomicBoolean inputResolved = new AtomicBoolean(false);
        p.yourTurn(input -> {
            if (!p.isDisqualified() && !inputResolved.get())
                game.playMove(input, p.getNr());

            inputResolved.set(true);
        });
    }

    @Override
    public GameRules.GameState getGameState() {
        return gameState;
    }
}
