import java.awt.*;
import java.awt.event.KeyEvent;

public class RemoteCommandExecutor {

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
        synchronized (instance) {
            if(instance == null) {
                instance = new RemoteCommandExecutor();
            }
            return instance;
        }
    }

    public void processCommand(Command cmd) {
        switch (cmd) {
            case PLAY:
                robot.keyPress(KeyEvent.VK_ESCAPE);
                robot.keyRelease(KeyEvent.VK_ESCAPE);
            case STOP:
                robot.keyPress(KeyEvent.CTRL_MASK);
                robot.keyPress(KeyEvent.VK_X);
                robot.keyPress(KeyEvent.CTRL_MASK);
                robot.keyPress(KeyEvent.VK_X);
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
        }

        String getValue() {return this.val;}
    }



}
