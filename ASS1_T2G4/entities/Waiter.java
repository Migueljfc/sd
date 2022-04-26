package entities;

import sharedRegions.*;


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
	
	private States state;
	
	/**
	 * Reference to the kitchen
	 */
	
	private final Kitchen kitchen;
	
	/**
	 * Reference to the bar
	 */
	
	private final Bar bar;
	
	/**
	 * Reference to the table
	 */
	private final Table table;
	
	/**
	 * 	@param  state
	 */
	
	public void setWaiterState(States state) {
		this.state = state;
	}
	
	/**
	 * 	@return waiter state
	 */

	public States getWaiterState() {
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
	
	public Waiter(String name, Table table, Bar bar, Kitchen kitchen) {
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
			requestId = bar.look_arround();
			studentId = bar.getCurrentStudent();
			assert (requestId >= 0 && requestId <= 4);
			if (requestId == 0) {
				table.salute_client(studentId);
				table.return_to_bar();
			} else if (requestId == 1) {
				table.get_the_pad();
				kitchen.hand_note_to_the_ched();
				kitchen.return_to_bar();
			} else if (requestId == 2) {
				while (!table.have_all_portions_delivered()) {
					kitchen.collectPortion();
					table.deliver_portion();
				}
				table.return_to_bar();
			} else if (requestId == 3) {
				bar.prepare_the_bill();
				table.present_the_bill();
				table.return_to_bar();
			} else if (requestId == 4) {
				all_left = bar.say_goodbye();
			}
		}

	}
}
