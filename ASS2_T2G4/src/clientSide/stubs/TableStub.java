package clientSide.stubs;

import clientSide.entities.*;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;

/**
 *  @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Stub to the Table.
 *
 *    It instantiates a remote reference to the barber shop.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class TableStub {
    /**
     * Name of the platform where is located the table server
     */
    private String serverHostName;
    /**
     * Port number for listening to service requests
     */
    private int serverPortNumb;


    /**
     * Instantiation of a stub to the Table.
     *
     * @param serverHostName name of the platform where is located the kitchen server
     * @param serverPortNumb port number for listening to service requests
     */
    public TableStub(String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }




    /**
     * Obtain id of the first student to arrive
     * @return id of the first student to arrive at the restaurant
     */
    public int getFirstToArrive()
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

        outMessage = new Message (MessageType.STFS);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.FSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
        return inMessage.getFirstToArrive();
    }




    /**
     * Obtain id of the last student to arrive
     * @return id of the last student to finish eating a meal
     */
    public int getLastToEat()
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

        outMessage = new Message (MessageType.GLSREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.GLSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
        return inMessage.getLastToEat();
    }


    /**
     * Set id of the first student to arrive
     * @param firstToArrive id of the first student to arrive
     */
    public void setFirstStudent(int firstToArrive)
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

        outMessage = new Message (MessageType.STFS, firstToArrive);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.FSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }

    /**
     * Set id of the last student to arrive
     * @param lastToArrive if of the last student to arrive to the restaurant
     */
    public void setLastToArrive(int lastToArrive)
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

        outMessage = new Message (MessageType.STLS, lastToArrive);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.LSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }






    /**
     * Operation salute the client
     *
     * It is called by the waiter when a student enters the restaurant and needs to be saluted
     * Waiter waits for the student to take a seat (if he hasn't done it yet)
     * Waiter waits for student to finish reading the menu
     */
    public void salute_client(int studentIdBeingAnswered)
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
        outMessage = new Message (MessageType.SCREQ, studentIdBeingAnswered, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SCDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentBeingAnswered() != -1)
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation return to the bar
     *
     * It is called by the waiter to return to the bar to the appraising situation state
     */
    public  void return_to_bar()
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

        outMessage = new Message (MessageType.RTBREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.RTBDONE)
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation get the pad
     *
     * It is called by the waiter when an order is going to be described by the first student to arrive
     * Waiter Blocks waiting for student to describe him the order
     */
    public  void get_the_pad()
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

        outMessage = new Message (MessageType.GTPREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.GTPDONE)
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation have all clients been served
     *
     * Called by the waiter to check if all clients have been served or not
     * @return true if all clients have been served, false otherwise
     */
    public  boolean have_all_portions_delivered()
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

        outMessage = new Message (MessageType.HAPDREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HAPDDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //Close communication channel
        com.close ();
        return inMessage.getAllClientsBeenServed();
    }



    /**
     * Operation deliver portion
     *
     * Called by the waiter, to deliver a portion
     */
    public  void deliver_portion()
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

        outMessage = new Message (MessageType.DPREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.DPDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }




    /**
     * Operation present the bill
     *
     * Called by the waiter to present the bill to the last student to arrive
     */
    public  void present_the_bill()
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

        outMessage = new Message (MessageType.PREBREQ, ((Waiter) Thread.currentThread()).getWaiterState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.PREBDONE)
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation siting at the table
     *
     * Student comes in the table and sits (blocks) waiting for waiter to bring him the menu
     * Called by the student (inside enter method in the bar)
     */
    public  void seat()
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

        outMessage = new Message (MessageType.SATREQ, ((StudentCloning) Thread.currentThread()).getStudentId(),((StudentCloning) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SATDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getStudentId() != ((StudentCloning) Thread.currentThread()).getStudentId())
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
        ((StudentCloning) Thread.currentThread ()).setStudentState (inMessage.getStudentState());

        //Close communication channel
        com.close ();
    }



    /**
     * Operation read the menu
     *
     * Called by the student to read a menu, wakes up waiter to signal that he already read the menu
     */
    public  void read_menu()
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

        outMessage = new Message (MessageType.RMREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.RMDONE)
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation prepare the order
     *
     * Called by the student to begin the preparation of the order (options of his companions)
     */
    public  void prepare_the_order()
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
        outMessage = new Message (MessageType.POREQ,((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.PODONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation everybody has chosen
     *
     * Called by the first student to arrive to check if all his companions have choose or not
     * Blocks if not waiting to be waker up be a companion to give him his preference
     * @return true if everybody choose their course choice, false otherwise
     */
    public  boolean has_everybody_chosen()
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

        outMessage = new Message (MessageType.HECREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HECDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
        return inMessage.getEverybodyHasChosen();
    }


    /**
     * Operation add up ones choices
     *
     * Called by the first student to arrive to add up a companions choice to the order
     */
    public  void add_up_ones_choice()
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

        outMessage = new Message (MessageType.AUOCREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.AUOCDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }



    /**
     * Operation describe the order
     *
     * Called by the first student to arrive to describe the order to the waiter
     * Blocks waiting for waiter to come with pad
     * Wake waiter up so he can take the order
     */
    public  void describe_the_order()
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

        outMessage = new Message (MessageType.DOREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.DODONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }

    /**
     * Operation join the talk
     *
     * Called by the first student to arrive so he can join his companions
     * while waiting for the courses to be delivered
     */
    public  void join_the_talk()
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

        outMessage = new Message (MessageType.JTREQ,((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.JTDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
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
        //Close communication channel
        com.close ();
    }



    /**
     * Operation inform companion
     *
     * Called by a student to inform the first student to arrive about their preferences
     * Blocks if someone else is informing at the same time
     */
    public  void inform_companion()
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

        outMessage = new Message (MessageType.ICREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.ICDONE)
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
        //Close communication channel
        com.close ();
    }




    /**
     * Operation start eating
     *
     * Called by the student to start eating the meal (During random time)
     */
    public void start_eating()
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

        outMessage = new Message (MessageType.SEREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SEDONE)
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
        //Close communication channel
        com.close ();
    }





    /**
     * Operation end eating
     *
     * Called by the student to signal that he has finished eating his meal
     */
    public  void end_eating()
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

        outMessage = new Message (MessageType.EEREQ, ((Student) Thread.currentThread()).getStudentId(),((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.EEDONE)
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
        //Close communication channel
        com.close ();
    }




    /**
     * Operation has everybody finished eating
     *
     * Called by the student to wait for his companions to finish eating
     */
    public  boolean has_everybody_finished()
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

        outMessage = new Message (MessageType.HEFREQ, ((Student) Thread.currentThread()).getStudentId());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HEFDONE)
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
        return inMessage.getHasEverybodyFinishedEating();
    }



    /**
     * Operation honour the bill
     *
     * Called by the student to pay the bill
     * Student blocks waiting for bill to be presented and signals waiter when it's time to pay it
     */
    public  void honor_the_bill()
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

        outMessage = new Message (MessageType.HBREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HBDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }




    /**
     * 	Operation have all courses been eaten
     *
     * 	Called by the student to check if there are more courses to be eaten
     * 	Student blocks waiting for the course to be served
     * 	@return true if all courses have been eaten, false otherwise
     */
    public  boolean have_all_courses_delivery()
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

        outMessage = new Message (MessageType.HACDREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HACDDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
        return inMessage.getAllCoursesEaten();
    }



    /**
     * Operation should have arrived earlier
     *
     * Called by the student to check which one was last to arrive
     * @return True if current student was the last to arrive, false otherwise
     */
    public  boolean should_have_arrived_earlier()
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

        outMessage = new Message (MessageType.SHAEREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SHAEDONE)
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
        //Close communication channel
        com.close ();
        return inMessage.getArrivedEarlier();
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

        outMessage = new Message (MessageType.TSREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.TSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }
}
