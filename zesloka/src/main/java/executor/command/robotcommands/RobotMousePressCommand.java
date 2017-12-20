package executor.command.robotcommands;

import executor.RemoteCommandExecutor;

public class RobotMousePressCommand extends  RobotCommand {


    public RobotMousePressCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[]{KEY_EVENT});
    }

    @Override
    public void execute() {
        super.execute();
        RemoteCommandExecutor.getInstance().getRobot()
                .mousePress(params.getIntegerValue(this, KEY_EVENT));
    }
}
