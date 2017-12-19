package executor;

import executor.command.Command;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RemoteCommandExecutor implements Command.CommandProcessor {

    private static final Map<String, Command> commands = new HashMap<>();
    private Robot robot;

    private static RemoteCommandExecutor instance;

    private RemoteCommandExecutor() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static RemoteCommandExecutor getInstance() {
            if(instance == null) {
                instance = new RemoteCommandExecutor();
            }
            return instance;

    }

    public Robot getRobot() {
        return this.robot;
    }

    // this is place where all magic happens
    // if you wonder how this works, this is place to start
    @Override
    public void processCommand(String cmd) {
        Command c = Command.getCommand(Command.getRootCommand(cmd));
        c.setParams(Command.parseParams(cmd)).executeAsync();
    }

}
