/**
 * 
 */
package clientSide.entities;

import clientSide.stubs.BarStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.TableStub;
import serverSide.main.SimulPar;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 * @summary
 * This datatype implements the Student thread
 */

public class Student extends Thread {

	/**
	 * Student identification.
	 */
	private int id;

	/**
	 * student's State.
	 */
	private States state;

	/**
	 * Table reference
	 */
	private TableStub table;

	/**
	 * Bar reference
	 */
	private BarStub bar;

	/**
	 * Repository reference
	 */
	private GeneralRepositoryStub repository;

	/**
	 * @param name  thread name
	 * @param table reference to the student table
	 * @param repository  reference to the general repository
	 */
	public Student(String name, int id, TableStub table, BarStub bar, GeneralRepositoryStub repository) {
		super(name);
		this.id = id;
		state = States.GOING_TO_THE_RESTAURANT;
		this.table = table;
		this.repository = repository;
		this.bar = bar;
	}

	/**
	 * Returns the Student's state.
	 * 
	 * @return student's current state
	 */
	public States getStudentState() {
		return state;
	}

	/**
	 * Returns the student's id
	 * 
	 * @return student's id
	 */
	public int getStudentId() {
		return id;
	}

	/**
	 * Sets the student's state.
	 * 
	 * @param s desired state
	 */
	public void setStudentState(States s) {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		state = s;

	}

	/**
	 * Sets the student's id.
	 * 
	 * @param i desired id
	 */
	public void setStudentId(int i) {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		id = i;
	}

	@Override
	public void run() {
		int current_course = 0;
		walk_a_bit();
		bar.enter();
		table.read_menu();
		
		if(id == repository.getFirstStudent())
		{
			table.prepare_the_order();
			while(!table.has_everybody_chosen())
				table.add_up_ones_choice();
			bar.call_the_waiter();
			table.describe_the_order();
			table.join_the_talk();
		}
		else
			table.inform_companion();

		while(!table.have_all_courses_delivery()) {
			table.start_eating();
			table.end_eating();
			current_course++;

			while (!table.has_everybody_finished()) ;
			if (id == repository.getLastStudent() && current_course != SimulPar.M)
				bar.signal_the_waiter();
		}
		if(table.should_have_arrived_earlier())
		{
			bar.signal_the_waiter();
			table.honor_the_bill();
		}
		bar.exit();
	}

	private void walk_a_bit() {
		try {
			sleep((long) (1 + 50 * Math.random()));
		} catch (InterruptedException e) {
		}

	}
}