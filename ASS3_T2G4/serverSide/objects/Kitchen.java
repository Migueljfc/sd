package serverSide.objects;

import clientSide.entities.Chef;
import clientSide.entities.States;
import clientSide.entities.Waiter;
import genclass.GenericIO;
import interfaces.GeneralRepositoryInterface;
import interfaces.KitchenInterface;
import serverSide.main.KitchenMain;
import serverSide.main.SimulPar;

import java.rmi.RemoteException;

/**
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
	 * Reference to the chef thread
	 */
	private Thread chef;

	/**
	 * Reference to the waiter thread
	 */
	private Thread waiter;

	/**
	 * Instantiation of Kitchen object
	 *
	 * @param repository repository of information
	 * @param chef
	 */
	public Kitchen(GeneralRepositoryInterface repository, Thread chef)
	{
		this.repository = repository;
		this.chef = new Thread();
		this.waiter = new Thread();
		this.portionsReady = 0;
		this.portionsDelivery = 0;
		this.coursesDelivery = 0;
		this.startOrder = false;
		this.startPreparation = false;

	}
	
	/**
	 * 	Part of the chef lifecycle to signal that is waiting the order
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */
	public synchronized int watch_news() throws RemoteException {
		int chefState;
		chefState = States.WAIT_FOR_AN_ORDER;
		try{
			repository.setChefState(chefState);

		}catch (RemoteException e){
			GenericIO.writelnString("remote exception on watch_news - : setChefState" + e.getMessage ());
		}
		while(!startOrder) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		return chefState;


	}
	
	

	/**
	 *  Part of the chef lifecycle to start the preparation and signal the waiter of that
	 *
	 *  @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */
	public synchronized int start_preparation() throws RemoteException
	{

		int chefState;
		chefState = States.PREPARING_A_COURSE;
		try{
			repository.setChefState(chefState);

		}catch (RemoteException e){
			GenericIO.writelnString("remote exception on start_preparation - : setChefState" + e.getMessage ());
		}
		startPreparation = true;
		//Notify waiter
		notifyAll();
		return chefState;
	}


	
	
	/**
	 * 	Part of the chef lifecycle to signal that the preparation was continued
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */
	public synchronized int proceed_preparation() throws RemoteException
	{
		int chefState;
		chef = Thread.currentThread();
		chefState = States.DISHING_THE_PORTIONS;
		try{
			repository.setChefState(chefState);

		}catch (RemoteException e){
			GenericIO.writelnString("remote exception on proced_preparation - : setChefState" + e.getMessage ());
		}

		portionsReady++;
		return chefState;
	}

	
	
	
	
	
	/**
	 * 	Part of the chef lifecycle to check if he needs to prepare another portion or not
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
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
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */

	public synchronized boolean has_the_order_been_completed() throws RemoteException
	{
		if (coursesDelivery == SimulPar.M)
			return true;
		return false;
	}

	
	/**
	 * 	Part of the chef lifecycle when we need to continue the preparation of another portion of the same course
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */

	public synchronized int continue_preparation() throws RemoteException
	{
		int chefState;
		chefState = States.PREPARING_A_COURSE;
		try{
			repository.setChefState(chefState);

		}catch (RemoteException e){
			GenericIO.writelnString("remote exception on continue_preparation - : setChefState" + e.getMessage ());
		}

		return chefState;
	}
	
	
	/**
	 * 	Part of the chef lifecycle to signal waiter that a portion has ready to be delivered
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */

	public synchronized int have_next_portion_ready() throws RemoteException
	{
		int chefState;
		chef = Thread.currentThread();
		chefState = States.DISHING_THE_PORTIONS;
		try{
			repository.setChefState(chefState);
		} catch (RemoteException e){
			GenericIO.writelnString("remote exception on have_next_portion_ready - : setChefState" + e.getMessage ());
		}

		portionsReady++;

		chef = Thread.currentThread();
		chefState = States.DELIVERING_THE_PORTIONS;
		try{
			repository.setChefState(chefState);
		} catch (RemoteException e){
			GenericIO.writelnString("remote exception on have_next_portion_ready - : setChefState" + e.getMessage ());
		}
		
		//Notify waiter
		notifyAll();
		return chefState;
	}
	
	/**
	 * 	Part of the chef lifecycle when the order has completed
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */

	public synchronized int clean_up() throws RemoteException
	{
		int chefState;
		chefState = States.CLOSING_SERVICE;
		try{
			repository.setChefState(chefState);

		}catch (RemoteException e){
			GenericIO.writelnString("remote exception on clean_up - : setChefState" + e.getMessage ());
		}

		return chefState;
	}
	
	/**
	 * 	Part of the waiter lifecycle to signal the waiter that a new order was started
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
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
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */
	
	public synchronized void return_to_bar() throws RemoteException
	{
		((Waiter) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
		repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
	}

	
	/**
	 * 	Part of the waiter lifecycle when he is waiting for a portion and one is ready and will be delivered
	 *
	 * 	@throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
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
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */
	public synchronized void shutdown() throws RemoteException {
		entities += 1;
		if (entities >= 1)
			KitchenMain.shutdown();
		notifyAll();
	}

}
