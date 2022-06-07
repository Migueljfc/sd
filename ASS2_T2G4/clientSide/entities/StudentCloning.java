package clientSide.entities;

/**
 *  @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary
 *      Implementation of a client-server model of type 2 (server replication).
 *      Communication is based on a communication channel under the TCP protocol.
 */
public interface StudentCloning {

    /**
     * Returns the Student's state.
     *
     * @return student's current state
     */
    public int getStudentState();

    /**
     * Returns the student's id
     *
     * @return student's id
     */
    public int getStudentId();

    /**
     * Sets the student's state.
     *
     * @param s desired state
     */
    public void setStudentState(int s) ;

    /**
     * Sets the student's id.
     *
     * @param i desired id
     */
    public void setStudentId(int i);
}
