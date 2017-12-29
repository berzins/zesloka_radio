package executor.command.utilcommands;

import executor.command.Command;

public class GetParamsCommand extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public GetParamsCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[] {PARAM_CMD_KEY});
    }

    @Override
    public void execute() {
        super.execute();
        String pk = "";
        for(String p : Command.getCommand(params.getStringValue(
                this, PARAM_CMD_KEY)).getParamKeys()) {
            pk = pk + p + "  ";
        }
        System.out.println(pk);
    }
}
