package executor.command.utilcommands.mouse;

import executor.command.robotcommands.RobotCommand;

public class MouseClick_2 extends MouseClick {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public MouseClick_2(String name, String key) {
        super(name, key);
    }

    @Override protected void init() {
        super.init();
        this.addParam(
                getSubCommand(RobotCommand.ROBOT_CMD_MOUSE_PRESS),
                RobotCommand.KEY_EVENT, MOUSE_2_DOWN);
        this.addParam(
                getSubCommand(RobotCommand.ROBOT_CMD_MOUSE_RELEASE),
                RobotCommand.KEY_EVENT, MOUSE_2_UP);
        this.setFinal(true);
    }
}
