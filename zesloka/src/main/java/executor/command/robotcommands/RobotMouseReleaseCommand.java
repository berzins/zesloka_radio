package executor.command.robotcommands;

import executor.RemoteCommandExecutorManager;

public class RobotMouseReleaseCommand extends RobotCommand {

    public RobotMouseReleaseCommand(Integer event) {
        super(event);
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutorManager.getInstance().getRobot().mouseRelease(this.event);
    }
}
