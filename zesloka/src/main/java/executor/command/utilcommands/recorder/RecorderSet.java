package executor.command.utilcommands.recorder;

import executor.command.CommandRecorderManager;

public class RecorderSet extends RecorderCommand {

    /**
     * @param name Command string representation
     * @param key  Unique command identifier
     */
    public RecorderSet(String name, String key) {
        super(name, key);
    }

    @Override
    public void execute() {
        super.execute();
        CommandRecorderManager.getInstance().getRecorder(getSessionId(params), false)
                .set(parseName(params), parseKey(params));
    }

    private String parseKey(String params) {
        //TODO: IMPLEMENT THIS!!!!!!!!!!
        return params;
    }

    private String parseName(String params) {
        //TODO: IMPLEMENT THIS!!!!!!!!!!
        return params;
    }
}
