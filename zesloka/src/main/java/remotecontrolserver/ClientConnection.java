package remotecontrolserver;

import java.io.BufferedReader;
import java.net.Socket;

public class ClientConnection implements RemoteControlServer.ClientConnectionListener {

    BufferedReader input;
    Socket client;

    @Override
    public void onConnect(Socket client) {
        this.client = client;
    }

    @Override
    public void onDisconnect() {
        //TODO: implement this
    }

    private void dispatchMsg() {

    }
}
