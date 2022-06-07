package serverSide.sharedRegions;

import clientSide.entities.States;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.BarClientProxy;


/**
 * @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Interface to the Bar
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Bar and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class BarInterface {
    /**
     * Reference to the Bar
     */
    private final Bar bar;


    /**
     * Instantiation of an interface to the Bar.
     * 	@param bar reference to the bar
     */
    public BarInterface(Bar bar)
    {
        this.bar = bar;
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
            case MessageType.ALREQ:
                if (inMessage.getChefState() < States.WAIT_FOR_AN_ORDER || inMessage.getChefState() > States.CLOSING_SERVICE)
                    throw new MessageException ("Invalid Chef state!", inMessage);
                break;

            //Waiter Messages that require only type verification
            case MessageType.LAREQ:
            case MessageType.BSREQ:
                break;
            // Waiter Messages that require type and state verification
            case MessageType.PBREQ:
            case MessageType.SGREQ:
                if (inMessage.getWaiterState() < States.APPRAISING_SITUATION || inMessage.getWaiterState() > States.RECEIVING_PAYMENT)
                    throw new MessageException("Inavlid Waiter state!", inMessage);
                break;

            //Student Messages that require only type and id verification
            case MessageType.CWREQ:
                break;
            // Student Messages that require type, state and id verification
            case MessageType.ENTREQ:
            case MessageType.SWREQ:
            case MessageType.EXITREQ:
                if (inMessage.getStudentState() < States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > States.GOING_HOME)
                    throw new MessageException("Invalid Student state!", inMessage);
                break;

            case MessageType.GCSREQ:
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */

        switch(inMessage.getMsgType())
        {
            case MessageType.ALREQ:
                ((BarClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                bar.alert_the_waiter();
                outMessage = new Message(MessageType.ALDONE, ((BarClientProxy) Thread.currentThread()).getChefState());
                break;
            case MessageType.LAREQ:
                int c = bar.look_around();
                outMessage = new Message(MessageType.LADONE, c, "");
                break;
            case MessageType.PBREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                bar.prepare_the_bill();
                outMessage = new Message(MessageType.PBDONE, ((BarClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.SGREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                boolean b = bar.say_goodbye();
                outMessage = new Message(MessageType.SGDONE, b);
                break;
            case MessageType.ENTREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.enter();
                outMessage = new Message(MessageType.ENTDONE, ((BarClientProxy) Thread.currentThread()).getStudentId(), ((BarClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.CWREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                bar.call_the_waiter();
                outMessage = new Message(MessageType.CWDONE, ((BarClientProxy) Thread.currentThread()).getStudentId());
                break;
            case MessageType.SWREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.signal_the_waiter();
                outMessage = new Message(MessageType.SWDONE, ((BarClientProxy) Thread.currentThread()).getStudentId(), ((BarClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.EXITREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                bar.exit();
                outMessage = new Message(MessageType.EXITDONE, ((BarClientProxy) Thread.currentThread()).getStudentId(), ((BarClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.GCSREQ:
                int id = bar.getCurrentStudent();
                outMessage = new Message(MessageType.GCSDONE, id);
                break;
            case MessageType.BSREQ:
                bar.shutdown();
                outMessage = new Message(MessageType.BSDONE);
                break;
        }

        return (outMessage);
    }
}
