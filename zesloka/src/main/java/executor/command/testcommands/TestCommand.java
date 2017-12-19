package executor.command.testcommands;

import executor.command.Command;

public class TestCommand extends Command {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public TestCommand(String name, String key) {
        super(name, key);
    }

    @Override
    public void execute() {
        super.execute();
        System.out.println("executing '" + this.getName() + "' command with params : " + params);
    }
}
