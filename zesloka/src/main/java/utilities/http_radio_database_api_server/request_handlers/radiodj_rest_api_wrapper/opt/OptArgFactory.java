package utilities.http_radio_database_api_server.request_handlers.radiodj_rest_api_wrapper.opt;

import java.util.HashMap;
import java.util.Map;

public class OptArgFactory {

    public static final Map<String, String> getArgs(String command, String arg, boolean argRequired)
    throws IllegalArgumentException {

        if(command == null) {
            throw new IllegalArgumentException("arg 'command' value is not set at " +
                    OptArgFactory.class.getCanonicalName());
        }

        Map<String, String> args = new HashMap<>();
        args.put("command", command);

        if(argRequired) {
            if(arg == null) {
                throw new IllegalArgumentException("arg 'command' value is not set at " +
                        OptArgFactory.class.getCanonicalName());
            }
            args.put("arg", arg);
        }
        return args;
    }
}
