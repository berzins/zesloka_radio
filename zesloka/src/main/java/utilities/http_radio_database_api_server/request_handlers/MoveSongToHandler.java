package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import db.DBConnection;
import db.DataBase;
import utilities.Util;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.Map;

public class MoveSongToHandler extends  RequestHandler {


    private static final String ATTRIB_TARGET_CATEGORY =  "target_category";

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);

        Map<String, String> attrib = queryToMap(t.getRequestURI().getQuery());
        try{
            String sql = "update songs set id_subcat=? where id=?";
            Integer cat = Integer.parseInt(attrib.get(ATTRIB_TARGET_CATEGORY));
            Integer songId = Integer.parseInt(attrib.get(ATTRIB_SONG_ID));

            if(cat == null || songId == null) {
                throw new NullPointerException(Util.getCurrentTraceInfo() + " 'cat' or 'songId' is null");
            }

            DBConnection conn = DataBase.Companion.getConnection();
            PreparedStatement pst = conn.getPreparedStatement(sql);
            pst.setInt(1, cat);
            pst.setInt(2, songId);

            Integer rowsUpdated =  pst.executeUpdate();
            sendResponse(rowsUpdated.toString().getBytes(), t, RESPONSE_SUCCESS);

        } catch(Exception e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        }
    }
}
