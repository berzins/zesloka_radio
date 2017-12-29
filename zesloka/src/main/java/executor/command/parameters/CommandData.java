package executor.command.parameters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandData implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cmd_key;
    private List<Parameter> params;

    public static CommandData getEmpty() {
        CommandData cd = new CommandData();
        cd.cmd_key = "cmd_data_empty";
        cd.setParams(new ArrayList<>());
        return cd;
    }

    public String getCmd_key() {
        return cmd_key;
    }

    public void setCmd_key(String cmd_key) {
        this.cmd_key = cmd_key;
    }

    public List<Parameter> getParams() {
        return params;
    }

    public void setParams(List<Parameter> params) {
        this.params = params;
    }

    public void addParam(Parameter param) {
        this.params.add(param);
    }

    public Parameter getParam(String cmd_key, String param_key) {
        for(Parameter p : params) {
            if(p.getCmd_key().equals(cmd_key) && p.getParam_key().equals(param_key)) {
                return p;
            }
        }
        return null;
    }
}
