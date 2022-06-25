package clientSide.main;

import clientSide.entities.Chef;
import genclass.GenericIO;
import interfaces.BarInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.KitchenInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

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



        String nameEntryGeneralRepos = "GeneralRepository";	//Public name of the General Repository object
        String nameEntryKitchen = "Kitchen";				//Public name of the Kitchen object
        String nameEntryBar = "Bar";						//Public name of the Bar object
        GeneralRepositoryInterface repositoryStub = null;				//Remote reference to the General Repository object
        KitchenInterface kitStub = null;					//Remote reference for the Kitchen object
        BarInterface barStub = null;						//Remote reference for the Bar object
        Registry registry = null;							//Remote reference for registration in the RMI Registry service
        Chef chef;											//Chef Thread

        //Locate RMI Registry server
        try {
            registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            GenericIO.writelnString ("RMI registry creation exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        //Locate GeneralRepos Server
        try {
            repositoryStub = (GeneralRepositoryInterface) registry.lookup(nameEntryGeneralRepos);
        } catch( RemoteException e ) {
            GenericIO.writelnString ("General Repository lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch( NotBoundException e ) {
            GenericIO.writelnString ("General Repository not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        //Locate Kitchen Server
        try {
            kitStub = (KitchenInterface) registry.lookup(nameEntryKitchen);
        } catch( RemoteException e ) {
            GenericIO.writelnString ("Kitchen lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch( NotBoundException e ) {
            GenericIO.writelnString ("Kitchen not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        //Locate Bar Server
        try {
            barStub = (BarInterface) registry.lookup(nameEntryBar);
        } catch( RemoteException e ) {
            GenericIO.writelnString ("Bar lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch( NotBoundException e ) {
            GenericIO.writelnString ("Bar not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        //initialise Chef thread
        chef = new Chef("chef", kitStub, barStub);

        /* start of the simulation */
        chef.start();

        /* waiting for the end of the simulation */
        try {
            chef.join();
        } catch (InterruptedException e) {}
        GenericIO.writelnString ("The chef thread has terminated.");

        //Kitchen shutdown
        try {
            kitStub.shutdown();
        } catch(RemoteException e) {
            GenericIO.writelnString ("Chef generator remote exception on Bar shutdown: " + e.getMessage ());
            System.exit (1);
        }
        //Bar shutdown
        try {
            barStub.shutdown();
        } catch(RemoteException e) {
            GenericIO.writelnString ("Chef generator remote exception on Kitchen shutdown: " + e.getMessage ());
            System.exit (1);
        }
        //GeneralRepos shutdown
        try {
            repositoryStub.shutdown();
        } catch(RemoteException e) {
            GenericIO.writelnString ("Chef generator remote exception on GeneralRepos shutdown: " + e.getMessage ());
            System.exit (1);
        }
    }
}