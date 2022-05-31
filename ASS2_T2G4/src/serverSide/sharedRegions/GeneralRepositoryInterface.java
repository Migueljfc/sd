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
                if (inMessage.getChefState().ordinal() < States.WAIT_FOR_AN_ORDER.ordinal() || inMessage.getChefState().ordinal() > States.CLOSING_SERVICE.ordinal()) {
                    System.out.println("############## FOI AQUI QUE DEU ERROOOOOOO ################\n");
                    throw new MessageException ("Invalid Chef state!", inMessage);
                }
                break;
            // verify Waiter state
            case STWST:
                if (inMessage.getWaiterState().ordinal() < States.APPRAISING_SITUATION .ordinal() || inMessage.getWaiterState().ordinal() > States.RECEIVING_PAYMENT.ordinal())
                    throw new MessageException("Invalid Waiter state!", inMessage);
                break;
            // verify Student state
            case STSST1:
            case STSST2:
                if (inMessage.getStudentState().ordinal() < States.GOING_TO_THE_RESTAURANT.ordinal() || inMessage.getStudentState().ordinal() > States.GOING_HOME.ordinal())
                    throw new MessageException("Invalid Student state!", inMessage);
                break;
            // verify only message type
            case GCSREQ:
            case STCOR:
            case STPOR:
            case SETNFIC:
            case STFS:
            case STLS:
            case STSS:
            case GLSREQ:
            case GFSREQ:
            case GRSREQ:
            case STSSWE:
                break;
            default:
                throw new MessageException ("Invalid message type!", inMessage);
        }

        switch (inMessage.getMsgType ()) {
            //case SETNFIC:
                //repos.initSimulation(inMessage.getLogFName());
                //outMessage = new Message(MessageType.NFICDONE);
                //break;

            case STSST1:
            case STSST2:
                if (inMessage.getMsgType() == MessageType.STSST1) {
                    repos.setStudentState(inMessage.getStudentId(), inMessage.getStudentState());
                    outMessage = new Message(MessageType.SST1DONE);
                    break;
                } else {
                    repos.setStudentState(inMessage.getStudentId(), inMessage.getStudentState(), inMessage.getPrint());
                    outMessage = new Message(MessageType.SST2DONE);
                }
                break;

            case STCST:
                repos.setChefState(inMessage.getChefState());
                outMessage = new Message(MessageType.CSTDONE);
                break;

            case STWST:
                repos.setWaiterState(inMessage.getWaiterState());
                outMessage = new Message(MessageType.WSDONE);
                break;
            case STFS:
                repos.setFirstStudent(inMessage.getStudentId());
                outMessage = new Message(MessageType.FSDONE);
                break;
            case STLS:
                repos.setLastStudent(inMessage.getStudentId());
                outMessage = new Message(MessageType.LSDONE);
                break;
            case STSS:
                repos.setStudentSeat(Bar.studentCount,inMessage.getStudentId());
                outMessage = new Message(MessageType.SSDONE);
                break;
            case GLSREQ:
                ((GeneralRepositoryClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((GeneralRepositoryClientProxy) Thread.currentThread ()).setStudentState(inMessage.getStudentState());
                repos.getLastStudent();
                outMessage = new Message(MessageType.GLSDONE,
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentId(),
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentState().ordinal());
                break;
            case GFSREQ:
                ((GeneralRepositoryClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((GeneralRepositoryClientProxy) Thread.currentThread ()).setStudentState(inMessage.getStudentState());
                repos.getFirstStudent();
                outMessage = new Message(MessageType.GFSDONE,
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentId(),
                        ((GeneralRepositoryClientProxy) Thread.currentThread()).getStudentState().ordinal());
                break;
            case STPOR:
                repos.setPortions(Kitchen.portionsDelivery);
                outMessage = new Message(MessageType.PORDONE);
                break;
            case STCOR:
                repos.setCourses(Kitchen.coursesDelivery);
                outMessage = new Message(MessageType.CORDONE);
                break;

            case GRSREQ:
                repos.shutdown();
                outMessage = new Message(MessageType.GRSDONE);
                break;
            case STSSWE:
                repos.setSeatAtLeaving(inMessage.getStudentId());
                outMessage = new Message(MessageType.SSWEDONE);
                break;
        }
        return (outMessage);
    }

}