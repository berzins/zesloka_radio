package executor.command.utilcommands.mouse;

import executor.command.Command;
import executor.command.parameters.CommandParams;
import executor.command.robotcommands.RobotCommand;
import executor.command.robotcommands.RobotMousePressCommand;
import executor.command.robotcommands.RobotMouseReleaseCommand;

public class MouseClick_1 extends MouseClick {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public MouseClick_1(String name, String key) {
        super(name, key);
    }


    @Override protected void init() {
        super.init();
        this.addParam(CommandParams.createParamKey(
                getSubCommand(RobotCommand.ROBOT_CMD_MOUSE_PRESS), RobotCommand.KEY_EVENT), MOUSE_1_DOWN);
        this.addParam(CommandParams.createParamKey(
                getSubCommand(RobotCommand.ROBOT_CMD_MOUSE_RELEASE), RobotCommand.KEY_EVENT), MOUSE_1_UP);
        this.setFinal(true);
    }
}
