package clientSide.entities;

import interfaces.BarInterface;
import interfaces.GeneralRepositoryInterface;
import interfaces.TableInterface;
import serverSide.main.SimulPar;

import java.rmi.RemoteException;

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
	private int state;

	/**
	 * Table reference
	 */
	private TableInterface table;

	/**
	 * Bar reference
	 */
	private BarInterface bar;

	/**
	 * Repository reference
	 */
	private GeneralRepositoryInterface repository;

	/**
	 * Instantition of student
	 * @param name  thread name
	 * @param table reference to the student table
	 *
	 */
	public Student(String name, int id, TableInterface table, BarInterface bar) {
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
	public int getStudentState() {
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
	public void setStudentState(int s) {
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

	/**
	 * lifecycle of student
	 */
	@Override
	public void run() {
		int current_course = 0;
		walk_a_bit();
		enter();
		read_menu();
		
		if(id == getFirstStudent())
		{
			prepare_the_order();
			while(!has_everybody_chosen())
				add_up_ones_choice();
			call_the_waiter();
			describe_the_order();
			join_the_talk();
		}
		else
			inform_companion();

		while(!have_all_courses_delivery()) {
			start_eating();
			end_eating();
			current_course++;

			while (!has_everybody_finished()) ;
			if (id == getLastToEat() && current_course != SimulPar.M)
				signal_the_waiter();
		}
		if(should_have_arrived_earlier())
		{
			signal_the_waiter();
			honour_the_bill();
		}
		exit();
	}

	private void walk_a_bit() {
		try {
			sleep((long) (1 + 50 * Math.random()));
		} catch (InterruptedException e) {
		}

	}

	private void enter(){
		try{
			state = bar.enter();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void read_menu(){
		try{
			state = table.read_menu();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void getFirstStudent(){
		try{
			state = table.getfirstStudent();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	private void prepare_the_order(){
		try{
			state = table.prepare_the_order();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void has_everybody_chosen(){
		try{
			state = table.has_everybody_chosen();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void add_up_ones_choice(){
		try{
			state = table.add_up_ones_choice();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void call_the_waiter(){
		try{
			state = bar.call_the_waiter();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void describe_the_order(){
		try{
			state = table.describe_the_order();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void join_the_talk(){
		try{
			state = table.join_the_talk();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void inform_companion(){
		try{
			state = table.inform_companion();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	private void have_all_courses_delivery(){
		try{
			state = table.have_all_courses_delivery();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void start_eating(){
		try{
			state = table.start_eating();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void end_eating(){
		try{
			state = table.end_eating();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void has_everybody_finished(){
		try{
			state = table.has_everybody_finished();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void getLastToEat(){
		try{
			state = table.getLastToEat();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void signal_the_waiter(){
		try{
			state = bar.signal_the_waiter();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void should_have_arrived_earlier(){
		try{
			state = table.should_have_arrived_earlier();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void honour_the_bill(){
		try{
			state = table.honor_the_bill();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void exit(){
		try{
			state = bar.exit();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}


}