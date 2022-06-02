package serverSide.main;

import clientSide.stubs.GeneralRepositoryStub;
import serverSide.entities.*;
import serverSide.sharedRegions.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

import clientSide.stubs.TableStub;

/**
 *  @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Server side of the Bar.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */
public class BarMain
{
    /**
     *  Flag signaling the service is active.
     */
    public static boolean waitConnection;

    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - port number for listening to service requests
     *        args[1] - name of the platform where is located the server for the general repository
     *        args[2] - port number where the server for the general repository is listening to service requests
     *        args[3] - name of the platform where is located the server for the table
     *        args[4] - port number where the server for the table is listening to service requests
     */
    public static void main (String [] args)
    {
        Bar bar;							// bar (service to be rendered)
        BarInterface barInter;				// interface to the bar
        GeneralRepositoryStub reposStub;			// stub to the general repository
        TableStub tableStub;				// stub to the table
        ServerCom serverCom, serverComi;				// communication channels
        int portNumb = -1;					// port number for listening to service requests
        String reposServerName;				// name of the platform where is located the server for the general repository
        int reposPortNumb = -1;				// port number where the server for the general repository is listening to service requests
        String tableServerName;				// name of the platform where is located the server for the table
        int tablePortNumb = -1;				// port number where the server for the table is listening to service requests


        if (args.length != 5) {
            GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }

        try {
            portNumb = Integer.parseInt (args[0]);
        } catch(NumberFormatException e) {
            GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }
        if((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
        }

        reposServerName = args[1];
        try {
            reposPortNumb = Integer.parseInt (args[2]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if((reposPortNumb < 4000) || (reposPortNumb >= 65536)) {
            GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        tableServerName = args[3];
        try {
            tablePortNumb = Integer.parseInt (args[4]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if((tablePortNumb < 4000) || (tablePortNumb >= 65536)) {
            GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        portNumb = Integer.parseInt (args[0]);
        reposPortNumb = Integer.parseInt (args[2]);
        tablePortNumb = Integer.parseInt (args[4]);
        reposServerName = args[1];
        tableServerName = args[3];

        /* service is established */

        reposStub = new GeneralRepositoryStub (reposServerName, reposPortNumb);	// communication to the general repository is instantiated
        tableStub = new TableStub (tableServerName, tablePortNumb);			// communication to the table is instantiated
        bar = new Bar(reposStub, tableStub);								// service is instantiated
        barInter = new BarInterface (bar);									// interface to the service is instantiated
        serverCom = new ServerCom (portNumb);									// listening channel at the public port is established
        serverCom.start ();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");

        /* service request processing */

        BarClientProxy cliProxy;							// service provider agent

        waitConnection = true;
        while (waitConnection)
        { try
        { serverComi = serverCom.accept ();							// enter listening procedure
            cliProxy = new BarClientProxy (serverComi, barInter);	// start a service provider agent to address
            cliProxy.start ();									// the request of service
        }
        catch (SocketTimeoutException e) {}
        }
        serverCom.end ();										// operations termination
        GenericIO.writelnString ("Server was shutdown.");
    }
}
