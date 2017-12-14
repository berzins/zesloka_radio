package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotKeyReleaseCommand extends RobotCommand {


    public RobotKeyReleaseCommand(Integer event) {
        super("key release", ROBOT_CMD_KEY_RELEASE, event);
    }

    @Override
    public void execute(String params) {
        super.execute(params);
        RemoteCommandExecutor.getInstance().getRobot().keyRelease(this.event);
    }
}
