package eventservice;

import java.io.IOException;

public interface IClientConnection {

    void close() throws IOException;

    void write(String data);

    Integer getId();
}
