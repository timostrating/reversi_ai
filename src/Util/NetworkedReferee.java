package Util;

import netwerk.Connection;

import java.util.concurrent.atomic.AtomicBoolean;

public class NetworkedReferee extends DefaultReferee {

    private Connection connection;

    public NetworkedReferee(GameRules game) {
        super(game);
        connection = CompositionRoot.getInstance().connection;
    }

    @Override
    public void letTheGameStart() {
        // register network events

        super.letTheGameStart();

        // unregister network events
    }

    @Override
    public void letPlayerPlay(Player p) {

        AtomicBoolean inputResolved = resolveInput(p);

        
    }

}
