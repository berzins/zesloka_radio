package remotecontrolserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteControlServer {

    public interface ClientConnectionListener {
        void onConnect(Socket client);
        void onDisconnect();
    }

    private List<ClientConnectionListener> clientConnectionListeners = new ArrayList<>();
    private ServerSocket server;
    private List<Socket> clients = new ArrayList<>();
    private int port = 0;

    public RemoteControlServer(int port) {
        this.port = port;
    }

    public void addClientConnectionListener(ClientConnectionListener ccl) {
        this.clientConnectionListeners.add(ccl);
    }

    public void start() {
        try{
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> waitForConnection());
        thread.start();
    }

    private void waitForConnection() {
        try {
            if(server != null) {
                Socket client = server.accept();
                synchronized (this.clients) {
                    this.clients.add(client);
                }
                for(ClientConnectionListener ccl : clientConnectionListeners) {
                    ccl.onConnect(client);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
