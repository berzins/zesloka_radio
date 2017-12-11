package remotecontrolserver;

import executor.RemoteCommandExecutor;
import sun.plugin2.message.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientConnection implements RemoteControlServer.ClientConnectionListener {

    BufferedReader input;
    Socket client;
    private List<MessageListener> msgListeners =  new ArrayList<>();
    private boolean isActive = true;

    public interface MessageListener {
        void onMessage(RemoteCommandExecutor.Command cmd);
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
    public void onDisconnect() {
        //TODO: implement this
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
                        ml.onMessage(RemoteCommandExecutor.Command.getCommand(cmd));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
