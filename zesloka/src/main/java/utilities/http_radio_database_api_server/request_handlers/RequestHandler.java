package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import db.DBConnection;
import db.DataBase;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import utilities.http_radio_database_api_server.Song;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RequestHandler implements HttpHandler {


    static final String ATTRIB_SONG_ID = "song_id";

    static final String OPTIONS_DELIMITER = "_";
    static final Integer RESPONSE_SUCCESS = 200;
    static final Integer RESPONSE_EXCEPTION = 200;
    static final Integer RESPONSE_INTERNAL_ERROR = 500;

    private DBConnection dbConnection = null;
    private HttpExchange httpExchange = null;

    RequestHandler() {
        dbConnection = DataBase.Companion.getConnection();
    }

    DBConnection getDbConnection() {
        if(dbConnection.isClosed()) {
            dbConnection = DataBase.Companion.getConnection();
        }
        return this.dbConnection;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        this.httpExchange = t;
        printRequestData(t);
    }

    Map<String, String> queryToMap(String query) {
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

    void sendResponse(byte[] data, HttpExchange t, int responseCode) throws IOException {
        t.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        t.getResponseHeaders().add("Content-Type:" ,"application/json; charset=UTF-8");
        t.sendResponseHeaders(responseCode, data.length);
        OutputStream out = t.getResponseBody();
        out.write(data);
        out.close();
    }

    List<Song> createSongList(ResultSet result) throws SQLException {
        List<Song> songs = new ArrayList<>();
        while(result.next()) {
            Song s = new Song();
            s.setId(result.getInt("id"));
            s.setArtist(result.getString("artist"));
            s.setTitle(result.getString("title"));
            songs.add(s);
        }
        result.close();
        return songs;
    }

    void printRequestData(HttpExchange t){
        System.out.println("incoming request from: " + t.getRemoteAddress() + ", URI: " + t.getRequestURI());
    }

    void sendIntegerParseException(String attrName, HttpExchange t) throws IOException {
        sendResponse(("Attribute '"+ attrName + "' value is not a number").getBytes()
                , t, RESPONSE_EXCEPTION);
    }

    public HttpExchange getHttpExchange() {
        return httpExchange;
    }
}
