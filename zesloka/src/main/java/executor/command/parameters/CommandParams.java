package executor.command.parameters;


import com.google.gson.JsonSyntaxException;
import executor.command.Command;
import executor.command.RootCommandResolver;
import utilities.JSONUtils;

import java.io.Serializable;

public class CommandParams implements Serializable, RootCommandResolver {

    private static final long serialVersionUID = 1L;

    public static final String CMD_PARAM_DIVIDER = "__";

    private CommandData commandData;

    public CommandParams() {
        commandData = CommandData.getEmpty();
    }

    public CommandParams(String cmd) throws JsonSyntaxException {
        set(cmd);
    }

    public void set(String cmd) throws JsonSyntaxException {
        CommandData cd = JSONUtils.parseCommandData(cmd);
        commandData = cd != null ? cd : CommandData.getEmpty();
    }

    public void addValue(String cmd_key, String key, String value) {

        if(commandData == null) {
            commandData = CommandData.getEmpty();
        }
        commandData.addParam(new Parameter(
                cmd_key, key, Parameter.TYPE_DEFAULT, value
        ));
    }

    public void setValue(String cmd_key, String key, String value) {
        Parameter p = commandData.getParam(cmd_key, key);
        if(p != null) {
            p.setValue(String.valueOf(value));
        } else {
            commandData.addParam(new Parameter(
                    cmd_key, key, Parameter.TYPE_DEFAULT, String.valueOf(value)));
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
        Parameter p = commandData.getParam(cmd.getKey(), key);
        if(p == null) throw new IllegalArgumentException(
                "Arguments does not contain parameter '" + cmd.getKey() + "." + key +"'");
        return p.getValue();
    }

    public CommandData getCommandData() {
        return this.commandData;
    }

    public boolean contains(String key) {
        for(Parameter p : this.commandData.getParams()) {
            if(p.getCmd_key().contains(key) || p.getParam_key().contains(key)) {
                return  true;
            }
        }
        return false;
    }

    @Override
    public Command getRootCommand() {
        return Command.getCommand(this.commandData.getCmd_key());
    }
}
