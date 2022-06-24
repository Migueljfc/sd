package serverSide.main;

import genclass.GenericIO;

import java.rmi.RemoteException;

/**
 *    Instantiation and registering of a table object.
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on Java RMI.
 */
public class TableMain {

    /**
     *  Flag signaling the end of operations.
     */

    private static boolean end = false;


    /**
     *  Close of operations.
     */

    public static void shutdown () throws RemoteException
    {
        end = true;
        try
        { synchronized (Class.forName ("serverSide.main.TableMain"))
        { (Class.forName ("serverSide.main.TableMain")).notify ();
        }
        }
        catch (ClassNotFoundException e)
        { GenericIO.writelnString ("The data type TableMain was not found (waking up)!");
            e.printStackTrace ();
            System.exit (1);
        }
    }
}
