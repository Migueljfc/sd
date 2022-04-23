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
	private MemFIFO<Request> requests;
	
	/**
	   *   Control if the waiter already said good bye to all students
	*/
	private int goodByesSaid;
	
	/**
	   *   Current student that is being attended 
	*/
	private int studentId;
	/**
	   *   Control if waiter is busy
	*/
	private boolean waiterIsBusy;
	
	/**
	 * Control if the course has finished
	 */
	private boolean finishCourse;
	
	
private GeneralRepository repository;
	
	public Bar(GeneralRepository repository, Table table) {
		this.repository = repository;
		this.table = table;
		this.goodByesSaid = 0;
		
		students = new Student[SimulPar.N]; 
		
		for(int i = 0; i < SimulPar.N; i++) {
			students[i] = null;
		}
		
		try {
			requests = new MemFIFO<> (new Request [SimulPar.N*SimulPar.M]);
		}
		catch (MemException e)
	      { GenericIO.writelnString ("Instantiation of portions FIFO failed: " + e.getMessage ());
	        requests = null;
	        System.exit (1);
	      }
		
		


	}
	
	public synchronized int look_arround() {
		Request req = null;
		while(!waiterIsBusy) {
			try {
				wait();
			}
			catch (InterruptedException e) {
				
			}
			
		}
		
		try {
				req = requests.read();
				waiterIsBusy = true;
				
		}
		catch (MemException e) {
			System.exit(1);  //NAO SEI SE TA BEM
		}
		
		studentId = req.studentId;
		return req.requestId;
		
		
	}

	public void enter() {
		synchronized (this) {
			Student st = (Student) Thread.currentThread();
			int id = st.getStudentId();
			students[id] = st;
			students[id].setStudentState(States.GOING_TO_THE_RESTAURANT);
			
			try {
				requests.write(new Request(id,0));
				
			}
			catch (MemException e) {
				
			}
			
			waiterIsBusy = true; //NAO SEI SE TA BEM
			
			students[id].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);
			repository.setStudentState(id,st.getStudentState());
			repository.setStudentSeat(id);
			
			notifyAll();
			
		}
		
		table.seat();
	}

	public void exit() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		Request req = new Request(id, 4);
		
		try {
			requests.write(req);
		}
		catch (MemException e) {
		
		}
		waiterIsBusy = true;
		students[id].setStudentState(States.GOING_HOME);
		repository.setStudentState(id,st.getStudentState());
	
		
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
