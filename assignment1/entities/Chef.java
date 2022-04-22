/**
 * 
 */
package entities;
import sharedRegions.*;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */
/**{@summary}
 * This datatype implements the Chef thread ... [Completar]
 * 
 */
public class Chef extends Thread{
    
    /**
    *  Chef's State.
    *  @serialField State
    */
    private States state;
    
    /**
    *  Kitchen
    *  @serialField Kitchen
    */
    private Kitchen kitchen;
    
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
   *   Instantiation of a Chef thread.
   *
   *     @param name thread name
   *     @param kit reference to the chef Kitchen
   *     @param repo reference to the general repository
   */
    
    public Chef(String name, Kitchen kitchen, Bar bar, GeneralRepository repository){
        super(name);
        state = States.WAIT_FOR_AN_ORDER;
        this.kitchen = kitchen;
        this.repository = repository;
        this.bar = bar;
    }
    
     /**
     * Sets the chef's state.
     * @param s desired state
     */
    public void setChefState(States s){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        state = s;
        
    }
    
     /**
     * Returns the Chef's state.
     * @return chef's current state
     */
    public States getChefState(){
        return state;
    }
    
    
    @Override
    public void run ()
    {
        boolean first_plate = true;

        kitchen.watch_news();
        kitchen.start_preparation();
        do
        {
            if(!first_plate)
                kitchen.continue_preparation();
            else
                first_plate = false;

            kitchen.proceed_preparation();
            bar.alert_waiter();

            while(!kitchen.have_all_portions_been_delivered())
                kitchen.have_next_portion_ready();
        }
        while(!kitchen.has_the_order_been_completed());

        kitchen.clean_up();
        
    }
}

