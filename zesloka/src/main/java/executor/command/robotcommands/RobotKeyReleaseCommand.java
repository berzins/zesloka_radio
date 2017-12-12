package executor.command.robotcommands;

import executor.RemoteCommandExecutorManager;

public class RobotKeyReleaseCommand extends RobotCommand {

    public RobotKeyReleaseCommand(Integer event) {
        super(event);
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutorManager.getInstance().getRobot().keyRelease(this.event);
    }
}
