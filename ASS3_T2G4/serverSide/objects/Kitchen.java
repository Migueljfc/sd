package serverSide.objects;

import clientSide.entities.Chef;
import clientSide.entities.States;
import clientSide.entities.Waiter;
import interfaces.GeneralRepositoryInterface;
import interfaces.KitchenInterface;
import serverSide.main.KitchenMain;
import serverSide.main.SimulPar;

import java.rmi.RemoteException;

/**
 *  @summary
 * Implementation of the kitchen shared region
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class Kitchen implements KitchenInterface {

	/**
	 * Reference to the General Repository.
	 */
	private final GeneralRepositoryInterface repository;

	/**
	 *	Count the portions ready
	 */

	private int portionsReady;

	/**
	 *	Count the portions been delivered
	 */

	private int portionsDelivery;

	/**
	 *	count courses delivered
	 */

	private int coursesDelivery;

	/**
	 *	control if a order has been requested
	 */
	private boolean startOrder;

	/**
	 *	start preparation
	 */
	private  boolean startPreparation;

	/**
	 * Count entities that requested shutdown
	 */
	private int entities;

	/**
	 * Instantiation of Kitchen object
	 *
	 * @param repository repository of information
	 */
	public Kitchen(GeneralRepositoryInterface repository)
	{
		this.repository = repository;
		this.portionsReady = 0;
		this.portionsDelivery = 0;
		this.coursesDelivery = 0;
		this.startOrder = false;
		this.startPreparation = false;

	}
	
	
	/**
	 * 	Part of the chef lifecycle to signal that is waiting the order
	 */
	public synchronized void watch_news() throws RemoteException {
		((Chef) Thread.currentThread()).setChefState(States.WAIT_FOR_AN_ORDER);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());
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
	public synchronized void start_preparation() throws RemoteException
	{

		((Chef) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());
		startPreparation = true;
		//Notify waiter
		notifyAll();
	}


	
	
	/**
	 * 	Part of the chef lifecycle to signal that the preparation was continued
	 */
	public synchronized void proceed_preparation() throws RemoteException
	{
		((Chef) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());

		portionsReady++;
	}

	
	
	
	
	
	/**
	 * 	Part of the chef lifecycle to check if he needs to prepare another portion or not
	 */

	public synchronized boolean have_all_portions_been_delivered() throws RemoteException
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

	public synchronized boolean has_the_order_been_completed() throws RemoteException
	{
		if (coursesDelivery == SimulPar.M)
			return true;
		return false;
	}

	
	/**
	 * 	Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
	 */

	public synchronized void continue_preparation() throws RemoteException
	{
		((Chef) Thread.currentThread()).setChefState(States.PREPARING_A_COURSE);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());
	}
	
	
	/**
	 * 	Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
	 */

	public synchronized void have_next_portion_ready() throws RemoteException
	{	

		((Chef) Thread.currentThread()).setChefState(States.DISHING_THE_PORTIONS);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());

		portionsReady++;

		((Chef) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());
		
		//Notify waiter
		notifyAll();
	}
	
	/**
	 * 	Part of the chef lifecycle when the order has completed
	 */

	public synchronized void clean_up() throws RemoteException
	{
		((Chef) Thread.currentThread()).setChefState(States.CLOSING_SERVICE);
		repository.setChefState(((Chef) Thread.currentThread()).getChefState());
	}
	
	/**
	 * 	Part of the waiter lifecycle to signal the waiter that a new order was started
	 */

	public synchronized void hand_note_to_the_chef() throws RemoteException {
		((Waiter) Thread.currentThread()).setWaiterState(States.PLACING_THE_ORDER);
		repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
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
	 * 	Part of the waiter lifecycle to signal that he is returning to bar
	 */
	
	public synchronized void return_to_bar() throws RemoteException
	{
		((Waiter) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
		repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
	}

	
	/**
	 * 	Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
	 */
	public synchronized void collectPortion() throws RemoteException
	{
		((Waiter) Thread.currentThread()).setWaiterState(States.WAITING_FOR_AN_PORTION);
		repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());

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
		
		repository.setnPortions(portionsDelivery);
		repository.setnCourses(coursesDelivery+1);
		
		//Notify chef
		notifyAll();
		
	}

	/**
	 * Operation kitchen server shutdown
	 */
	public synchronized void shutdown() throws RemoteException {
		entities += 1;
		if (entities >= 1)
			KitchenMain.shutdown();
		notifyAll();
	}

}
