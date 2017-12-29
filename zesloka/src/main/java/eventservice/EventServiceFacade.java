package eventservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Describes input/output services what can provide streams for reading and writing
 * command data.
 */
public abstract class EventServiceFacade implements IEventService, IEventServiceConnection {

    protected List<IClientConnectionListener> clientConnectionListeners = new ArrayList<>();

    @Override
    public void addClientConnectionListener(IClientConnectionListener ccl) {
        this.clientConnectionListeners.add(ccl);
    }

    @Override
    public IClientConnectionListener getClient(int clientHash) {
        for(IClientConnectionListener ccl : clientConnectionListeners) {
            if(ccl.hashCode() == clientHash) {
                return ccl;
            }
        }
        return null;
    }
}
