package executor.command;

import executor.command.parameters.CommandData;
import executor.command.parameters.CommandParams;
import executor.command.parameters.Parameter;
import executor.command.robotcommands.*;
import executor.command.testcommands.TestCommand;
import executor.command.utilcommands.GetParamsCommand;
import executor.command.utilcommands.database.GetSongLike;
import executor.command.utilcommands.mouse.*;
import executor.command.utilcommands.recorder.*;
import utilities.Storage;
import utilities.TimeUtils;
import utilities.Util;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.*;

/**
 * Represents class what can perform previously defined action.
 */
public abstract class Command implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CMD_GLOBAL = "cmd_global";
    public static final Command GLOBAL_PARAMS = new GlobalCommand("", Command.CMD_GLOBAL);
    public static final String PARAM_GLOBAL_SESSION_ID = "session_id";

    protected static final String PARAM_CMD_NAME = "cmd_name";
    protected static final String PARAM_CMD_KEY = "cmd_key";
    protected static final String PARAM_TEXT = "text";

    protected String key;
    protected String name;
    protected final List<Parameter> paramKeys;
    protected CommandParams params;
    private Long timeout = 0L;
    private Command nextCommand;
    private boolean finall;

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
    public Command setParams(CommandParams params) {
        if(this.isFinal()) return this;
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
     * Add parameter value pair to current parameters
     */
    public Command addParam(Command cmd, String key, String value) {
        if(this.isFinal()) return this;
        params.addValue(cmd.getKey(), key, value);
        this.setParams(getParams());
        return this;
    }


    /**
     * Collects registered parameter keys of all sub commands
     */
    public CommandData getRequiredParameters() {
        CommandData cd = CommandData.getEmpty();
        if(!isFinal()) {
            if(nextCommand != null) {
                cd = this.nextCommand.getRequiredParameters();
                cd.getParams().addAll(this.paramKeys);
            } else {
                cd.getParams().addAll(this.paramKeys);
            }
        }
        cd.setCmd_key(this.getKey());
        return cd;
    }

    /**
     * Search for particular command in sub commands;
     * @param key command key
     * @return if match not found return is null
     */
    public Command getSubCommand(String key) {
        if(nextCommand!= null) {
            if(nextCommand.getKey().equals(key)) {
                return nextCommand;
            }
            return nextCommand.getSubCommand(key);
        }
        return null;
    }

    /**
     * Determines if this and all sub command params can be changed
     */
    public void setFinal(boolean b) {
        finall = b;
        if(nextCommand != null) {
            nextCommand.setFinal(b);
        }
    }

    /**
     * Determines if this command params can be changed
     */
    protected boolean isFinal(){
        return this.finall;
    }

    /**
     * Init parameter keys what are used to get parameter values out of current Params.
     * @param keys
     */
    protected void initParamKeys(Parameter[] keys){
        if(keys != null) {
            for(Parameter k : keys) {
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
        void processCommand(Command cmd);
    }


    // ------------- DEFAULT INIT ---------------

    public static String CMD_NONE = "cmd_none";
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
        }
        return Util.writeToFile(Storage.FILE_PATH_COMMANDS, cmds);
    }


    public static void initDefaultCommands() {

        initCommand(new Command("none", CMD_NONE) {});
        initCommand(new Command("radio play", CMD_RADIO_PLAY) {
            @Override protected void init() {
                initParamKeys(new Parameter[] {new Parameter(
                        this.getKey(),
                        RobotCommand.KEY_EVENT,
                        Parameter.TYPE_INTEGER,
                        "")});
                Command press = new RobotKeyPressCommand("robot key press", RobotCommand.ROBOT_CMD_KEY_PRESS);
                Command release = new RobotKeyPressCommand("robot key release", RobotCommand.ROBOT_CMD_KEY_RELEASE);
                release.setTimeout(10L);
                this.add(press);
                this.add(release);
                this.addParam(press, RobotCommand.KEY_EVENT, String.valueOf(KeyEvent.VK_ESCAPE));
                this.addParam(release, RobotCommand.KEY_EVENT, String.valueOf(KeyEvent.VK_ESCAPE));
                this.setFinal(true);
            }
        });
        initCommand(new RecorderReset("recorder reset", "cmd_recorder_reset"));
        initCommand(new RecorderSet("recorder set", "cmd_recorder_set"));
        initCommand(new RecorderStart("recorder start", "cmd_recorder_start"));
        initCommand(new RecorderStop("recorder stop", "cmd_recorder_stop"));
        initCommand(new RecorderStore("recorder store", "cmd_recorder_store"));
        initCommand(new TestCommand("test command", "cmd_test"));
        initCommand(new GetParamsCommand("get parameters", "cmd_get_param_key"));
        initCommand(new MouseClick_1("mouse click 1", "cmd_mouse_click_1"));
        initCommand(new MouseClick_2("mouse click 2", "cmd_mouse_click_2"));
        initCommand(new MouseClick_3("mouse click 3", "cmd_mouse_click_3"));
        initCommand(new MouseMove("mouse move", "cmd_mouse_move"));
        initCommand(new MouseMoveTo("mouse move to", "cmd_mouse_move_to"));
        initCommand(new GetSongLike("get song like", "cmd_get_song_like"));
    }

    public static void initUserCommands() {
        List<Command> cmds = Util.readFromFile(Storage.FILE_PATH_COMMANDS);
        if(cmds != null) {
            for(Command c : cmds) {
                initCommand(c);
            }
        }
    }

    public static Command getCommand(String key) {
        Command c = initializedCommands.get(key);
        return c != null? Util.serializedCopy(c) : new Command("none", CMD_NONE) {};
    }
}

