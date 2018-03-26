package utilities.http_radio_database_api_server.request_handlers;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.sun.net.httpserver.HttpExchange;
import utilities.JSONUtils;
import utilities.http_radio_database_api_server.Song;
import utilities.http_radio_database_api_server.SongThumbnail;
import utilities.youtube.YoutubeService;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongThumbnailsHandler extends RequestHandler {



    @Override
    public void handle(HttpExchange t) throws IOException {
        //get songs

        printRequestData(t);
        SongsByIndexHandler songFetcher = new SongsByIndexHandler();
        List<Song> songs = null;
        try {
            songs = songFetcher.getSongsByIndexQuery(t.getRequestURI().getQuery());
        } catch (SQLException e) {
            this.sendResponse(
                    ("song fetching failed in SongThumbnailHandler with error : >> " + e.toString()).getBytes(),
                    t, 500);
            e.printStackTrace();
            return;
        }

        // fetch google for images
        // todo: make this loop multithreaded

        YouTube yt = YoutubeService.getYouTubeService();
        List<SongThumbnail> thumnails = new ArrayList<>();
        for(Song s : songs) {
            String query = s.getArtist() + " " + s.getTitle();

            String imgSrc = this.getYoutubeThumbnail(yt, query);
            if(imgSrc == null) {
                imgSrc = "http://simpleicon.com/wp-content/uploads/play1.png";
            }

            SongThumbnail st = new SongThumbnail();
            st.setSong(s);
            st.setImg(imgSrc);
            thumnails.add(st);
        }
        String payload = JSONUtils.createJSON(thumnails);
        this.sendResponse(payload.getBytes(), t, 200);
    }


    public String getYoutubeThumbnail(YouTube yt, String query) {

        try {
            YouTube.Search.List list = yt.search().list("snippet");
            list.setMaxResults(1L);
            list.setQ(query);
            list.setType("video");

            SearchListResponse response = list.execute();
            return response.getItems().get(0).getSnippet().getThumbnails().getMedium().getUrl();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
