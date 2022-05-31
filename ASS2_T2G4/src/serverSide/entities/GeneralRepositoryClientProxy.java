package serverSide.entities;

import clientSide.entities.States;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.sharedRegions.GeneralRepositoryInterface;

public class GeneralRepositoryClientProxy extends Thread {
    /**
     * Number of instantiayed threads.
     */

    private static int nProxy = 0;

    /**
     * Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface to the General Repository of Information.
     */

    private GeneralRepositoryInterface reposInter;

    /**
     * Student identification
     */
    private int studentId;

    /**
     * Student state
     */

    private States studentState;

    /**
     * Chef State
     */
    private int chefState;

    /**
     * Waiter State
     */
    private int waiterState;

    /**
     * value of either number courses, number portions, seats
     */
    private int count;

    /**
     * Instantiation of a client proxy.
     *
     * @param sconi      communication channel
     * @param reposInter interface to the general repository of information
     */

    public GeneralRepositoryClientProxy(ServerCom sconi, GeneralRepositoryInterface reposInter) {
        super("GeneralReposProxy_" + GeneralRepositoryClientProxy.getProxyId());
        this.sconi = sconi;
        this.reposInter = reposInter;
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */

    private static int getProxyId() {
        Class<?> cl = null;                                            // representation of the GeneralReposClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.GeneralRepositoryClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type GeneralRepositoryClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    /**
     *
     * @param studentId student identification
     */
    public void setStudentId(int studentId) {

        this.studentId = studentId;
    }
    /**
     *
     * @param studentState Student State
     */
    public void setStudentState(States studentState) {
        
        this.studentState = studentState;
    }

    /**
     *
     * @return student identification
     */
    public int getStudentId() {
        return  studentId;
    }

    /**
     *
     * @return Student state
     */
    public States getStudentState() {
        return studentState;
    }

    /**
     * Get chef state
     * @return chef state
     */
    public int getChefState() {
        return chefState;
    }

    /**
     * Set chef state
     * @param chefState state of the chef
     */
    public void setChefState(int chefState) {
        this.chefState = chefState;
    }

    /**
     * Get waiter state
     * @return waiter state
     */
    public int getWaiterState() {
        return waiterState;
    }

    /**
     * Set waiter state
     * @param waiterState state of the waiter
     */
    public void setWaiterState(int waiterState) {
        this.waiterState = waiterState;
    }

    /**
     * Get value of number courses, portions or seats
     * 	@return number of courses/portions/seats
     */
    public int getCount() {
        return count;
    }

    /**
     * Set value of number courses, portions or seats
     * 	@param count number of courses/portions/seats
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Life cycle of the service provider agent.
     */

    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject();                     // get service request
        try {
            outMessage = reposInter.processAndReply(inMessage);         // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // send service reply
        sconi.close();                                                // close the communication channel
    }



}
