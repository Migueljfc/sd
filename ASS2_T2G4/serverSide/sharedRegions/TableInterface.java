package serverSide.sharedRegions;

import serverSide.entities.*;
import commInfra.*;

/**
 *  Interface to the Table.
 *
 *    It is responsible to validate and process the incoming message, execute the corresponding method on the
 *    Table and generate the outgoing message.
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class TableInterface {

    /**
     *  Reference to the table.
     */
    private final Table table;

    /**
     *  Instantiation of an interface to the table.
     *
     *    @param table reference to the kitchen
     */
    public TableInterface (Table table){
        this.table = table;
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


    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null; // mensagem de resposta


        switch(inMessage.getMsgType()) {
            case MessageType.REQSC:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if (table.saluteClient()) {
                    outMessage = new Message(MessageType.SCDONE,
                            ((TableClientProxy) Thread.currentThread()).getWaiterState());
                }
                break;

            case MessageType.REQRB:
                ((TableClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if (table.returnBar()) {
                    outMessage = new Message(MessageType.RBDONE,
                            ((TableClientProxy) Thread.currentThread()).getWaiterState());
                }
                break;



            case MessageType.SHUT:
                table.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }

}
