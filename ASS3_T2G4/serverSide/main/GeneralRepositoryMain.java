package serverSide.main;

import genclass.GenericIO;

import java.rmi.RemoteException;

public class GeneralRepositoryMain {

    /**
     * Flag signaling the end of operations.
     */

    private static boolean end = false;


    /**
     * Close of operations.
     */

    public static void shutdown() throws RemoteException {
        end = true;
        try {
            synchronized (Class.forName("serverSide.main.GeneralRepositoryMain")) {
                (Class.forName("serverSide.main.GeneralRepositoryMain")).notify();
            }
        } catch (ClassNotFoundException e) {
            GenericIO.writelnString("The data type GeneralRepositoryMain was not found (waking up)!");
            e.printStackTrace();
            System.exit(1);
        }
    }
}