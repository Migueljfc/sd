package serverSide.main;

import commInfra.ServerCom;
import genclass.GenericIO;
import serverSide.entities.GeneralRepositoryClientProxy;
import serverSide.sharedRegions.GeneralRepository;
import serverSide.sharedRegions.GeneralRepositoryInterface;

import java.net.SocketTimeoutException;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary Server side of the General Repository of Information.
 * <p>
 * Implementation of a client-server model of type 2 (server replication).
 * Communication is based on a communication channel under the TCP protocol.
 */

public class GeneralRepositoryMain {
    /**
     * Flag signaling the service is active.
     */

    public static boolean waitConnection;

    /**
     * Main method.
     *
     * @param args runtime arguments
     *             args[0] - port number for listening to service requests
     */

    public static void main(String[] args) {
        GeneralRepository repos;                                            // general repository of information (service to be rendered)
        GeneralRepositoryInterface reposInter;                              // interface to the general repository of information
        ServerCom serverCom, serverComi;                                         // communication channels
        int portNumb = -1;                                             // port number for listening to service requests

        if (args.length != 1) {
            GenericIO.writelnString("Wrong number of parameters!");
            System.exit(1);
        }
        try {
            portNumb = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            GenericIO.writelnString("args[0] is not a number!");
            System.exit(1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536)) {
            GenericIO.writelnString("args[0] is not a valid port number!");
            System.exit(1);
        }
        /* service is established */

        repos = new GeneralRepository("logger");                                   // service is instantiated
        reposInter = new GeneralRepositoryInterface(repos);                // interface to the service is instantiated
        serverCom = new ServerCom(portNumb);                               // listening channel at the public port is established
        serverCom.start();
        GenericIO.writelnString("Service is established!");
        GenericIO.writelnString("Server is listening for service requests.");

        /* service request processing */

        GeneralRepositoryClientProxy cliProxy;                                  // service provider agent

        waitConnection = true;
        while (waitConnection) {
            try {
                serverComi = serverCom.accept();                                              // enter listening procedure
                cliProxy = new GeneralRepositoryClientProxy(serverComi, reposInter);          // start a service provider agent to address
                cliProxy.start();                                                   //   the request of service
            } catch (SocketTimeoutException e) {
            }
        }
        serverCom.end();                                                   // operations termination
        GenericIO.writelnString("Server was shutdown.");
    }
}
