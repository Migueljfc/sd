package clientSide.main;

import clientSide.entities.Student;
import commInfra.SimulPar;
import genclass.GenericIO;
import interfaces.BarInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.TableInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


/**
 *    Client side of the Restaurant (Students).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class StudentMain {

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
        String nameEntryBar = "Bar";						//Public name of the Bar object
        String nameEntryTable = "Table";					//Public name of the Table object
        GeneralRepositoryInterface repositoryStub = null;				//Remote reference to the General Repository object
        BarInterface barStub = null;						//Remote reference for the Bar object
        TableInterface tabStub = null;						//Remote reference for the Table object
        Registry registry = null;							//Remote reference for registration in the RMI Registry service
        Student[] student = new Student[SimulPar.N];	//Array of student Threads


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

        //Locate Table Server
        try {
            tabStub = (TableInterface) registry.lookup(nameEntryTable);
        } catch( RemoteException e ) {
            GenericIO.writelnString ("Table lookup exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        } catch( NotBoundException e ) {
            GenericIO.writelnString ("Table not bound exception: " + e.getMessage ());
            e.printStackTrace ();
            System.exit (1);
        }

        for(int i=0; i < SimulPar.N; i++)
            student[i] = new Student("student_"+(i+1), i, tabStub, barStub);

        /* start of the simulation */
        for(int i=0; i < SimulPar.N; i++)
            student[i].start();

        /* waiting for the end of the simulation */
        for(int i=0; i < SimulPar.N; i++)
        {
            try
            { student[i].join();
            }
            catch(InterruptedException e) {}
            GenericIO.writelnString ("The student "+(i+1) + " has terminated.");
        }


        //Table shutdown
        try {
            tabStub.shutdown();
        } catch(RemoteException e) {
            GenericIO.writelnString ("Chef generator remote exception on Table shutdown: " + e.getMessage ());
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