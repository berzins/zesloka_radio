package executor.command.testcommands;

import eventservice.ClientConnection;
import eventservice.ClientConnectionManager;
import eventservice.IClientConnection;
import executor.command.Command;
import utilities.TimeUtils;

public class TestCommand extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public TestCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[] {PARAM_TEXT});
    }

    @Override
    public void execute() {
        super.execute();
        IClientConnection cc = ClientConnectionManager
                .getInstance()
                .getClientConnection(Integer.valueOf(params.getStringValue(
                        GLOBAL_PARAMS, PARAM_GLOBAL_CLIENT_ID
                )));
        if(cc != null) {
            cc.write(TimeUtils.getCurrentTimeString() +
                    ": executing '" + this.getName() + "' command with params : " +
                    this.params.getStringValue(this, PARAM_TEXT));
        }

    }
}
