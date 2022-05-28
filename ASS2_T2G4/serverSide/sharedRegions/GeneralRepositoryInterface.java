package serverSide.sharedRegions;

import commInfra.*;
import serverSide.entities.GeneralRepositoryClientProxy;
import serverSide.entities.TableClientProxy;

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
        Message outMessage = null;                                     // mensagem de resposta


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