package utilities.http_radio_database_api_server;

import com.sun.net.httpserver.HttpServer;
import db.DBConnection;
import db.DataBase;
import utilities.JSONUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRadioDatabaseAPIServer {

    private static HttpRadioDatabaseAPIServer instance;
    private HttpServer server = null;
    private int port = 8744;
    private DBConnection dbConnection = null;


    private HttpRadioDatabaseAPIServer() {

    }

    public static HttpRadioDatabaseAPIServer getInstance() {
        if(instance == null) {
            instance = new HttpRadioDatabaseAPIServer();
        }
        return instance;
    }


    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/songslike", new SongLikeHandler());
            server.setExecutor(null);
            server.start();
            if(dbConnection != null) {
                dbConnection.close();
            }
            dbConnection = DataBase.Companion.getConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        server.stop(0);
        dbConnection.close();
    }


    private class SongLikeHandler implements com.sun.net.httpserver.HttpHandler {

        @Override
        public void handle(com.sun.net.httpserver.HttpExchange t) throws IOException {
            // process query and get out text value
            String query = t.getRequestURI().getQuery();
            Map<String, String> map = this.queryToMap(query);

            // fetch database for results
            ResultSet res = dbConnection.findSongsWhatIsLike(map.get("value"));

            //process database results
            try {
                List<Song> songs = new ArrayList<>();
                while(res.next()) {
                    Song s = new Song();
                    s.setId(res.getInt("id"));
                    s.setArtist(res.getString("artist"));
                    s.setTitle(res.getString("title"));
                    songs.add(s);
                }
                String output = songs.size() > 0 ? JSONUtils.createJSON(songs) : "[]";

                // Send response
                byte b[] = output.getBytes();
                t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                t.getResponseHeaders().add("Content-Type:" ,"application/json; charset=UTF-8");
                t.sendResponseHeaders(200, b.length);
                OutputStream out = t.getResponseBody();
                out.write(b);
                out.close();
                res.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        private Map<String, String> queryToMap(String query) {
            Map<String, String> map = new HashMap();
            for(String param : query.split("&" )) {
                String pair[] = param.split("=");
                if(pair.length > 1) {
                    map.put(pair[0], pair[1]);
                } else {
                    map.put(pair[0], "");
                }
            }
            return map;
        }
    }
}
