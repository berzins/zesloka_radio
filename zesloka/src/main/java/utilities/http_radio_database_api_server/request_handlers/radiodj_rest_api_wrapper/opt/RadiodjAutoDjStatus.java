package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

public class RadiodjAutoDjStatus extends RadiodjOptCommand {

    public RadiodjAutoDjStatus() {
        this.setCommand(OptCommands.StatusAutoDJ);
        this.setArgRequired(false);
    }
}
