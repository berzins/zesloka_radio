package eventservice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public interface IClientConnectionListener {

    void onConnect(InputStream in, OutputStream out);

    void onDisconnect() throws IOException;

}
