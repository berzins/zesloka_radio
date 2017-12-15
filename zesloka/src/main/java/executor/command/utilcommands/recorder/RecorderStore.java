package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;

public class RecorderStore extends RecorderCommand {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderStore(String name, String key) {
        super(name, key);
    }

    @Override
    public void execute(String params) {
        CommandRecorderManager.getInstance().
                getRecorder(getSessionId(params), true).save();

    }
}