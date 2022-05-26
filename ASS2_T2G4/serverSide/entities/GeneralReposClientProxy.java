package serverSide.entities;

import commInfra.Message;
import commInfra.MessageException;
import commInfra.ServerCom;
import genclass.GenericIO;

public class GeneralReposClientProxy extends Thread {
    /**
     * Number of instantiayed threads.
     */

    private static int nProxy = 0;

    /**
     * Communication channel.
     */

    private ServerCom sconi;

    /**
     * Interface to the General Repository of Information.
     */

    private GeneralReposInterface reposInter;

    /**
     * Instantiation of a client proxy.
     *
     * @param sconi      communication channel
     * @param reposInter interface to the general repository of information
     */

    public GeneralReposClientProxy(ServerCom sconi, GeneralReposInterface reposInter) {
        super("GeneralReposProxy_" + GeneralReposClientProxy.getProxyId());
        this.sconi = sconi;
        this.reposInter = reposInter;
    }

    /**
     * Generation of the instantiation identifier.
     *
     * @return instantiation identifier
     */

    private static int getProxyId() {
        Class<?> cl = null;                                            // representation of the GeneralReposClientProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.GeneralReposClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type GeneralReposClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }
        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }
        return proxyId;
    }

    /**
     * Life cycle of the service provider agent.
     */

    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject();                     // get service request
        try {
            outMessage = reposInter.processAndReply(inMessage);         // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread " + getName() + ": " + e.getMessage() + "!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // send service reply
        sconi.close();                                                // close the communication channel
    }
}
