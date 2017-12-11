package executor;

import remotecontrolserver.ClientConnection;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class RemoteCommandExecutor {

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

    public void processCommand(Command cmd) {
        switch (cmd) {
            case PLAY:
                robot.keyPress(KeyEvent.VK_SPACE);
                robot.keyRelease(KeyEvent.VK_SPACE);
                break;
            case STOP:
                robot.keyPress(KeyEvent.CTRL_MASK);
                robot.keyPress(KeyEvent.VK_X);
                robot.keyPress(KeyEvent.CTRL_MASK);
                robot.keyPress(KeyEvent.VK_X);
                break;
                default:
                    // no action
        }
    }

    public enum Command {

        EXIT("exit"),
        PLAY("play"),
        NEXT("next"),
        STOP("stop");

        String val;

        Command(String val) {
            this.val = val;
            commands.put(val, this);
        }

        public String getValue() {return this.val;}

        public static Command getCommand(String val) {
            return commands.get(val);
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }


}
