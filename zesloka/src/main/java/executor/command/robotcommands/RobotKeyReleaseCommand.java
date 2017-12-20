package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotKeyReleaseCommand extends RobotCommand {


    public RobotKeyReleaseCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[]{KEY_EVENT});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .keyRelease(params.getIntegerValue(this, KEY_EVENT));
    }
}
