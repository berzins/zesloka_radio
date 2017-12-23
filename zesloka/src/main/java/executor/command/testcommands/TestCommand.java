package executor.command.testcommands;

import executor.command.Command;
import utilities.TimeUtils;

public class TestCommand extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public TestCommand(String name, String key) {
        super(name, key);
        initParamKeys(new String[] {PARAM_TEXT});
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println(TimeUtils.getCurrentTimeString() +
                ": executing '" + this.getName() + "' command with params : " + params);
    }
}
