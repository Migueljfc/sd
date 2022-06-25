package interfaces;


import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Operational interface of a remote object of type Bar.
 *
 *     It provides the functionality to access Bar.
 */

public interface BarInterface extends Remote {


    /**
     * Return id of the student being answered
     *
     * @return Id of the student being answered
     */
    public int getCurrentStudent() throws RemoteException;


    /**
     * Part of the chef lifecycle is called to alert the waiter that a portion has ready
     */
    public int alert_the_waiter() throws RemoteException;


    /**
     * Is the part of the waiter life cycle where he waits for requests or served the pending and returns the id of the request
     *
     * @return request id
     */
    public int look_around() throws RemoteException;


    /**
     * Part of the waiter lifecycle to update his state to signal that is preparing the bill
     */
    public int prepare_the_bill() throws RemoteException;


    /**
     * Part of the waiter lifecycle to say goodbye to the students when we signal that wants to go home
     *
     * @return true if all students left the restaurant
     */
    public boolean say_goodbye() throws RemoteException;

    /**
     * Is the part of student life cycle when we decide to enter the restaurant adding a new request to wake up the waiter
     */
    public int enter(int id) throws RemoteException;


    /**
     * Part of the 1º student lifecycle to alert the waiter that the order has ready to he get
     */
    public void call_the_waiter(int id) throws RemoteException;


    /**
     * Part of the student lifecycle to signal the waiter that he ends the current course or that the last student wants to pay the bill
     */
    public void signal_the_waiter(int id) throws RemoteException;


    /**
     * Is the part of student life cycle when we decide to leave the restaurant adding a new request to wake up the waiter
     */
    public int exit(int id) throws RemoteException;


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
