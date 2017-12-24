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

    public void addValue(String key, String value) {
        if(params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }

    public void setValue(Command cmd, String key, Float value) {
        if(params.get(createParamKey(cmd, key)) != null) {
            params.replace(createParamKey(cmd,key), String.valueOf(value));
        } else {
            params.put(createParamKey(cmd, key), String.valueOf(value));
        }
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

    public Integer getIntegerValue(Command cmd, String key) {
        return Integer.valueOf(getRawVal(cmd, key));
    }

    private String getRawVal(Command cmd, String key) throws IllegalArgumentException{
        String paramKey = createParamKey(cmd, key);
        String val = params.get(paramKey);
        if(val == null) throw new IllegalArgumentException("Arguments does not contain parameter '" + paramKey +"'");
        return val;
    }

    public static String createParamKey(Command cmd, String key) {
        return cmd.getKey() + CMD_PARAM_DIVIDER +  key;
    }


}
