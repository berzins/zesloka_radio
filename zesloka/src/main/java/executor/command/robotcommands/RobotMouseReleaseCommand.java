package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotMouseReleaseCommand extends RobotCommand {


    public RobotMouseReleaseCommand(Integer event) {
        super("mouse release", RobotCommand.ROBOT_CMD_MOUSE_RELEASE, event);
    }

    @Override
    public void execute(String params) {
        super.execute(params);
        RemoteCommandExecutor.getInstance().getRobot().mouseRelease(this.event);
    }
}
