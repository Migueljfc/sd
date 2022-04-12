/**
 * 
 */
package entities;
import sharedRegions.*;

/**
 * @author miguel
 *
 */

public class Student extends Thread {
    
    
    /**
    *  Student identification.
    *  @serialField id
    */
    private int id;
    
     /**
    *  student's State.
    *  @serialField state
    */
    private States state;
    
    /**
    *  Table
    *  @serialField table
    */
    private Table table;
    
     /**
    *  Bar
    *  @serialField bar
    */
    private Bar bar;
    
    /**
    *  Repository
    * @serialField Repository
    */
    private GeneralRepository repository;
    
        /**
       *   Instantiation of a Student thread.
       *
       *     @param name thread name
       *     @param table reference to the student table
       *     @param repo reference to the general repository
       */
    public Student(String name, int id, Table table, Bar bar, GeneralRepository repository){
        super(name);
        this.id = id;
        state = States.GOING_TO_THE_RESTAURANT;
        this.table = table;
        this.repository = repository;
        this.bar = bar;
    }
    
    
     /**
     * Returns the Student's state.
     * @return student's current state
     */
    public States getStudentState(){
        return state;
    }
    
     /**
     * Returns the student's id
     * @return student's id
     */
    public int getStudentId(){
        return id;
    }
    
    /**
     * Sets the student's state.
     * @param s desired state
     */
    public void setStudentState(States s){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        state = s;
        
    }
    /**
     * Sets the student's id.
     * @param i desired id
     */
    public void setStudentId(int i){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        id= i;
    }
}