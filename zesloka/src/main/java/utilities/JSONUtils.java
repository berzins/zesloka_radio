package utilities;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import executor.command.parameters.CommandData;
import utilities.youtube.SongList;

public class JSONUtils {

    private static Integer lastCmdHash = 0;
    private static CommandData lastParsedCommand;
    public static CommandData parseCommandData(String cmd) throws JsonSyntaxException {
        //check if is there need to parse Command data.
        if(cmd.hashCode() == lastCmdHash) {
            return Util.serializedCopy(lastParsedCommand);
        }
        Gson gson = new Gson();
        lastCmdHash = cmd.hashCode();
        try {
            lastParsedCommand = gson.fromJson(cmd, CommandData.class);
        } catch (JsonSyntaxException e) {
            throw  e;
        }
        return Util.serializedCopy(lastParsedCommand);
    }

    public static GlobalSettings.Settings parseSettings(String json) {
        Gson gson = new Gson();
        GlobalSettings.Settings ret =gson.fromJson(json, GlobalSettings.Settings.class);
        return ret;
    }

    public static <T> String createJSON(T data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static SongList parsePlaylistResponse(String json) {
        SongList songs = null;
        Gson gson = new Gson();

        try {
            songs = gson.fromJson(json, SongList.class);
        } catch (JsonSyntaxException e) {
            throw e;
        }
        return songs;
    }


}
