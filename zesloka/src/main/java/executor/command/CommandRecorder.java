package executor.command;

import java.time.Duration;
import java.time.Instant;

public class CommandRecorder  implements Command.CommandProcessor{


    Instant start;
    Command record;
    boolean isRecording = false;


    @Override
    public void processCommand(String cmd) {
        if(cmd.contains("recorder")) return;
        if(isRecording) {
            Command c = Command.getCommand(Command.parseCommand(cmd));
            c.setTimeout(getTimeout());
            record.add(Command.getCommand(Command.parseCommand(cmd)).setParams(Command.parseParams(cmd)));
        }
    }

    public void set(String name, String key) {
        this.record = new EmptyCommand(name, key);
    }

    public void reset() {
        this.set(record.getName(), record.getKey());
    }

    public void start() {
        CommandProcessorManager.getInstance().add(this); // add self to listen for incoming commands
        start = Instant.now();
        isRecording = true;
    }

    public void stop() {
        CommandProcessorManager.getInstance().remove(this);
        isRecording = false;
    }

    public Command save() {
        Command.saveCommand(record);
        return record;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public Long getTimeout() {
        Instant end = Instant.now();
        Duration to = Duration.between(start, end);
        start = end;
        return to.toMillis();
    }

}
