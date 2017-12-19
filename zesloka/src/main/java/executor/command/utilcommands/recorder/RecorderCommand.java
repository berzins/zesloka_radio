package executor.command.utilcommands.recorder;

import executor.command.Command;
import executor.command.CommandRecorder;

public class RecorderCommand extends Command {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderCommand(String name, String key) {
        super(name, key);
    }

    protected String getSessionId(String params) {
        //TODO: it would be nice to implement some solid logic how ids ar created and decoded
        // for now this is just placeholder.
        return params;
    }

}
