package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.ExecuteResult;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjAutoDjStatus;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjEnableAutoDj;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjOptCommand;
import java.io.IOException;


/**
 * Toggles RadioDj autoDj state..
 */
public class ToggleAutoDj extends RequestHandler {

    public static final int ENABLE = 1;
    public static final int DISABLE = 0;

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);

        try {
            // find out the state of AutoDj
            RadiodjOptCommand optCmd = new RadiodjAutoDjStatus();
            ExecuteResult adjStatus = optCmd.execute();

            if(adjStatus.getResult() == ExecuteResult.SUCCESS) {
                String res = adjStatus.getResponse().getResponse();
                String s = GetAutoDjStateHandler.parseAutoDjResponse(res);
                boolean enabled = Boolean.parseBoolean(s);

                // turn it on or off based on condition
                if(enabled) {
                    this.makeRestCall(new RadiodjEnableAutoDj(DISABLE), "false", t);
                } else {
                    this.makeRestCall(new RadiodjEnableAutoDj(ENABLE), "true", t);
                }
            } else {
                sendResponse(adjStatus.getResponse().getResponse().getBytes(), t, RESPONSE_INTERNAL_ERROR);
            }
        } catch(Exception e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        }

    }


    private void makeRestCall(RadiodjOptCommand cmd, String data, HttpExchange t) throws IOException {
        ExecuteResult er = cmd.execute();
        if(er.getResult() == ExecuteResult.SUCCESS) {
            sendResponse(data.getBytes(), t, RESPONSE_SUCCESS);
        } else {
            sendResponse(er.getMessage().getBytes(), t, RESPONSE_INTERNAL_ERROR);
        }
    }
}
