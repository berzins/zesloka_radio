package executor.command;

import executor.command.parameters.CommandParams;

import java.time.Duration;
import java.time.Instant;

public class CommandRecorder  implements Command.CommandProcessor{


    Instant start;
    Command record;
    boolean isRecording = false;


    @Override
    public void processCommand(Command cmd) {
        CommandParams cp = cmd.getParams();
        if(cp.contains("recorder")) return;
        if(isRecording) {
            cmd.setTimeout(getTimeout());
            //cmd.setParams(cp);
            cmd.setFinal(true);
            record.add(cmd);
        }
    }

    public void set(String name, String key) {
        synchronized (this) {
            this.record = new EmptyCommand(name, key);
        }
    }

    public void reset() {
        synchronized (this) {
            this.set(record.getName(), record.getKey());
        }
    }

    public void start() {
        synchronized (this) {
            CommandProcessorManager.getInstance().add(this); // add self to listen for incoming commands
            start = Instant.now();
            isRecording = true;
        }
    }

    public void stop() {
        synchronized (this) {
            if(isRecording) {
                CommandProcessorManager.getInstance().remove(this);
                isRecording = false;
            }
        }
    }

    public Command save() {
        synchronized (this) {
            Command.saveCommand(record);
            return record;
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    public Long getTimeout() {
        synchronized (this) {
            Instant end = Instant.now();
            Duration to = Duration.between(start, end);
            start = end;
            return to.toMillis();
        }
    }
}
