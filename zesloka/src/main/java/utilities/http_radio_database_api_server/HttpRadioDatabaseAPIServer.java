package utilities.http_radio_database_api_server;

import com.sun.net.httpserver.HttpServer;
import db.DBConnection;
import db.DataBase;
import utilities.http_radio_database_api_server.request_handlers.*;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjAutoDjStatus;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjClearPlaylist;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjPlay;
import utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt.RadiodjStop;

import java.io.IOException;
import java.net.InetSocketAddress;

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
            server.createContext("/songslike", new SongsLikeHandler());
            server.createContext("/songsByIndex", new SongsByIndexHandler());
            server.createContext("/songCount" , new SongCountHandler());
            server.createContext("/songThumbnails", new SongThumbnailsHandler());
            server.createContext("/requestSong", new RequestSongHandler());
            server.createContext("/updatePlaylist", new UpdatePlaylistHandler());
            server.createContext("/loadSong", new LoadSongHandler());
            server.createContext("/moveSongTo", new MoveSongToHandler());
            server.createContext("/play", new RadiodjNoArgHandler(new RadiodjPlay()));
            server.createContext("/stop", new RadiodjNoArgHandler(new RadiodjStop()));
            server.createContext("/clearPlaylist", new RadiodjNoArgHandler(new RadiodjClearPlaylist()));
            server.createContext("/toggleAutoDj", new ToggleAutoDj());
            server.createContext("/getAutoDjState", new GetAutoDjStateHandler());
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
}
