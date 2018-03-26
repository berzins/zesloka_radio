package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper;

public class RadiodjRestResponse {

    private int code;
    private String response;

    public RadiodjRestResponse(int code, String response) {
        this.code = code;
        this.response = response;
    }

    public RadiodjRestResponse() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
