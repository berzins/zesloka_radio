package executor.command;

import executor.RemoteCommandExecutorManager;

import java.awt.event.KeyEvent;

public class CommandExecutorFactory {

    public static CommandExecutor getCommandExecutor(RemoteCommandExecutorManager.Command cmd) {
        switch (cmd) {
            case PLAY:
                return new Play();
            case STOP:
                return new Stop();
            case NEXT:
                return new Next();
                default:
                    return new CommandExecutor();
        }

    }

    private static class Play extends  CommandExecutor {
        public Play() {
            this.addRobotCommand(new RobotKeyPressCommand(KeyEvent.VK_ESCAPE));
            this.addRobotCommand(new RobotKeyReleaseCommand(KeyEvent.VK_ESCAPE));
        }
    }

    private static class Stop extends CommandExecutor {
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





}
