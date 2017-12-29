package executor.command.utilcommands.mouse;

import executor.command.Command;
import executor.command.parameters.CommandParams;
import executor.command.robotcommands.RobotCommand;
import executor.command.robotcommands.RobotMousePressCommand;
import executor.command.robotcommands.RobotMouseReleaseCommand;

public class MouseClick_3 extends MouseClick {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public MouseClick_3(String name, String key) {
        super(name, key);
    }

    @Override protected void init() {
        super.init();
        this.addParam(
                getSubCommand(RobotCommand.ROBOT_CMD_MOUSE_PRESS),
                RobotCommand.KEY_EVENT, MOUSE_3_DOWN);
        this.addParam(
                getSubCommand(RobotCommand.ROBOT_CMD_MOUSE_RELEASE),
                RobotCommand.KEY_EVENT, MOUSE_3_UP);
        this.setFinal(true);
    }

    @Override
    public Command setParams(CommandParams params) {
        return super.setParams(params);
    }
}
