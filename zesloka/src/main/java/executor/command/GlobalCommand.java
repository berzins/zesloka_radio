package executor.command;

import executor.command.parameters.Parameter;

public class GlobalCommand extends Command {

    public static final String PARAM_CLIENT_ID = "client_id";
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public GlobalCommand(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        initParamKeys(new Parameter[] {
           new Parameter(this.getKey(), PARAM_CLIENT_ID, Parameter.TYPE_DEFAULT, "")
        });
    }
}
