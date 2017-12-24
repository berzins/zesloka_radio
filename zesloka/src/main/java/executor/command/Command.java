package executor.command;

import executor.command.parameters.CommandParams;
import executor.command.robotcommands.*;
import executor.command.testcommands.TestCommand;
import executor.command.utilcommands.GetParamsCommand;
import executor.command.utilcommands.database.GetSongLike;
import executor.command.utilcommands.mouse.*;
import executor.command.utilcommands.recorder.*;
import org.w3c.dom.events.EventTarget;
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

    private static final long serialVersionUID = 1L;

    protected static final String PARAM_CMD_NAME = "cmd_name";
    protected static final String PARAM_CMD_KEY = "cmd_key";
    protected static final String PARAM_TEXT = "text";

    protected String key;
    protected String name;
    protected final List<String> paramKeys;
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
    public Command addParam(String key, String value) {
        if(this.isFinal()) return this;
        params.addValue(key, value);
        this.setParams(getParams());
        return this;
    }


    /**
     * Collects all sub command parameter keys
     */
    public List<String> getParamKeys() {
        List<String> pk;
        if(!isFinal()) { // expose params only if they can take any effect.
            if(this.nextCommand!=null) {
                pk = this.nextCommand.getParamKeys();
                    pk.addAll(this.paramKeys);
            } else {
                pk = this.paramKeys;
            }
        } else {pk = new ArrayList<>();}
        return pk;
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
     */
    protected void initParamKeys(String[] keys){
        if(keys != null) {
            for(String k : keys) {
                this.paramKeys.add(CommandParams.createParamKey(this, k));
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

    public static String addSourceIdToParams(String cmd,int hash) {
        return cmd + "&source_id=" + hash;
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
                initParamKeys(new String[] {RobotCommand.KEY_EVENT});
                Command press = new RobotKeyPressCommand("robot key press", RobotCommand.ROBOT_CMD_KEY_PRESS);
                Command release = new RobotKeyPressCommand("robot key release", RobotCommand.ROBOT_CMD_KEY_RELEASE);
                release.setTimeout(10L);
                this.add(press);
                this.add(release);
                this.addParam(CommandParams.createParamKey(press, RobotCommand.KEY_EVENT), String.valueOf(KeyEvent.VK_ESCAPE));
                this.addParam(CommandParams.createParamKey(release, RobotCommand.KEY_EVENT), String.valueOf(KeyEvent.VK_ESCAPE));
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

