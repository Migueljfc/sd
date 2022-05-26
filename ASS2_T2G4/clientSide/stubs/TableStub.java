package clientSide.stubs;

/**
 * Stub to the table
 */

public class TableStub {
    /**
     *  Name of the platform where is located the table server.
     */

    private String serverHostName;

    /**
     *  Port number for listening to service requests.
     */

    private int serverPortNumb;

    /**
     *
     * Instantiation of a stub to the table.
     *
     * @param serverHostName name of the platform where is located the table server
     * @param serverPortNumb port number for listening to service requests
     */
    public TableStub (String serverHostName, int serverPortNumb)
    {
        this.serverHostName = serverHostName;
        this.serverPortNumb = serverPortNumb;
    }


}
