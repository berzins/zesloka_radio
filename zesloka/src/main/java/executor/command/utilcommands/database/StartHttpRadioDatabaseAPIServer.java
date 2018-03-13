package executor.command.utilcommands.database;

import eventservice.ClientConnectionManager;
import executor.command.Command;
import executor.command.GlobalCommand;
import utilities.http_radio_database_api_server.HttpRadioDatabaseAPIServer;


public class StartHttpRadioDatabaseAPIServer extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */

    HttpRadioDatabaseAPIServer databaseAPIServer = null;

    public StartHttpRadioDatabaseAPIServer(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void execute() {
        super.execute();

        if(databaseAPIServer == null) {
            this.databaseAPIServer = HttpRadioDatabaseAPIServer.getInstance();
            this.databaseAPIServer.start();
            ClientConnectionManager.getInstance()
                    .getClientConnection(getParams().getIntegerValue(GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID))
                    .write("Database http API Server started");
        }
    }
}
