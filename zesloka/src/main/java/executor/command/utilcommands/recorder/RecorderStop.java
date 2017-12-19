package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;

public class RecorderStop extends RecorderCommand {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderStop(String name, String key) {
        super(name, key);
    }

    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance().
                getRecorder(getSessionId(params), true).stop();
    }
}
