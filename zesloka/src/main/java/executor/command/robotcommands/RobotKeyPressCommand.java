package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotKeyPressCommand extends RobotCommand {


    public RobotKeyPressCommand(Integer event)
    {
        super("robot key press", ROBOT_CMD_KEY_PRESS, event);
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot().keyPress(this.event);
    }
}
