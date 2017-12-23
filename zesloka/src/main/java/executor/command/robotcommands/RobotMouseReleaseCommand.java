package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotMouseReleaseCommand extends RobotCommand {


    public RobotMouseReleaseCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[]{KEY_EVENT});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .mouseRelease(params.getIntegerValue(this, KEY_EVENT));
    }
}
