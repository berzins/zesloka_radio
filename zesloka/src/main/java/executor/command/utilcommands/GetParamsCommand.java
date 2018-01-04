package executor.command.utilcommands;

import eventservice.ClientConnectionManager;
import executor.command.Command;
import executor.command.GlobalCommand;
import executor.command.parameters.CommandData;
import executor.command.parameters.Parameter;
import utilities.JSONUtils;

public class GetParamsCommand extends Command {

    public static final String CMD_GET_PARAMS = "cmd_get_params";

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public GetParamsCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), PARAM_CMD_KEY, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)});
        setRecordable(false);
    }

    @Override
    public void execute() {
        super.execute();
        CommandData dataTempl =  Command.getCommand(params.getStringValue(
                this, PARAM_CMD_KEY)).getRequiredParameters();

        String json = JSONUtils.createJSON(dataTempl);
        ClientConnectionManager.getInstance()
                .getClientConnection(params.getIntegerValue(GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID))
                .write(json);
    }
}
