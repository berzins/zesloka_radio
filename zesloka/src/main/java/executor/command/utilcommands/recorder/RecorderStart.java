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
     * params are expected to be a sessionId, so any unique char sequence is valid param
     */
    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance().getRecorder(getSessionId(params), true).start();
    }

}
