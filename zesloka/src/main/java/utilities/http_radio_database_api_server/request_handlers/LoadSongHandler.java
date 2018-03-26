package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import utilities.Util;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.ExecuteResult;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.RadiodjRestCommand;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjSongLoader;

import java.io.IOException;
import java.util.Map;

public class LoadSongHandler extends RequestHandler {

    private static final String ATTRIB_TRACK_ID = "track_id";
    private static final String ATTRIB_PLACEMENT = "placement";

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);

        Map<String, String> map = queryToMap(t.getRequestURI().getQuery());

        Integer trackID;
        String placement;
        try{
            trackID = Integer.parseInt(map.get(ATTRIB_TRACK_ID));
            placement = map.get(ATTRIB_PLACEMENT);
            if(trackID == null || placement == null) throw  new NullPointerException();
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(("" + Util.getCurrentTraceInfo() + " - " + e.toString()).getBytes(), t, 200);
            return;
        }

        ExecuteResult res =  null;
        try {
            RadiodjRestCommand cmd = new RadiodjSongLoader(
                    placement, trackID.toString());
            res = cmd.execute();
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        }

        // handle success
        if(res != null && res.getResult() > 0) {
            sendResponse(res.getResponse().getResponse().getBytes(), t, RESPONSE_SUCCESS);
        } else { // handle all other crap
            if(res != null) {
                sendResponse(res.getMessage().getBytes(), t, RESPONSE_INTERNAL_ERROR);
            } else {
                sendResponse(("Something went wrong at " +
                        this.getClass().getCanonicalName()).getBytes(),
                        t, RESPONSE_INTERNAL_ERROR);
            }
        }
    }
}
