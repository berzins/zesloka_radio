package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;
import executor.command.parameters.CommandParams;

public class RecorderSet extends RecorderCommand {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderSet(String name, String key) {
        super(name, key);
        this.initParamKeys(new String[]{
                CommandParams.createParamKey(this, SESSION_ID),
                CommandParams.createParamKey(this, CMD_NAME),
                CommandParams.createParamKey(this, CMD_KEY)});
    }

    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance()
                .getRecorder(params.getStringValue(this, SESSION_ID), false)
                .set(   params.getStringValue(this, CMD_NAME),
                        params.getStringValue(this, CMD_KEY));
    }
}
