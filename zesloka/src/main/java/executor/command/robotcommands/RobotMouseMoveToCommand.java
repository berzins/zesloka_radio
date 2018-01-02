package executor.command.robotcommands;

import executor.RemoteCommandExecutor;
import executor.command.parameters.Parameter;

public class RobotMouseMoveToCommand extends RobotMouseMoveCommand {

    public RobotMouseMoveToCommand(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), X, Parameter.TYPE_FLOAT, Parameter.VALUE_UNDEFINED),
                new Parameter(this.getKey(), Y, Parameter.TYPE_FLOAT, Parameter.VALUE_UNDEFINED)
        });
    }

    @Override
    public void execute() {
        super.execute();
        int x = Math.round(params.getFloatValue(this, X));
        int y = Math.round(params.getFloatValue(this, Y));
        RemoteCommandExecutor.getInstance().getRobot().mouseMove(x, y);
    }
}
