package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Operational interface of a remote object of type General Repository.
 *
 *     It provides the functionality to access General Repository.
 */

public interface GeneralRepositoryInterface extends Remote {


    public void reportInitialStatus() throws RemoteException;

    /**
     * Write a line at the end of the logging file.
     */

    public void reportStatus() throws RemoteException;


    /**
     * Write in the logging file the new chef state
     * @param value chef state to set
     */
    public void setChefState(int value) throws RemoteException;

    /**
     * Write in the logging file the new waiter state
     * @param value waiter state to set
     */
    public void setWaiterState(int value) throws RemoteException;
    /**
     * Updated student state and report status
     * @param id student id
     * @param value student state to set
     */
    public void updateStudentState(int id, int value) throws RemoteException;

    /**
     * Update student state
     * @param id student id
     * @param value student state to set
     * @param hold specifies if prints line of report status
     */
    public void updateStudentState(int id, int value, boolean hold) throws RemoteException;

    /**
     * Set variable courses
     * @param n courses value to set
     */
    public void setnCourses(int n) throws RemoteException;

    /**
     * set variable portion
     *
     * @param n portions value to set
     */
    public void setnPortions(int n) throws RemoteException;

    /**
     * Write to the logging file the updated seats values at the table
     *
     * @param seat seat at the table
     * @param id student id to sit
     */
    public void updateSeatsAtTable(int seat, int id) throws RemoteException;


    /**
     * Update student seat to -1 when he leaves the table to report status
     *
     * @param id student id that leaves the table
     */
    public void updateSeatsAtLeaving(int id) throws RemoteException;


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
