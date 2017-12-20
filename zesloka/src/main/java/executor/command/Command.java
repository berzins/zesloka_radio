package executor.command;

import executor.command.parameters.CommandParams;
import executor.command.robotcommands.*;
import executor.command.testcommands.TestCommand;
import executor.command.utilcommands.recorder.*;
import utilities.Storage;
import utilities.TimeUtils;
import utilities.Util;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.*;

/**
 * Represents class what can perform previously defined action.
 */
public abstract class Command implements Serializable {

    protected String key;
    protected String name;
    protected final List<String> paramKeys;
    protected CommandParams params;
    private Long timeout = 0L;
    private Command nextCommand;

    /**
     * @param name Command string representation
     * @param key Unique command identifier
     */
    public Command(String name, String key) {
        this.key = key;
        this.name = name;
        this.paramKeys = new ArrayList<>();
        this.params = new CommandParams("");
        init();
    }

    /**
     * Executes all sub command structure.
     * Method can have blocking behaviour so it should be executed in separate thread
     */
    public void execute() {
        if(timeout > 0) {
            try {
                Thread.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(nextCommand != null) {
            nextCommand.executeAsync();
        }
        System.out.println(TimeUtils.getCurrentTimeString() + ": executing " + this.getKey() + " after " + this.getTimeout() + " mils");
    }

    /**
     * Execute command async. This will do call on 'void execute()' in separate thread.
     */
    public final void executeAsync() {
        new Thread(() -> Command.this.execute()).start();
    }


    /**
     * add Command to this roots last command in execution chain
     * @param cmd
     */
    public void add(Command cmd) {
        if(this.nextCommand == null) {
            this.nextCommand = Util.serializedCopy(cmd);
        } else {
            this.nextCommand.add(cmd);
        }
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
     * Set Command operation parameters.. if not set.. default is empty
     */
    public final Command setParams(CommandParams params) { //  <  param_key , param_value >
        this.params =  params;
        if(this.nextCommand != null) {
            this.nextCommand.setParams(params);
        }
        return this;
    }

    /**
     * Command and subCommand current parameters
     */
    public CommandParams getParams() {
        return (this.params);
    }


    /**
     * Collects all sub command parameter keys
     */
    public List<String> getParamKeys() {
        List<String> pk;
        if(this.nextCommand!=null) {
            pk = this.nextCommand.getParamKeys();
            pk.addAll(this.paramKeys);
        } else {
            pk = this.paramKeys;
        }
        return pk;
    }


    /**
     * Init parameter keys what are used to get parameter values out of current Params.
     */
    protected void initParamKeys(String[] keys){
        if(keys != null) {
            for(String k : keys) {
                this.paramKeys.add(k);
            }
        }
    }

    /**
     * Override this method to do inline Command Initialization.
     * This is called as last method in Command constructor.
     */
    protected void init(){}



    /**
     * Sets timeout before command execution.
     */
    final public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }
    final public Long getTimeout(){return this.timeout; }

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
    public static String getRootCommand(String cmd) {
        if(cmd.contains("?")) {
            String[] c = cmd.split("\\?");
            return c[0];
        }
        return cmd;
    }

    /**
     * Extracts command parameters from incoming command string.
     */
    public static Map<String,String> parseParams(String cmd) {
        Map<String,String> ret = null;
        if(cmd.contains("?")) {
            ret = new HashMap<>();
            String paramStr = cmd.split("\\?")[1];
            String[] params = paramStr.split("\\&");

            for(String p :  params) {
                String[] pair = p.split("\\=");
                ret.put(pair[0], pair[1]);
            }
        }
        return ret;
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
                this.add(new RobotMouseMoveCommand() {});
            }
        });
        initCommand(new Command("mouse click 1", CMD_MOUSE_CLICK_1) {
            @Override protected void init() {
                this.add(new RobotMousePressCommand(InputEvent.BUTTON1_DOWN_MASK));
                this.add(new RobotMouseReleaseCommand(InputEvent.BUTTON1_MASK));
            }
        });
        initCommand(new Command("mouse click 2", CMD_MOUSE_CLICK_2) {
            @Override protected void init() {
                this.add(new RobotMousePressCommand(InputEvent.BUTTON2_DOWN_MASK));
                this.add(new RobotMouseReleaseCommand(InputEvent.BUTTON2_MASK));
            }
        });
        initCommand(new Command("mouse click 3", CMD_MOUSE_CLICK_3) {
            @Override protected void init() {
                this.add(new RobotMousePressCommand(InputEvent.BUTTON3_DOWN_MASK));
                this.add(new RobotMouseReleaseCommand(InputEvent.BUTTON3_MASK));
            }
        });
        initCommand(new Command("radio play", CMD_RADIO_PLAY) {
            @Override protected void init() {
                this.add(new RobotKeyPressCommand(KeyEvent.VK_ESCAPE));
                this.add(new RobotKeyReleaseCommand(KeyEvent.VK_ESCAPE));
            }
        });
        initCommand(new RecorderReset("recorder reset", "cmd_recorder_reset"));
        initCommand(new RecorderSet("recorder set", "cmd_recorder_set"));
        initCommand(new RecorderStart("recorder start", "cmd_recorder_start"));
        initCommand(new RecorderStop("recorder stop", "cmd_recorder_stop"));
        initCommand(new RecorderStore("recorder store", "cmd_recorder_store"));
        initCommand(new TestCommand("test command", "cmd_test"));
    }

    public static Command getCommand(String key) {
        Command c = initializedCommands.get(key);
        return c != null? Util.serializedCopy(c) : new Command("none", CMD_NONE) {};
    }
}

