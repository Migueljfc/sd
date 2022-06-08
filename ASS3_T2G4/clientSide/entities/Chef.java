package clientSide.entities;


import interfaces.BarInterface;
import interfaces.KitchenInterface;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 *
* @summary
 * This datatype implements the Chef thread
 *
 */
public class Chef extends Thread{

    /**
    *  Chef's State.
    */
    private int state;

    /**
    *  Kitchen reference
    */
    private KitchenInterface kitchen;

     /**
    *  Bar reference
    */
    private BarInterface bar;



    /**
     * @param name    thread name
     * @param kitchen reference to the chef Kitchen
     */

    public Chef(String name, KitchenInterface kitchen, BarInterface bar){
        super(name);
        state = States.WAIT_FOR_AN_ORDER;
        this.kitchen = kitchen;
        this.bar = bar;
    }

     /**
     * Sets the chef's state.
     * @param s desired state
     */
    public void setChefState(int s){
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        state = s;

    }

     /**
     * Returns the Chef's state.
     * @return chef's current state
     */
    public int getChefState(){
        return state;
    }

    /**
     * Chefs lifecycle
     */
    @Override
    public void run ()
    {
        boolean first_course = true;

        /*kitchen.watch_news();
        kitchen.start_preparation();
        do
        {
            if(!first_course)
                kitchen.continue_preparation();
            else
            	first_course = false;

            kitchen.proceed_preparation();
            bar.alert_the_waiter();

            while(!kitchen.have_all_portions_been_delivered())
                kitchen.have_next_portion_ready();
        }
        while(!kitchen.has_the_order_been_completed());

        kitchen.clean_up();*/

    }
}

