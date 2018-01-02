package eventservice.client;

import eventservice.EventServiceFacade;
import eventservice.IClientConnectionListener;

import java.io.*;

public class TestCaseClient extends ConsoleClient {

    String [] cmds = new String[] {
            "{\"cmd_key\":\"cmd_recorder_set\",\"params\":[{\"cmd_key\":\"cmd_recorder_set\",\"param_key\":\"session_id\",\"type\":\"string\",\"value\":\"123\"},{\"cmd_key\":\"cmd_recorder_set\",\"param_key\":\"cmd_name\",\"type\":\"string\",\"value\":\"costum user command\"},{\"cmd_key\":\"cmd_recorder_set\",\"param_key\":\"cmd_key\",\"type\":\"string\",\"value\":\"cmd_user_1\"}]}\r\n",
            "{\"cmd_key\":\"cmd_recorder_start\",\"params\":[{\"cmd_key\":\"cmd_recorder_start\",\"param_key\":\"session_id\",\"type\":\"string\",\"value\":\"123\"}]}\r\n",
            "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is a wonderful text\"}]}\r\n",
            "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is some other text\"}]}\r\n",
            "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is nothing special\"}]}\r\n",
            "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"oh my dog!\"}]}\r\n",
            "{\"cmd_key\":\"cmd_recorder_stop\",\"params\":[{\"cmd_key\":\"cmd_recorder_stop\",\"param_key\":\"session_id\",\"type\":\"string\",\"value\":\"123\"}]}\r\n",
            "{\"cmd_key\":\"cmd_recorder_store\",\"params\":[{\"cmd_key\":\"cmd_recorder_store\",\"param_key\":\"session_id\",\"type\":\"string\",\"value\":\"123\"}]}\r\n"

    };

    @Override
    public void start() {
        for(IClientConnectionListener ccl : clientConnectionListeners) {

            for(String s : cmds) {
                ByteArrayInputStream bais = new ByteArrayInputStream(s.getBytes());
                ccl.onConnect(bais, System.out);
                try{
                    Thread.sleep(1000L);
                    //ccl.onDisconnect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
