package executor.command.robotcommands;

import executor.command.Command;

import java.io.Serializable;

public abstract class RobotCommand extends Command implements Serializable {

    public static String ROBOT_CMD_KEY_PRESS = "robot_cmd_key_press";
    public static String ROBOT_CMD_KEY_RELEASE = "robot_cmd_key_release";
    public static String ROBOT_CMD_MOUSE_MOVE = "robot_cmd_mouse_move";
    public static String ROBOT_CMD_MOUSE_PRESS = "robot_cmd_mouse_press";
    public static String ROBOT_CMD_MOUSE_RELEASE = "robot_cmd_mouse_release";
    public static String ROBOT_CMD_MOUSE_MOVE_TO = "robot_cmd_mouse_move_to";


    public static final String X = "x";
    public static final String Y = "y";
    public static final String KEY_EVENT = "key_event";

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RobotCommand(String name, String key) {
        super(name, key);
    }
}
