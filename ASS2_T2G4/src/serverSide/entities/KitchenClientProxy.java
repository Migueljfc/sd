package serverSide.entities;

import serverSide.sharedRegions.*;
import clientSide.entities.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *  Service provider agent for access to the Kitchen.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class KitchenClientProxy extends Thread implements  WaiterCloning, ChefCloning  {

    /**
     *  Number of instantiayed threads.
     */

    private static int nProxy = 0;

    /**
     *  Communication channel.
     */

    private ServerCom sconi;

    /**
     *  Interface to the Kitchen.
     */

    private KitchenInterface kitchenInter;


    /**
     *  Chef state.
     */

    private States chefState;

    /**
     *  Waiter state.
     */

    private States waiterState;


    /**
     *  Instantiation of a client proxy.
     *
     *     @param sconi communication channel
     *     @param kitchenInter interface to the Kitchen
     */

    public KitchenClientProxy(ServerCom sconi, KitchenInterface kitchenInter) {
        super ("KitchenProxy_"+KitchenClientProxy.getProxyId());
        this.sconi = sconi;
        this.kitchenInter = kitchenInter;
    }

    /**
     *  Generation of the instantiation identifier.
     *
     *     @return instantiation identifier
     */

    private static int getProxyId() {
        Class<?> cl = null;                                            // representation of the KitchenProxy object in JVM
        int proxyId;                                                   // instantiation identifier

        try {
            cl = Class.forName("serverSide.entities.KitchenClientProxy");
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("Data type KitchenClientProxy was not found!");
            e.printStackTrace();
            System.exit(1);
        }

        synchronized(cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }


    /**
     *   Set waiter state.
     *
     *     @param state new waiter state
     */

    public void setWaiterState(States state) {
        waiterState = state;
    }

    /**
     *   Get waiter state.
     *
     *     @return waiter state
     */

    public States getWaiterState() {
        return waiterState;
    }

    /**
     *   Set chef state.
     *
     *     @param state new chef state
     */

    public void setChefState(States state) {
        chefState = state;
    }

    /**
     *   Get chef state.
     *
     *     @return chef state
     */

    public States getChefState() {
        return chefState;
    }
    /**
     *  Life cycle of the service provider agent.
     */

    @Override
    public void run() {
        Message inMessage = null,                                      // service request
                outMessage = null;                                     // service reply

        /* service providing */

        inMessage = (Message) sconi.readObject();                     // get service request
        try {
            outMessage = kitchenInter.processAndReply(inMessage);         // process it
        } catch (MessageException e) {
            GenericIO.writelnString("Thread "+getName()+": "+e.getMessage()+"!");
            GenericIO.writelnString(e.getMessageVal().toString());
            System.exit(1);
        }

        sconi.writeObject(outMessage);                                // send service reply
        sconi.close();                                                // close the communication channel
    }
}