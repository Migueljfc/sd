package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *   Operational interface of a remote object of type Table.
 *
 *     It provides the functionality to access Table.
 */

public interface TableInterface extends Remote {

    /**
     * Get id of the first student to enter
     *
     * @return id of the first student to enter in the restaurant
     */
    public int getfirstStudent() throws RemoteException;

    /**
     * Get id of the last student to enter
     *
     * @return id of the last student to finish eating a meal
     */
    public int getLastToEat() throws RemoteException;

    /**
     * Set id of the first student to arrive
     *
     * @param id id of the first student to arrive
     */
    public void setFirstStudent(int id) throws RemoteException;

    /**
     * Set id of the last student to arrive
     *
     * @param id if of the last student to arrive to the restaurant
     */
    public void setLastStudent(int id) throws RemoteException;


    /**
     * Part of the waiter lifecycle is called when a student enter the restaurant
     *
     * @param id id of the student to be saluted
     */
    public int salute_client(int id) throws RemoteException;


    /**
     * Part of the waiter lifecycle is called when we return to bar
     */
    public int return_to_bar() throws RemoteException;


    /**
     * Part of the waiter lifecycle is called when the first student intent to describe the order
     */
    public int get_the_pad() throws RemoteException;


    /**
     * Part of the waiter lifecycle is called to check if all portions have been delivered
     *
     * @return true if all portions been delivered
     */
    public boolean have_all_portions_delivered() throws RemoteException;


    /**
     * Part of the waiter lifecycle is used to signal that a portion have been delivered
     */
    public void deliver_portion() throws RemoteException;


    /**
     * Part of the waiter lifecycle is used present the bill and signal the last student to pay
     */
    public int present_the_bill() throws RemoteException;


    /**
     * Called when a student enter the bar to register the position in the table and to wait by the waiter to present the menu
     */
    public void seat(int id) throws RemoteException;


    /**
     * Part of the student lifecycle used to update the student state and update the read array to notify that the student already read the menu
     */
    public int read_menu(int id) throws RemoteException;


    /**
     * Part of the 1º student lifecycle to update his state and signal that is organizing the order
     */
    public int prepare_the_order() throws RemoteException;


    /**
     * Part of the 1º student lifecycle to check if the others students chosen their orders
     *
     * @return true if the others students already chosen their orders
     */
    public boolean has_everybody_chosen() throws RemoteException;


    /**
     * Part of the 1º student lifecycle to update the number os orders and notify the other students of that
     */
    public void add_up_ones_choice() throws RemoteException;


    /**
     * Part of the 1º student lifecycle to wake up the waiter and describe the order
     */
    public void describe_the_order() throws RemoteException;

    /**
     * Part of the 1º student lifecycle to join the talk with the other students
     */
    public int join_the_talk() throws RemoteException;


    /**
     * Part of the students' lifecycle to inform the 1º student about his course option
     */
    public int inform_companion(int id) throws RemoteException;


    /**
     * Part of the students' lifecycle to start eating and update his state, for simulate that is used the function sleep
     */
    public int start_eating(int id) throws RemoteException;


    /**
     * Part of the student lifecycle to update his state and signal that he end his course and register last student to eat
     */
    public int end_eating(int id) throws RemoteException;


    /**
     * Part of the student lifecycle to wait for the last student to finish his course
     */
    public boolean has_everybody_finished() throws RemoteException;


    /**
     * Part of the student lifecycle to wait for the waiter to give him the bill
     */
    public void honor_the_bill() throws RemoteException;


    /**
     * Part of the students' lifecycle to check if all courses have been delivered or not
     *
     * @return true if all courses have been delivered
     */
    public boolean have_all_courses_delivery() throws RemoteException;


    /**
     * Part of the student lifecycle to check if he is the last to arrive and change his state to pay the bill
     *
     * @return True if student was the last to arrive
     */
    public boolean should_have_arrived_earlier(int id) throws RemoteException;

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
