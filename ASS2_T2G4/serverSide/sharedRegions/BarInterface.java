package serverSide.sharedRegions;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;
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

    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;


        switch(inMessage.getMsgType()) {
            case ENTREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.enter();
                outMessage = new Message(MessageType.ENTDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState());
                break;

            case CWREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.call_the_waiter();
                outMessage = new Message(MessageType.CWDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState());
                //nao sei se falta alguma coisa
                break;

            case SWREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.signal_the_waiter();
                outMessage = new Message(MessageType.SWDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState());
                //nao sei se falta alguma coisa
                break;

            case EXITREQ:
                ((BarClientProxy) Thread.currentThread()).setStudentId(inMessage.getStudentId());
                ((BarClientProxy) Thread.currentThread()).setStudentState(inMessage.getStudentState());
                bar.exit();
                outMessage = new Message(MessageType.EXITDONE,
                        ((BarClientProxy) Thread.currentThread()).getStudentId(),
                        ((BarClientProxy) Thread.currentThread()).getStudentState());
                //nao sei se falta alguma coisa
                break;

            case LAREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                int request = bar.look_arround();
                outMessage = new Message(MessageType.LADONE,
                        ((BarClientProxy)Thread.currentThread()).getWaiterState(), request);
                //nao sei se falta alguma coisa
                break;

            case SGREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                if(bar.say_goodbye()) {
                    outMessage = new Message(MessageType.SGDONE,
                            ((BarClientProxy) Thread.currentThread()).getWaiterState());
                }
                //nao sei se falta alguma coisa
                break;

            case PBREQ:
                ((BarClientProxy) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
                bar.prepare_the_bill();
                outMessage = new Message(MessageType.PBDONE,
                        ((BarClientProxy) Thread.currentThread()).getWaiterState());
                //nao sei se falta alguma coisa
                break;

            case ALREQ:
                ((BarClientProxy) Thread.currentThread()).setChefState(inMessage.getChefState());
                bar.alert_the_waiter();
                outMessage = new Message(MessageType.ALDONE,
                        ((BarClientProxy) Thread.currentThread()).getChefState());
                //nao sei se falta alguma coisa
                break;

            case SHUT:
                bar.shutdown();
                outMessage = new Message(MessageType.SHUTDONE);
                break;
        }

        return (outMessage);
    }
}
