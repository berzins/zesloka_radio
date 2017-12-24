package remotecontrolserver;

import com.sun.security.ntlm.Client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteControlServer {

    public interface ClientConnectionListener {
        void onConnect(Socket client);
        void onDisconnect() throws IOException;
        void close() throws IOException;
    }

    public interface ClientOutputListener {
        void write(String msg);
    }

    private List<ClientConnectionListener> clientConnectionListeners = new ArrayList<>();
    private ServerSocket server;
    private List<Socket> clients = new ArrayList<>();
    private int port = 0;
    private boolean waitConnection = true;

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
        System.out.println("Server has started");
        Thread thread = new Thread(() -> waitForConnection());
        thread.start();
    }

    public void stop() {
        try {
            for(ClientConnectionListener ccl : clientConnectionListeners) {
                ccl.close();
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
                    for(ClientConnectionListener ccl : clientConnectionListeners) {
                        ccl.onConnect(client);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ClientConnectionListener getClient(int clientHash) {
        for(ClientConnectionListener ccl : clientConnectionListeners) {
            if(ccl.hashCode() == clientHash) {
                return ccl;
            }
        }
        return null;
    }
}
