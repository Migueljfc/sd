package serverSide.sharedRegions;

import clientSide.entities.States;
import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
import serverSide.entities.GeneralRepositoryClientProxy;

/**
 *  Interface to the General Repository of Information.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    General Repository and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralRepositoryInterface {
    /**
     *  Reference to the general repository.
     */

    private final GeneralRepository repos;

    /**
     *  Instantiation of an interface to the general repository.
     *
     *    @param repos reference to the general repository
     */

    public GeneralRepositoryInterface (GeneralRepository repos) {
        this.repos = repos;
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
        Message outMessage = null;

        /* Validation of the incoming message */

        switch(inMessage.getMsgType())
        {
            // verify Chef state
            case STCST:
                if (inMessage.getChefState() != States.WAIT_FOR_AN_ORDER || inMessage.getChefState() != States.CLOSING_SERVICE)
                    throw new MessageException ("Invalid Chef state!", inMessage);
                break;
            // verify Waiter state
            case STWST:
                if (inMessage.getWaiterState() != States.APPRAISING_SITUATION || inMessage.getWaiterState() != States.RECEIVING_PAYMENT)
                    throw new MessageException("Invalid Waiter state!", inMessage);
                break;
            // verify Student state
            case STSST:
                if (inMessage.getStudentState() != States.GOING_TO_THE_RESTAURANT || inMessage.getStudentState() != States.GOING_HOME)
                    throw new MessageException("Invalid Student state!", inMessage);
                break;
            // verify only message type
            case STCOR:
            case STPOR:
            case SETNFIC:
            case STFS:
            case STLS:
            case GLSREQ:
            case GFSREQ:
            case SHUT:
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        switch (inMessage.getMsgType ()) {
            case SETNFIC:
                repos.initSimulation(inMessage.getLogFName());
                outMessage = new Message(MessageType.NFICDONE);
                break;

            case STSST:
                repos.setStudentState(inMessage.getStudentId(), inMessage.getStudentState());
                outMessage = new Message(MessageType.SACK);
                break;

            case STCST:
                repos.setChefState(inMessage.getChefState());
                outMessage = new Message(MessageType.SACK);
                break;

            case STWST:
                repos.setWaiterState(inMessage.getWaiterState());
                outMessage = new Message(MessageType.SACK);
                break;
            case STFS:
                repos.setFirstStudent(inMessage.getStudentId());
                outMessage = new Message(MessageType.SACK);
                break;
            case STLS:
                repos.setLastStudent(inMessage.getStudentId());
                outMessage = new Message(MessageType.SACK);
                break;
            case STSS:
                repos.setStudentSeat(Bar.studentCount,inMessage.getStudentId());  //VAI DAR MERDA
                outMessage = new Message(MessageType.SACK);
                break;
            case GLSREQ:
                ((GeneralRepositoryClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((GeneralRepositoryClientProxy) Thread.currentThread ()).setStudentState(inMessage.getStudentState());
                repos.getLastStudent();
                outMessage = new Message(MessageType.GLSDONE,
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentId(),
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentState());
                break;
            case GFSREQ:
                ((GeneralRepositoryClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((GeneralRepositoryClientProxy) Thread.currentThread ()).setStudentState(inMessage.getStudentState());
                repos.getFirstStudent();
                outMessage = new Message(MessageType.GFSDONE,
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentId(),
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentState());
                break;
            case STPOR:
                repos.setPortions(Kitchen.portionsDelivery);  //VAI DAR MERDA
                outMessage = new Message(MessageType.SACK);
                break;
            case STCOR:
                repos.setCourses(Kitchen.coursesDelivery);  //VAI DAR MERDA
                outMessage = new Message(MessageType.SACK);
                break;

            case SHUT:
                repos.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;

        }

        return (outMessage);
    }

}