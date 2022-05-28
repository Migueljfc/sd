package serverSide.sharedRegions;

import clientSide.entities.States;
import clientSide.stubs.GeneralRepositoryStub;
import serverSide.entities.KitchenClientProxy;
import serverSide.main.KitchenMain;
import serverSide.main.SimulPar;

/**
 *  @summary
 * Implementation of the kitchen shared region
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class Kitchen
{
	/**
	 *   Number of entity groups requesting the shutdown.
	 */
	private int entities;

	/**
	 * Reference to the General Repository.
	 */
	private final GeneralRepositoryStub repository;

	/**
	 *	Count the portions ready
	 */

	private int portionsReady;

	/**
	 *	Count the portions been delivered
	 */

	public static int portionsDelivery;

	/**
	 *	count courses delivered
	 */

	public static int coursesDelivery;

	/**
	 *	control if an order has been requested
	 */
	private boolean startOrder;


	private  boolean startPreparation;

	/**
	 *
	 * @param repository repository of information
	 */
	public Kitchen(GeneralRepositoryStub repository)
	{
		this.repository = repository;
		this.portionsReady = 0;
		this.portionsDelivery = 0;
		this.coursesDelivery = 0;
		this.startOrder = false;
		this.startPreparation = false;
		this.entities = 0;

	}

	/**
	 * 	Part of the chef lifecycle to signal that is waiting the order
	 */
	public synchronized void watch_news()
	{
		//KitchenClientProxy chef = ((KitchenClientProxy) Thread.currentThread());
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.WAIT_FOR_AN_ORDER);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
		while(!startOrder) {

			try {
				wait();
			} catch (InterruptedException e) {

			}
		}


	}



	/**
	 *  Part of the chef lifecycle to start the preparation and signal the waiter of that
	 */
	public synchronized void start_preparation()
	{

		((KitchenClientProxy) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
		startPreparation = true;
		//Notify waiter
		notifyAll();
	}




	/**
	 * 	Part of the chef lifecycle to signal that the preparation was continued
	 */
	public synchronized void proceed_preparation()
	{
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		portionsReady++;
	}






	/**
	 * 	Part of the chef lifecycle to check if he needs to prepare another portion or not
	 */

	public synchronized boolean have_all_portions_been_delivered()
	{
		while( portionsReady != 0) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		if(portionsDelivery == SimulPar.N)
		{
			coursesDelivery++;
			return true;
		}

		return false;

	}




	/**
	 * 	Part of the chef lifecycle to check if all courses have been delivered
	 */

	public synchronized boolean has_the_order_been_completed()
	{
		if (coursesDelivery == SimulPar.M)
			return true;
		return false;
	}




	/**
	 * 	Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
	 */

	public synchronized void continue_preparation()
	{
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
	}




	/**
	 * 	Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
	 */

	public synchronized void have_next_portion_ready()
	{

		((KitchenClientProxy) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		portionsReady++;

		((KitchenClientProxy) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());

		//Notify waiter
		notifyAll();
	}




	/**
	 * 	Part of the chef lifecycle when the order has completed
	 */

	public synchronized void clean_up()
	{
		((KitchenClientProxy) Thread.currentThread()).setChefState(States.CLOSING_SERVICE);
		repository.setChefState(((KitchenClientProxy) Thread.currentThread()).getChefState());
	}





	/**
	 * 	Part of the waiter lifecycle to signal the waiter that a new order was started
	 */

	public synchronized void hand_note_to_the_chef()
	{
		((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.PLACING_THE_ORDER);
		repository.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());
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
	 * 	Part of the waiter lifecycle to signal that he is returning to bar
	 */

	public synchronized void return_to_bar()
	{
		((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
		repository.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());
	}


	/**
	 * 	Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
	 */
	public synchronized void collectPortion()
	{
		((KitchenClientProxy) Thread.currentThread()).setWaiterState(States.WAITING_FOR_AN_PORTION);
		repository.setWaiterState(((KitchenClientProxy) Thread.currentThread()).getWaiterState());

		while ( portionsReady == 0) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		portionsReady--;
		portionsDelivery++;
		if(portionsDelivery > SimulPar.N)
			portionsDelivery = 1;

		repository.setPortions(portionsDelivery);
		repository.setCourses(coursesDelivery+1);

		//Notify chef
		notifyAll();

	}

	/**
	 *   Operation server shutdown.
	 *
	 *   New operation.
	 */
	public synchronized void shutdown() {
		entities += 1;
		if (entities >= SimulPar.N)
			KitchenMain.waitConnection = false;
	}



}
