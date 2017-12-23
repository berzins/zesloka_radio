package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;

public class RecorderSet extends RecorderCommand {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderSet(String name, String key) {
        super(name, key);
        this.initParamKeys(new String[]{
                SESSION_ID,
                PARAM_CMD_NAME,
                PARAM_CMD_KEY});
    }

    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance()
                .getRecorder(params.getStringValue(this, SESSION_ID), false)
                .set(   params.getStringValue(this, PARAM_CMD_NAME),
                        params.getStringValue(this, PARAM_CMD_KEY));
    }
}
