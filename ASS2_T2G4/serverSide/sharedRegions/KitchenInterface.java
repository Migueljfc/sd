package serverSide.sharedRegions;

import serverSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Kitchen.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Kitchen and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class KitchenInterface {
    /**
     *  Reference to the kitchen.
     */

    private final Kitchen kitchen;

    /**
     *  Instantiation of an interface to the kitchen.
     *
     *    @param kitchen reference to the kitchen
     */

    public KitchenInterface (Kitchen kitchen) {
        this.kitchen = kitchen;
    }

    /**
     *  Processing of the incoming messages.
     *
     *  Validation, execution of the corresponding method and generation of the outgoing message.
     *
     *    @param inMessage service request
     *    @return service reply
     *    @throws MessageException if the incoming message is not valid
     */


    public Message processAndReply (Message inMessage) throws MessageException {
        Message outMessage = null;                                     // outgoing message


        switch(inMessage.getMsgType()) {
            case MessageType.WTNREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.watch_news();) {
                    outMessage = new Message(MessageType.WTNDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getChefState());
                }
                break;

            case MessageType.SPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.start_preparation();) {
                    outMessage= new Message(MessageType.SPDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getChefState());
                }
                break;

            case MessageType.PPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.proceed_preparation();) {
                    outMessage= new Message(MessageType.PPDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getChefState());
                }
                break;

            case MessageType.HNPRREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.have_next_portion_ready();) {
                    outMessage= new Message (MessageType.HNPRDONE,
                            ((KitchenClientProxy) Thread.currentThread ()).getChefState());
                }
                break;

            case MessageType.CPREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.continue_preparation();) {
                    outMessage= new Message (MessageType.CPDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getChefState());
                }
                break;

            case MessageType.HAPBDREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.have_all_portions_been_delivered()) {
                    outMessage= new Message(MessageType.HAPBDDONE,
                            ((KitchenClientProxy) Thread.currentThread ()).getChefState());
                }
                break;

            case MessageType.HTOBCREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.has_the_order_been_completed ()) {
                    outMessage= new Message (MessageType.HTOBCDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getChefState());
                }
                break;

            case MessageType.CUREQ:
                ((KitchenClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                if(kitchen.clean_up();) {
                    outMessage= new Message (MessageType.CUDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getChefState());
                }
                break;

            case MessageType.RBREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(kitchen.return_to_bar();) {
                    outMessage= new Message (MessageType.RBDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getWaiterState());
                }
                break;

            case MessageType.HNTCREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(kitchen.hand_note_to_the_chef();) {
                    outMessage= new Message (MessageType.HNTCDONE,
                            ((KitchenClientProxy) Thread.currentThread()).getWaiterState());
                }
                break;

            case MessageType.CPOREQ:
                ((KitchenClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(kitchen.collectPortion();) {
                    outMessage= new Message (MessageType.CPODONE,
                            ((KitchenClientProxy) Thread.currentThread()).getWaiterState());
                }
                break;

            case MessageType.SHUT:
                kitchen.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }

}