package serverSide.sharedRegions;

import clientSide.entities.States;
import clientSide.stubs.GeneralRepositoryStub;
import serverSide.entities.KitchenClientProxy;
import serverSide.main.KitchenMain;
import serverSide.main.SimulPar;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary Kitchen
 * Implementation of the kitchen shared region
 */

public class Kitchen {
    /**
     * Count portions ready
     */
    private int portionsReady;

    /**
     * Count portions delivered in at each course
     */
    private int portionsDelivered;

    /**
     * Count courses delivered
     */
    private int coursesDelivered;

    /**
     * Count portions prepared by the chef
     */
    private int portionsPrepared;

    /**
     * Reference to the stub of the General Repository.
     */
    private final GeneralRepositoryStub reposStub;

    /**
     * Count entities that requested shutdown
     */
    private int entities;

    /**
     * Control if a order has been requested
     */
    private boolean startOrder;

    /**
     * control if chef start preparation
     */
    private boolean startPreparation;

    /**
     * Kitchen instantiation
     *
     * @param reposStub reference to general repository stub
     */
    public Kitchen(GeneralRepositoryStub reposStub) {
        this.portionsReady = 0;
        this.portionsDelivered = 0;
        this.coursesDelivered = 0;
        this.reposStub = reposStub;
        this.entities = 0;
        this.startOrder = false;
        this.startPreparation = false;

    }


    /**
     * Part of the chef lifecycle to signal that is waiting the order
     */
    public synchronized void watch_news() {
        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.WAIT_FOR_AN_ORDER);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

        while (!startOrder) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
    }


    /**
     * Part of the chef lifecycle to start the preparation and signal the waiter of that
     */
    public synchronized void start_preparation() {
        //Update new Chef State
        reposStub.setnCourses(coursesDelivered + 1);
        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
        startPreparation = true;
        //Notify Waiter that the preparation of the order has started
        notifyAll();
    }


    /**
     * Part of the chef lifecycle to signal that the preparation was continued
     */
    public synchronized void proceed_preparation() {
        portionsPrepared++;

        reposStub.setnPortions(portionsPrepared);
        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

        portionsReady++;
    }


    /**
     * Part of the chef lifecycle to check if he needs to prepare another portion or not
     * @return true if all portions have been delivered
     */
    public synchronized boolean have_all_portions_been_delivered() {

        while (portionsReady != 0) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

        if (portionsDelivered == SimulPar.N) {

            coursesDelivered++;
            return true;
        }
        return false;

    }


    /**
     * Part of the chef lifecycle to check if all courses have been delivered
     * @return true if all courses have been completed
     */
    public synchronized boolean has_the_order_been_completed() {
        if (coursesDelivered == SimulPar.M)
            return true;
        return false;
    }


    /**
     * Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
     */
    public synchronized void continue_preparation() {
        reposStub.setnCourses(coursesDelivered + 1);
        portionsPrepared = 0;
        reposStub.setnPortions(portionsPrepared);

        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
    }


    /**
     * Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
     */
    public synchronized void have_next_portion_ready() {
        portionsPrepared++;
        reposStub.setnPortions(portionsPrepared);
        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

        portionsReady++;

        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

        //Notify Waiter
        notifyAll();
    }


    /**
     * Part of the chef lifecycle when the order has completed
     */
    public synchronized void clean_up() {

        ((KitchenClientProxy) Thread.currentThread()).setChefState(States.CLOSING_SERVICE);
        reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
    }


    /**
     * Part of the waiter lifecycle to signal the waiter that a new order was started
     */
    public synchronized void hand_note_to_the_chef() {

        ((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.PLACING_THE_ORDER);
        reposStub.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());
        startOrder = true;
        //Notify chef
        notifyAll();

        while (!startPreparation) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }

    }


    /**
     * Part of the waiter lifecycle to signal that he is returning to bar
     */
    public synchronized void return_to_bar() {

        ((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
        reposStub.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());
    }


    /**
     * Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
     */
    public synchronized void collectPortion() {
        ((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.WAITING_FOR_AN_PORTION);
        reposStub.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());

        while (portionsReady == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }

        portionsReady--;
        portionsDelivered++;

        if (portionsDelivered > SimulPar.N)
            portionsDelivered = 1;


        reposStub.setnPortions(portionsDelivered);
        reposStub.setnCourses(coursesDelivered + 1);

        //Notify chef
        notifyAll();

    }


    /**
     * Operation kitchen server shutdown
     */
    public synchronized void shutdown() {
        entities += 1;
        if (entities >= 1)
            KitchenMain.waitConnection = false;
        notifyAll();
    }
}
