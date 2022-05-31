package commInfra;

import clientSide.entities.States;
import genclass.GenericIO;

import java.io.Serializable;

/**
 * Internal structure of the exchanged messages.
 * <p>
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable {
    /**
     * Serialization key.
     */

    private static final long serialVersionUID = 2021L;


    /**
     * Message type.
     */

    private final MessageType msgType;

    /**
     * Student identification.
     */

    private int studentId = -1;

    /**
     * Student state.
     */

    private States studentState = States.NULL;


    /**
     * Chef state.
     */

    private States chefState = States.NULL;

    /**
     * Waiter state.
     */

    private States waiterState = States.NULL;

    /**
     *  End of operations (student).
     */

    /**
     * student being attended
     */
    private int currentStudent = -1;

    private boolean endOp = false;

    /**
     * Name of the logging file.
     */

    private String fName = null;

    /**
     * Id of the request made to the waiter
     */
    private int requestId = -1;

    /**
     * Nº of courses delivered
     */
    private int courses = -1;

    /**
     * Nº of portions delivered
     */
    private int portions = -1;
    /**
     * Seat identification
     */
    private int seat=-1;


    /**
     * controll if all portions have been delivered
     */
    private boolean haveallPortionsBeenDelivered;

    /**
     * control if order has been completed
     */
    private boolean hasTheOrderBeenCompleted;

    /**
     * Control if all students leave
     */
    private boolean allLeave;


    /**
     * Control if all clients have been served
     */
    private boolean allClientsBeenServed;

    /**
     * Control if everybody has chosen
     */
    private boolean hasEverybodyChosen;

    /**
     * Control if everybody has finished eating, false otherwise
     */
    private boolean hasEverybodyFinished;

    /**
     * Control if all courses have been eaten
     */
    private boolean haveAllCoursesBeenEaten;

    /**
     * Control which student was the last to arrive at the Table
     */
    private boolean shouldArrivedEarlier;

    /**
     * Id of the first student to arrive
     */
    private int firstStudent=-1;

    /**
     * Id of the last student to eat
     */
    private int lastToEat=-1;

    /**
     * Id of the last student to arrive
     */
    private int lastStudent=-1;


    /**
     * Control if is necessary to print in reportStatus
     */
    private boolean print;

    /**
     * Message instantiation (form 1).
     *
     * @param type message type
     */

    public Message(MessageType type) {
        msgType = type;
    }

    /**
     * Message instantiation (form 2).
     *
     * @param type  message type
     * @param iOS   student identification or student / chef / waiter state or Nº courses / portions
     */

    public Message(MessageType type, int iOS) {
        msgType = type;
        int entitieType = getEntitieType(type);
        if (entitieType == 1) {
            chefState = States.values()[iOS];
        } else if (entitieType == 2) {
            waiterState = States.values()[iOS];
        } else if (entitieType == 3) {
            if ((msgType == MessageType.CWREQ) || (msgType == MessageType.CWDONE) || (msgType == MessageType.HEFREQ)) {
                studentId = iOS;
            } else if ((msgType == MessageType.POREQ) || (msgType == MessageType.PORDONE) || (msgType == MessageType.JTREQ) || (msgType == MessageType.JTDONE)) {
                studentState = States.values()[iOS];
            }
        } else if (entitieType == 4) {
            if (msgType == MessageType.GFSDONE) {
                firstStudent = iOS;
            } else if (msgType == MessageType.GLSDONE) {
                lastStudent = iOS;
            } else if (msgType == MessageType.STFS) {
                firstStudent = iOS;
            } else if (msgType == MessageType.STLS) {
                lastStudent = iOS;
            } else if (msgType == MessageType.GCSDONE) {
                currentStudent = iOS;
            }
        } else if (entitieType == 5) {
            if (msgType == MessageType.STCST) {
                chefState = States.values()[iOS];
            } else if (msgType == MessageType.STWST) {
                waiterState = States.values()[iOS];
            } else if (msgType == MessageType.STCOR) {
                courses = iOS;
            } else if (msgType == MessageType.STPOR) {
                portions = iOS;
            } else if (msgType == MessageType.STSSWE) {
                studentId = iOS;
            }

        } else {
            GenericIO.writelnString("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit(1);
        }
    }
   

    /**
     * Message instantiation (form 3).
     *
     * @param type  message type
     * @param bol flag of control variables
     */

    public Message(MessageType type,  boolean bol) {
        msgType = type;
        if (msgType == MessageType.HAPBDDONE)
            haveallPortionsBeenDelivered = bol;
        else if (msgType == MessageType.HTOBCDONE)
            hasTheOrderBeenCompleted = bol;
        else if (msgType == MessageType.SGDONE)
            allLeave = bol;
        else if (msgType == MessageType.HAPDDONE)
            allClientsBeenServed = bol;
        else if (msgType == MessageType.HECDONE)
            hasEverybodyChosen = bol;
        else if (msgType == MessageType.HACDDONE)
            haveAllCoursesBeenEaten = bol;
        else if (msgType == MessageType.HEFDONE)
            hasEverybodyFinished = bol;

    }

    /**
     *  Message instantiation (form 4).
     *
     *     @param type message type
     *     @param id student identification
     *     @param sOS  state of an entitie or seat at the table
     */

    public Message (MessageType type, int id, int sOS)
    {
        msgType = type;
        int entity = getEntitieType(type);

        if(msgType == MessageType.SATREQ){
            seat = sOS;
        } else if (msgType == MessageType.SCREQ || msgType == MessageType.SCDONE){
            currentStudent = id;
            waiterState = States.values()[sOS];
            return;
        } else {
            studentState = States.values()[sOS];
        }

        studentId = id;
    }


    /**
     * Message instantiation (form 5).
     *
     * 		@param type message type
     * 		@param id id of the student
     * 		@param hasEverybodyFinished control if everybody has finished eating
     */
    public Message (MessageType type, int id, boolean hasEverybodyFinished)
    {
        msgType = type;
        studentId = id;
        this.hasEverybodyFinished = hasEverybodyFinished;

    }

    /**
     *  Message instantiation (form 6).
     *
     *     @param type message type
     *     @param id student identification
     *     @param state student state
     *     @param bol control if a student should have arrived earlier or not
     */

    public Message (MessageType type, int id, States state, boolean bol)
    {
        msgType = type;
        studentId = id;
        studentState = state;
        if(msgType == MessageType.SHAEDONE)
            shouldArrivedEarlier = bol;
        else if (msgType == MessageType.STSST2)
            print = bol;
    }

    /**
     * Message instantiation (form 7).
     *
     * @param type message type
     * @param requestId request id
     * @param s string to identify a request message
     */

    public Message(MessageType type, int requestId, String s) {
        msgType = type;
        this.requestId = requestId;
    }


    /**
     * Getting message type.
     *
     * @return message type
     */

    public MessageType getMsgType() {
        return (msgType);
    }

    /**
     * Getting student identification.
     *
     * @return student identification
     */

    public int getStudentId() {
        return (studentId);
    }

    /**
     * Getting student state.
     *
     * @return student state
     */

    public States getStudentState() {
        return (studentState);
    }

    /**
     * Getting chef state.
     *
     * @return chef state
     */

    public States getChefState() {
        return (chefState);
    }

    /**
     * Getting waiter state.
     *
     * @return waiter state
     */

    public States getWaiterState() {
        return (waiterState);
    }

    /**
     * Getting end of operations flag .
     *
     * @return end of operations flag
     */

    public boolean getEndOp() {
        return (endOp);
    }

    /**
     * Getting name of logging file.
     *
     * @return name of the logging file
     */

    public String getLogFName() {
        return (fName);
    }

    public int getRequest() {
        return requestId;
    }

    public int getCurrentStudent(){
        return currentStudent;
    }

    public boolean getHaveallPortionsBeenDelivered(){
        return haveallPortionsBeenDelivered;
    }
    /**
     * Get has the order been completed value
     * @return true if order has been completed, false otherwise
     */
    public boolean getHasTheOrderBeenCompleted() { return (hasTheOrderBeenCompleted); }

    /**
     * Get request id
     * @return request id
     */
    public int getRequestId() { return (requestId); }

    /**
     * Get if all students left the restaurant
     * @return true if all students left the restaurant
     */
    public boolean getAllLeave() { return (allLeave); }


    /**
     * Get all clients have been served
     * @return true if all clients have been served
     */
    public boolean getAllClientsBeenServed() { return (allClientsBeenServed); }

    /**
     * Get has everybody chosen
     * @return true if everybody has chosen their preference, false otherwise
     */
    public boolean getHasEverybodyChosen() { return (hasEverybodyChosen); }

    /**
     * Get everybody has finished eating
     * @return true if everybody has eaten
     */
    public boolean getHasEverybodyFinishedEating() { return (hasEverybodyFinished); }

    /**
     * Get the value of have all courses been eaten
     * @return true if all courses have been eaten, false otherwise
     */
    public boolean getAllCoursesEaten() { return (haveAllCoursesBeenEaten); }

    /**
     * Get should have arrived earlier
     * @return  true if shouldArrivedEarlier
     */
    public boolean getArrivedEarlier() { return (shouldArrivedEarlier); }

    /**
     * Get id of the first student
     * @return id of the student to arrive the restaurant
     */
    public int getFirstStudent() { return (firstStudent); }

    /**
     * Get id of the last student to eat
     * @return id of the last student to eat
     */
    public int gestLastToEat() { return (lastToEat); }

    /**
     * Get id of the last student to arrive
     * @return id of the last student tha arrive in restaurant
     */
    public int getLastStudent() { return (lastStudent); }

    /**
     * Get seat
     * @return seat
     */
    public int getSeat() { return (seat); }

    /**
     * Get Courses
     * @return nCourses value
     */
    public int getCourses() { return (courses); }

    /**
     * Get Portions
     * @return Portions
     */
    public int getPortions() { return (portions); }

    /**
     * Get hold variable value
     * @return the value of hold variable used to specify if is necessary to print report status or not
     */
    public boolean getPrint() { return (print); }


    public int getEntitieType(MessageType msgType) {
        if ((msgType == MessageType.WTNREQ) || (msgType == MessageType.WTNDONE) || (msgType == MessageType.SPREQ) || (msgType == MessageType.SPDONE) || (msgType == MessageType.CPREQ) || (msgType == MessageType.CPDONE) || (msgType == MessageType.PPREQ) || (msgType == MessageType.PPDONE) || (msgType == MessageType.ALREQ) || (msgType == MessageType.ALDONE) || (msgType == MessageType.HAPBDREQ) || (msgType == MessageType.HAPBDDONE) || (msgType == MessageType.HNPRREQ) || (msgType == MessageType.HNPRDONE) || (msgType == MessageType.HTOBCREQ) || (msgType == MessageType.HTOBCDONE) || (msgType == MessageType.CUREQ) || (msgType == MessageType.CUDONE)) {
            return 1; //Chef
        } else if ((msgType == MessageType.LAREQ) || (msgType == MessageType.LADONE) || (msgType == MessageType.SCREQ) || (msgType == MessageType.SCDONE) || (msgType == MessageType.RTBREQ) || (msgType == MessageType.RTBDONE) || (msgType == MessageType.GTPREQ) || (msgType == MessageType.GTPDONE) || (msgType == MessageType.HNTCREQ) || (msgType == MessageType.HNTCDONE) || (msgType == MessageType.CPOREQ) || (msgType == MessageType.CPODONE) || (msgType == MessageType.RBREQ) || (msgType == MessageType.RBDONE) || (msgType == MessageType.HAPDREQ) || (msgType == MessageType.HAPDDONE) || (msgType == MessageType.DPREQ) || (msgType == MessageType.DPDONE) || (msgType == MessageType.PREBREQ) || (msgType == MessageType.PREBDONE) || (msgType == MessageType.PBREQ) || (msgType == MessageType.PBDONE) || (msgType == MessageType.SGREQ) || (msgType == MessageType.SGDONE)) {
            return 3; //Waiter
        } else if ( (msgType == MessageType.ENTREQ) || (msgType == MessageType.ENTDONE) || (msgType == MessageType.RMREQ) || (msgType == MessageType.RMDONE) || (msgType == MessageType.POREQ) || (msgType == MessageType.PODONE) || (msgType == MessageType.HEFREQ) || (msgType == MessageType.HEFDONE) || (msgType == MessageType.AUOCREQ) || (msgType == MessageType.AUOCDONE) || (msgType == MessageType.CWREQ) || (msgType == MessageType.CWDONE) || (msgType == MessageType.DOREQ) || (msgType == MessageType.DODONE) || (msgType == MessageType.JTREQ) || (msgType == MessageType.JTDONE) || (msgType == MessageType.ICREQ) || (msgType == MessageType.ICDONE) || (msgType == MessageType.HACDREQ) || (msgType == MessageType.HACDDONE) || (msgType == MessageType.SEREQ) || (msgType == MessageType.SEDONE) || (msgType == MessageType.EEREQ) || (msgType == MessageType.EEDONE) || (msgType == MessageType.SWREQ) || (msgType == MessageType.SWDONE) || (msgType == MessageType.SHAEREQ) || (msgType == MessageType.SHAEDONE) || (msgType == MessageType.HBREQ) || (msgType == MessageType.HBDONE) || (msgType == MessageType.EXITREQ) || (msgType == MessageType.EXITDONE) || (msgType == MessageType.SATREQ) || (msgType == MessageType.SATDONE) ||(msgType == MessageType.HECREQ) || (msgType == MessageType.HECDONE)) {
                return 2; //Student
        } else if ((msgType == MessageType.GCSREQ) || (msgType == MessageType.GCSDONE) || (msgType == MessageType.GFSREQ) || (msgType == MessageType.GFSDONE) || (msgType == MessageType.GLSREQ) || (msgType == MessageType.GLSDONE) || (msgType == MessageType.STFS) || (msgType == MessageType.FSDONE) || (msgType == MessageType.STLS) || (msgType == MessageType.LSDONE) || (msgType == MessageType.KSREQ) || (msgType == MessageType.KSDONE) || (msgType == MessageType.BSREQ) || (msgType == MessageType.BSDONE) || (msgType == MessageType.TSREQ) || (msgType == MessageType.TSDONE)) {
            return 4; //Support Messages
        } else if ((msgType == MessageType.STCST) || (msgType == MessageType.CSTDONE) || (msgType == MessageType.STSST1) || (msgType == MessageType.SST1DONE) || (msgType == MessageType.STSST2) || (msgType == MessageType.SST2DONE) || (msgType == MessageType.STWST) || (msgType == MessageType.WSDONE) || (msgType == MessageType.STPOR) || (msgType == MessageType.PORDONE) || (msgType == MessageType.STSS) || (msgType == MessageType.SSDONE) || (msgType == MessageType.STCOR) || (msgType == MessageType.CORDONE) || (msgType == MessageType.GRSREQ) || (msgType == MessageType.GRSDONE) || (msgType == MessageType.STSSWE) || (msgType == MessageType.SSWEDONE)) {
            return 5; //GeneralRepository Messages
        } else {
            return -1;
        }
    }



    /**
     * Printing the values of the internal fields.
     * <p>
     * It is used for debugging purposes.
     *
     * @return string containing, in separate lines, the pair field name - field value
     */

    @Override
    public String toString() {
        return ("Message type = " + msgType +
                "\nChef State = " + chefState +
                "\nWaiter State = " + waiterState +
                "\nStudentId = " + studentId + " StudentState = " + studentState +
                "\nCurrentStudent = " + currentStudent +
                "\nHave All Portions Been Delivered ? " + haveallPortionsBeenDelivered +
                "\nHas the Order been completed = " + hasTheOrderBeenCompleted +
                "\nRequest id = " + requestId +
                "\nHave all clients been served = " + allClientsBeenServed +
                "\nHas everybody has chosen = " + hasEverybodyChosen +
                "\nHas everybody finished  = " + hasEverybodyFinished +
                "\nHave all courses been eaten = " + haveAllCoursesBeenEaten +
                "\nShould have arrived earlier = " + shouldArrivedEarlier +
                "\nFirst to arrive = " + firstStudent + " Lat to arrive = " +lastStudent +
                "\nLast to eat = " + lastToEat +
                "\nCourses = " + courses + " Portions = "+ portions +
                "\nPrint status = " + print + " Seat at the table = " + seat +
                "");
    }




}
