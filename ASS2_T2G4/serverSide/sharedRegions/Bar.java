package serverSide.sharedRegions;

import clientSide.entities.States;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.TableStub;
import commInfra.MemException;
import commInfra.MemFIFO;
import serverSide.entities.BarClientProxy;
import serverSide.main.BarMain;
import serverSide.main.SimulPar;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary Implementation of the Bar shared region
 */

public class Bar {
    /**
     * Used to control number of students present in the restaurant
     */
    private int studentCount;

    /**
     * Used to control number of pending requests to be answered by the waiter
     */
    private int requestCount;

    /**
     * Boolean variable used to store if a course was finished or not
     */
    private boolean courseReady;

    /**
     * Pending Requests
     */
    private MemFIFO<Request> requests;

    /**
     * Reference to the student threads
     */
    private final BarClientProxy[] students;

    /**
     * Reference to the stub of the general repository
     */
    private final GeneralRepositoryStub reposStub;

    /**
     * Student who is being answered
     */
    private int currentStudent;

    /**
     * Array to control if the waiter already said good bye to students
     */
    private boolean[] goodbyeIds;

    /**
     * Reference to the stub of the table
     */
    private final TableStub tabStub;

    /**
     * Number of entity groups requesting the shutdown
     */
    private int nEntities;


    /**
     * Bar instantiation
     *
     * @param reposStub reference to the stub of the general repository
     * @param tabStub   reference to the stub of the table
     */
    public Bar(GeneralRepositoryStub reposStub, TableStub tabStub) {

        students = new BarClientProxy[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            students[i] = null;

        try {
            requests = new MemFIFO<>(new Request[SimulPar.N * SimulPar.M]);
        } catch (MemException e) {
            requests = null;
            System.exit(1);
        }

        this.courseReady = true;
        this.currentStudent = -1;
        this.reposStub = reposStub;
        this.tabStub = tabStub;
        this.nEntities = 0;

        this.goodbyeIds = new boolean[SimulPar.N];
        for (int i = 0; i < SimulPar.N; i++)
            goodbyeIds[i] = false;
    }


    /**
     * Return id of the student being answered
     *
     * @return Id of the student being answered
     */
    public int getCurrentStudent() {
        return currentStudent;
    }


    /**
     * Part of the chef lifecycle is called to alert the waiter that a portion has ready
     */
    public synchronized void alert_the_waiter() {

        while (!courseReady) {
            try {
                wait();
            } catch (InterruptedException e1) {

            }
        }

        Request r = new Request(SimulPar.N + 1, 2);


        try {
            requests.write(r);
        } catch (MemException e) {

        }


        requestCount++;
        courseReady = false;

        ((BarClientProxy) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
        reposStub.setChefState(((BarClientProxy) Thread.currentThread()).getChefState());

        //Notify waiter
        notifyAll();
    }


    /**
     * Is the part of the waiter life cycle where he waits for requests or served the pending and returns the id of the request
     *
     * @return request id
     */
    public synchronized int look_around() {
        Request r;

        while (requestCount == 0) {
            try {
                wait();
            } catch (InterruptedException e) {

                e.printStackTrace();
            }
        }

        try {
            r = requests.read();
            requestCount--;
        } catch (MemException e) {
            return 0;
        }
        currentStudent = r.requestorId;
        return r.requestId;
    }


    /**
     * Part of the waiter lifecycle to update his state to signal that is preparing the bill
     */
    public synchronized void prepare_the_bill() {
        ((BarClientProxy) Thread.currentThread()).setWaiterState(States.PROCESSING_THE_BILL);
        reposStub.setWaiterState(((BarClientProxy) Thread.currentThread()).getWaiterState());
    }


    /**
     * Part of the waiter lifecycle to say goodbye to the students when we signal that wants to go home
     *
     * @return true if all students left the restaurant
     */
    public synchronized boolean say_goodbye() {
        goodbyeIds[currentStudent] = true;
        notifyAll();

        studentCount--;

        reposStub.updateSeatsAtLeaving(currentStudent);
        currentStudent = -1;

        if (studentCount == 0)
            return true;
        return false;

    }


    /**
     * Is the part of student life cycle when we decide to enter the restaurant adding a new request to wake up the waiter
     */
    public void enter() {
        synchronized (this) {
            int studentId = ((BarClientProxy) Thread.currentThread()).getStudentId();

            students[studentId] = ((BarClientProxy) Thread.currentThread());
            students[studentId].setStudentState(States.GOING_TO_THE_RESTAURANT);
            ((BarClientProxy) Thread.currentThread()).setStudentState(States.GOING_TO_THE_RESTAURANT);

            studentCount++;

            if (studentCount == 1)
                tabStub.setFirstStudent(studentId);
            else if (studentCount == SimulPar.N)
                tabStub.setLastStudent(studentId);

            try {
                requests.write(new Request(studentId, 0));
            } catch (MemException e) {

            }

            requestCount++;

            students[studentId].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);
            ((BarClientProxy) Thread.currentThread()).setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);
            reposStub.updateStudentState(studentId, students[studentId].getStudentState(), true);

            reposStub.updateSeatsAtTable(studentCount - 1, studentId);

            notifyAll();
        }

        tabStub.seat();

    }


    /**
     * Part of the 1º student lifecycle to alert the waiter that the order has ready to he get
     */
    public synchronized void call_the_waiter() {
        int id = ((BarClientProxy) Thread.currentThread()).getStudentId();
        Request r = new Request(id, 1);

        try {
            requests.write(r);
        } catch (MemException e) {

        }

        requestCount++;

        notifyAll();
    }


    /**
     * Part of the student lifecycle to signal the waiter that he ends the current course or that the last student wants to pay the bill
     */
    public synchronized void signal_the_waiter() {
        int studentId = ((BarClientProxy) Thread.currentThread()).getStudentId();

        if (((BarClientProxy) Thread.currentThread()).getStudentState() == States.PAYING_THE_BILL) {

            try {
                requests.write(new Request(studentId, 3));
            } catch (MemException e) {

            }

            requestCount++;

            notifyAll();

        } else {
            courseReady = true;
            //Notify chef
            notifyAll();
        }
    }


    /**
     * Is the part of student life cycle when we decide to leave the restaurant adding a new request to wake up the waiter
     */
    public synchronized void exit() {
        int studentId = ((BarClientProxy) Thread.currentThread()).getStudentId();
        Request r = new Request(studentId, 4);

        try {
            requests.write(r);
        } catch (MemException e) {

        }


        requestCount++;

        students[studentId].setStudentState(States.GOING_HOME);
        ((BarClientProxy) Thread.currentThread()).setStudentState(States.GOING_HOME);
        reposStub.updateStudentState(studentId, students[studentId].getStudentState());
        //notify waiter
        notifyAll();


        while (goodbyeIds[studentId] == false) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
    }


    /**
     * Operation bar server shutdown
     */
    public synchronized void shutdown() {
        nEntities += 1;
        if (nEntities >= 1)
            BarMain.waitConnection = false;
        notifyAll();
    }
}



