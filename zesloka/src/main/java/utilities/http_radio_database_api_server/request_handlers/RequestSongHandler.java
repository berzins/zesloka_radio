package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class RequestSongHandler extends RequestHandler {

    public int insertSongIntoRequests(int songIndex, HttpExchange t) throws SQLException {
        String sql = "insert into `requests` " +
                "(songID, username, userIP, message, requested, played)" +
                " values(?,?,?,?,?,?)";
        PreparedStatement stm = getDbConnection().getPreparedStatement(sql);
        stm.setInt(1, songIndex);
        stm.setString(2, String.valueOf(t.getRequestURI()));
        stm.setString(3, String.valueOf(t.getRemoteAddress()));
        stm.setString(4 ,"song request from space");
        stm.setTimestamp(5, new Timestamp(new Date().getTime()));
        stm.setInt(6, 0);

        return stm.executeUpdate();

    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);
        Map<String, String> map = this.queryToMap(t.getRequestURI().getQuery());
        try{

            int si = Integer.parseInt(map.get("song_index"));
            int rowsAffected = insertSongIntoRequests(si, t);
            if(rowsAffected  == 0) {
                sendResponse("0 rows updated.".getBytes(), t, 200);
                return;
            }
            sendResponse((rowsAffected + " rows updated").getBytes(), t, 200);

        } catch (NumberFormatException e){
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, 500);
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, 500);
        }
    }
}
