package commInfra;

import java.io.*;
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

    private int msgType = -1;

    /**
     *  Student identification.
     */

    private int studentId = -1;

    /**
     *  Student state.
     */

    private int studentState = -1;


    /**
     *  Chef state.
     */

    private int chefState = -1;

    /**
     *  Waiter state.
     */

    private int waiterState = -1;

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

    public Message (int type)
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

    public Message (int type, int id, int state)
    {
        msgType = type;
        if ((msgType == MessageType.STBST) || (msgType == MessageType.CALLCUST) || (msgType == MessageType.RPAYDONE))
        { studentId= id;
            studentState = state;
        }
        else if ((msgType == MessageType.STCST) || (msgType == MessageType.REQCUTH) || (msgType == MessageType.CUTHDONE) ||
                (msgType == MessageType.BSHOPF))
        {
            chefState = state;
        } else if (msgType == MessageType.SHUT) {
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

    public Message (int type, int id)
    {
        msgType = type;
        studentId= id;
    }

    /**
     *  Message instantiation (form 4).
     *
     *     @param type message type
     *     @param id student identification
     *     @param endOP end of operations flag
     */

    public Message (int type, int id, boolean endOp)
    {
        msgType = type;
        studentId= id;
        this.endOp = endOp;
    }

    /**
     *  Message instantiation (form 5).
     *
     *     @param type message type
     *     @param studentId student identification
     *     @param studentState barber state
     */

    public Message (int type, int studentId, int studentState, int custId)
    {
        msgType = type;
        this.studentId= studentId;
        this.studentState = studentState;
    }

    /**
     *  Message instantiation (form 6).
     *
     *     @param type message type
     *     @param studentId student identification
     *     @param studentState barber state
     *     @param waiterState customer state
     */

    public Message (int type, int studentId, int studentState, int waiterState)
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

    public Message (int type, String name)
    {
        msgType = type;
        fName= name;

    }

    /**
     *  Getting message type.
     *
     *     @return message type
     */

    public int getMsgType ()
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

    public int getStudentState ()
    {
        return (studentState);
    }

    /**
     *  Getting chef state.
     *
     *     @return chef state
     */

    public int getChefState ()
    {
        return (chefState);
    }

    /**
     *  Getting waiter state.
     *
     *     @return waiter state
     */

    public int getWaiterState ()
    {
        return (chefState);
    }

    /**
     *  Getting end of operations flag (barber).
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
     *  Getting the number of iterations of the customer life cycle.
     *
     *     @return number of iterations of the customer life cycle
     */


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
