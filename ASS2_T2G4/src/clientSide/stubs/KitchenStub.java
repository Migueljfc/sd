package clientSide.stubs;

import clientSide.entities.Chef;
import clientSide.entities.States;
import clientSide.entities.Waiter;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;

/**
 * Stub to the kitchen
 */

public class KitchenStub {
    /**
     *  Name of the platform where is located the kitchen server.
     */

    private String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     *
     * Instantiation of a stub to the kitchen.
     *
     * @param serverHostName name of the platform where is located the kitchen server
     * @param serverPortNumb port number for listening to service requests
     */
    public KitchenStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }


    /**
     * 	Part of the chef lifecycle to signal that is waiting the order
     */
    public void watch_news()
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


        outMessage = new Message (MessageType.WTNREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());

        com.writeObject (outMessage); 			//Write outGoing message in the communication channel

        inMessage = (Message) com.readObject(); //Read inGoing message


        if(inMessage.getMsgType() != MessageType.WTNDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal() || inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal())
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid chef state!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState());
        com.close ();
    }

    /**
     *  Part of the chef lifecycle to start the preparation and signal the waiter of that
     */
    public void start_preparation()
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

        outMessage = new Message (MessageType.SPREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.SPDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal() || inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal())
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
     * 	Part of the chef lifecycle to signal that the preparation was continued
     */
    public void proceed_preparation()
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

        outMessage = new Message (MessageType.PPREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.PPDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal() || inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal())
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
     * 	Part of the chef lifecycle to check if he needs to prepare another portion or not
     */

    public boolean have_all_portions_been_delivered()
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

        outMessage = new Message (MessageType.HAPBDREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HAPBDDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
        return inMessage.getAllClientsBeenServed();     // NAO SEI
    }


    /**
     * 	Part of the chef lifecycle to check if all courses have been delivered
     */

    public boolean has_the_order_been_completed()
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

        outMessage = new Message (MessageType.HTOBCREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HTOBCDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }

        //Close communication channel
        com.close ();
        return inMessage.getHasTheOrderBeenCompleted();
    }


    /**
     * 	Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
     */

    public void continue_preparation()
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

        outMessage = new Message (MessageType.CPREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.CPDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal() || inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal())
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
     * 	Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
     */

    public void have_next_portion_ready()
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

        outMessage = new Message (MessageType.HNPRREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HNPRDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal()|| inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Chef State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState());
        //Close communication channel
        com.close ();

    }



    /**
     * 	Part of the chef lifecycle when the order has completed
     */

    public void clean_up()
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

        outMessage = new Message (MessageType.CUREQ, ((Chef) Thread.currentThread()).getChefState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.CUDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal()|| inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Chef State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        ((Chef) Thread.currentThread ()).setChefState (inMessage.getChefState());
        //Close communication channel
        com.close ();
    }


    /**
     * 	Part of the waiter lifecycle to signal the waiter that a new order was started
     */

    public void hand_note_to_the_chef()
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

        outMessage = new Message (MessageType.HNTCREQ, ((Waiter) Thread.currentThread()).getWaiterState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.HNTCDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getWaiterState().ordinal() < States.APPRAISING_SITUATION.ordinal() || inMessage.getWaiterState().ordinal() > States.RECEIVING_PAYMENT.ordinal()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState());

        //Close communication channel
        com.close ();
    }



    /**
     * 	Part of the waiter lifecycle to signal that he is returning to bar
     */

    public void return_to_bar()
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

        outMessage = new Message (MessageType.RBREQ, ((Waiter) Thread.currentThread()).getWaiterState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.RBDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getWaiterState().ordinal() < States.APPRAISING_SITUATION.ordinal() || inMessage.getWaiterState().ordinal() > States.RECEIVING_PAYMENT.ordinal()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState());

        //Close communication channel
        com.close ();

    }


    /**
     * 	Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
     */
    public void collectPortion()
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

        outMessage = new Message (MessageType.CPOREQ, ((Waiter) Thread.currentThread()).getWaiterState().ordinal());
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.CPODONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        if(inMessage.getWaiterState().ordinal() < States.APPRAISING_SITUATION.ordinal() || inMessage.getWaiterState().ordinal() > States.RECEIVING_PAYMENT.ordinal()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread ()).setWaiterState (inMessage.getWaiterState());
        //Close communication channel
        com.close ();

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

        outMessage = new Message (MessageType.KSREQ);
        com.writeObject (outMessage); 			//Write outGoing message in the communication channel
        inMessage = (Message) com.readObject(); //Read inGoing message

        //Validate inGoing message type and arguments
        if(inMessage.getMsgType() != MessageType.KSDONE)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        //Close communication channel
        com.close ();
    }

}