package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;

public class RecorderStop extends RecorderCommand {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderStop(String name, String key) {
        super(name, key);
        this.initParamKeys(new String[]{SESSION_ID});
    }

    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance().
                getRecorder(params.getStringValue(this, SESSION_ID), true).stop();
    }
}
