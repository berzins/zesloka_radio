package executor.command.robotcommands;

import executor.RemoteCommandExecutor;
import executor.command.Command;
import executor.command.parameters.CommandParams;

import java.awt.*;

public class RobotMouseMoveCommand extends RobotCommand {

    public RobotMouseMoveCommand() {
        super("mouse move", RobotCommand.ROBOT_CMD_MOUSE_MOVE, 0); // key identifier dummy value.. not needed for move command
        initParamKeys(new String[] {
                CommandParams.createParamKey(this, X),
                CommandParams.createParamKey(this, Y)
        });
    }

    @Override
    public void execute() {
        super.execute();
        int x = MouseInfo.getPointerInfo()
                .getLocation().x + Math.round(params.getFloatValue(this, X));
        int y = MouseInfo.getPointerInfo()
                .getLocation().y + Math.round(params.getFloatValue(this, Y));
        RemoteCommandExecutor.getInstance().getRobot().mouseMove(x, y);

    }
}
