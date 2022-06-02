package clientSide.main;

import clientSide.entities.Student;
import clientSide.stubs.BarStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.TableStub;
import genclass.GenericIO;
import serverSide.main.SimulPar;

/**
 *    Client side of the Restaurant (Students).
 *
 *    Implementation of a client-server model of type 2 (server replication).
 *    Communication is based on a communication channel under the TCP protocol.
 */

public class StudentMain {

    public static void main(String[] args) {


        String barServerHostName;                                   // name of the platform where is located the bar server
        int barServerPortNum = -1;                                  // port number for listening to service requests
        String tableServerHostName;                                 // name of the platform where is located the table server
        int tableServerPortNum = -1;                                // port number for listening to service requests
        String genReposServerHostName;                              // name of the platform where is located the general repository server
        int genReposServerPortNum = -1;                             // port number for listening to service requests
        BarStub bar;										        // remote reference to the bar
        TableStub table;                                            // remote reference to the table
        Student[] students = new Student[SimulPar.N];             // array of students threads
        GeneralRepositoryStub genReposStub;						// remote reference to the general repository


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
        table = new TableStub(tableServerHostName, tableServerPortNum);
        genReposStub = new GeneralRepositoryStub(genReposServerHostName, genReposServerPortNum);

        for(int i=0; i<SimulPar.N; i++){
            students[i] = new Student("Student_"+i, i, table, bar);
        }


        // Start of simulation
        /* start simulation */
        for (int i = 0; i < SimulPar.N; i++) {
            GenericIO.writelnString ("Launching Student Thread "+i);
            students[i].start();
        }

        /* waiting for the end of the simulation */
        for(int i = 0; i < SimulPar.N; i++)
        {
            try {
                students[i].join();
            }catch(InterruptedException e) {}
            GenericIO.writelnString ("The student"+(i+1)+" thread has terminated.");
        }
        genReposStub.shutdown();
    }
}