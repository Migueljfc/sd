package clientSide.entities;


import genclass.GenericIO;
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
				kitchenReturnBar();
			} else if (requestId == 1) {
				get_the_pad();
				hand_note_to_the_chef();
				tableReturnBar();
			} else if (requestId == 2) {
				while (!have_all_portions_delivered()) {
					collectPortion();
					deliver_portion();
				}
				tableReturnBar();
			} else if (requestId == 3) {
				prepare_the_bill();
				present_the_bill();
				tableReturnBar();
			} else if (requestId == 4) {
				all_left = say_goodbye();
			}
		}
	}

	private int look_around(){
		int requestId;
		try{
			requestId = bar.look_around();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		if(requestId!=0 && requestId!=1 && requestId!=2 && requestId!=3 && requestId!=4)
		{
			GenericIO.writelnString("Invalid service type!");
			System.exit(1);
		}
		return requestId;
	}

	private int getCurrentStudent(){
		int studentId = -1;
		try{
			studentId = bar.getCurrentStudent();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		if(studentId == -1)
		{
			GenericIO.writelnString("Invalid student id!");
			System.exit(1);
		}

		return studentId;
	}

	private void salute_client(int studentId){
		try{
			state = table.salute_client(studentId);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void kitchenReturnBar(){
		try{
			state = kitchen.return_to_bar();
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

	private void tableReturnBar(){
		try{
			state = table.return_to_bar();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean have_all_portions_delivered(){
		boolean deliver = false;
		try{
			deliver = table.have_all_portions_delivered();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return deliver;
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
			table.deliver_portion();
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

	private boolean say_goodbye(){
		boolean goodbye = false;
		try{
			goodbye = bar.say_goodbye();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return goodbye;
	}



}

