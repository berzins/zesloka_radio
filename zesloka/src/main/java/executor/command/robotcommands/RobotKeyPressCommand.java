package executor.command.robotcommands;

import executor.RemoteCommandExecutor;
import executor.command.parameters.Parameter;

public class RobotKeyPressCommand extends RobotCommand {

    public RobotKeyPressCommand(String name, String key) {
        super(name, key);
        initParamKeys(new Parameter[]{
                new Parameter(this.getKey(), KEY_EVENT, Parameter.TYPE_INTEGER, Parameter.VALUE_UNDEFINED)});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .keyPress(params.getIntegerValue(this, KEY_EVENT));
    }
}
