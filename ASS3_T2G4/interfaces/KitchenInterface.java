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
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     * service fails
     */
    public int watch_news() throws RemoteException;

    /**
     * Part of the chef lifecycle to start the preparation and signal the waiter of that
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                         service fails
     */
    public int start_preparation() throws RemoteException;


    /**
     * Part of the chef lifecycle to signal that the preparation was continued
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int proceed_preparation() throws RemoteException;


    /**
     * Part of the chef lifecycle to check if he needs to prepare another portion or not
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                              service fails
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
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                              service fails
     */
    public int continue_preparation() throws RemoteException;


    /**
     * Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                              service fails
     */
    public int have_next_portion_ready() throws RemoteException;

    /**
     * Part of the chef lifecycle when the order has completed
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                               service fails
     */
    public int clean_up() throws RemoteException;


    /**
     * Part of the waiter lifecycle to signal the waiter that a new order was started
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                             service fails
     */
    public int hand_note_to_the_chef() throws RemoteException;


    /**
     * Part of the waiter lifecycle to signal that he is returning to bar
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                            service fails
     */
    public int return_to_bar() throws RemoteException;


    /**
     * Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
     * @throws RemoteException if either the invocation of the remote method, or the communication with the registry
     *                         service fails
     */
    public int collectPortion() throws RemoteException;

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
