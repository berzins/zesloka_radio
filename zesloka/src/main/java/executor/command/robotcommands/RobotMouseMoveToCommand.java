package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotMouseMoveToCommand extends RobotMouseMoveCommand {

    public RobotMouseMoveToCommand(String name, String key) {
        super(name, key);
    }

    @Override
    public void execute() {
        super.execute();
        int x = Math.round(params.getFloatValue(this, X));
        int y = Math.round(params.getFloatValue(this, Y));
        RemoteCommandExecutor.getInstance().getRobot().mouseMove(x, y);
    }
}
