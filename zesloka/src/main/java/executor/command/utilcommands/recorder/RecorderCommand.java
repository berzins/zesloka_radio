package executor.command.utilcommands.recorder;

import executor.command.Command;

public class RecorderCommand extends Command {

    protected static final String SESSION_ID = "session_id";

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderCommand(String name, String key) {
        super(name, key);
    }


}
