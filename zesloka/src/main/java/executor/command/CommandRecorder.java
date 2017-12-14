package executor.command;

public class CommandRecorder  implements Command.CommandProcessor{


    Long lastRecordTime;
    RecordableCommand record;
    boolean isRecording = false;


    @Override
    public void processCommand(String cmd) {
        Long timeout = (System.currentTimeMillis() % 1000) -  lastRecordTime;
        Command c = Command.getCommand(Command.parseCommand(cmd));
        c.setTimeout(timeout);
        record.record(Command.getCommand(Command.parseCommand(cmd)), Command.parseParams(cmd));
    }

    public void set(String name, String key) {
        this.record = new RecordableCommand(name, key);
    }

    public void reset() {
        this.set(record.getName(), record.getKey());
    }

    public void start() {
        CommandProcessorManager.getInstance().add(this);
        lastRecordTime = System.currentTimeMillis() % 1000;
        isRecording = true;
    }

    public void stop() {
        CommandProcessorManager.getInstance().remove(this);
        isRecording = false;
    }

    public RecordableCommand save() {
        Command.saveCommand(record);
        return record;
    }

    public boolean isRecording() {
        return isRecording;
    }

}
