package executor.command.utilcommands.mouse;

import executor.command.Command;
import executor.command.robotcommands.RobotCommand;
import executor.command.robotcommands.RobotMouseMoveToCommand;

public class MouseMoveTo extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public MouseMoveTo(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        this.add(new RobotMouseMoveToCommand("robot mouse move to", RobotCommand.ROBOT_CMD_MOUSE_MOVE_TO));
    }

    @Override
    public void execute() {
        super.execute();
    }
}
