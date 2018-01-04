package executor.command.utilcommands;

import eventservice.ClientConnection;
import eventservice.ClientConnectionManager;
import eventservice.IClientConnection;
import executor.command.Command;
import executor.command.GlobalCommand;
import executor.command.parameters.CommandData;
import utilities.JSONUtils;

import java.util.ArrayList;
import java.util.List;

public class GetAllCommandParams extends Command {

    public static final String CMD_GET_ALL_COMMAND_PARAMS = "cmd_get_all_cmd_params";
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public GetAllCommandParams(String name, String key) {
        super(name, key);
    }

    @Override
    public void execute() {
        super.execute();
        List<Command> cmds = Command.getInitializedCommands();
        List<CommandData> params = new ArrayList<>();
        for(Command c : cmds) {
            params.add(c.getParams().getCommandData());
        }
        Integer id = getParams().getIntegerValue(
                GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID);
        IClientConnection cc = ClientConnectionManager.getInstance()
                .getClientConnection(id);
        for(Command c : cmds) {
            cc.write(c.getKey() + " <:> " + c.getName());
        }
//        cc.write(JSONUtils.createJSON(params));
    }
}
