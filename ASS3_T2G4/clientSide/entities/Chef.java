package clientSide.entities;


import interfaces.BarInterface;
import interfaces.KitchenInterface;

import java.rmi.RemoteException;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary This datatype implements the Chef thread
 */
public class Chef extends Thread {

    /**
     * Chef's State.
     */
    private int state;

    /**
     * Kitchen reference
     */
    private KitchenInterface kitchen;

    /**
     * Bar reference
     */
    private BarInterface bar;


    /**
     * @param name    thread name
     * @param kitchen reference to the chef Kitchen
     */

    public Chef(String name, KitchenInterface kitchen, BarInterface bar) {
        super(name);
        state = States.WAIT_FOR_AN_ORDER;
        this.kitchen = kitchen;
        this.bar = bar;
    }

    /**
     * Sets the chef's state.
     *
     * @param s desired state
     */
    public void setChefState(int s) {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        state = s;

    }

    /**
     * Returns the Chef's state.
     *
     * @return chef's current state
     */
    public int getChefState() {
        return state;
    }

    /**
     * Chefs lifecycle
     */
    @Override
    public void run() {
        boolean first_course = true;

        watch_news();
        start_preparation();

        do {
            if (!first_course)
                continue_preparation();
            else
                first_course = false;

            proceed_preparation();
            alert_the_waiter();

            while (!have_all_portions_been_delivered())
                have_next_portion_ready();
        }
        while (!has_the_order_been_completed());

        clean_up();

    }

    /**
     * 	Part of the chef lifecycle to signal that is waiting the order
     *
     *
     */
    private void watch_news() {
        try {
            state = kitchen.watch_news();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *  Part of the chef lifecycle to start the preparation and signal the waiter of that
     */
    private void start_preparation() {
        try {
            state = kitchen.start_preparation();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
    * 	Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
    */
    private void continue_preparation() {
        try {
            state = kitchen.continue_preparation();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 	Part of the chef lifecycle to signal that the preparation was continued
     *
     */
    private void proceed_preparation() {
        try {
            state = kitchen.proceed_preparation();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 	Part of the chef lifecycle when the order has completed
     *
     */
    private void clean_up() {
        try {
            state = kitchen.clean_up();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void alert_the_waiter() {
        try {
            state = bar.alert_the_waiter();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 	Part of the chef lifecycle to check if he needs to prepare another portion or not
     *
     */
    private boolean have_all_portions_been_delivered() {
        boolean val;
        try {
            val = kitchen.have_all_portions_been_delivered();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return val;
    }

    private void have_next_portion_ready() {
        try {
            state = kitchen.have_next_portion_ready();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean has_the_order_been_completed() {
        boolean val;
        try {
            val = kitchen.has_the_order_been_completed();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        return val;
    }

}

