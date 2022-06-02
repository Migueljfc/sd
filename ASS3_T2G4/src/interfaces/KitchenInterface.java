package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KitchenInterface extends Remote {


    /**
     *   Operation server shutdown.
     *
     *   New operation.
     *
     *     @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */

    public void shutdown() throws RemoteException;
}
