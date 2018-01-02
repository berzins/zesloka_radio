package eventservice.client;

import eventservice.IClientConnectionListener;
import eventservice.EventServiceFacade;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RemoteControlServer extends EventServiceFacade {

    private ServerSocket server;
    private List<Socket> clients = new ArrayList<>();
    private int port = 0;
    private boolean waitConnection = true;

    public RemoteControlServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try{
            server = new ServerSocket(this.port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server has started");
        Thread thread = new Thread(this::waitForConnection);
        thread.start();
    }

    @Override
    public void stop() {
        try {
            for(IClientConnectionListener ccl : clientConnectionListeners) {
                ccl.onDisconnect();
            }
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void waitForConnection() {
        while(waitConnection) {
            try {
                if(server != null) {
                    System.out.println("Waiting for connection...");
                    Socket client = server.accept();
                    System.out.println("Connection form " + client.getInetAddress().toString() + " accepted.");
                    synchronized (this.clients) {
                        this.clients.add(client);
                    }
                    for(IClientConnectionListener ccl : clientConnectionListeners) {
                        ccl.onConnect(client.getInputStream(), client.getOutputStream());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
