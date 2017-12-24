package remotecontrolserver;

import executor.command.Command;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection implements RemoteControlServer.ClientConnectionListener {

    BufferedReader input;
    BufferedWriter output;
    Socket client;
    private List<MessageListener> msgListeners =  new ArrayList<>();
    private boolean isActive = true;

    public interface MessageListener {
        void onMessage(String cmd);
    }

    public void addMsgListener(MessageListener msgList) {
        this.msgListeners.add(msgList);
    }

    @Override
    public void onConnect(Socket client) {
        this.client = client;
        try {
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> waitForMsg());
        thread.start();
    }

    @Override
    public void onDisconnect() throws IOException {
        this.close();
    }

    @Override
    public void close() throws IOException {
        isActive = false;
        input.close();
        client.close();
    }

    public void waitForMsg() {
        try {
            while(true) {
                String cmd = input.readLine();
                if(isActive) {
                    for(MessageListener ml : msgListeners) {
                        //add self identifier to message params and then call onMessage
                        ml.onMessage(Command.addSourceIdToParams(cmd, this.hashCode()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String msg) {
            try {
                if(output == null) {
                    output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                }
                output.write(msg);
                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
