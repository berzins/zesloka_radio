package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

public class RadiodjStop extends RadiodjOptCommand {

    public RadiodjStop () {
        this.setCommand(OptCommands.StopPlayer);
        this.setArgRequired(false);
    }
}
