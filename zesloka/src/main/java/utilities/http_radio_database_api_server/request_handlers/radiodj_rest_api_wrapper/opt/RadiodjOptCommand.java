package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.ExecuteResult;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.RadiodjRestCommand;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.RadiodjRestResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public abstract class RadiodjOptCommand  extends RadiodjRestCommand {


    private String command;
    private String argument;
    private boolean isArgRequired;

    RadiodjOptCommand() {
        this.setType("opt");
    }

    @Override
    public ExecuteResult execute() throws IllegalStateException {

        Map<String, String> args = OptArgFactory.getArgs(this.getCommand(), this.getArgument(), isArgRequired());

        try {
            URL url = new URL(this.getCommandString(args));
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                sb.append(line);
            }

            if(conn.getResponseCode() == 200) {
                return new ExecuteResult(new RadiodjRestResponse(
                        200, sb.toString()), ExecuteResult.SUCCESS);
            } else {
                return new ExecuteResult(new RadiodjRestResponse(
                        conn.getResponseCode(), sb.toString()), ExecuteResult.ERROR_GENERIC, conn.getResponseMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ExecuteResult(null, ExecuteResult.ERROR_GENERIC, e.toString());
        }
    }

    String getCommand() {
        return command;
    }

    void setCommand(String command) {
        this.command = command;
    }

    public String getArgument() {
        return argument;
    }

    public void setArgument(String argument) {
        this.setArgRequired(true);
        this.argument = argument;
    }

    boolean isArgRequired() {
        return isArgRequired;
    }

    void setArgRequired(boolean argRequired) {
        isArgRequired = argRequired;
    }
}
