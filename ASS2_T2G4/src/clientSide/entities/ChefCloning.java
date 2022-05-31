package clientSide.entities;

/**
 *    Cloning Chef.
 *
 *      It specifies his own attributes.
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public interface ChefCloning {
    /**
     * Sets the chef's state.
     *
     * @param s desired state
     */
    public void setChefState(States s);

    /**
     * Returns the Chef's state.
     *
     * @return chef's current state
     */
    public States getChefState();
}
