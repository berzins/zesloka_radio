package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper;

import utilities.GlobalSettings;

import java.util.Map;

public abstract class RadiodjRestCommand {


    private static final String SETTINGS_SERVER_URL = "radiodj_rest_server_url";
    private static final String SETTINGS_SERVER_PSWD = "radiodj_rest_server_pswd";

    private static String serverURL = null;
    private static String serverPassword =  null;
    private String type;

    public RadiodjRestCommand() {

        if(serverURL == null) {
            serverURL = GlobalSettings.getInstance().
                    getSettings().getValue(SETTINGS_SERVER_URL);
        }
        if(serverPassword == null) {
            serverPassword = GlobalSettings.getInstance()
                    .getSettings().getValue(SETTINGS_SERVER_PSWD);
        }
    }


    /**
     * executes command and returns execution state information
     * if command does not require argument it should be left blank
     * @return negative if execution failed or positive if success
     */
    public abstract ExecuteResult execute();

    public String getCommandBaseString() throws IllegalStateException {
        if(type == null) throw new IllegalStateException("Radiodj REST server command type is not defined");
        return serverURL + type + "?auth=" + serverPassword;
    }

    public String getCommandString(Map<String, String> args) throws IllegalStateException {
        String baseStr = getCommandBaseString();
        for(Map.Entry<String, String> e : args.entrySet()){
            baseStr += "&" +
                    e.getKey() + "=" +
                    e.getValue();
        }
        return baseStr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
