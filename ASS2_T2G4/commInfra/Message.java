package commInfra;

import java.io.*;

import clientSide.entities.States;
import genclass.GenericIO;

/**
 *   Internal structure of the exchanged messages.
 *
 *   Implementation of a client-server model of type 2 (server replication).
 *   Communication is based on a communication channel under the TCP protocol.
 */

public class Message implements Serializable
{
    /**
     *  Serialization key.
     */

    private static final long serialVersionUID = 2021L;

    /**
     *  Message type.
     */

    private MessageType msgType;

    /**
     *  Student identification.
     */

    private int studentId = -1;

    /**
     *  Student state.
     */

    private States studentState ;


    /**
     *  Chef state.
     */

    private States chefState;

    /**
     *  Waiter state.
     */

    private States waiterState;

    /**
     *  End of operations (student).
     */

    private boolean endOp = false;

    /**
     *  Name of the logging file.
     */

    private String fName = null;


    /**
     *  Message instantiation (form 1).
     *
     *     @param type message type
     */

    public Message (MessageType type)
    {
        msgType = type;
    }

    /**
     *  Message instantiation (form 2).
     *
     *     @param type message type
     *     @param id student identification
     *     @param state student / chef / waiter state
     */

    public Message (MessageType type, int id, States state)
    {
        msgType = type;
        if ((msgType == MessageType.ALREQ) || (msgType == MessageType.ALDONE) || (msgType == MessageType.ENTREQ )|| (msgType == MessageType.ENTDONE) || (msgType == MessageType.RMREQ)|| (msgType == MessageType.RMDONE) || (msgType == MessageType.POREQ) || (msgType == MessageType.PODONE)|| (msgType == MessageType.EHCDONE)|| (msgType == MessageType.EHCREQ)|| (msgType == MessageType.AUOCREQ)|| (msgType == MessageType.AUOCDONE) || (msgType == MessageType.CWREQ)|| (msgType == MessageType.CWDONE)|| (msgType == MessageType.DOREQ)|| (msgType == MessageType.DODONE)|| (msgType == MessageType.JTREQ)|| (msgType == MessageType.JTDONE)|| (msgType == MessageType.ICREQ)|| (msgType == MessageType.ICDONE)|| (msgType == MessageType.HACDREQ)|| (msgType == MessageType.HACDDONE)|| (msgType == MessageType.SEREQ) || (msgType == MessageType.SEDONE)|| (msgType == MessageType.EEREQ)|| (msgType == MessageType.EEDONE)|| (msgType == MessageType.HEFREQ)|| (msgType == MessageType.HEFDONE)|| (msgType == MessageType.GLSREQ)|| (msgType == MessageType.GLSDONE)|| (msgType == MessageType.SWREQ)|| (msgType == MessageType.SWDONE)|| (msgType == MessageType.SHAEREQ)|| (msgType == MessageType.SHAEDONE) || (msgType == MessageType.HBREQ)|| (msgType == MessageType.HBDONE)|| (msgType == MessageType.EXITREQ)|| (msgType == MessageType.EXITDONE)|| (msgType == MessageType.GFSREQ)|| (msgType == MessageType.GFSDONE) )
        {
            studentId= id;
            studentState = state;
        }
        else if ((msgType == MessageType.WTNREQ) || (msgType == MessageType.WTNDONE) || (msgType == MessageType.SPREQ) || (msgType == MessageType.SPDONE) || (msgType == MessageType.CPREQ)|| (msgType == MessageType.CPDONE)|| (msgType == MessageType.PPREQ)|| (msgType == MessageType.PPDONE) || (msgType == MessageType.ALREQ) || (msgType == MessageType.ALDONE) || (msgType == MessageType.HAPBDREQ) || (msgType == MessageType.HAPBDDONE) || (msgType == MessageType.HNPRREQ) || (msgType == MessageType.HNPRDONE) || (msgType == MessageType.HTOBCREQ) || (msgType == MessageType.HTOBCDONE) || (msgType == MessageType.CUREQ) || (msgType == MessageType.CUDONE))
        {
            chefState = state;
        } else if ((msgType == MessageType.LAREQ) || (msgType == MessageType.LADONE) || (msgType == MessageType.LADONE) || (msgType == MessageType.GCSREQ)|| (msgType == MessageType.GCSDONE) || (msgType == MessageType.LADONE) || (msgType == MessageType.SCREQ) || (msgType == MessageType.SCDONE) || (msgType == MessageType.RTBREQ)|| (msgType == MessageType.RTBDONE) || (msgType == MessageType.GTPREQ)|| (msgType == MessageType.GTPDONE)|| (msgType == MessageType.HNTCREQ)|| (msgType == MessageType.HNTCDONE)|| (msgType == MessageType.CPOREQ)|| (msgType == MessageType.CPODONE)|| (msgType == MessageType.RBREQ)|| (msgType == MessageType.RBDONE)|| (msgType == MessageType.HAPDREQ)|| (msgType == MessageType.HAPDDONE)|| (msgType == MessageType.DPREQ)|| (msgType == MessageType.DPDONE)|| (msgType == MessageType.PREBREQ)|| (msgType == MessageType.PREBDONE) || (msgType == MessageType.PBREQ)|| (msgType == MessageType.PBDONE) || (msgType == MessageType.SGREQ)|| (msgType == MessageType.SGDONE)) {
            waiterState = state;

        } else { GenericIO.writelnString ("Message type = " + msgType + ": non-implemented instantiation!");
            System.exit (1);
        }
    }

    /**
     *  Message instantiation (form 3).
     *
     *     @param type message type
     *     @param id student identification
     */

    public Message (MessageType type, int id)
    {
        msgType = type;
        studentId= id;
    }

    /**
     *  Message instantiation (form 4).
     *
     *     @param type message type
     *     @param id student identification
     *     @param endOp end of operations flag
     */

    public Message (MessageType type, int id, boolean endOp)
    {
        msgType = type;
        studentId= id;
        this.endOp = endOp;
    }


    /**
     *  Message instantiation (form 6).
     *
     *     @param type message type
     *     @param studentId student identification
     *     @param studentState barber state
     *     @param waiterState customer state
     */

    public Message (MessageType type, int studentId, States studentState, States waiterState)
    {
        msgType = type;
        this.studentId= studentId;
        this.studentState = studentState;
        this.waiterState= waiterState;
    }

    /**
     *  Message instantiation (form 7).
     *
     *     @param type message type
     *     @param name name of the logging file
     */

    public Message (MessageType type, String name)
    {
        msgType = type;
        fName= name;

    }

    /**
     *
     * @param type message type
     * @param waiterState waiter state
     */
    public Message (MessageType type, States waiterState){
        msgType = type;
        this.waiterState = waiterState;
    }

    /**
     *  Getting message type.
     *
     *     @return message type
     */

    public MessageType getMsgType ()
    {
        return (msgType);
    }

    /**
     *  Getting student identification.
     *
     *     @return student identification
     */

    public int getStudentId ()
    {
        return (studentId);
    }

    /**
     *  Getting student state.
     *
     *     @return student state
     */

    public States getStudentState ()
    {
        return (studentState);
    }

    /**
     *  Getting chef state.
     *
     *     @return chef state
     */

    public States getChefState ()
    {
        return (chefState);
    }

    /**
     *  Getting waiter state.
     *
     *     @return waiter state
     */

    public States getWaiterState ()
    {
        return (waiterState);
    }

    /**
     *  Getting end of operations flag .
     *
     *     @return end of operations flag
     */

    public boolean getEndOp ()
    {
        return (endOp);
    }

    /**
     *  Getting name of logging file.
     *
     *     @return name of the logging file
     */

    public String getLogFName ()
    {
        return (fName);
    }



    /**
     *  Printing the values of the internal fields.
     *
     *  It is used for debugging purposes.
     *
     *     @return string containing, in separate lines, the pair field name - field value
     */

    @Override
    public String toString ()
    {
        return ("Message type = " + msgType +
                "\nStudent Id = " + studentId +
                "\nStudent State = " + studentState +
                "\nChef State = " + chefState +
                "\nWaiter State = " + waiterState +
                "\nEnd of Operations (barber) = " + endOp +
                "\nName of logging file = " + fName);
    }
}
