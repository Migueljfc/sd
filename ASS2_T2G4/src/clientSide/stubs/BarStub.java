package clientSide.stubs;

import clientSide.entities.*;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;

/**
 *  @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Stub to the Bar.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class BarStub {
    /**
     * Name of the platform where is located the bar server
     */
    private String serverHostName;
    /**
     * Port number for listening to service requests
     */
    private int serverPortNumb;


    /**
     * Instantiation of a stub to the Bar.
     *
     * @param serverHostName name of the platform where is located the bar server
     * @param serverPortNumb port number for listening to service requests
     */
    public BarStub(String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }



    /**
     * Return id of the student being answered
     * @return Id of the student being answered
     */
    public int getCurrentStudent()
    {
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

        outMessage = new Message (MessageType.GCSREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message


        if(inMessage.getMsgType() != MessageType.GCSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
        return inMessage.getStudentBeingAnswered();
    }




    /**
     * Part of the chef lifecycle is called to alert the waiter that a portion has ready
     */
    public void alert_the_waiter()
    {
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

        outMessage = new Message (MessageType.ALREQ, ((Chef) Thread.currentThread()).getChefState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message


        if(inMessage.getMsgType() != MessageType.ALDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState() < States.WAIT_FOR_AN_ORDER || inMessage.getChefState() > States.CLOSING_SERVICE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState());

        com.close ();
    }



    /**
     * Is the part of the waiter life cycle where he waits for requests or served the pending and returns the id of the request
     * @return request id
     */
    public int look_arround()
    {
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

        outMessage = new Message (MessageType.LAREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        if(inMessage.getMsgType() != MessageType.LADONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close ();
        return inMessage.getRequestType();
    }



    /**
     * Operation prepare the Bill
     *
     * It is called the waiter to prepare the bill of the meal eaten by the students
     */
    public void prepare_the_bill()
    {
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

        outMessage = new Message (MessageType.PBREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.PBDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getWaiterState() < States.APPRAISING_SITUATION || inMessage.getWaiterState() > States.RECEIVING_PAYMENT)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid waiter state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState());

        com.close ();
    }



    /**
     * Operation say Goodbye
     *
     * It is called by the waiter to say goodbye to a student that's leaving the restaurant
     * @return true if there are no more students at the restaurant, false otherwise
     */
    public boolean say_goodbye()
    {
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

        outMessage = new Message (MessageType.SGREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        if(inMessage.getMsgType() != MessageType.SGDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }


        com.close ();
        return inMessage.getStudentsAtRestaurant();
    }




    /**
     * Operation enter the restaurant
     *
     * It is called by the student to signal that he is entering the restaurant
     */
    public void enter()
    {
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

        outMessage = new Message (MessageType.ENTREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

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
        if(inMessage.getStudentState() < States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > States.GOING_HOME)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState());

        com.close ();
    }




    /**
     * Operation call the waiter
     *
     * It is called by the first student to arrive the restaurant to call the waiter to describe the order
     *
     */
    public void call_the_waiter()
    {
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

        outMessage = new Message (MessageType.CWREQ, ((Student) Thread.currentThread()).getStudentId());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message


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

        com.close ();
    }



    /**
     * Operation signal the waiter
     *
     * It is called by the last student to finish eating that next course can be brought
     * signal chef that he can put request in the queue and waiter that he proceed his executing to collect portions
     * It is also used by last student to arrive to signal that he wishes to pay the bill
     */
    public void signal_the_waiter()
    {
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

        outMessage = new Message (MessageType.SWREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

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
        if(inMessage.getStudentState() < States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > States.GOING_HOME)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState());

        com.close ();
    }



    /**
     * Operation exit the restaurant
     *
     * It is called by a student when he leaves the restaurant
     */
    public void exit()
    {
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

        outMessage = new Message (MessageType.EXITREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message


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
        if(inMessage.getStudentState() < States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > States.GOING_HOME)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid student state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Student) Thread.currentThread ()).setStudentState (inMessage.getStudentState());

        com.close ();
    }


    /**
     * Operation server shutdown
     */
    public void shutdown()
    {
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

        outMessage = new Message (MessageType.BSREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        if(inMessage.getMsgType() != MessageType.BSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        com.close ();
    }

}
