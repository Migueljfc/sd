package clientSide.stubs;

import clientSide.entities.Chef;
import clientSide.entities.States;
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
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while(!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }


        outMessage = new Message(MessageType.WTNREQ, ((Chef) Thread.currentThread()).getChefState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();


        if((inMessage.getMsgType() != MessageType.WTNDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getChefState() != States.WAIT_FOR_AN_ORDER) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Chef State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Chef) Thread.currentThread()).setChefState(inMessage.getChefState());
        com.close();
    }

    /**
     *  Part of the chef lifecycle to start the preparation and signal the waiter of that
     */
    public void start_preparation()
    {

    }


    /**
     * 	Part of the chef lifecycle to signal that the preparation was continued
     */
    public void proceed_preparation()
    {

    }


    /**
     * 	Part of the chef lifecycle to check if he needs to prepare another portion or not
     */

    public boolean have_all_portions_been_delivered()
    {

    }


    /**
     * 	Part of the chef lifecycle to check if all courses have been delivered
     */

    public boolean has_the_order_been_completed()
    {

    }


    /**
     * 	Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
     */

    public void continue_preparation()
    {

    }


    /**
     * 	Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
     */

    public void have_next_portion_ready()
    {

    }



    /**
     * 	Part of the chef lifecycle when the order has completed
     */

    public void clean_up()
    {

    }


    /**
     * 	Part of the waiter lifecycle to signal the waiter that a new order was started
     */

    public void hand_note_to_the_chef()
    {

    }


    /**
     * 	Part of the waiter lifecycle to signal that he is returning to bar
     */

    public void return_to_bar()
    {

    }


    /**
     * 	Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
     */
    public void collectPortion()
    {


    }


}


