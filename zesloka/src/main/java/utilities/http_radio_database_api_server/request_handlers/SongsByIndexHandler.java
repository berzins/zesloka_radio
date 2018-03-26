package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import utilities.JSONUtils;
import utilities.http_radio_database_api_server.Song;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SongsByIndexHandler extends RequestHandler{

    /**
     * Make request to radio database and return list of songs based on passed indexes in http query
     * @param query http query.. expected to contain "song_index" property
     * @return Return fetched songs from database
     * @throws SQLException
     */
    public List<Song> getSongsByIndexQuery(String query) throws SQLException{
        Map<String, String> map = queryToMap(query);
        String[] s_options = map.get("song_index").split(OPTIONS_DELIMITER);
        List<Integer> options = new ArrayList<>();
        for(String s : s_options) {
            options.add(Integer.valueOf(s));
        }

        Integer [] indices = new Integer[options.size()];
        for(int i = 0; i < indices.length; i++) {
            indices[i] = options.get(i);
        }
        ResultSet res = getDbConnection().getSongsByIndecies(indices);
        return createSongList(res);
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        printRequestData(t);
        try {

            List<Song> songs = this.getSongsByIndexQuery(t.getRequestURI().getQuery());
            String output = songs.size() > 0 ? JSONUtils.createJSON(songs) : "[]";

            // lets send response
            sendResponse(output.getBytes(), t, 200);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
