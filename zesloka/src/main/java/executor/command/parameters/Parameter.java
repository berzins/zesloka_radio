package executor.command.parameters;

import java.io.Serializable;


public class Parameter implements Serializable {

    private static final long serialVersionUID = 1L;


    public static final String TYPE_STRING = "string";
    public static final String TYPE_INTEGER = "int";
    public static final String TYPE_FLOAT = "float";
    public static final String TYPE_LONG = "long";
    public static final String TYPE_BOOLEAN = "bool";
    public static final String TYPE_DEFAULT = TYPE_STRING;
    public static final String VALUE_UNDEFINED = "UNDEFINED";


    private String cmd_key;
    private String param_key;
    private String type;
    private String value;

    public Parameter() {
        this.init();
    }

    public Parameter(String cmd_key, String param_key, String type, String value) {
        this.init(cmd_key, param_key, type, value);
    }

    public void init() {}

    public void init(String cmd_key, String param_key, String type, String value) {
        this.cmd_key = cmd_key;
        this.param_key = param_key;
        this.type = type;
        this.value = value;
    }

    public String getCmd_key() {
        return cmd_key;
    }

    public void setCmd_key(String cmd_key) {
        this.cmd_key = cmd_key;
    }

    public String getParam_key() {
        return param_key;
    }

    public void setParam_key(String param_key) {
        this.param_key = param_key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}