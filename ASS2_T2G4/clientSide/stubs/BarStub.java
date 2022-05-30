package clientSide.stubs;

import clientSide.entities.Chef;
import clientSide.entities.States;
import clientSide.entities.Student;
import clientSide.entities.Waiter;
import commInfra.MessageType;
import commInfra.ClientCom;
import commInfra.Message;
import genclass.GenericIO;

/**
 * Stub to the bar
 */

public class BarStub {
    /**
     *  Name of the platform where is located the bar server.
     */

    private String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     *
     * Instantiation of a stub to the bar.
     *
     * @param serverHostName name of the platform where is located the bar server
     * @param serverPortNumb port number for listening to service requests
     */
    public BarStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Is the part of student life cycle when we decide to enter the restaurant adding a new request to wake up the waiter
     */
    public void enter(){
        ClientCom com;					//Client communication
        Message outMessage, inMessage; 	//outGoing and inGoing messages

        com = new ClientCom (serverHostName, serverPortNumb);
        //Wait for a connection to be established
        while(!com.open())
        {	try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.ENTREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.ENTDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId())
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentState() != States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() != States.GOING_HOME)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState());
        //Close communication channel
        com.close ();
    }

    /**
     * Part of the 1ยบ student lifecycle to alert the waiter that the order has ready to he get
     */
    public void call_the_waiter() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try
            {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.CWREQ, ((Student) Thread.currentThread()).getStudentId());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.CWDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId())
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }

    /**
     * Part of the student lifecycle to signal the waiter that he ends the current course or that the last student wants to pay the bill
     */
    public void signal_the_waiter()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try
            {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.SWREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SWDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId())
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentState() != States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() != States.GOING_HOME)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState());
        //Close communication channel
        com.close ();
    }

    /**
     * Is the part of student life cycle when we decide to leave the restaurant adding a new request to wake up the waiter
     */
    public void exit()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())                                           // waits for a connection to be established
        {
            try
            {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.EXITREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.EXITDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId())
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student id!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentState() != States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() != States.GOING_HOME)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState());
        //Close communication channel
        com.close ();
    }

    /**
     * Is the part of the waiter life cycle where he waits for requests or served the pending and returns the id of the request
     * @return request id
     */
    public int look_arround()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }


        outMessage = new Message (MessageType.LAREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.LADONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
        return inMessage.getRequestId();
    }

    /**
     * Part of the waiter lifecycle to update his state to signal that is preparing the bill
     */
    public void prepare_the_bill()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }


        outMessage = new Message (MessageType.PBREQ, ((Waiter) Thread.currentThread()).getWaiterState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.PBDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getWaiterState() != States.APPRAISING_SITUATION || inMessage.getWaiterState() != States.RECEIVING_PAYMENT)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState());
        //Close communication channel
        com.close ();
    }

    /**
     * Part of the waiter lifecycle to say goodbye to the students when we signal that wants to go home
     * @return true if all students left the restaurant
     */
    public boolean say_goodbye()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SGREQ, ((Waiter) Thread.currentThread()).getWaiterState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SGDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //Close communication channel
        com.close ();
        return inMessage.getAllLeave();
    }

    /**
     * Part of the chef lifecycle is called to alert the waiter that a portion has ready
     */
    public void alert_the_waiter()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message (MessageType.ALREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.ALDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState() != States.WAIT_FOR_AN_ORDER || inMessage.getChefState() != States.CLOSING_SERVICE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState());
        //Close communication channel
        com.close ();
    }

    /**
     * @return Id of the student that are being attended
     */
    public int getCurrentStudent()
    {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.GCSREQ, ((Waiter) Thread.currentThread()).getWaiterState().ordinal());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.GCSDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();

        return inMessage.getStudentId();
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */
    public void shutdown(){
        ClientCom com;					//Client communication
        Message outMessage, inMessage; 	//outGoing and inGoing messages

        com = new ClientCom (serverHostName, serverPortNumb);
        //Wait for a connection to be established
        while(!com.open())
        {	try
        { Thread.currentThread ().sleep ((long) (10));
        }
        catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.SHUT);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SHUTDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }





}