package executor.command.utilcommands.database;

import db.DBConnection;
import db.DataBase;
import eventservice.ClientConnectionManager;
import eventservice.IClientConnection;
import executor.command.Command;
import executor.command.GlobalCommand;
import executor.command.parameters.Parameter;
import executor.command.parameters.complexvalue.Song;
import utilities.JSONUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetSongLike extends Command {

    public static final String SONG_NAME = "song_name";

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public GetSongLike(String name, String key) {
        super(name, key);
    }

    @Override
    protected void init() {
        super.init();
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), SONG_NAME, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)
        });
    }

    @Override
    public void execute() {
        super.execute();
        DBConnection conn = DataBase.Companion.getConnection();
        ResultSet res = conn.findSongsWhatIsLike(params.getStringValue(this, SONG_NAME));

        List<Song> songs = new ArrayList<>();
        try {
            while(res.next()) {
                Song song = new Song();
                song.setId(res.getInt("id"));
                song.setArtist(res.getString("artist"));
                song.setTile(res.getString("title"));
                songs.add(song);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        IClientConnection cc = ClientConnectionManager.getInstance().getClientConnection(
                getParams().getIntegerValue(GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID));
        cc.write(JSONUtils.createJSON(songs));
    }
}
