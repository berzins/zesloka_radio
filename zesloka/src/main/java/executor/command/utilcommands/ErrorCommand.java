package executor.command.utilcommands;

import eventservice.ClientConnectionManager;
import eventservice.IClientConnection;
import executor.command.Command;
import executor.command.GlobalCommand;
import executor.command.parameters.Parameter;


public class ErrorCommand extends Command {

    public static final String CAD_ERROR = "cmd_error";
    public static final String PARAM_ERROR = "error";

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public ErrorCommand(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), PARAM_ERROR, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)
        });
    }

    @Override
    public void execute() {
        super.execute();

        IClientConnection cc = ClientConnectionManager.getInstance().getClientConnection(
                getParams().getIntegerValue(GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID));
        cc.write("SYNTAX ERROR: " + this.getParams().getStringValue(this, PARAM_ERROR));
    }
}
