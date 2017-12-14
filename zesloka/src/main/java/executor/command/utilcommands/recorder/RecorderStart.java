package executor.command.utilcommands.recorder;

import executor.command.Command;
import executor.command.CommandProcessorManager;
import executor.command.CommandRecorder;
import executor.command.CommandRecorderManager;

public class RecorderStart extends RecorderCommand {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderStart(String name, String key) {
        super(name, key);
    }

    /**
     * Create new Recorder for session and launch recording mechanism
     * @param params params are expected to be a sessionId, so any unique char sequence is valid param
     */
    @Override
    public void execute(String params) {
        CommandRecorderManager.getInstance().getRecorder(getSessionId(params), true).start();
    }

}
