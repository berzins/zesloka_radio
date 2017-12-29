package executor.command.utilcommands.database;

import db.DBConnection;
import db.DataBase;
import executor.command.Command;
import executor.command.parameters.Parameter;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        try {
            while(res.next()) {
                String line =
                        "ID: " + res.getInt("id") + ", " +
                        "Artist: " + res.getString("artist") + ", " +
                        "Title: " + res.getString("title");
                System.out.println(line);

                //TODO: implement system how to replay to client with found results. !!!!!!!!!!!!!!!!!!!
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
