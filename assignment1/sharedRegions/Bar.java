/**
 * 
 */
package sharedRegions;

import java.util.Arrays;
import entities.*;
import genclass.GenericIO;
import main.SimulPar;
import commInfra.*;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */
public class Bar {
	
	private final Table table;
	
	/**
	   *   Reference to students threads
	*/
	private final Student[] students;
	
	/**
	   *   Waiting portions
	*/
	private MemFIFO<Integer> portions;
	
	/**
	   *   Control if the waiter already said good bye to all students
	*/
	private boolean alreadySaidGoodbyeToAll;
	
	/**
	   *   Current student that is being attended 
	*/
	private int studentId;
	/**
	   *   Control if waiter is busy
	*/
	private boolean waiterIsBusy;
	
	/**
	   *   Saves the current waiter state
	*/
	private int waiterState; //apraising situation
	
	/**
	 * Control if the course has finished
	 */
	private boolean finishCourse;
	
	
private GeneralRepository repository;
	
	public Bar(GeneralRepository repository, Table table) {
		this.repository = repository;
		this.table = table;
		
		students = new Student[SimulPar.N]; 
		
		for(int i = 0; i < SimulPar.N; i++) {
			students[i] = null;
		}
		
		try {
			portions = new MemFIFO<> (new Integer [SimulPar.N*SimulPar.M]);
		}
		catch (MemException e)
	      { GenericIO.writelnString ("Instantiation of portions FIFO failed: " + e.getMessage ());
	        portions = null;
	        System.exit (1);
	      }


	}
	
	public synchronized int look_arround() {
		while(!waiterIsBusy) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				
			}
		}
		
		try {
			if() {
				
			}
		}
		
	}

	public void enter() {
		// TODO Auto-generated method stub
		
	}

	public void exit() {
		// TODO Auto-generated method stub
		
	}

	public void alert_waiter() {
		// TODO Auto-generated method stub
		
	}

	public void salute_client() {
		// TODO Auto-generated method stub
		
	}

	public void return_to_bar() {
		// TODO Auto-generated method stub
		
	}

	public void present_the_bill() {
		// TODO Auto-generated method stub
		
	}

	public void prepare_the_bill() {
		// TODO Auto-generated method stub
		
	}

	public void say_godbey() {
		// TODO Auto-generated method stub
		
	}

}
