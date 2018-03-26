package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

public class RadiodjClearPlaylist extends RadiodjOptCommand {

    public RadiodjClearPlaylist() {
        this.setCommand(OptCommands.ClearPlaylist);
        this.setArgRequired(false);
    }
}
