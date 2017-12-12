package executor.command.robotcommands;

import executor.RemoteCommandExecutorManager;

public class RobotKeyPressCommand extends RobotCommand {

    public RobotKeyPressCommand(Integer event) {
        super(event);
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutorManager.getInstance().getRobot().keyPress(this.event);
    }
}
