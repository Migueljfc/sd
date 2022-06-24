package clientSide.entities;


import interfaces.BarInterface;
import interfaces.KitchenInterface;
import interfaces.TableInterface;

import java.rmi.RemoteException;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary
 * This datatype implements the Waiter thread
 */

public class Waiter extends Thread{

	/**
	 * 	Waiter state
	 */
	
	private int state;
	
	/**
	 * Reference to the kitchen
	 */
	
	private final KitchenInterface kitchen;
	
	/**
	 * Reference to the bar
	 */
	
	private final BarInterface bar;
	
	/**
	 * Reference to the table
	 */
	private final TableInterface table;
	
	/**
	 * Set waiter state
	 * 	@param  state
	 */
	
	public void setWaiterState(int state) {
		this.state = state;
	}
	
	/**
	 * Get waiter state
	 * 	@return waiter state
	 */

	public int getWaiterState() {
		return state;
	}
	
	/**
	 * 	Instantiation of waiter thread
	 *
	 * @param name  thread name
	 * @param table reference to the table
	 * @param kitchen  reference to the kitchen
	 * @param bar  reference to the bar
	 */
	
	public Waiter(String name, TableInterface table, BarInterface bar, KitchenInterface kitchen) {
		super(name);
		this.state = States.APPRAISING_SITUATION;
		this.kitchen = kitchen;
		this.bar = bar;
		this.table = table;
	}
	
	/**
	 *	Life cycle of the waiter
	 */

	@Override
	public void run() {
		int requestId, studentId;
		boolean all_left = false;
		while (!all_left) {
			requestId = look_around();
			studentId = getCurrentStudent();

			if (requestId == 0) {
				salute_client(studentId);
				return_to_bar();
			} else if (requestId == 1) {
				get_the_pad();
				hand_note_to_the_chef();
				return_to_bar();
			} else if (requestId == 2) {
				while (!have_all_portions_delivered()) {
					collectPortion();
					deliver_portion();
				}
				return_to_bar();
			} else if (requestId == 3) {
				prepare_the_bill();
				present_the_bill();
				return_to_bar();
			} else if (requestId == 4) {
				all_left = say_goodbye();
			}
		}
	}

	private void look_around(){
		try{
			state = bar.look_around();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void getCurrentStudent(){
		try{
			state = bar.getCurrentStudent();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void salute_client(){
		try{
			state = table.salute_client();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void return_to_bar(){
		try{
			state = table.return_to_bar();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void get_the_pad(){
		try{
			state = table.get_the_pad();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void hand_note_to_the_chef(){
		try{
			state = kitchen.hand_note_to_the_chef();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void return_to_bar(){
		try{
			state = table.return_to_bar();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void have_all_portions_delivered(){
		try{
			state = table.have_all_portions_delivered();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void collectPortion(){
		try{
			state = kitchen.collectPortion();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void deliver_portion(){
		try{
			state = table.deliver_portion();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void prepare_the_bill(){
		try{
			state = bar.prepare_the_bill();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void present_the_bill(){
		try{
			state = table.present_the_bill();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void say_goodbye(){
		try{
			state = bar.say_goodbye();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}



}

