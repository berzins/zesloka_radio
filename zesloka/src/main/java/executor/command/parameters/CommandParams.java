package executor.command.parameters;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParams {

    private static final Map<String, List<String>> commandParams = new HashMap<>();

    public static void registerParamKeys(String cmd_key, String param_key) {
        List<String> params = commandParams.get(cmd_key);
        if(params != null) {
            if(!params.contains(param_key)) {
                params.add(param_key);
            }
        } else {
            params = new ArrayList<>();
            params.add(param_key);
            commandParams.put(cmd_key, params);
        }
    }

    public static List<String> getCommandParamKeys(String cmd_key) {
        return commandParams.get(cmd_key);
    }

}
