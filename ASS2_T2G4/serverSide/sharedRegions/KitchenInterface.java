package serverSide.sharedRegions;

import serverSide.entities.*;
import clientSide.entities.*;
import commInfra.*;

/**
 * @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Interface to the Kitchen
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Kitchen and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class KitchenInterface {

    /**
     * Reference to the kitchen
     */
    private final Kitchen kit;


    /**
     * Instantiation of an interface to the kitchen.
     * 	@param kit reference to the kitchen
     */
    public KitchenInterface(Kitchen kit)
    {
        this.kit = kit;
    }


    /**
     * Processing of the incoming messages
     * Validation, execution of the corresponding method and generation of the outgoing message.
     *
     * 	@param inMessage service request
     * 	@return service reply
     * 	@throws MessageException if incoming message was not valid
     */
    public Message processAndReply (Message inMessage) throws MessageException
    {
        //outGoing message
        Message outMessage = null;

        /* Validation of the incoming message */
        switch(inMessage.getMsgType())
        {
            // Chef Messages that require type and state verification
            case MessageType.WTNREQ:
            case MessageType.SPREQ:
            case MessageType.PPREQ:
            case MessageType.HNPRREQ:
            case MessageType.CPREQ:
            case MessageType.CUREQ:
                if ((inMessage.getChefState() < States.WAIT_FOR_AN_ORDER) || (inMessage.getChefState() > States.CLOSING_SERVICE))
                    throw new MessageException ("Invalid Chef state!", inMessage);
                break;

            // Chef Messages that require only type verification
            case MessageType.HAPBDREQ:
            case MessageType.HTOBCREQ:
            case MessageType.KSREQ:
                break;

            // Waiter Messages that require type and state verification
            case MessageType.HNTCREQ:
            case MessageType.RBREQ:
            case MessageType.CPOREQ:
                if(inMessage.getWaiterState() < States.APPRAISING_SITUATION || inMessage.getWaiterState() > States.RECEIVING_PAYMENT)
                    throw new MessageException ("Invalid Waiter state!", inMessage);
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* Processing */
        switch(inMessage.getMsgType())
        {
            case MessageType.WTNREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kit.watch_news();
                outMessage = new Message(MessageType.WTNDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.SPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kit.start_preparation();
                outMessage = new Message(MessageType.SPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.PPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kit.proceed_preparation();
                outMessage = new Message(MessageType.PPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.HAPBDREQ:
                boolean portionsDelivered = kit.have_all_portions_been_delivered();
                outMessage = new Message(MessageType.HAPBDDONE, portionsDelivered);
                break;
            case MessageType.HTOBCREQ:
                boolean orderCompleted = kit.has_the_order_been_completed();
                outMessage = new Message(MessageType.HTOBCDONE, orderCompleted);
                break;
            case MessageType.CPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kit.continue_preparation();
                outMessage = new Message(MessageType.CPDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.HNPRREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kit.have_next_portion_ready();
                outMessage = new Message(MessageType.HNPRDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.CUREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                kit.clean_up();
                outMessage = new Message(MessageType.CUDONE, ((KitchenClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.HNTCREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                kit.hand_note_to_the_chef();
                outMessage = new Message(MessageType.HNTCDONE, ((KitchenClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.RBREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                kit.return_to_bar();
                outMessage = new Message(MessageType.RBDONE, ((KitchenClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.CPOREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                kit.collectPortion();
                outMessage = new Message(MessageType.CPODONE, ((KitchenClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.KSREQ:
                kit.shutdown();
                outMessage = new Message(MessageType.KSDONE);
                break;
        }

        return (outMessage);
    }

}
