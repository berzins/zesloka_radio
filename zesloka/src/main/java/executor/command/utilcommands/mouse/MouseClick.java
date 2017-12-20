package executor.command.utilcommands.mouse;

import executor.command.Command;
import executor.command.parameters.CommandParams;
import executor.command.robotcommands.RobotCommand;
import executor.command.robotcommands.RobotMousePressCommand;
import executor.command.robotcommands.RobotMouseReleaseCommand;
import jdk.internal.util.xml.impl.Input;

import java.awt.event.InputEvent;

public abstract class MouseClick extends Command{

    public static final String MOUSE_1_DOWN = String.valueOf(InputEvent.BUTTON1_DOWN_MASK);
    public static final String MOUSE_1_UP = String.valueOf(InputEvent.BUTTON1_MASK);
    public static final String MOUSE_2_DOWN = String.valueOf(InputEvent.BUTTON2_DOWN_MASK);
    public static final String MOUSE_2_UP = String.valueOf(InputEvent.BUTTON2_MASK);
    public static final String MOUSE_3_DOWN = String.valueOf(InputEvent.BUTTON3_DOWN_MASK);
    public static final String MOUSE_3_UP = String.valueOf(InputEvent.BUTTON3_MASK);

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public MouseClick(String name, String key) {
        super(name, key);
    }


}
