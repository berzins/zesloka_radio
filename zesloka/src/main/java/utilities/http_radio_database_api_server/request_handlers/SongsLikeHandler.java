package utilities.http_radio_database_api_server.request_handlers;

import utilities.JSONUtils;
import utilities.http_radio_database_api_server.Song;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SongsLikeHandler extends RequestHandler {

    @Override
    public void handle(com.sun.net.httpserver.HttpExchange t) throws IOException {
        printRequestData(t);
        // process query and get out text value
        Map<String, String> map = this.queryToMap(t.getRequestURI().getQuery());
        String opt = map.get("search_in");
        String[] options = null;
        if(opt != null) {
            options = opt.split(OPTIONS_DELIMITER);
        }

        // fetch database for results
        ResultSet res = getDbConnection().findSongs(
                map.get("value"),
                options != null && options.length > 0 ?
                        options : new String[]{"artist", "title"});

        //process database results
        try {
            List<Song> songs = createSongList(res);
            String output = songs.size() > 0 ? JSONUtils.createJSON(songs) : "[]";

            // Send response
            sendResponse(output.getBytes(), t, 200);
            res.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
