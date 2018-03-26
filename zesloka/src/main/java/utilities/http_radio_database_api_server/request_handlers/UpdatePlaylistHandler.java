package utilities.http_radio_database_api_server.request_handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  This handler update/replace request table with songs chosen by rotations playlist rules
 *  By now songs are chosen in rotation setup order and randomly.
 *
 *  Possible attributes are:
 *  @playlist_index values: values in range what are stored in 'rotations' table
 *  @mode  (optional) values: (replace, add) default value is 'add'
 *  @repeat (optional) values: number what represents how much rotation cycles will be updated.
 */
public class UpdatePlaylistHandler extends RequestHandler {

    private static final String MODE_REPLACE = "replace";
    private static final String MODE_ADD = "add";

    private static final String ATTRIB_MODE = "mode";
    private static final String ATTRIB_PLAYLIST_INDEX = "playlist_index";
    private static final String ATTRIB_REPEAT = "repeat";

    @Override
    public void handle(HttpExchange t) throws IOException {
        super.handle(t);

        Map<String, String> map = queryToMap(t.getRequestURI().getQuery());

        String mode = map.get(ATTRIB_MODE);
        if(mode != null) {
            // do mode specific stuff
            if (mode.equals(MODE_REPLACE)) {
                PreparedStatement ps = getDbConnection().getPreparedStatement("truncate `requests`");
                try {
                    int raf = ps.executeUpdate();
                    sendResponse((raf + " rows affected").getBytes(), t, RESPONSE_SUCCESS);
                } catch (SQLException e) {
                    e.printStackTrace();
                    sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
                }
            }
        }

        // get playlist by index
        Rotation rot = null;
        try {
            Integer pindex = Integer.parseInt(map.get(ATTRIB_PLAYLIST_INDEX));
            String sql = "select * from `rotations_list` where pID=?";

            PreparedStatement pst = getDbConnection().getPreparedStatement(sql);
            //TODO: should check if rotation by this index exists
            pst.setInt(1, pindex);
            ResultSet res = pst.executeQuery();
            rot = new Rotation(res);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            sendIntegerParseException(ATTRIB_PLAYLIST_INDEX, t);
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        }

        // check if we can continue.
        if(rot == null) {
            String msg = "Something extremely unexpected happened in UpdatePlaylistHandler.. ";
            System.out.println(msg);
            sendResponse(msg.getBytes(), t, RESPONSE_INTERNAL_ERROR);
            return;
        }

        // lets get to real shit - start gathering songs
        try {

            Integer repeat = Integer.parseInt(map.get(ATTRIB_REPEAT));
            repeat = (repeat == null || repeat < 1 ) ? 1 : repeat;

            for(int i = 0; i < repeat; i++) {

                String sql = "select id, artist, title from `songs` where id_subcat=? and enabled=?";

                // store already fetched song categories here
                Map<String, List<Song>> catSongs = new HashMap<>();

                // loop through rotation tracks and pickup a song
                for(TrackDescription track : rot.getTracks()) {

                    String catKey = "" + track.getCatID() + track.getSubID();
                    List<Song> songs = catSongs.get(catKey);

                    // if songs is not already fetched lets go and fetch them
                    if(songs == null) {
                        PreparedStatement pst = getDbConnection().getPreparedStatement(sql);
//                        pst.setInt(1, track.getCatID());
                        pst.setInt(1, track.getSubID());
                        pst.setInt(2, 1); // is enabled
                        ResultSet res = pst.executeQuery();
                        songs = new ArrayList<>();
                        while(res.next()) {
                            songs.add(new Song(
                                    res.getInt("ID"),
                                    res.getString("artist"),
                                    res.getString("title")
                            ));
                        }
                    }


                    if(songs.size() <= 0) continue;

                    // choose one song from song list
                    Song song = songs.get((int) (Math.random() * songs.size()));

                    // now insert it into requests table...
                    new RequestSongHandler().insertSongIntoRequests(song.getId(), t);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sendIntegerParseException(ATTRIB_REPEAT, t);
        } catch (SQLException e) {
            e.printStackTrace();
            sendResponse(e.toString().getBytes(), t, RESPONSE_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Container Class for Database Track Rotation Response
     */
    private class Rotation {

        private List<TrackDescription> tracks;


        public Rotation(ResultSet rs) throws SQLException {

            this.tracks = new ArrayList<>();

            while(rs.next()) {
                this.tracks.add(new TrackDescription(
                        rs.getInt("catID"),
                        rs.getInt("subID"),
                        rs.getInt("ord")
                ));
            }

            this.tracks.sort((a, b) -> a.getOrder() < b.getOrder() ? -1 :
                    a.getOrder() > b.getOrder() ? 1 : 0);
        }

        List<TrackDescription> getTracks() {
            return this.tracks;
        }

    }

    private class TrackDescription {
        private int catID;
        private int subID;
        private int order;

        public TrackDescription(int catID, int subID, int order) {
            this.setCatID(catID);
            this.setSubID(subID);
            this.setOrder(order);
        }

        public int getCatID() {
            return catID;
        }

        public void setCatID(int catID) {
            this.catID = catID;
        }

        public int getSubID() {
            return subID;
        }

        public void setSubID(int subID) {
            this.subID = subID;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }

    private class Song {

        public Song(int id, String artist, String title) {
            this.setId(id);
            this.setArtist(artist);
            this.setTitle(title);
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        private Integer id;
        private String artist;
        private String title;
    }
}
