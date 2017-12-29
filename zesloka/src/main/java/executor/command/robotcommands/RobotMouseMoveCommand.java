package executor.command.robotcommands;

import executor.RemoteCommandExecutor;
import executor.command.parameters.Parameter;

import java.awt.*;

public class RobotMouseMoveCommand extends RobotCommand {

    public RobotMouseMoveCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[]{
                new Parameter(this.getKey(), X, Parameter.TYPE_FLOAT, Parameter.VALUE_UNDEFINED),
                new Parameter(this.getKey(), Y, Parameter.TYPE_FLOAT, Parameter.VALUE_UNDEFINED)});
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
