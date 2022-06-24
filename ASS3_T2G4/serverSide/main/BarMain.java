package serverSide.main;

import genclass.GenericIO;
import interfaces.RegisterInterface;
import serverSide.objects.RegisterRemoteObject;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *    Instantiation and registering of a Bar object.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class BarMain {
    /**
     *  Flag signaling the end of operations.
     */

    private static boolean end = false;
    /**
     *  Main method.
     *
     *    @param args runtime arguments
     *        args[0] - port number for listening to service requests
     *        args[1] - name of the platform where is located the RMI registering service
     *        args[2] - port number where the registering service is listening to service requests
     */

    public static void main(String[] args)
    {
        int portNumb = -1;                                             // port number for listening to service requests
        String rmiRegHostName;                                         // name of the platform where is located the RMI registering service
        int rmiRegPortNumb = -1;                                       // port number where the registering service is listening to service requests

        if (args.length != 3)
        { GenericIO.writelnString ("Wrong number of parameters!");
            System.exit (1);
        }
        try
        { portNumb = Integer.parseInt (args[0]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[0] is not a number!");
            System.exit (1);
        }
        if ((portNumb < 4000) || (portNumb >= 65536))
        { GenericIO.writelnString ("args[0] is not a valid port number!");
            System.exit (1);
        }
        rmiRegHostName = args[1];
        try
        { rmiRegPortNumb = Integer.parseInt (args[2]);
        }
        catch (NumberFormatException e)
        { GenericIO.writelnString ("args[2] is not a number!");
            System.exit (1);
        }
        if ((rmiRegPortNumb < 4000) || (rmiRegPortNumb >= 65536))
        { GenericIO.writelnString ("args[2] is not a valid port number!");
            System.exit (1);
        }

        /* create and install the security manager */

        if (System.getSecurityManager () == null)
            System.setSecurityManager (new SecurityManager ());
        GenericIO.writelnString ("Security manager was installed!");

        /* instantiate a registration remote object and generate a stub for it */

        RegisterRemoteObject regEngine = new RegisterRemoteObject (rmiRegHostName, rmiRegPortNumb);  // object that enables the registration
        // of other remote objects
        RegisterInterface regEngineStub = null;                                                               // remote reference to it

        try
        { regEngineStub = (RegisterInterface) UnicastRemoteObject.exportObject (regEngine, portNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject stub generation exception: " + e.getMessage ());
            System.exit (1);
        }
        GenericIO.writelnString ("Stub was generated!");

        /* register it with the local registry service */

        String nameEntry = "RegisterHandler";                          // public name of the remote object that enables
        // the registration of other remote objects
        Registry registry = null;                                      // remote reference for registration in the RMI registry service

        try
        { registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            System.exit (1);
        }
        GenericIO.writelnString ("RMI registry was created!");

        try
        { registry.rebind (nameEntry, regEngineStub);
        }
        catch (RemoteException e)
        { GenericIO.writelnString ("RegisterRemoteObject remote exception on registration: " + e.getMessage ());
            System.exit (1);
        }
        GenericIO.writelnString ("RegisterRemoteObject object was registered!");
    }

    /**
     *  Close of operations.
     */

    public static void shutdown () throws RemoteException
    {
        end = true;
        try
        { synchronized (Class.forName ("serverSide.main.BarMain"))
        { (Class.forName ("serverSide.main.BarMain")).notify ();
        }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type BarMain was not found (waking up)!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
