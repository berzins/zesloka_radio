package executor.command;

public class GlobalCommand extends Command {
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
        initParamKeys(new String[] {
                PARAM_GLOBAL_CLIENT_ID
        });
    }
}
