package executor.command;

import executor.command.robotcommands.*;
import executor.command.utilcommands.recorder.*;
import utilities.Storage;
import utilities.Util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents class what can perform previously defined action.
 */
public abstract class Command implements Serializable {


    protected String key;
    protected String name;
    protected Long timeout = 0L;
    final protected List<Command> commands;

    /**
     * @param name Command string representation
     * @param key Unique command identifier
     */
    public Command(String name, String key) {
        this.key = key;
        this.name = name;
        commands = new ArrayList<>();
        init();
    }

    protected void init() {}

    /**
     * Executes all sub command structure.
     * Method can have blocking behaviour so it should be executed in separate thread
     *
     */
    public void execute(final String params) {
        if(timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for(Command cmd : commands) {
            cmd.executeAsync(params);
        }
    }

    public final void executeAsync(final String params) {
        new Thread(() -> Command.this.execute(params)).start();
    }

    /**
     * Command representation string
     */
    public String getName() {
        return this.name;
    }

    /**
     * Unique command identifier (it should be)
     */
    public String getKey() {
        return this.key;
    }

    /**
     * List reference to internal sub commands
     */
    final public List<Command> getCommands() {
        return this.commands;
    }

    /**
     * Sets timeout before command execution call.
     */
    final public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }


    // -------------  INTERFACES ----------------

    /**
     * Defines class what suppose to perform incoming command further processing
     */
    public interface CommandProcessor {
        void processCommand(String cmd);
    }


    // ------------- HELPER METHODS -------------

    /**
     * Extracts command identifier from incoming command string.
     */
    public static String parseCommand(String cmd) {
        if(cmd.contains("|")) {
            String[] c = cmd.split("\\|");
            return c[0];
        }
        return cmd;
    }

    /**
     * Extracts command parameters from incoming command string.
     */
    public static String parseParams(String cmd) {
        if(cmd.contains("|")) {
            int from = cmd.indexOf("|") + 1;
            int length = cmd.length();
            String p = cmd.substring(from,length);
            return p;
        }
        return null;
    }



    // ------------- DEFAULT INIT ---------------

    public static String CMD_NONE = "cmd_none";
    public static String CMD_MOUSE_MOVE = "mouse_move";
    public static String CMD_MOUSE_CLICK_1 = "mouse_click_1";
    public static String CMD_MOUSE_CLICK_2 = "mouse_click_2";
    public static String CMD_MOUSE_CLICK_3 = "mouse_click_3";
    public static String CMD_RADIO_PLAY = "radio_play";


    private static Map<String, Command> initializedCommands = new HashMap<>();
    private static void initCommand(Command c){
        initializedCommands.put(c.getKey(), c);
    }

    public static boolean saveCommand(Command cmd) {
        initCommand(cmd);
        return saveCommandOnFile(cmd);
    }

    //TODO: this method probably can cause data loss.. should closer investigate what causes read from file failure.
    public static boolean saveCommandOnFile(Command c) {
        List<Command> cmds = Util.readFromFile(Storage.FILE_PATH_COMMANDS);
        if(cmds != null) {
            cmds.add(c);
        } else {
            cmds = new ArrayList<>();
            cmds.add(c);
        }
        return Util.writeToFile(Storage.FILE_PATH_COMMANDS, cmds);
    }


    public static void initDefaultCommands() {

        initCommand(new Command("none", CMD_NONE) {});

        initCommand(new Command("mouse move", CMD_MOUSE_MOVE) {
            @Override protected void init() {
                this.commands.add(new RobotMouseMoveCommand());
            }
        });
        initCommand(new Command("mouse click 1", CMD_MOUSE_CLICK_1) {
            @Override protected void init() {
                this.commands.add(new RobotMousePressCommand(InputEvent.BUTTON1_DOWN_MASK));
                this.commands.add(new RobotMouseReleaseCommand(InputEvent.BUTTON1_MASK));
            }
        });
        initCommand(new Command("mouse click 2", CMD_MOUSE_CLICK_2) {
            @Override protected void init() {
                this.commands.add(new RobotMousePressCommand(InputEvent.BUTTON2_DOWN_MASK));
                this.commands.add(new RobotMouseReleaseCommand(InputEvent.BUTTON2_MASK));
            }
        });
        initCommand(new Command("mouse click 3", CMD_MOUSE_CLICK_3) {
            @Override protected void init() {
                this.commands.add(new RobotMousePressCommand(InputEvent.BUTTON3_DOWN_MASK));
                this.commands.add(new RobotMouseReleaseCommand(InputEvent.BUTTON3_MASK));
            }
        });
        initCommand(new Command("radio play", CMD_RADIO_PLAY) {
            @Override protected void init() {
                this.commands.add(new RobotKeyPressCommand(KeyEvent.VK_ESCAPE));
                this.commands.add(new RobotKeyReleaseCommand(KeyEvent.VK_ESCAPE));
            }
        });
        initCommand(new RecorderReset("recorder reset", "cmd_recorder_reset"));
        initCommand(new RecorderSet("recorder set", "cmd_recorder_set"));
        initCommand(new RecorderStart("recorder start", "cmd_recorder_start"));
        initCommand(new RecorderStop("recorder stop", "cmd_recorder_stop"));
        initCommand(new RecorderStore("recorder store", "cmd_recorder_store"));
    }

    public static Command getCommand(String key) {
        Command c = initializedCommands.get(key);
        return c != null? c : new Command("none", CMD_NONE) {};
    }
}
