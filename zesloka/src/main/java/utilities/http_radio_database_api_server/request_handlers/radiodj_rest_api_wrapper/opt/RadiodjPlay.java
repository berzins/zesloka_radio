package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

public class RadiodjPlay extends RadiodjOptCommand {

    public RadiodjPlay () {
        this.setCommand(OptCommands.PlayPlaylistTrack);
        this.setArgument("0");
        this.setArgRequired(true);
    }
}
