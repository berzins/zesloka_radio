package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;
import executor.command.parameters.Parameter;

public class RecorderStore extends RecorderCommand {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderStore(String name, String key) {
        super(name, key);
        this.initParamKeys(new Parameter[]{
                new Parameter(this.getKey(), SESSION_ID, Parameter.TYPE_DEFAULT, Parameter.VALUE_UNDEFINED)});
    }

    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance().
                getRecorder(params.getStringValue(this, SESSION_ID), true).save();

    }
}
