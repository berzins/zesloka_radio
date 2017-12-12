package executor.command;

import executor.command.robotcommands.*;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import static executor.Command.*;

public class RobotCommandExecutorFactory {

    public static RobotCommandExecutor getCommandExecutor(String cmd, String params) {
        if(cmd == null) return new None();
        switch (cmd) {
            case TYPE_KEY_PLAY:
                return new Play();
            case TYPE_KEY_STOP:
                return new Stop();
            case TYPE_KEY_NEXT:
                return new Next();
            case TYPE_MOUSE_MOVE:
                return new Mouse_Move(params);
            case TYPE_MOUSE_CLICK_1:
                return new Mouse_Click_1();
            default:
                return new RobotCommandExecutor();
        }
    }

    private static class None extends RobotCommandExecutor {
        // dummy command in case unknown command received.
    }

    private static class Play extends RobotCommandExecutor {
        public Play() {
            this.addRobotCommand(new RobotKeyPressCommand(KeyEvent.VK_ESCAPE));
            this.addRobotCommand(new RobotKeyReleaseCommand(KeyEvent.VK_ESCAPE));
        }
    }

    private static class Stop extends RobotCommandExecutor {
        public Stop() {
            this.addRobotCommand(new RobotKeyPressCommand(KeyEvent.CTRL_MASK));
            this.addRobotCommand(new RobotKeyPressCommand(KeyEvent.VK_X));
            this.addRobotCommand(new RobotKeyReleaseCommand(KeyEvent.CTRL_MASK));
            this.addRobotCommand(new RobotKeyReleaseCommand(KeyEvent.VK_X));
        }
    }

    private static class Next extends Play {
        // same thing
    }

    private static class Mouse_Move extends RobotCommandExecutor {
        public Mouse_Move(String params) {
            this.addRobotCommand(new RobotMouseMoveCommand(params));
        }
    }

    private static class Mouse_Click_1 extends RobotCommandExecutor {
        public Mouse_Click_1() {
            this.addRobotCommand( new RobotMousePressCommand(InputEvent.BUTTON1_DOWN_MASK));
            this.addRobotCommand( new RobotMouseReleaseCommand(InputEvent.BUTTON1_MASK));
        }
    }
}
