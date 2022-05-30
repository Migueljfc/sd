package serverSide.sharedRegions;

import clientSide.entities.States;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import genclass.GenericIO;
import serverSide.entities.BarClientProxy;

public class BarInterface {

    /**
     * Reference to Bar
     */
    private final Bar bar;

    /**
     * @param bar reference to Bar
     */
    public BarInterface(Bar bar) {
        this.bar = bar;
    }

    /**
     *
     * @param inMessage service request
     * @return service reply
     * @throws MessageException Message error
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;

        /* Validation of the incoming message */

        switch(inMessage.getMsgType())
        {
            // Chef Messages that require type and state verification
            case ALREQ: 		// Alert the Waiter Request
                if (inMessage.getChefState() != States.WAIT_FOR_AN_ORDER || inMessage.getChefState() != States.CLOSING_SERVICE)
                    throw new MessageException ("Invalid Chef state!", inMessage);
                break;

            //Waiter Messages that require only type verification
            case LAREQ: 		// Look around Request
            case BSREQ:		// Bar shutdown
                break;
            // Waiter Messages that require type and state verification
            case PBREQ: 		// Prepare the bill Request
            case SGREQ: 		// Say goodbye Request
                if (inMessage.getWaiterState() != States.APPRAISING_SITUATION || inMessage.getWaiterState() != States.RECEIVING_PAYMENT)
                    throw new MessageException("Invalid Waiter state!", inMessage);
                break;

            //Student Messages that require only type and id verification (already done in Message Constructor)
            case CWREQ:		// Call the waiter Request
                break;
            // Student Messages that require type, state and id verification (done in Message Constructor)
            case ENTREQ:			// Enter Request
            case SWREQ:			// Signal the waiter Request
            case EXITREQ:			// exit Request
                if (inMessage.getStudentState() != States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() != States.GOING_HOME)
                    throw new MessageException("Invalid Student state!", inMessage);
                break;

            //Additional Messages
            //case REQGETSTDBEIANSW:
                //break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }


        switch(inMessage.getMsgType()) {
            case ENTREQ:
                GenericIO.writelnString("Tá quase a entrar");
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.enter();
                outMessage = new Message(MessageType.ENTDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState().ordinal());
                break;

            case CWREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.call_the_waiter();
                outMessage = new Message(MessageType.CWDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState().ordinal());
                break;

            case SWREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.signal_the_waiter();
                outMessage = new Message(MessageType.SWDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState().ordinal());
                break;

            case EXITREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.exit();
                outMessage = new Message(MessageType.EXITDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState().ordinal());
                break;

            case LAREQ:

                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                int request = bar.look_arround();
                outMessage = new Message(MessageType.LADONE, request, "");
                break;

            case SGREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                boolean bol = bar.say_goodbye();
                outMessage = new Message(MessageType.SGDONE,bol);

                break;

            case PBREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                bar.prepare_the_bill();
                outMessage = new Message(MessageType.PBDONE,
                        ((BarClientProxy) Thread.currentThread()).getWaiterState().ordinal());
                break;

            case ALREQ:
                ((BarClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                bar.alert_the_waiter();
                outMessage = new Message(MessageType.ALDONE,
                        ((BarClientProxy) Thread.currentThread()).getChefState().ordinal());
                break;

            case GCSREQ:
                int id = bar.getCurrentStudent();
                outMessage = new Message(MessageType.GCSDONE,id);
                break;
            case BSREQ:
                bar.shutdown();
                outMessage = new Message(MessageType.BSDONE);
                break;


        }

        return (outMessage);
    }
}
