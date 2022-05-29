package serverSide.sharedRegions;

import clientSide.entities.States;
import genclass.GenericIO;
import serverSide.entities.*;
import commInfra.*;

/**
 * Interface to the Table.
 * <p>
 * It is responsible to validate and process the incoming message, execute the corresponding method on the
 * Table and generate the outgoing message.
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class TableInterface {

    /**
     * Reference to the table.
     */
    private final Table table;

    /**
     * Instantiation of an interface to the table.
     *
     * @param table reference to the kitchen
     */
    public TableInterface(Table table) {
        this.table = table;
    }

    /**
     * Processing of the incoming messages.
     * <p>
     * Validation, execution of the corresponding method and generation of the outgoing message.
     *
     * @param inMessage service request
     * @return service reply
     * @throws MessageException if the incoming message is not valid
     */


    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null; // mensagem de resposta

        /* Validation of the incoming message */

        switch(inMessage.getMsgType())
        {
            //Waiter Messages that require only type verification
            case HAPDREQ:		// Have all clients been served
            case DPREQ:				// Deliver portion
            case SHUT:			// Table shutdown
                break;
            // Waiter Messages that require type and state verification
            case SCREQ:			// Salute the clients
            case RTBREQ:			// Return to the bar
            case GTPREQ:				// Get the pad
            case PREBREQ:			// Present the bill
                if (inMessage.getWaiterState() != States.APPRAISING_SITUATION || inMessage.getWaiterState() != States.RECEIVING_PAYMENT)
                    throw new MessageException("Invalid Waiter state!", inMessage);
                break;
            //Student Messages that require only type verification
            case HECREQ:		// Has everybody chosen
            case AUOCREQ:			// Add up ones choices
            case DOREQ:			// Describe order
            case HEFREQ:	// Has everybody finished eating
            case HBREQ:			// Honour the bill
            case HACDREQ:		// Have all courses been eaten
                break;
            // Student Messages that require type, state and id verification (done in Message Constructor)
            case SATREQ:			// Seat at table
            case RMREQ:				// Read menu
            case POREQ:			// Prepare the order
            case JTREQ:			// Join the talk
            case ICREQ:			// Inform companion
            case SEREQ:			// Start eating
            case EEREQ:			// End eating
            case SHAEREQ:		// Should have arrived earlier
                if (inMessage.getStudentState() != States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() != States.GOING_HOME)
                    throw new MessageException("Inavlid Student state!", inMessage);
                break;
            //Aditional messages that require type verification
            //case MessageType.REQGETFRSTARR:			//Get first to arrive
            //case MessageType.REQGETLSTEAT:			//Get last to eat
            //case MessageType.REQSETFRSTARR:			//Set first to arrive
            //case MessageType.REQSETLSTARR:			//Set last to arrive
                //break;
            //default:
                //throw new MessageException ("Invalid message type!", inMessage);
        }

        switch (inMessage.getMsgType()) {
            case SCREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                table.salute_client(inMessage.getStudentId());
                outMessage = new Message(MessageType.SCDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());


                break;

            case RTBREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                table.return_to_bar();
                outMessage = new Message(MessageType.RTBDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());

                break;
            case GTPREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                table.get_the_pad();
                outMessage = new Message(MessageType.GTPDONE,
                        ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;

            case HAPDREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if (table.have_all_portions_delivered()) {
                    outMessage = new Message(MessageType.HAPDDONE,
                            ((TableClientProxy) Thread.currentThread()).getWaiterState());
                }
                break;

            case DPREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                table.deliver_portion();
                outMessage = new Message(MessageType.DPDONE,
                        ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;

            case PREBREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                table.present_the_bill();
                outMessage = new Message(MessageType.PREBDONE,
                        ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;



            case SATREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.seat();
                outMessage = new Message(MessageType.SATDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case RMREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.read_menu();
                outMessage = new Message(MessageType.RMDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case POREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.prepare_the_order();
                outMessage = new Message(MessageType.PODONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case HECREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                if (table.has_everybody_chosen()) {
                    outMessage = new Message(MessageType.HECDONE,
                            ((TableClientProxy) Thread.currentThread()).getStudentId(),
                            ((TableClientProxy) Thread.currentThread()).getStudentState());
                }
                break;

            case AUOCREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.add_up_ones_choice();
                outMessage = new Message(MessageType.AUOCDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case DOREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState (inMessage.getStudentState());
                table.describe_the_order();
                outMessage = new Message(MessageType.DODONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case JTREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.join_the_talk();
                outMessage = new Message(MessageType.JTDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case ICREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.inform_companion();
                outMessage = new Message(MessageType.ICDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case SEREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.start_eating();
                outMessage = new Message(MessageType.SEDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case EEREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                table.end_eating();
                outMessage = new Message(MessageType.EEDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case HEFREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                if (table.has_everybody_finished()) {
                    outMessage = new Message(MessageType.HEFDONE,
                            ((TableClientProxy) Thread.currentThread()).getStudentId(),
                            ((TableClientProxy) Thread.currentThread()).getStudentState());
                }
                break;

            case HBREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState (inMessage.getStudentState());
                table.honor_the_bill();
                outMessage = new Message(MessageType.HBDONE,
                        ((TableClientProxy) Thread.currentThread()).getStudentId(),
                        ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;

            case HACDREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                if (table.have_all_courses_delivery()) {
                    outMessage = new Message(MessageType.HACDDONE,
                            ((TableClientProxy) Thread.currentThread()).getStudentId(),
                            ((TableClientProxy) Thread.currentThread()).getStudentState());
                }
                break;

            case SHAEREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                if (table.should_have_arrived_earlier()) {
                    outMessage = new Message(MessageType.SHAEDONE,
                            ((TableClientProxy) Thread.currentThread()).getStudentId(),
                            ((TableClientProxy) Thread.currentThread()).getStudentState());
                }
                break;


            case SHUT:
                table.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }

}
