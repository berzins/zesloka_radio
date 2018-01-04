package executor.command;

import executor.command.parameters.CommandParams;
import utilities.Util;

import java.time.Duration;
import java.time.Instant;

public class CommandRecorder  implements Command.CommandProcessor{


    Instant start;
    Command record;
    boolean isRecording = false;


    public String getRecordKey() {
        return  this.record.getKey();
    }

    @Override
    public void processCommand(Command cmd) {
        //TODO: Replace this if statement with better solution
        // where recorder and command don't know about recordable state.
        // The way to go could be use command processor manager to redirect
        // recordable commands to recorder
        if(!cmd.isRecordable()) return;
        // end of to do.
        if(isRecording) {
            Command c = Util.serializedCopy(cmd); // don't mess with other cmd processors
            c.setTimeout(getTimeout());
            //cmd.setParams(cp);
            c.setFinal(true);
            record.add(c);
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

    /**
     * Store command in user defined commands..
     * @return on success value is stored command, otherwise null
     */
    public Command save() {
        synchronized (this) {
            return Command.saveCommand(record) ? record : null;
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
