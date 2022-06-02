package serverSide.sharedRegions;

import clientSide.stubs.GeneralRepositoryStub;
import serverSide.main.*;
import clientSide.entities.States;

import serverSide.entities.KitchenClientProxy;

/**
 * @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Kitchen
 *
 * 	It is responsible for keeping track of portions prepared and delivered.
 *  Is implemented as an implicit monitor.
 *  All public methods are executed in mutual exclusion.
 *	Synchronisation points include:
 *		Chef has to wait for the note that describes the order given by the waiter
 *		Chef has to wait for waiter to collect portions
 *		Waiter has to wait for chef to start preparing the order
 *		Waiter has to wait for portions from the chef
 *
 */

public class Kitchen
{
	/**
	 *	Number of portions ready
	 */
	private int portionsReady;

	/**
	 *	Number of portions delivered in at each course
	 */
	private int portionsDelivered;

	/**
	 *	Number of courses delivered
	 */
	private int coursesDelivered;

	/**
	 * Number of portions prepared by the chef
	 */
	private int portionsPrepared;

	/**
	 * Reference to the stub of the General Repository.
	 */
	private final GeneralRepositoryStub reposStub;

	/**
	 * Number of entities that requested shutdown
	 */
	private int nEntities;

	/**
	 *	control if a order has been requested
	 */
	private boolean startOrder;

	/**
	 * control if chef start preparation
	 */
	private  boolean startPreparation;

	/**
	 * Kitchen instantiation
	 *
	 * @param reposStub reference to general repository
	 */
	public Kitchen(GeneralRepositoryStub reposStub)
	{
		this.portionsReady = 0;
		this.portionsDelivered = 0;
		this.coursesDelivered = 0;
		this.reposStub = reposStub;
		this.nEntities = 0;
		this.startOrder = false;
		this.startPreparation = false;

	}



	/**
	 * 	Operation watch the news
	 *
	 * 	It is called by the chef, he waits for waiter to notify him of the order
	 */
	public synchronized void watch_news()
	{
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.WAIT_FOR_AN_ORDER);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		while(!startOrder) {
			/**Fita cola preta */
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
	}



	/**
	 * 	Operation start presentation
	 *
	 * 	It is called by the chef after waiter has notified him of the order to be prepared
	 * 	to signal that preparation of the course has started
	 */
	public synchronized void start_preparation()
	{
		//Update new Chef State
		reposStub.setnCourses(coursesDelivered+1);
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
		startPreparation = true;
		//Notify Waiter that the preparation of the order has started
		notifyAll();
	}




	/**
	 * 	Operation proceed presentation
	 *
	 * 	It is called by the chef when a portion needs to be prepared
	 */
	public synchronized void proceed_preparation()
	{
		//Update new Chef state
		portionsPrepared++;
		reposStub.setnPortions(portionsPrepared);
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		//Update portionsReady
		portionsReady++;
	}






	/**
	 * 	Operation have all portions been delivered
	 *
	 * 	It is called by the chef when he finishes a portion and checks if another one needs to be prepared or not
	 * 	It is also here were the chef blocks waiting for waiter do deliver the current portion
	 * 	@return true if all portions have been delivered, false otherwise
	 */
	public synchronized boolean have_all_portions_been_delivered()
	{

		while( portionsReady != 0) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		if(portionsDelivered == SimulPar.N)
		{

			coursesDelivered++;
			return true;
		}
		return false;

	}




	/**
	 *	Operation has order been completed
	 *
	 * 	It is called by the chef when he finishes preparing all courses to check if order has been completed or not
	 * 	@return true if all courses have been completed, false or not
	 */
	public synchronized boolean has_the_order_been_completed()
	{
		//Check if all courses have been delivered
		if (coursesDelivered == SimulPar.M)
			return true;
		return false;
	}




	/**
	 * 	Operation continue preparation
	 *
	 * 	It is called by the chef when all portions have been delivered, but the course has not been completed yet
	 */
	public synchronized void continue_preparation()
	{
		//Update chefs state
		reposStub.setnCourses(coursesDelivered+1);
		portionsPrepared = 0;
		reposStub.setnPortions(portionsPrepared);

		((KitchenClientProxy) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
	}




	/**
	 * Operation have next portion ready
	 *
	 * It is called by the chef after a portion has been delivered and another one needs to be prepared
	 */
	public synchronized void have_next_portion_ready()
	{
		//Update chefs state
		portionsPrepared++;
		reposStub.setnPortions(portionsPrepared);
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		//Update portionsReady
		portionsReady++;

		//Update chefs state
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		//Notify Waiter that there is a portion waiting to be delivered
		notifyAll();
	}




	/**
	 * Operation clean up
	 *
	 * It is called by the chef when he finishes the order, to close service
	 */
	public synchronized void clean_up()
	{
		//Update chefs state to terminate life cycle
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.CLOSING_SERVICE);
		reposStub.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
	}





	/**
	 * Operation hand note to chef
	 *
	 * Called by the waiter to wake chef up chef to give him the description of the order
	 */
	public synchronized void hand_note_to_the_chef()
	{

		((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.PLACING_THE_ORDER);
		reposStub.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());
		startOrder = true;
		//Notify chef
		notifyAll();

		while(!startPreparation){
			/** Fita cola preta */
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

	}





	/**
	 * Operation return to the bar
	 *
	 * Called by the waiter when he is the kitchen and returns to the bar
	 */
	public synchronized void return_to_bar()
	{
		//Update waiter state
		((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
		reposStub.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());
	}





	/**
	 * Operation collect portion
	 *
	 * Called by the waiter when there is a portion to be delivered. Collect and signal chef that the portion was delivered
	 */
	public synchronized void collectPortion()
	{
		((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.WAITING_FOR_AN_PORTION);
		reposStub.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());

		//If there are no portions to deliver waiter must block
		while (portionsReady == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		//Update number of portions ready and delivered
		portionsReady--;
		portionsDelivered++;

		//If a new course is being delivered then portionsDelivered must be "reseted"
		if(portionsDelivered > SimulPar.N)
			portionsDelivered = 1;

		//Update portion number and course number in general repository
		reposStub.setnPortions(portionsDelivered);
		reposStub.setnCourses(coursesDelivered+1);

		//Signal chef that portion was delivered
		notifyAll();

	}


	/**
	 * Operation kitchen server shutdown
	 */
	public synchronized void shutdown()
	{
		nEntities += 1;
		if(nEntities >= SimulPar.E)
			KitchenMain.waitConnection = false;
		notifyAll ();
	}
}
