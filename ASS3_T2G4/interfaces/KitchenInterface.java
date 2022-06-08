package interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Operational interface of a remote object of type Kitchen.
 *
 *     It provides the functionality to access Kitchen.
 */

public interface KitchenInterface extends Remote {

    /**
     * Part of the chef lifecycle to signal that is waiting the order
     */
    public void watch_news() throws RemoteException;

    /**
     * Part of the chef lifecycle to start the preparation and signal the waiter of that
     */
    public void start_preparation() throws RemoteException;


    /**
     * Part of the chef lifecycle to signal that the preparation was continued
     */
    public void proceed_preparation() throws RemoteException;


    /**
     * Part of the chef lifecycle to check if he needs to prepare another portion or not
     *
     * @return true if all portions have been delivered
     */
    public boolean have_all_portions_been_delivered() throws RemoteException;


    /**
     * Part of the chef lifecycle to check if all courses have been delivered
     *
     * @return true if all courses have been completed
     */
    public boolean has_the_order_been_completed() throws RemoteException;


    /**
     * Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
     */
    public void continue_preparation() throws RemoteException;


    /**
     * Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
     */
    public void have_next_portion_ready() throws RemoteException;

    /**
     * Part of the chef lifecycle when the order has completed
     */
    public void clean_up() throws RemoteException;


    /**
     * Part of the waiter lifecycle to signal the waiter that a new order was started
     */
    public void hand_note_to_the_chef() throws RemoteException;


    /**
     * Part of the waiter lifecycle to signal that he is returning to bar
     */
    public void return_to_bar() throws RemoteException;


    /**
     * Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
     */
    public void collectPortion() throws RemoteException;

    /**
     * Operation server shutdown.
     * <p>
     * New operation.
     *
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                         service fails
     */

    public void shutdown() throws RemoteException;
}
