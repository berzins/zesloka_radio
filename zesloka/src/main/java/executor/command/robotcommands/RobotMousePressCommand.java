package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotMousePressCommand extends  RobotCommand {


    public RobotMousePressCommand(Integer event) {
        super("mouse press", ROBOT_CMD_MOUSE_PRESS, event);
    }

    @Override
    public void execute(String params) {
        super.execute(params);
        RemoteCommandExecutor.getInstance().getRobot().mousePress(this.event);
    }
}
