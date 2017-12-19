package executor.command;

public class EmptyCommand extends Command {
    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public EmptyCommand(String name, String key) {
        super(name, key);
    }
}
