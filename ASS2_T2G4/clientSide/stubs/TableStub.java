package clientSide.stubs;

import clientSide.entities.States;
import clientSide.entities.Student;
import clientSide.entities.Waiter;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;
import serverSide.sharedRegions.Table;

/**
 * Stub to the table
 */

public class TableStub {
    /**
     * Name of the platform where is located the table server.
     */

    private String serverHostName;

    /**
     * Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     * Instantiation of a stub to the table.
     *
     * @param serverHostName name of the platform where is located the table server
     * @param serverPortNumb port number for listening to service requests
     */
    public TableStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Part of the waiter lifecycle is called when a student enter the restaurant
     * @param id current student id
     */
    public void salute_client(int id) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.SCREQ, id, ((Waiter) Thread.currentThread()).getWaiterState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.SCDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getWaiterState() != States.PRESENTING_THE_MENU) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != Table.currentStudent) {                         //VAI DAR MERDA VAI DAR MERDA
            GenericIO.writelnString("Thread Student"+inMessage.getStudentId()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();
    }

    /**
     * Part of the waiter lifecycle is called when we return to bar
     */
    public void return_to_bar() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.RBREQ, ((Waiter) Thread.currentThread()).getWaiterState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.RBDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getWaiterState() != States.APPRAISING_SITUATION) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();
    }

    /**
     * Part of the waiter lifecycle is called when the first student intent to describe the order
     */
    public void get_the_pad() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.GTPREQ, ((Waiter) Thread.currentThread()).getWaiterState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.GTPDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getWaiterState() != States.TAKING_THE_ORDER) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();
    }

    /**
     * Part of the waiter lifecycle is called to check if all portions have been delivered
     *
     * @return true if all portions been delivered
     */
    public boolean have_all_portions_delivered() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.HAPDREQ, ((Waiter) Thread.currentThread()).getWaiterState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.HAPDDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();

        return (inMessage.getMsgType() == MessageType.RBDONE);
    }

    /**
     * Part of the waiter lifecycle is used to signal that a portion have been delivered
     */
    public void deliver_portion() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.DPREQ, ((Waiter) Thread.currentThread()).getWaiterState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.DPDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();
    }

    /**
     * Part of the waiter lifecycle is used present the bill and signal the last student to pay
     */
    public void present_the_bill() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.PREBREQ, ((Waiter) Thread.currentThread()).getWaiterState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.PREBDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getWaiterState() != States.RECEIVING_PAYMENT) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Waiter State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Waiter) Thread.currentThread()).setWaiterState(inMessage.getWaiterState());
        com.close();
    }

    /**
     * Called when a student enter in the bar to register the position in the table and to wait by the waiter to present the menu
     */
    public void seat() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.SATREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.SATDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.TAKING_A_SEAT_AT_THE_TABLE) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the student lifecycle used to update the student state and update the read array to notify that the student already read the menu
     */
    public void read_menu() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.RMREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.RMDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.SELECTING_THE_COURSES) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the 1º student lifecycle to update his state and signal that is organizing the order
     */
    public void prepare_the_order() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.POREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.PODONE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.ORGANIZING_THE_ORDER) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the 1º student lifecycle to check if the others students chosen their orders
     *
     * @return true if the others students already chosen their orders
     */
    public boolean has_everybody_chosen() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.HECREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.HECDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();

        return (inMessage.getMsgType() == MessageType.HECDONE);
    }

    /**
     * Part of the 1º student lifecycle to update the number os orders and notify the other students of that
     */
    public void add_up_ones_choice() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }

        outMessage = new Message(MessageType.AUOCREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if ((inMessage.getMsgType() != MessageType.AUOCDONE)) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the 1º student lifecycle to wake up the waiter and describe the order
     */
    public void describe_the_order() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.DOREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.DODONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the 1º student lifecycle to join the talk with the other students
     */
    public void join_the_talk() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.JTREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.JTDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.CHATING_WITH_COMPANIONS) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the students' lifecycle to inform the 1º student about his course option
     */
    public void inform_companion() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.ICREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.ICDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.CHATING_WITH_COMPANIONS) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the students' lifecycle to check if all courses have been delivered or not
     *
     * @return true if all courses have been delivered
     */
    public boolean have_all_courses_delivery() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.HACDREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.HACDDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();

        return (inMessage.getMsgType() == MessageType.HACDDONE);
    }

    /**
     * Operation start eating
     * <p>
     * Part of the students' lifecycle to start eating and update his state, for simulate that is used the function sleep
     */
    public void start_eating() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.SEREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.SEDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.ENJOYING_THE_MEAL) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the student lifecycle to update his state and signal that he end his course and register last student to eat
     */
    public void end_eating() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.EEREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.EEDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.CHATING_WITH_COMPANIONS) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     * Part of the student lifecycle to wait for the last student to finish his course
     */
    public boolean has_everybody_finished() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        outMessage = new Message(MessageType.HEFREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if((inMessage.getMsgType() != MessageType.HEFDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();

        return (inMessage.getMsgType() == MessageType.HEFDONE);
    }

    /**
     * Part of the student lifecycle to check if he is the last to arrive and change his state to pay the bill
     *
     * @return True if student was the last to arrive
     */
    public boolean should_have_arrived_earlier() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.SHAEREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.SHAEDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentId() != ((Student) Thread.currentThread()).getStudentId()) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student ID!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        if(inMessage.getStudentState() != States.PAYING_THE_BILL) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Student State!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();

        return (inMessage.getMsgType() == MessageType.SHAEDONE);
    }

    /**
     * Part of the student lifecycle to wait for the waiter to give him the bill
     */
    public void honor_the_bill() {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.currentThread().sleep((long)(10));
            } catch(InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.HBREQ, ((Student) Thread.currentThread()).getStudentId(), ((Student) Thread.currentThread()).getStudentState());

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        //TODO Message Types - enter
        if((inMessage.getMsgType() != MessageType.HBDONE)) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid Message Type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }

        ((Student) Thread.currentThread()).setStudentState(inMessage.getStudentState());
        com.close();
    }

    /**
     *   Operation server shutdown.
     *
     *   New operation.
     */
    public void shutdown(){
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open ()) {
            try {
                Thread.sleep((long) (1000));
            }
            catch (InterruptedException e) {}
        }

        //MESSAGES
        outMessage = new Message(MessageType.SHUT);

        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();

        if (inMessage.getMsgType() != MessageType.SHUTDONE) {
            GenericIO.writelnString("Thread "+Thread.currentThread().getName()+": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }

}