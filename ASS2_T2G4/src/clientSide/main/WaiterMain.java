package clientSide.main;

import clientSide.entities.Waiter;
import clientSide.stubs.BarStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.KitchenStub;
import clientSide.stubs.TableStub;
import genclass.GenericIO;

/**
 *    Client side of the Restaurant (Students).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class WaiterMain {

    public static void main(String[] args) {


        String barServerHostName;                               // name of the platform where is located the bar server
        int barServerPortNum = -1;                              // port number for listening to service requests
        String tableServerHostName;                             // name of the platform where is located the table server
        int tableServerPortNum = -1;                            // port number for listening to service requests
        String genReposServerHostName;                          // name of the platform where is located the general repository server
        int genReposServerPortNum = -1;                         // port number for listening to service requests
        String kitchenServerHostName;                           // name of the platform where is located the kitchen server
        int kitchenServerPortNum = -1;                          // port number for listening to service requests
        BarStub bar;										    // remote reference to the bar
        TableStub table;                                        // remote reference to the table
        KitchenStub kitchen;                                    // remote reference to the kitchen
        Waiter waiter;                                          // remote reference to the waiter
        GeneralRepositoryStub genReposStub;								// remote reference to the general repository


        /* getting problem runtime parameters */

        if (args.length != 8)
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

        tableServerHostName = args[2];
        try {
            tableServerPortNum = Integer.parseInt(args[3]);
        } catch(NumberFormatException e) {
            GenericIO.writelnString("args[3] is not a valid port number!");
            System.exit(1);
        }
        if ((tableServerPortNum < 4000) || (tableServerPortNum >= 65536)) {
            GenericIO.writelnString ("args[3] is not a valid port number!");
            System.exit (1);
        }

        kitchenServerHostName = args[4];
        try {
            kitchenServerPortNum = Integer.parseInt(args[5]);
        } catch(NumberFormatException e) {
            GenericIO.writelnString("args[5] is not a valid port number!");
            System.exit(1);
        }
        if ((kitchenServerPortNum < 4000) || (kitchenServerPortNum >= 65536)) {
            GenericIO.writelnString ("args[5] is not a valid port number!");
            System.exit (1);
        }

        genReposServerHostName = args[6];
        try
        { genReposServerPortNum = Integer.parseInt (args[7]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[7] is not a number!");
            System.exit (1);
        }
        if ((genReposServerPortNum < 4000) || (genReposServerPortNum >= 65536)) {
            GenericIO.writelnString ("args[7] is not a valid port number!");
            System.exit (1);
        }

        //Initialization

        bar = new BarStub(barServerHostName, barServerPortNum);
        table = new TableStub(tableServerHostName, tableServerPortNum);
        kitchen = new KitchenStub(kitchenServerHostName, kitchenServerPortNum);
        genReposStub = new GeneralRepositoryStub(genReposServerHostName, genReposServerPortNum);


        waiter = new Waiter("Waiter", table, bar, kitchen);


        // Start of simulation
        waiter.start();

        GenericIO.writelnString("Waiter thread" + Thread.currentThread().getName() + " Started");

        try {
            waiter.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bar.shutdown();
        kitchen.shutdown();
        table.shutdown();
        genReposStub.shutdown();
    }

}