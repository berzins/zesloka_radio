package executor.command.robotcommands;

import executor.command.Command;

import java.io.Serializable;

public abstract class RobotCommand extends Command implements Serializable {

    public static String ROBOT_CMD_KEY_PRESS = "robot_cmd_key_press";
    public static String ROBOT_CMD_KEY_RELEASE = "robot_cmd_key_release";
    public static String ROBOT_CMD_MOUSE_MOVE = "robot_cmd_mouse_move";
    public static String ROBOT_CMD_MOUSE_PRESS = "robot_cmd_mouse_press";
    public static String ROBOT_CMD_MOUSE_RELEASE = "robot_cmd_mouse_release";


    protected static final String X = "x";
    protected static final String Y = "y";

    Integer event;


    public RobotCommand(String name, String key, Integer event) {
        super(name, key);
        this.event = event;
    }

    @Override
    public void execute() {
        super.execute();
    }
}
