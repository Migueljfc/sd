package serverSide.sharedRegions;

import commInfra.*;

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
            case MessageType.SETNFIC:
                repos.initSimul(inMessage.getLogFName(), inMessage.getNIter());
                outMessage = new Message(MessageType.NFICDONE);
                break;

            case MessageType.STSST:
                repos.setBarberState(inMessage.getStudentID(), inMessage.getStudentState());
                outMessage = new Message(MessageType.SACK);
                break;

            case MessageType.STCST:
                repos.setChefState(inMessage.getChefID(), inMessage.getCustState());
                outMessage = new Message(MessageType.SACK);
                break;
            case MessageType.STWST:
                repos.setWaiterState(inMessage.getWaiterID(), inMessage.getWaiterState());
                outMessage = new Message (MessageType.SACK);
                break;

            case MessageType.SHUT:
                repos.shutdown();
                outMessage = new Message (MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }

}