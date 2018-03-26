package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SongCountHandler extends RequestHandler{


    @Override
    public void handle(HttpExchange t) throws IOException {
        printRequestData(t);
        ResultSet res = getDbConnection().getRowCountOfTable("songs");
        try {
            if(res.next()) {
                Integer output = res.getInt("count(*)");
                sendResponse(output.toString().getBytes(), t, 200);
            }
        } catch (SQLException e) {
            sendResponse(("sql exception thrown in" + getClass()).getBytes(), t, 500 );
            e.printStackTrace();
        }
    }
}
