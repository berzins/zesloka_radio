package executor;

import executor.command.RobotCommandExecutor;
import executor.command.RobotCommandExecutorFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class RemoteCommandExecutorManager {

    private static final Map<String, Command> commands = new HashMap<>();
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

    public void processCommand(String cmd) {
        RobotCommandExecutor executor = RobotCommandExecutorFactory.getCommandExecutor(
                this.parseCommand(cmd), this.parseParams(cmd)
        );
        executor.execute();
    }

    private String parseCommand(String cmd) {
        if(cmd.contains("|")) {
            String[] c = cmd.split("\\|");
            return c[0];
        }
        return cmd;
    }

    private String parseParams(String cmd) {
        if(cmd.contains("|")) {
            int from = cmd.indexOf("|") + 1;
            int length = cmd.length();
            String p = cmd.substring(from,length);
            return p;
        }
        return null;
    }


}
