package eventservice;

/**
 *
 */
public interface IEventServiceConnection {
    void addClientConnectionListener(IClientConnectionListener listener);
    IClientConnectionListener getClient(int clientHash);
}
