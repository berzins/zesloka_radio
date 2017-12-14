package executor.command;

import java.util.HashMap;
import java.util.Map;

public class CommandRecorderManager {

    final private Map<String, CommandRecorder> recorders = new HashMap<>();

    private static CommandRecorderManager instance;

    private CommandRecorderManager() {}

    public static CommandRecorderManager getInstance() {
        if(instance == null) {
            instance = new CommandRecorderManager();
        }
        return instance;
    }

    /**
     * This is CommandRecorder access point
     * @param sessionId current session id ( unique string what identifies client session )
     * @param isExisting search for existing Recorder for session.
     * @return if existing is true and session not found, return null.
     *          else return existing Recorder if found, else return new recorder.
     */
    public CommandRecorder getRecorder(String sessionId, boolean isExisting){
        CommandRecorder rec = this.recorders.get(sessionId);
        if(rec == null && !isExisting) {
            rec = new CommandRecorder();
            this.recorders.put(sessionId, rec);
        }
        return rec;
    }

    public void closeRecorderSession(String sessionId) {
        this.recorders.remove(sessionId);
    }
}
