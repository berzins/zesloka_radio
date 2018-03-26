package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

import utilities.Util;

public class RadiodjSongLoader extends RadiodjOptCommand {

    public static final InsertOptions options = new InsertOptions();

    /**
     * Inserts song in playlist at top or bottom based on passed option value
     * @param opt  possible values are defined in static 'options' field.
     */
    public RadiodjSongLoader(String opt, String arg) throws IllegalArgumentException {
        this.setArgument(arg);
        if(opt.equals(options.TOP)) {
            this.setCommand(OptCommands.LoadTrackToTop); return;
        }
        else if(opt.equals(options.BOTTOM)) {
            this.setCommand(OptCommands.LoadTrackToBottom); return;
        }
        else {
            throw new IllegalArgumentException(Util.getCurrentTraceInfo() +
                   " value: '" + opt + "' for 'String opt' is not valid value."
            );
        }
    }

    public static class InsertOptions {
        public final String TOP = "top";
        public final String BOTTOM = "bottom";
    }
}
