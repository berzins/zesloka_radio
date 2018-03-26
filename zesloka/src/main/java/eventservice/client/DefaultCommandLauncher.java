package eventservice.client;

import eventservice.IClientConnectionListener;

import java.io.*;

public class DefaultCommandLauncher extends ConsoleClient {


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
                "{\"cmd_key\":\"cmd_start_radio_db_api\",\"params\":[]}"
        };

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
