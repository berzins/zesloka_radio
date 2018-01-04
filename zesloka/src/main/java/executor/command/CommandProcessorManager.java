package executor.command;

import java.util.ArrayList;
import java.util.List;

public class CommandProcessorManager implements Command.CommandProcessor {

    private static final List<Command.CommandProcessor> processors = new ArrayList<>();
    private static  CommandProcessorManager instance;

    private CommandProcessorManager(){}

    public static CommandProcessorManager getInstance() {
        if(instance == null) {
            instance = new CommandProcessorManager();
        }
        return instance;
    }

    public void add(Command.CommandProcessor processor) {
        synchronized (processors) {
            processors.add(processor);
        }
    }

    public Command.CommandProcessor remove(Command.CommandProcessor processor) {
        synchronized (processors) {
            return processors.remove(processors.indexOf(processor));
        }
    }

    @Override
    public void processCommand(Command cmd) {
        synchronized (processors) {
            for(Command.CommandProcessor cp : processors) {
                cp.processCommand(cmd);
            }
        }
    }
}
