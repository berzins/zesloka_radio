package executor;

import executor.command.KeyCommandExecutor;
import executor.command.KeyCommandExecutorFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RemoteCommandExecutorManager {

    private static final Map<String, KeyCommand> commands = new HashMap<>();
    private Robot robot;

    private static RemoteCommandExecutorManager instance;

    private RemoteCommandExecutorManager() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static RemoteCommandExecutorManager getInstance() {
            if(instance == null) {
                instance = new RemoteCommandExecutorManager();
            }
            return instance;

    }

    public Robot getRobot() {
        return this.robot;
    }

    public void processCommand(KeyCommand cmd) {
        KeyCommandExecutor executor = KeyCommandExecutorFactory.getCommandExecutor(cmd);
        executor.execute();
    }

    public enum KeyCommand {

        PLAY("play"),
        NEXT("next"),
        STOP("stop");

        String val;

        KeyCommand(String val) {
            this.val = val;
            commands.put(val, this);
        }

        public String getValue() {return this.val;}

        public static KeyCommand getCommand(String val) {
            return commands.get(val);
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }
}
