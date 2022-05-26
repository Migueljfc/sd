package serverSide.sharedRegions;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.MessageType;

public class BarInterface {

    /**
     * Reference to Bar
     */
    private final Bar bar;

    /**
     *
     * @param bar reference to Bar
     */
    public BarInterface(Bar bar) {
        this.bar = bar;
    }

    public Message processAndReply (Message inMessage) throws MessageException{
        Message outMessage = null;
        switch (inMessage.getMsgType()) {
            case MessageType.ALREQ:
                if ((inMessage.getWaiterState() < 0) || (inMessage.getWaiterState() > 6))
                    throw new MessageException("Invalid waiter state!", inMessage);
                break;
            case MessageType.ENT:
                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
                    throw new MessageException("Invalid student state!", inMessage);
                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
                    throw new MessageException("Invalid student ID!", inMessage);
                break;
            case MessageType.CW:
                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
                    throw new MessageException("Invalid student state!", inMessage);
                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
                    throw new MessageException("Invalid student ID!", inMessage);
                break;
            case MessageType.AL:
                if ((inMessage.getChefState() < 0) || (inMessage.getChefState() > 4))
                    throw new MessageException("Invalid chef state!", inMessage);
                break;
            case MessageType.SW:
                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
                    throw new MessageException("Invalid student state!", inMessage);
                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
                    throw new MessageException("Invalid student ID!", inMessage);
                break;
            case MessageType.SHOULD_HAVE_ARRIVED_EARLIER:
                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
                    throw new MessageException("Invalid student state!", inMessage);
                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
                    throw new MessageException("Invalid student ID!", inMessage);
                break;
            case MessageType.PB:
                if ((inMessage.getWaiterState() < 0) || (inMessage.getWaiterState() > 6))
                    throw new MessageException("Invalid waiter state!", inMessage);
                break;
            case MessageType.SG:
                if ((inMessage.getWaiterState() < 0) || (inMessage.getWaiterState() > 6))
                    throw new MessageException("Invalid waiter state!", inMessage);
                break;
            case MessageType.EXIT:
                if ((inMessage.getStudentState() < 0) || (inMessage.getStudentState() > 7))
                    throw new MessageException("Invalid student state!", inMessage);
                if ((inMessage.getStudentID() < 0) || (inMessage.getStudentID() > 6))
                    throw new MessageException("Invalid student ID!", inMessage);
                break;
            case MessageType.SHUT:
                break;
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }

        // check nothing

        // processing

        switch (inMessage.getMsgType ())

        {   case MessageType.REQENT:  ((BarClientProxy) Thread.currentThread ()).setStudentId (inMessage.getStudentId ());
            ((BarClientProxy) Thread.currentThread ()).setStudentState (inMessage.getStudentState ());
            if (bar.enter ())
                outMessage = new Message (MessageType.ENTDONE,
                        ((BarClientProxy) Thread.currentThread ()).getStudentId (),
                        ((BarClientProxy) Thread.currentThread ()).getStudentState ());
            //   else outMessage = new Message (MessageType.BSHOPF,
            //                                  ((BarClientProxy) Thread.currentThread ()).getCustomerId (),
            //                                  ((BarClientProxy) Thread.currentThread ()).getCustomerState ());
            break;
            case MessageType.REQCW: ((BarClientProxy) Thread.currentThread()).setStudentId (inMessage.getStudentId ());
                ((BarClientProxy) Thread.currentThread()).setStudentId (inMessage.getStudentState ());
                if(bar.callWaiter ())
                    outMessage = new Message (MessageType.CWDONE,
                            ((BarClientProxy)Thread.currentThread())-getStudentId(),
                            ((BarClientProxy)Thread.currentThread()).setStudentState());
                //nao sei se falta alguma coisa

            case MessageType.REQEXIT: ((BarClientProxy) Thread.currentThread()).setStudentId (inMessage.getStudentId ());
                ((BarClientProxy) Thread.currentThread()).setStudentId (inMessage.getStudentState ());
                if(bar.exit ())
                    outMessage = new Message (MessageType.EXITDONE,
                            ((BarClientProxy)Thread.currentThread())-getStudentId(),
                            ((BarClientProxy)Thread.currentThread()).setStudentState());

                //nao sei se falta alguma coisa

            case MessageType.REQLA: ((BarClientProxy) Thread.currentThread()).setWaiterState (inMessage.getWaiterState ());
                if(bar.lookAround ())
                    outMessage = new Message (MessageType.LADONE,
                            ((BarClientProxy)Thread.currentThread()).setWaiterState());

                //nao sei se falta alguma coisa

            case MessageType.REQSG: ((BarClientProxy) Thread.currentThread()).setWaiterState (inMessage.getWaiterState ());
                if(bar.sayGoodbye ())
                    outMessage = new Message (MessageType.SGDONE,
                            ((BarClientProxy)Thread.currentThread()).setWaiterState());

                //nao sei se falta alguma coisa

            case MessageType.REQPB: ((BarClientProxy) Thread.currentThread()).setWaiterState (inMessage.getWaiterState ());
                if(bar.prepareTheBill ())
                    outMessage = new Message (MessageType.PBDONE,
                            ((BarClientProxy)Thread.currentThread()).setWaiterState());

                //nao sei se falta alguma coisa

            case MessageType.REQAL: ((BarClientProxy) Thread.currentThread()).setChefState (inMessage.getChefState ());
                if(bar.alertTheWaiter ())
                    outMessage = new Message (MessageType.ALDONE,
                            ((BarClientProxy)Thread.currentThread()).setChefState());

                //nao sei se falta alguma coisa

            case MessageType.SHUT:     bar.shutdown ();
                outMessage = new Message (MessageType.SHUTDONE);
                break;
        }
        return outMessage;
    }
}
