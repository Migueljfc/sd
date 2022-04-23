/**
 * 
 */
package sharedRegions;

import entities.*;
import main.SimulPar;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */


public class Table {
	private GeneralRepository repository;

	private int seats[];
	
	private final Student students[];
	
	public Table(GeneralRepository repository) {
		this.repository = repository;
		seats = new int[SimulPar.N];
		students = new Student[SimulPar.N];
		for(int i = 0; i < SimulPar.N; i++) {
			seats[i] = -1;
			students[i] = null;
		}
		
	
	}

	public void salute_client(int studentId) {
		Waiter w = (Waiter) Thread.currentThread();
		w.setWaiterState(States.PRESENTING_THE_MENU);
		repository.setWaiterState(w.getWaiterState());
		
		
		
		
	}

	public void prepare_the_order() {
		// TODO Auto-generated method stub
		
	}

	public boolean has_everybody_chosen() {
		// TODO Auto-generated method stub
		return false;
	}

	public void add_up_ones_choice() {
		// TODO Auto-generated method stub
		
	}

	public void call_the_waiter() {
		// TODO Auto-generated method stub
		
	}

	public void describe_the_order() {
		// TODO Auto-generated method stub
		
	}

	public void join_the_talk() {
		// TODO Auto-generated method stub
		
	}

	public void inform_companion() {
		// TODO Auto-generated method stub
		
	}

	public boolean have_all_courses_delivery() {
		// TODO Auto-generated method stub
		return false;
	}

	public void start_eating() {
		// TODO Auto-generated method stub
		
	}

	public boolean has_everybody_finished() {
		// TODO Auto-generated method stub
		return false;
	}

	public int last_finished() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void signal_the_waiter() {
		// TODO Auto-generated method stub
		
	}

	public boolean should_have_arrived_earlier() {
		// TODO Auto-generated method stub
		return false;
	}

	public void honor_the_bill() {
		// TODO Auto-generated method stub
		
	}

	public void presenting_menu() {
		// TODO Auto-generated method stub
		
	}

	public void get_the_pad() {
		// TODO Auto-generated method stub
		
	}

	public boolean have_all_portions_delivered() {
		// TODO Auto-generated method stub
		return false;
	}

	public void deliver_portion() {
		// TODO Auto-generated method stub
		
	}

	public void return_to_bar() {
		// TODO Auto-generated method stub
		
	}

	public void seat() {
		// TODO Auto-generated method stub
		
	}

	public void present_the_bill() {
		// TODO Auto-generated method stub
		
	}

	public void read_the_menu() {
		// TODO Auto-generated method stub
		
	}

	public void end_eating() {
		// TODO Auto-generated method stub
		
	}

}
