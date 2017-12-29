package utilities;

import com.google.gson.Gson;
import executor.command.Command;
import executor.command.parameters.CommandData;

public class JSONUtils {

    private static Integer lastCmdHash = 0;
    private static CommandData lastParsedCommand;
    public static CommandData parseCommandData(String cmd) {
        //check if is there need to parse Command data.
        if(cmd.hashCode() == lastCmdHash) {
            return Util.serializedCopy(lastParsedCommand);
        }
        Gson gson = new Gson();
        lastCmdHash = cmd.hashCode();
        lastParsedCommand = gson.fromJson(cmd, CommandData.class);
        return Util.serializedCopy(lastParsedCommand);
    }

    public static String createCommandDataJSON(CommandData data) {
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}
