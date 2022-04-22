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
	private char waiterState;
	
	
	
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
	
	public synchronized void look_arround() {
		while(!waiterIsBusy) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				
			}
		}
		
		try {
			
		}
		
	}
}
