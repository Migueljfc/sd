package clientSide.entities;

import commInfra.ReturnValue;
import genclass.GenericIO;
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
			state = bar.enter(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void read_menu(){
		try{
			state = table.read_menu(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private int getFirstStudent(){
		int studentId = -1;
		try{
			studentId = table.getfirstStudent();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		if (studentId == -1)
		{
			GenericIO.writelnString("Invalid id received in getFirstToArrive");
			System.exit(1);
		}
		return studentId;
	}
	private void prepare_the_order(){
		try{
			state = table.prepare_the_order();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean has_everybody_chosen(){
		boolean everybodyChose = false;
		try{
			everybodyChose = table.has_everybody_chosen();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return everybodyChose;
	}

	private void add_up_ones_choice(){
		try{
			table.add_up_ones_choice();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void call_the_waiter(){
		try{
			bar.call_the_waiter(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void describe_the_order(){
		try{
			table.describe_the_order();
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
			state = table.inform_companion(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}
	private boolean have_all_courses_delivery(){
		boolean coursesDeliver = false;
		try{
			coursesDeliver = table.have_all_courses_delivery();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return coursesDeliver;
	}

	private void start_eating(){
		try{
			state = table.start_eating(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void end_eating(){
		try{
			state = table.end_eating(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean has_everybody_finished(){
		boolean everybodyFinish = false;
		try{
			everybodyFinish = table.has_everybody_finished();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return everybodyFinish;
	}

	private int getLastToEat(){
		int studentId = -1;
		try{
			studentId = table.getLastToEat();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return studentId;
	}

	private void signal_the_waiter(){
		try{
			bar.signal_the_waiter(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean should_have_arrived_earlier(){
		boolean value = false;
		try{
			value = table.should_have_arrived_earlier(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		return value;
	}

	private void honour_the_bill(){
		try{
			table.honor_the_bill();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}

	private void exit(){
		try{
			state = bar.exit(id);
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
	}


}