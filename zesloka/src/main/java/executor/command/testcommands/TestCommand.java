package executor.command.testcommands;

import eventservice.ClientConnectionManager;
import eventservice.IClientConnection;
import executor.command.Command;
import executor.command.GlobalCommand;
import executor.command.parameters.Parameter;
import utilities.JSONUtils;
import utilities.TimeUtils;

public class TestCommand extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public TestCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), PARAM_TEXT, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)});
    }

    @Override
    public void execute() {
        super.execute();
        IClientConnection cc = ClientConnectionManager
                .getInstance()
                .getClientConnection(Integer.valueOf(params.getStringValue(
                        GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID
                )));
        if(cc != null) {
            cc.write(TimeUtils.getCurrentTimeString() +
                    ": executing '" + this.getName() + "' command with params : " +
                    this.params.getStringValue(this, PARAM_TEXT));
            cc.write(JSONUtils.createJSON(params.getCommandData()));
        }
    }
}
