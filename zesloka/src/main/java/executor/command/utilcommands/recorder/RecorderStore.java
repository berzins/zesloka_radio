package executor.command.utilcommands.recorder;

import executor.command.Command;
import executor.command.CommandProcessorManager;
import executor.command.CommandRecorder;
import executor.command.CommandRecorderManager;
import executor.command.parameters.CommandParams;
import executor.command.parameters.Parameter;
import executor.command.utilcommands.ErrorCommand;
import utilities.Util;

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
        CommandRecorder rc = CommandRecorderManager.getInstance().
                getRecorder(params.getStringValue(this, SESSION_ID), true);
        if (rc.save() == null) {
            ErrorCommand.riseError(
                    "ERROR: Storing command with key " + rc.getRecordKey() + " failed.",
                    Util.serializedCopy(this.getParams()));
        }
    }
}
