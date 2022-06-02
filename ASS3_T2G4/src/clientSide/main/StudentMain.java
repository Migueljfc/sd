package clientSide.main;

import genclass.GenericIO;


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




        /*bar = new BarStub(barServerHostName, barServerPortNum);
        table = new TableStub(tableServerHostName, tableServerPortNum);
        genReposStub = new GeneralRepositoryStub(genReposServerHostName, genReposServerPortNum);

        for(int i=0; i<SimulPar.N; i++){
            students[i] = new Student("Student_"+i, i, table, bar);
        }


        for (int i = 0; i < SimulPar.N; i++) {
            GenericIO.writelnString ("Launching Student Thread "+i);
            students[i].start();
        }

        for(int i = 0; i < SimulPar.N; i++)
        {
            try {
                students[i].join();
            }catch(InterruptedException e) {}
            GenericIO.writelnString ("The student"+(i+1)+" thread has terminated.");
        }
        genReposStub.shutdown();*/
    }
}