package executor.command.parameters;


import executor.command.Command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandParams implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String CMD_PARAM_DIVIDER = "__";

    private Map<String, String> params;

    public CommandParams(String cmd) {
        set(cmd);
    }

    public void set(String cmd) {
        Map<String, String> p = Command.parseParams(cmd);
        params = p != null ? p : new HashMap<>();
    }

    public Long getLongValue(Command cmd, String key) throws IllegalArgumentException {
        return Long.valueOf(getRawVal(cmd, key));
    }

    public Float getFloatValue(Command cmd, String key) throws IllegalArgumentException {
        return Float.valueOf(getRawVal(cmd, key));
    }

    public String getStringValue(Command cmd, String key) throws IllegalArgumentException {
        return getRawVal(cmd, key);
    }

    private String getRawVal(Command cmd, String key) throws IllegalArgumentException{
        String paramKey = cmd.getKey() + CMD_PARAM_DIVIDER + key;
        String val = params.get(paramKey);
        if(val == null) throw new IllegalArgumentException("Arguments does not contain parameter '" + paramKey +"'");
        return val;
    }

    public static String createParamKey(Command cmd, String key) {
        return cmd.getKey() + CMD_PARAM_DIVIDER + key;
    }


}
