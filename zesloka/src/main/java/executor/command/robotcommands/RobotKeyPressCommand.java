package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotKeyPressCommand extends RobotCommand {

    public RobotKeyPressCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[]{KEY_EVENT});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .keyPress(params.getIntegerValue(this, KEY_EVENT));
    }
}
