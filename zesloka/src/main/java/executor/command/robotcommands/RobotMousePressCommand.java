package executor.command.robotcommands;

import executor.RemoteCommandExecutorManager;

public class RobotMousePressCommand extends  RobotCommand {

    public RobotMousePressCommand(Integer event) {
        super(event);
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutorManager.getInstance().getRobot().mousePress(this.event);
    }
}
