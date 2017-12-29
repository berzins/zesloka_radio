package eventservice.consoleclient;

import eventservice.EventServiceFacade;
import eventservice.IClientConnectionListener;

import java.io.IOException;

public class ConsoleClient extends EventServiceFacade {


    @Override
    public void start() {
        for(IClientConnectionListener ccl : clientConnectionListeners) {
            ccl.onConnect(System.in, System.out);
        }
    }

    @Override
    public void stop() {
        try {
            for(IClientConnectionListener ccl : clientConnectionListeners) {
                ccl.onDisconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
