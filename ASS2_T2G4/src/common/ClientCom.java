package common;
import java.io.*;
import java.net.*;

/** Data type that implements the communication in the client side **/

public class ClientCom {

    /**
     *  communication socket
     *    @serialField commSocket
     */
    private Socket comsocket = null;

    /**
     *  Host name of the server
     *    @serialField serverHost
     */
    private String serverHost;

    /**
     *  Number of the port that communicate
     *    @serialField serverPort
     */
    private int serverPort;

    /**
     *  in stream of communication channel
     *    @serialField in
     */
    private ObjectInputStream in = null;

    /**
     *  out stream of communication channel
     *  @serialField in
     */

    private ObjectOutputStream out = null;


    /**
     *
     * @param serverHost name of the computer system where the server are running
     * @param serverPort port number that are listening in the server
     */
    public ClientCom(String serverHost, int serverPort){
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public boolean open(){

    }
}
