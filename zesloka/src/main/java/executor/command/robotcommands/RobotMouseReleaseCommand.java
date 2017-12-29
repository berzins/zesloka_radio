package executor.command.robotcommands;

import executor.RemoteCommandExecutor;
import executor.command.parameters.Parameter;

public class RobotMouseReleaseCommand extends RobotCommand {


    public RobotMouseReleaseCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[]{
                new Parameter(this.getKey(), KEY_EVENT, Parameter.TYPE_INTEGER, Parameter.VALUE_UNDEFINED)});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .mouseRelease(params.getIntegerValue(this, KEY_EVENT));
    }
}
