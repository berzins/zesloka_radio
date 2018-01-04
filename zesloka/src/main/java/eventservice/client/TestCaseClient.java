package eventservice.client;

import eventservice.IClientConnectionListener;

import java.io.*;

public class TestCaseClient extends ConsoleClient {


    Input in;

    @Override
    public void start() {

        in = new Input();

        for(IClientConnectionListener ccl : clientConnectionListeners) {
            ccl.onConnect(in, System.out);
        }

    }

    public void nextCommand() {
        in.setText();
    }

    public class Input extends InputStream {

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

//        String [] cmds = new String[] {
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is a wonderful text\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is some other text\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is nothing special\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"oh my dog!\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is a wonderful text\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is some other text\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is nothing special\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"oh my dog!\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is a wonderful text\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is some other text\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"this is nothing special\"}]}\r\n",
//                "{\"cmd_key\":\"cmd_test\",\"params\":[{\"cmd_key\":\"cmd_test\",\"param_key\":\"text\",\"type\":\"string\",\"value\":\"oh my dog!\"}]}\r\n"
//        };

        int cmdIndex = 0;
        String str = "";
        int index = 0;
        Thread callerThread;
        boolean susspended = false;

        public Input() {
        }

        @Override
        public int read() throws IOException {

            if(susspended) {
                callerThread = Thread.currentThread();
                callerThread.suspend();
            }
            int c = -1;
            if(index < str.length()) {
                c = str.charAt(index++);
            }
            if(c == -1 && index == str.length()) {
                this.susspended = true;
            }
            return c;
        }

        public void setText() {
            str = cmds[cmdIndex];
            index = 0;
            cmdIndex++;
            if(callerThread != null) {
                this.susspended = false;
                callerThread.resume();
            }
        }
    }
}
