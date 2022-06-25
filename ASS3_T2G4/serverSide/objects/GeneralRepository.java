package serverSide.objects;

import clientSide.entities.States;
import genclass.GenericIO;
import genclass.TextFile;
import interfaces.GeneralRepositoryInterface;
import serverSide.main.GeneralRepositoryMain;
import serverSide.main.SimulPar;

import java.rmi.RemoteException;
import java.util.Objects;

/**
 * {@summary}It is responsible to keep the visible internal state of the problem and to provide means for it
 * to be printed in the logging file.
 *
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class GeneralRepository implements GeneralRepositoryInterface {
    /**
     * Count entities that requested shutdown
     */
    private int entities;

    /**
     * Name of the logging file.
     */
    private final String logName;

    /**
     * Number of portions served.
     */
    private int nPortions;

    /**
     * Number of Courses served.
     */
    private int nCourses;

    /**
     * State of the Chef.
     */
    private int chefState;

    /**
     * State of the Waiter.
     */
    private int waiterState;

    /**
     * State of the Students.
     */
    private int[] studentStates;

    /**
     * seat where each student is.
     */
    private int[] seats;

    /**
     * Id of first student
     */
    private int firstStudent;

    /**
     * Id of last student
     */
    private int lastStudent;

    /**
     * Instantiation of General Repository object
     *
     * @param logName name of output file
     */
    public GeneralRepository(String logName) {
        if ((logName == null) || Objects.equals(logName, "")) {
            this.logName = "logger";
        } else {
            this.logName = logName;
        }
        studentStates = new int[SimulPar.N];
        seats = new int[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++) {
            studentStates[i] = States.GOING_TO_THE_RESTAURANT;
            seats[i] = -1;
        }

        chefState = States.WAIT_FOR_AN_ORDER;

        waiterState = States.APPRAISING_SITUATION;

        reportInitialStatus();

    }

    /**
     * Print header.
     */
    public void reportInitialStatus() {
        TextFile logFile = new TextFile();

        if (!logFile.openForWriting(".", logName)) {
            GenericIO.writelnString("The operation of creating the file " + logName + " failed!");
            System.exit(1);
        }
        logFile.writelnString("\t\t\t\t\t\t  The Restaurant - Description of the internal state");
        logFile.writelnString("\n\tChef\tWaiter\tStu0\tStu1\tStu2\tStu3\tStu4\tStu5\tStu6\tNCourse\tNPortion\t\t\tTable\n");
        logFile.writelnString("\n\tState\tState\tState\tState\tState\tState\tState\tState\tState\t\t\tSeat0\tSeat1\tSeat2\tSeat3\tSeat4\tSeat5\tSeat6\n");
        if (!logFile.close()) {
            GenericIO.writelnString("The operation of closing the file " + logName + " failed!");
            System.exit(1);
        }
        reportStatus();
    }

    /**
     * Write the body of the logging file.
     */
    public void reportStatus() {
        TextFile logFile = new TextFile();

        String lineStatus = "";

        if (!logFile.openForAppending(".", logName)) {
            GenericIO.writelnString("The operation of opening for appending the file " + logName + " failed!");
            System.exit(1);
        }

        switch (chefState) {
            case States.WAIT_FOR_AN_ORDER:
                lineStatus += "\tWAFOR";
                break;
            case States.PREPARING_A_COURSE:
                lineStatus += "\tPRPCS";
                break;
            case States.DISHING_THE_PORTIONS:
                lineStatus += "\tDSHPT";
                break;
            case States.DELIVERING_THE_PORTIONS:
                lineStatus += "\tDLVPT";
                break;
            case States.CLOSING_SERVICE:
                lineStatus += "\tCLSSV";
                break;
        }
        switch (waiterState) {
            case States.APPRAISING_SITUATION:
                lineStatus += "\tAPPST";
                break;
            case States.PRESENTING_THE_MENU:
                lineStatus += "\tPRSMN";
                break;
            case States.TAKING_THE_ORDER:
                lineStatus += "\tTKODR";
                break;
            case States.PLACING_THE_ORDER:
                lineStatus += "\tPCODR";
                break;
            case States.WAITING_FOR_AN_PORTION:
                lineStatus += "\tWTFPT";
                break;
            case States.PROCESSING_THE_BILL:
                lineStatus += "\tPRCBL";
                break;
            case States.RECEIVING_PAYMENT:
                lineStatus += "\tRECPM";
                break;
        }
        for (int i = 0; i < SimulPar.N; i++)
            switch (studentStates[i]) {
                case States.GOING_TO_THE_RESTAURANT:
                    lineStatus += "\tGGTRT";
                    break;
                case States.TAKING_A_SEAT_AT_THE_TABLE:
                    lineStatus += "\tTKSTT";
                    break;
                case States.SELECTING_THE_COURSES:
                    lineStatus += "\tSELCS";
                    break;
                case States.ORGANIZING_THE_ORDER:
                    lineStatus += "\tOGODR";
                    break;
                case States.CHATING_WITH_COMPANIONS:
                    lineStatus += "\tCHTWC";
                    break;
                case States.PAYING_THE_BILL:
                    lineStatus += "\tPYTML";
                    break;
                case States.ENJOYING_THE_MEAL:
                    lineStatus += "\tEJTML";
                    break;
                case States.GOING_HOME:
                    lineStatus += "\tGGHOM";
                    break;

            }
        lineStatus += "\t" + nCourses + "\t" + nPortions;

        for (int i = 0; i < SimulPar.N; i++) {
            lineStatus += "\t";
            if (seats[i] == -1) {
                lineStatus += "-";
            } else {
                lineStatus += seats[i];
            }
        }

        logFile.writelnString(lineStatus);
        if (!logFile.close()) {
            GenericIO.writelnString("The operation of closing the file " + logName + " failed!");
            System.exit(1);
        }
    }

    /**
     * Set Chef state.
     *
     * @param chefState chef state
     */
    public synchronized void setChefState(int chefState) throws RemoteException {
        this.chefState = chefState;
        reportStatus();
    }

    /**
     * Set Waiter state.
     *
     * @param waiterState waiter state
     */
    public synchronized void setWaiterState(int waiterState) throws RemoteException {
        this.waiterState = waiterState;
        reportStatus();
    }

    /**
     * Set Student state.
     *
     * @param id           student id
     * @param studentState student state
     * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void updateStudentState(int id, int studentState) throws RemoteException {
        studentStates[id] = studentState;
        reportStatus();
    }

    public void updateStudentState(int id, int value, boolean hold) throws RemoteException {
        int seat = 0;

        for (int i = 0; i < this.seats.length; i++) {
            if (this.seats[i] == id)
                seat = i;
        }

        this.seats[seat] = -1;
    }


    public void updateSeatsAtLeaving(int id) throws RemoteException {
        int seat = 0;

        for (int i = 0; i < this.seats.length; i++) {
            if (this.seats[i] == id)
                seat = i;
        }

        this.seats[seat] = -1;
    }

    /**
     * Set who is seated at the table.
     *
     * @param id   student id
     * @param seat of the student at the restaurant
     * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void updateSeatsAtTable(int seat, int id) throws RemoteException {
        seats[seat] = id;
    }

    /**
     * @return the student seat position in the table
     */
    public synchronized int getStudentSeat(int id) throws RemoteException  {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i] == id) return i;
        }
        return -1;
    }

    /**
     * Set number of portions.
     *
     * @param portionsDelivery number of portions delivered
     */
    public synchronized void setnPortions(int portionsDelivery) throws RemoteException  {
        nPortions = portionsDelivery;
    }

    /**
     * Set number of Courses.
     *
     * @param i number of courses
     */
    public synchronized void setnCourses(int i) throws RemoteException  {
        nCourses = i;
    }

    /**
     * Operation kitchen server shutdown
     *
     * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void shutdown() throws RemoteException {
        entities += 1;
        if (entities >= 1)
            GeneralRepositoryMain.shutdown();
        notifyAll();
    }
}
