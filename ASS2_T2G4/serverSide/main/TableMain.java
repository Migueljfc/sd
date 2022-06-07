package serverSide.main;

import serverSide.entities.*;
import serverSide.sharedRegions.*;
import clientSide.stubs.*;
import commInfra.*;
import genclass.GenericIO;
import java.net.*;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary Server side of the Table.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class TableMain
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
     */

    public static void main (String [] args)
    {
        Table tab;
        TableInterface tabInter;
        GeneralRepositoryStub reposStub;
        ServerCom serverCom, serverComi;
        int portNumb = -1;
        String reposServerName;
        int reposPortNumb = -1;

        if (args.length != 3) {
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


        reposStub = new GeneralRepositoryStub (reposServerName, reposPortNumb);
        tab = new Table (reposStub);
        tabInter = new TableInterface (tab);
        serverCom = new ServerCom (portNumb);
        serverCom.start ();
        GenericIO.writelnString ("Service is established!");
        GenericIO.writelnString ("Server is listening for service requests.");


        TableClientProxy cliProxy;

        waitConnection = true;
        while (waitConnection)
        { try
        { serverComi = serverCom.accept ();
            cliProxy = new TableClientProxy (serverComi, tabInter);
            cliProxy.start ();
        }
        catch (SocketTimeoutException e) {}
        }
        serverCom.end ();
        GenericIO.writelnString ("Server was shutdown.");
    }
}
