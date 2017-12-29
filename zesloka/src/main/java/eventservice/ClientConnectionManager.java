package eventservice;

import executor.command.Command;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientConnectionManager implements IClientConnectionListener {

    protected List<IClientConnection> clientConnections = new ArrayList<>();
    protected static ClientConnectionManager instance;

    private ClientConnectionManager(){}

    public static ClientConnectionManager getInstance() {
        if(instance == null) {
            instance = new ClientConnectionManager();
        }
        return instance;
    }

    /**
     * Initialize input and output stream readers and writers
     * @param in client input stream.
     * @param out client output stream.
     */
    @Override
    public void onConnect(InputStream in, OutputStream out) {
        IClientConnection cc = new ClientConnection(
                new BufferedReader(new InputStreamReader(in)),
                new BufferedWriter(new OutputStreamWriter(out))
        );
        this.clientConnections.add(cc);
        Thread thread = new Thread(() -> ((ClientConnection)cc).waitForMsg());
        thread.start();
    }

    @Override
    public void onDisconnect() throws IOException {
        synchronized (clientConnections) {
            for (IClientConnection cc : clientConnections) {
                cc.close();
            }
        }
    }

    public IClientConnection getClientConnection(Integer id) {
        synchronized (clientConnections) {
            for(IClientConnection cc : clientConnections) {
                if(cc.getId() == id) {
                    return cc;
                }
            }
        }
        return null;
    }


}
