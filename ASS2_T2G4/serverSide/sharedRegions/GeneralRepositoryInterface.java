package serverSide.sharedRegions;

import clientSide.entities.States;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;

import serverSide.entities.GeneralRepositoryClientProxy;


/**
 * @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary  Interface to the General Repository of Information
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class GeneralRepositoryInterface {
    /**
     * Reference to the General Repos
     */
    private final GeneralRepository repos;

    /**
     * Instantiation of an interface to the General Repos.
     * 	@param repos reference to the General Repository
     */
    public GeneralRepositoryInterface(GeneralRepository repos) {
        this.repos = repos;
    }

    /**
     * Processing of the incoming messages
     * Validation, execution of the corresponding method and generation of the outgoing message.
     *
     * 	@param inMessage service request
     * 	@return service reply
     * 	@throws MessageException if incoming message was not valid
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        //outGoing message
        Message outMessage = null;

        /* Validation of the incoming message */

        switch(inMessage.getMsgType())
        {
            // verify Chef state
            case MessageType.STCST:
                if (inMessage.getChefState() < States.WAIT_FOR_AN_ORDER || inMessage.getChefState() > States.CLOSING_SERVICE)
                    throw new MessageException ("Invalid Chef state!", inMessage);
                break;
            // verify Waiter state
            case MessageType.STWST:
                if (inMessage.getWaiterState() < States.APPRAISING_SITUATION || inMessage.getWaiterState() > States.RECEIVING_PAYMENT)
                    throw new MessageException("Invalid Waiter state!", inMessage);
                break;
            // verify Student state
            case MessageType.STSST1:
            case MessageType.STSST2:
                if (inMessage.getStudentState() < States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() > States.GOING_HOME)
                    throw new MessageException("Invalid Student state!", inMessage);
                break;
            // verify only message type
            case MessageType.STCOR:
            case MessageType.STPOR:
            case MessageType.STSS:
            case MessageType.STSSWE:
            case MessageType.GRSREQ:
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        /* Processing of the incoming message */

        switch(inMessage.getMsgType())
        {
            case MessageType.STCST:
                repos.setChefState(inMessage.getChefState());
                outMessage = new Message(MessageType.CSTDONE);
                break;
            case MessageType.STWST:
                repos.setWaiterState(inMessage.getWaiterState());
                outMessage = new Message(MessageType.WSDONE);
                break;
            case MessageType.STSST1:
            case MessageType.STSST2:
                if (inMessage.getMsgType() == MessageType.STSST1) {
                    repos.updateStudentState(inMessage.getStudentId(), inMessage.getStudentState());
                    outMessage = new Message(MessageType.SST1DONE);
                    break;
                } else {
                    repos.updateStudentState(inMessage.getStudentId(), inMessage.getStudentState(), inMessage.getHold());
                    outMessage = new Message(MessageType.SST2DONE);
                }
                break;
            case MessageType.STCOR:
                ((GeneralRepositoryClientProxy) Thread.currentThread()).setValue(inMessage.getNCourses());
                repos.setnCourses(((GeneralRepositoryClientProxy) Thread.currentThread()).getValue());
                outMessage = new Message(MessageType.CORDONE);
                break;
            case MessageType.STPOR:
                ((GeneralRepositoryClientProxy) Thread.currentThread()).setValue(inMessage.getNPortions());
                repos.setnPortions(((GeneralRepositoryClientProxy) Thread.currentThread()).getValue());
                outMessage = new Message(MessageType.PORDONE);
                break;
            case MessageType.STSS:
                repos.updateSeatsAtTable(inMessage.getSeatAtTable(), inMessage.getStudentId());
                outMessage = new Message(MessageType.SSDONE);
                break;
            case MessageType.STSSWE:
                repos.updateSeatsAtTable(inMessage.getStudentId(), -1);
                outMessage = new Message(MessageType.SSWEDONE);
                break;
            case MessageType.GRSREQ:
                repos.shutdown();
                outMessage = new Message(MessageType.GRSDONE);
                break;
        }
        return (outMessage);
    }

}
