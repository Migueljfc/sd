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
     * Get id of the first student to arrive
     * @return id of the first student
     */
    public int getFirstStudent()
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
     * Obtain id of the last student to finish eating
     * @return id of the last student to finish eating
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
     * @param id id of the first student to arrive
     */
    public void setFirstStudent(int id)
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

        outMessage = new Message (MessageType.STFS, id);
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
     * Set id of the last student that enter in restaurant
     * @param id if of the last student that enter in restaurant
     */
    public void setLastStudent(int id)
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

        outMessage = new Message (MessageType.STLS, id);
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
     * Part of the waiter lifecycle is called when a student enter the restaurant
     * @param id id of the student to be saluted
     */
    public void salute_client(int id)
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
        outMessage = new Message (MessageType.SCREQ, id, ((Waiter) Thread.currentThread()).getWaiterState());
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
     * Part of the waiter lifecycle is called when we return to bar
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
     * Part of the waiter lifecycle is called when the first student intent to describe the order
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
     * Part of the waiter lifecycle is called to check if all portions have been delivered
     * @return true if all portions been delivered
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
     * Part of the waiter lifecycle is used to signal that a portion have been delivered
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
     * Part of the waiter lifecycle is used present the bill and signal the last student to pay
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
     * Called when a student enter in the bar to register the position in the table and to wait by the waiter to present the menu
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
     * Part of the student lifecycle used to update the student state and update the read array to notify that the student already read the menu
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
     * Part of the 1º student lifecycle to update his state and signal that is organizing the order
     */
    public void prepare_the_order()
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
     * Part of the 1º student lifecycle to check if the others students chosen their orders
     * @return true if the others students already chosen their orders
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
     * Part of the 1º student lifecycle to update the number os orders and notify the other students of that
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
     * Part of the 1º student lifecycle to wake up the waiter and describe the order
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
     * Part of the 1º student lifecycle to join the talk with the other students
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
     * Part of the students' lifecycle to inform the 1º student about his course option
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
     * Part of the students' lifecycle to start eating and update his state, for simulate that is used the function sleep
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
     * Part of the student lifecycle to update his state and signal that he end his course and register last student to eat
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
     * Part of the student lifecycle to wait for the last student to finish his course
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
     * Part of the student lifecycle to wait for the waiter to give him the bill
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
     *  Part of the students' lifecycle to check if all courses have been delivered or not
     * 	@return true if all courses have been delivered
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
     * Part of the student lifecycle to check if he is the last to arrive and change his state to pay the bill
     * @return True if student was the last to arrive
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
