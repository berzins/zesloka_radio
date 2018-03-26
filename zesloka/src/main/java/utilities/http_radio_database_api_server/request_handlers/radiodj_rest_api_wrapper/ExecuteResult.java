package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper;

public class ExecuteResult {

    public static final int ERROR_GENERIC = -1;
    public static final int SUCCESS = 1;

    private RadiodjRestResponse response;
    private int result;
    private String message;

    public ExecuteResult(RadiodjRestResponse response, int result) {
        this(response, result, "No message has been set.");
    }

    public ExecuteResult(RadiodjRestResponse response, int result, String message) {
        this.response = response;
        this.result = result;
        this.message = message;
    }

    public RadiodjRestResponse getResponse() {
        return response;
    }

    public int getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
