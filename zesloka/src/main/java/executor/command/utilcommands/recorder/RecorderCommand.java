package executor.command.utilcommands.recorder;

import executor.command.Command;
import executor.command.CommandRecorder;

public class RecorderCommand extends Command {

    protected static final String SESSION_ID = "session_id";
    protected static final String CMD_NAME = "cmd_name";
    protected static final String CMD_KEY = "cmd_key";


    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderCommand(String name, String key) {
        super(name, key);
    }


}
