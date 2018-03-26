package executor.command.testcommands;


import eventservice.ClientConnectionManager;
import eventservice.client.DefaultCommandLauncher;
import executor.command.Command;

public class TestCaseCommand extends Command {


    private static final DefaultCommandLauncher testClient = new DefaultCommandLauncher();
    private static boolean initialized = false;
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public TestCaseCommand(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        if(!initialized) {
            ClientConnectionManager ccm = ClientConnectionManager.getInstance();
            testClient.addClientConnectionListener(ccm);
            testClient.start();
            initialized = true;
            setRecordable(false);
        }
    }

    @Override
    public void execute() {
        super.execute();
        testClient.nextCommand();
    }
}
