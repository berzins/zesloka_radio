package executor.command.robotcommands;

import executor.RemoteCommandExecutor;
import executor.command.parameters.Parameter;

public class RobotKeyReleaseCommand extends RobotCommand {


    public RobotKeyReleaseCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[]{
                new Parameter(this.getKey(), KEY_EVENT, Parameter.TYPE_INTEGER, Parameter.VALUE_UNDEFINED)});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .keyRelease(params.getIntegerValue(this, KEY_EVENT));
    }
}
