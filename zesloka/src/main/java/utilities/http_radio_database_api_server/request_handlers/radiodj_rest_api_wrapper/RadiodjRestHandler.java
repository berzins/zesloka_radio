package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper;

public interface RadiodjRestHandler {

    void onSuccess(ExecuteResult er);

    void onFailure(ExecuteResult er);
}
