package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.ExecuteResult;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.RadiodjRestCommand;

import java.io.IOException;

public class RadiodjNoArgHandler extends RequestHandler {

    private RadiodjRestCommand restCmd;

    public RadiodjNoArgHandler(RadiodjRestCommand cmd) {
        this.restCmd = cmd;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);

        try{
            ExecuteResult er = restCmd.execute();
            if(er.getResult() == ExecuteResult.SUCCESS) {
                sendResponse((er.getResponse().getResponse()).getBytes(), t, RESPONSE_SUCCESS);
            } else {
                sendResponse(er.getMessage().getBytes(), t, RESPONSE_INTERNAL_ERROR);
            }
        } catch(Exception e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        }
    }
}

