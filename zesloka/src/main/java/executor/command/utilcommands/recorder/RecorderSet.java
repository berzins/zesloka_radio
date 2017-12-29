package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;
import executor.command.parameters.Parameter;

public class RecorderSet extends RecorderCommand {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderSet(String name, String key) {
        super(name, key);
        this.initParamKeys(new Parameter[]{
                new Parameter(this.getKey(), SESSION_ID, Parameter.TYPE_DEFAULT, Parameter.VALUE_UNDEFINED),
                new Parameter(this.getKey(), PARAM_CMD_NAME, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED),
                new Parameter(this.getKey(), PARAM_CMD_KEY, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)});
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



