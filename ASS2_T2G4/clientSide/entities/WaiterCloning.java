package clientSide.entities;

/**
 *    @author miguel cabral 93091
 *    @author rodrigo santos 93173
 *
 *   @summary
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public interface WaiterCloning {

    /**
     * Set waiter state
     *
     * @param state
     */
    public void setWaiterState(int state);

    /**
     * Get waiter state
     *
     * @return waiter state
     */
    public int getWaiterState() ;
}
