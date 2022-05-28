package clientSide.stubs;
import clientSide.entities.States;
import commInfra.ClientCom;
import commInfra.Message;
import commInfra.MessageType;
import genclass.GenericIO;
import genclass.TextFile;
import serverSide.main.SimulPar;
import java.util.Objects;
/**
 * Stub to the general repository
 */
public class GeneralReposStub {
    /**
     * Name of the platform where is located the general repository server.
     */
    private String serverHostName;
    /**
     * Port number for listening to service requests.
     */
    private int serverPortNumb;
    /**
     * Instantiation of a stub to the general repository.
     *
     * @param serverHostName name of the platform where is located the general repository server
     * @param serverPortNumb port number for listening to service requests
     */
    public GeneralReposStub(String serverHostName, int serverPortNumb) {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }

    /**
     * Set Chef state.
     *
     * @param chefState chef state
     */
    public void setChefState(States chefState) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message
        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STCST, chefState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }
    /**
     * Set Waiter state.
     *
     * @param waiterState waiter state
     */
    public void setWaiterState(States waiterState) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        { try
        { Thread.sleep ((long) (1000));
        }
        catch (InterruptedException e) {}
        }
        outMessage = new Message (MessageType.STWST, 1, waiterState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        { GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();
    }

    /**
     * Set Student state.
     *
     * @param id           student id
     * @param studentState student state
     */
    public void setStudentState(int id, States studentState) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;
        com = new ClientCom (serverHostName, serverPortNumb);
        while (!com.open ())
        {
            try {
                Thread.sleep ((long) (1000));
            }
            catch (InterruptedException e) {}
        }

        outMessage = new Message (MessageType.STSST, id, studentState);
        com.writeObject (outMessage);
        inMessage = (Message) com.readObject ();
        if (inMessage.getMsgType() != MessageType.SACK)
        {
            GenericIO.writelnString ("Thread " + Thread.currentThread ().getName () + ": Invalid message type!");
            GenericIO.writelnString (inMessage.toString ());
            System.exit (1);
        }
        com.close ();

    }


    /**
     * Operation initialization of the simulation.
     *
     * @param fileName logging file name
     */
    public void initSimul(String fileName) {
        ClientCom com;                                                 // communication channel
        Message outMessage,                                            // outgoing message
                inMessage;                                             // incoming message

        com = new ClientCom(serverHostName, serverPortNumb);
        while (!com.open()) {
            try {
                Thread.sleep((long) (1000));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.SETNFIC, fileName);
        com.writeObject(outMessage);
        inMessage = (Message) com.readObject();
        if (inMessage.getMsgType() != MessageType.NFICDONE) {
            GenericIO.writelnString("Thread " + Thread.currentThread().getName() + ": Invalid message type!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
        com.close();
    }



    /**
     *   Set who is seated at the table.
     *
     *     @param id student id
     *     @param seat of the student at the restaurant
     */
    public synchronized void setStudentSeat(int seat, int id) {

    }

    /**
     *   Set number of portions.
     *
     *     @param portionsDelivery number of portions delivered
     */
    public void setPortions(int portionsDelivery) {


    }

    /**
     *   Set number of Courses.
     *
     *     @param i number of courses
     */
    public void setCourses(int i) {

    }

    /**
     * @param id the firstStudent to set
     */
    public void setFirstStudent(int id) {

    }

    /**
     * @param id the lastStudent to set
     */
    public void setLastStudent(int id) {

    }

    /**
     * @param id student id
     */
    public int getStudentSeat(int id) {

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

