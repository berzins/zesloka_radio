package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

public class RadiodjEnableAutoDj extends RadiodjOptCommand {
    /**
     * @param enabled 1 for enable and 0 for disable
     */
    public RadiodjEnableAutoDj(int enabled) {
        this.setCommand(OptCommands.EnableAutoDJ);
        this.setArgument(Integer.toString(enabled));
    }

}
