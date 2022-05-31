package serverSide.sharedRegions;

import clientSide.entities.States;
import serverSide.entities.*;
import commInfra.*;

/**
 * Interface to the Kitchen.
 * <p>
 * It is responsible to validate and process the incoming message, execute the corresponding method on the
 * Kitchen and generate the outgoing message.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class KitchenInterface {
    /**
     * Reference to the kitchen.
     */

    private final Kitchen kitchen;

    /**
     * Instantiation of an interface to the kitchen.
     *
     * @param kitchen reference to the kitchen
     */

    public KitchenInterface(Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    /**
     * Processing of the incoming messages.
     * <p>
     * Validation, execution of the corresponding method and generation of the outgoing message.
     *
     * @param inMessage service request
     * @return service reply
     * @throws MessageException the incoming message is not valid
     */


    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                                     // outgoing message

        /* Validation of the incoming message */
        switch(inMessage.getMsgType())
        {
            // Chef Messages that require type and state verification
            case WTNREQ: 		// Watching the news request
            case SPREQ: 			// Start preparation of a course request
            case PPREQ: 		// Proceed to presentation request
            case HNPRREQ:	// Have next portion ready
            case CPREQ: 		// Continue preparation
            case CUREQ: 		// Clean up
                if ((inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal()) || (inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal()))
                    throw new MessageException ("Invalid Chef state!", inMessage);
                break;

            // Chef Messages that require only type verification
            case HAPBDREQ: 		// Have all portions been delivered
            case HTOBCREQ: 		// Has the order been completed
            case KSREQ:		//Kitchen shutdown
                break;

            // Waiter Messages that require type and state verification
            case HNTCREQ: 	// Hand note to chef
            case RBREQ: 	// Return to bar
            case CPOREQ: 		// Collect portion
                if(inMessage.getWaiterState().ordinal() < States.APPRAISING_SITUATION.ordinal() || inMessage.getWaiterState().ordinal() > States.RECEIVING_PAYMENT.ordinal())
                    throw new MessageException ("Invalid Waiter state!", inMessage);
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        switch (inMessage.getMsgType()) {
            case WTNREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kitchen.watch_news();
                outMessage = new Message(MessageType.WTNDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState().ordinal());
                break;

            case SPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kitchen.start_preparation();
                outMessage = new Message(MessageType.SPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState().ordinal());
                break;

            case PPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kitchen.proceed_preparation();
                break;

            case HNPRREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kitchen.have_next_portion_ready();
                outMessage = new Message(MessageType.HNPRDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState().ordinal());

                break;

            case CPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kitchen.continue_preparation();
                outMessage = new Message(MessageType.CPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState().ordinal());

                break;

            case HAPBDREQ:
                boolean portionsBeenDelivered = kitchen.have_all_portions_been_delivered();
                outMessage = new Message(MessageType.HAPBDDONE, portionsBeenDelivered);

                break;

            case HTOBCREQ:
                boolean orderBeenCompleted = kitchen.has_the_order_been_completed();
                outMessage = new Message(MessageType.HTOBCDONE, orderBeenCompleted);

                break;

            case CUREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kitchen.clean_up();
                outMessage = new Message(MessageType.CUDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState().ordinal());

                break;

            case RBREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                kitchen.return_to_bar();
                outMessage = new Message(MessageType.RBDONE, ((KitchenClientProxy) Thread.currentThread()).getWaiterState().ordinal());

                break;

            case HNTCREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                kitchen.hand_note_to_the_chef();
                outMessage = new Message(MessageType.HNTCDONE, ((KitchenClientProxy) Thread.currentThread()).getWaiterState().ordinal());
                break;

            case CPOREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                kitchen.collectPortion();
                outMessage = new Message(MessageType.CPODONE, ((KitchenClientProxy) Thread.currentThread()).getWaiterState().ordinal());

                break;

            case KSREQ:
                kitchen.shutdown();
                outMessage = new Message(MessageType.KSDONE);
                break;
        }

        return (outMessage);
    }

}