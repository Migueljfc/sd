package clientSide.main;

import clientSide.entities.*;
import clientSide.stubs.*;
import serverSide.main.*;
import commInfra.*;
import genclass.GenericIO;

/**
 *    Client side of the Restaurant (Chef).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class ChefMain {

    public static void main(String[] args) {

        String barServerHostName;                               // name of the platform where is located the bar server
        int barServerPortNum = -1;                              // port number for listening to service requests
        String genReposServerHostName;                          // name of the platform where is located the general repository server
        int genReposServerPortNum = -1;                         // port number for listening to service requests
        String kitchenServerHostName;                           // name of the platform where is located the kitchen server
        int kitchenServerPortNum = -1;                          // port number for listening to service requests
        Chef chef;                   						    // remote reference to the chef
        BarStub bar;										    // remote reference to the bar
        KitchenStub kitchen;                                    // remote reference to the kitchen
        GeneralRepositoryStub genReposStub;								// remote reference to the general repository


        /* getting problem runtime parameters */

        if (args.length != 6)
        { GenericIO.writelnString("Wrong number of parameters!");
            System.exit (1);
        }
        barServerHostName = args[0];
        try
        {
            barServerPortNum = Integer.parseInt (args[1]);
        }
        catch (NumberFormatException e)
        {
            GenericIO.writelnString ("args[1] is not a number!");
            System.exit (1);
        }
        if ((barServerPortNum < 4000) || (barServerPortNum >= 65536))
        {
            GenericIO.writelnString ("args[1] is not a valid port number!");
            System.exit(1);
        }

        kitchenServerHostName = args[2];
        try {
            kitchenServerPortNum = Integer.parseInt(args[3]);
        } catch(NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }
        if ((kitchenServerPortNum < 4000) || (kitchenServerPortNum >= 65536)) {
            GenericIO.writelnString ("args[3] is not a valid port number!");
            System.exit (1);
        }

        genReposServerHostName = args[4];
        try
        {
            genReposServerPortNum = Integer.parseInt (args[5]);
        }
        catch (NumberFormatException e)
        {
            GenericIO.writelnString ("args[3] is not a number!");
            System.exit (1);
        }
        if ((genReposServerPortNum < 4000) || (genReposServerPortNum >= 65536)) {
            GenericIO.writelnString ("args[3] is not a valid port number!");
            System.exit (1);
        }


        //Initialization


        bar = new BarStub(barServerHostName, barServerPortNum);
        kitchen = new KitchenStub(kitchenServerHostName, kitchenServerPortNum);
        genReposStub = new GeneralRepositoryStub(genReposServerHostName, genReposServerPortNum);

        chef = new Chef("Chef", kitchen, bar,genReposStub);


        // Start of simulation
        chef.start();

        GenericIO.writelnString("Chef thread" + Thread.currentThread().getName() + " Started");

        try {
            chef.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bar.shutdown();
        kitchen.shutdown();
        genReposStub.shutdown();
    }
}