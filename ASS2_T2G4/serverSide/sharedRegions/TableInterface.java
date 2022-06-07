package serverSide.sharedRegions;

import clientSide.entities.States;
import clientSide.entities.States;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.TableClientProxy;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary  Interface to the Table
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Table and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class TableInterface {

    /**
     * Reference to the Table
     */
    private final Table tab;


    /**
     * Instantiation of an interface to the Table.
     * 	@param tab reference to the table
     */
    public TableInterface(Table tab)
    {
        this.tab = tab;
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
        // outGoing message
        Message outMessage = null;

        /* Validation of the incoming message */

        switch(inMessage.getMsgType())
        {
            //Waiter Messages that require only type verification
            case MessageType.HAPDREQ:
            case MessageType.DPREQ:
            case MessageType.TSREQ:
                break;
            // Waiter Messages that require type and state verification
            case MessageType.SCREQ:
            case MessageType.RTBREQ:
            case MessageType.GTPREQ:
            case MessageType.PREBREQ:
                if (inMessage.getWaiterState() < States.APPRAISING_SITUATION || inMessage.getWaiterState() > States.RECEIVING_PAYMENT)
                    throw new MessageException("Invalid Waiter state!", inMessage);
                break;
            //Student Messages that require only type verification
            case MessageType.HECREQ:
            case MessageType.AUOCREQ:
            case MessageType.DOREQ:
            case MessageType.HEFREQ:
            case MessageType.HBREQ:
            case MessageType.HACDREQ:
                break;
            // Student Messages that require type, state and id verification (done in Message Constructor)
            case MessageType.SATREQ:
            case MessageType.RMREQ:
            case MessageType.POREQ:
            case MessageType.JTREQ:
            case MessageType.ICREQ:
            case MessageType.SEREQ:
            case MessageType.EEREQ:
            case MessageType.SHAEREQ:
                if (inMessage.getStudentState() < States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > States.GOING_HOME)
                    throw new MessageException("Inavlid Student state!", inMessage);
                break;
            //Aditional messages that require type verification
            case MessageType.GFSREQ:
            case MessageType.GLSREQ:
            case MessageType.STFS:
            case MessageType.STLS:
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */

        switch(inMessage.getMsgType())
        {
            case MessageType.SCREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                ((TableClientProxy) Thread.currentThread()).setStudentBeingAnswered(inMessage.getStudentIdBeingAnswered());
                int i = inMessage.getStudentBeingAnswered();
                tab.salute_client(i);
                outMessage = new Message(MessageType.SCDONE,  ((TableClientProxy) Thread.currentThread()).getStudentBeingAnswered(), ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.RTBREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                tab.return_to_bar();
                outMessage = new Message(MessageType.RTBDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.GTPREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                tab.get_the_pad();
                outMessage = new Message(MessageType.GTPDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.HAPDREQ:
                boolean b = tab.have_all_portions_delivered();
                outMessage = new Message(MessageType.HAPDDONE, b);
                break;
            case MessageType.DPREQ:
                tab.deliver_portion();
                outMessage = new Message(MessageType.DPDONE);
                break;
            case MessageType.PREBREQ:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                tab.present_the_bill();
                outMessage = new Message(MessageType.PREBDONE, ((TableClientProxy) Thread.currentThread()).getWaiterState());
                break;
            case MessageType.SATREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                tab.seat();
                outMessage = new Message(MessageType.SATDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.RMREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId( inMessage.getStudentId() );
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                tab.read_menu();
                outMessage = new Message(MessageType.RMDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.POREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                tab.prepare_the_order();
                outMessage = new Message(MessageType.PODONE, ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.HECREQ:
                boolean everyBodyChose = tab.has_everybody_chosen();
                outMessage = new Message(MessageType.HECDONE, everyBodyChose);
                break;
            case MessageType.AUOCREQ:
                tab.add_up_ones_choice();
                outMessage = new Message(MessageType.AUOCDONE);
                break;
            case MessageType.DOREQ:
                tab.describe_the_order();
                outMessage = new Message(MessageType.DODONE);
                break;
            case MessageType.JTREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                tab.join_the_talk();
                outMessage = new Message(MessageType.JTDONE, ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.ICREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                tab.inform_companion();
                outMessage = new Message(MessageType.ICDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.SEREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                tab.start_eating();
                outMessage = new Message(MessageType.SEDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.EEREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                tab.end_eating();
                outMessage = new Message(MessageType.EEDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), ((TableClientProxy) Thread.currentThread()).getStudentState());
                break;
            case MessageType.HEFREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                boolean everybodyEaten = tab.has_everybody_finished();
                outMessage = new Message(MessageType.HEFDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), everybodyEaten);
                break;
            case MessageType.HBREQ:
                tab.honor_the_bill();
                outMessage = new Message(MessageType.HBDONE);
                break;
            case MessageType.HACDREQ:
                boolean haveAllCoursesBeenEaten = tab.have_all_courses_delivery();
                outMessage = new Message(MessageType.HACDDONE, haveAllCoursesBeenEaten);
                break;
            case MessageType.SHAEREQ:
                ((TableClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                ((TableClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                boolean shouldHaveArrived = tab.should_have_arrived_earlier();
                outMessage = new Message(MessageType.SHAEDONE, ((TableClientProxy) Thread.currentThread()).getStudentId(), ((TableClientProxy) Thread.currentThread()).getStudentState(), shouldHaveArrived);
                break;
            case MessageType.GFSREQ:
                int idFirst = tab.getfirstStudent();
                outMessage = new Message(MessageType.GFSDONE, idFirst);
                break;
            case MessageType.GLSREQ:
                int idLast = tab.getfirstStudent();
                outMessage = new Message(MessageType.GLSDONE, idLast);
                break;
            case MessageType.STFS:
                tab.setFirstStudent(inMessage.getFirstToArrive());
                outMessage = new Message(MessageType.FSDONE);
                break;
            case MessageType.STLS:
                tab.setLastStudent(inMessage.getLastToArrive());
                outMessage = new Message(MessageType.LSDONE);
                break;
            case MessageType.TSREQ:
                tab.shutdown();
                outMessage = new Message(MessageType.TSDONE);
                break;
        }

        return (outMessage);
    }
}
