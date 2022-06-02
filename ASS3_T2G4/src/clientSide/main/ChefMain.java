package clientSide.main;

import genclass.GenericIO;

/**
 *
 * @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Client side of the Restaurant (Chef).
 *
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */
public class ChefMain {

    public static void main(String[] args) {

        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests


        /* getting problem runtime parameters */

        if (args.length != 2)
        { GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        rmiRegHostName = args[0];
        try
        { rmiRegPortNumb = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        { GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit (1);
        }



        /*kitchen = new KitchenStub(kitchenServerHostName, kitchenServerPortNum);
        bar = new BarStub(barServerHostName, barServerPortNum);
        genReposStub = new GeneralRepositoryStub(genReposServerHostName, genReposServerPortNum);
        chef = new Chef("chef", kitchen, bar);


        GenericIO.writelnString("Launching Chef Thread ");
        chef.start();


        try {
            chef.join();
        } catch (InterruptedException e) {
        }
        GenericIO.writelnString("____THE CHEF THREAD HAS TERMINATED_______");
        kitchen.shutdown();
        genReposStub.shutdown();*/
    }
}