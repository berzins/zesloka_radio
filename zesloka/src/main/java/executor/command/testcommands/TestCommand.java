package executor.command.testcommands;

import eventservice.ClientConnectionManager;
import eventservice.IClientConnection;
import executor.RemoteCommandExecutor;
import executor.command.Command;
import executor.command.CommandProcessorManager;
import executor.command.GlobalCommand;
import executor.command.parameters.Parameter;
import executor.command.utilcommands.recorder.RecorderCommand;
import utilities.JSONUtils;
import utilities.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class TestCommand extends Command {


    List<Command> cmds;

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public TestCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[] {
                new Parameter(this.getKey(), PARAM_TEXT, Parameter.TYPE_STRING, Parameter.VALUE_UNDEFINED)});
    }

    @Override
    protected void init() {
        super.init();
        cmds = new ArrayList<>();
        Command c = Command.getCommand("cmd_recorder_set");
        c.getParams().setValue("cmd_recorder_set", "cmd_name", "user defined command");
        c.getParams().setValue("cmd_recorder_set", "cmd_key", "cmd_user_2");
        cmds.add(c);
        cmds.add(Command.getCommand("cmd_recorder_start"));
        cmds.add(new Command("none", "cmd_none"){});
        cmds.add(new Command("none", "cmd_gone"){});
        cmds.add(new Command("none", "cmd_phone"){});
        cmds.add(new Command("none", "cmd_what"){});
        cmds.add(Command.getCommand("cmd_recorder_stop"));
        cmds.add(Command.getCommand("cmd_recorder_store"));

        for(Command ccc : cmds) {
            ccc.getParams().setValue(ccc.getKey(), "session_id", "123");
        }
    }

    @Override
    public void execute() {
        super.execute();
        IClientConnection cc = ClientConnectionManager
                .getInstance()
                .getClientConnection(Integer.valueOf(params.getStringValue(
                        GLOBAL_PARAMS, GlobalCommand.PARAM_CLIENT_ID
                )));
        if(cc != null) {
            cc.write(TimeUtils.getCurrentTimeString() +
                    ": executing '" + this.getName() + "' command with params : " +
                    this.params.getStringValue(this, PARAM_TEXT));
            //cc.write(JSONUtils.createJSON(params.getCommandData()));
        }

//        for(Command c : cmds) {
//            CommandProcessorManager.getInstance().processCommand(c);
//            try {
//                Thread.sleep(1000L);
//            } catch (Exception e){
//                e.printStackTrace();
//            }
//        }
    }
}
