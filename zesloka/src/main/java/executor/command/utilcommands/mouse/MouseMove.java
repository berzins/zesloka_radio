package executor.command.utilcommands.mouse;

import executor.command.Command;
import executor.command.robotcommands.RobotCommand;
import executor.command.robotcommands.RobotMouseMoveCommand;


public class MouseMove extends Command{
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public MouseMove(String name, String key) {
        super(name, key);
    }

    @Override protected void init() {
        Command move = new RobotMouseMoveCommand("robot mouse move", RobotCommand.ROBOT_CMD_MOUSE_MOVE);
    }
}
