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
	 * Reference to students threads
	 */
	private final Student[] students;

	/**
	 * Waiting portions
	 */
	private MemFIFO<Request> requests;

	/**
	 * Array to control if the waiter already said good bye to students
	 */
	private final int[] goodbyeIds;

	/**
	 * Current student that is being attended
	 */
	private int studentId;
	/**
	 * Control if waiter is busy
	 */
	private boolean waiterIsBusy;

	/**
	 * Control if the Chef has completed the course
	 */
	private boolean courseHasReady;
	
	/**
	 * Count the number of good byes said by the waiter
	 */
	private int goodbyesCount;
	
	/**
	 * Count the number of students in restaurant
	 */
	private int studentCount;
	

	private GeneralRepository repository;

	public Bar(GeneralRepository repository, Table table) {
		this.repository = repository;
		this.table = table;
		this.goodbyesCount = 0;
		this.courseHasReady = true;
		this.studentCount = 0;
		
		goodbyeIds = new int[SimulPar.N];
		students = new Student[SimulPar.N];

		for (int i = 0; i < SimulPar.N; i++) {
			students[i] = null;
			goodbyeIds[i] = -1;
		}

		try {
			requests = new MemFIFO<>(new Request[SimulPar.N * SimulPar.M]);
		} catch (MemException e) {
			GenericIO.writelnString("Instantiation of portions FIFO failed: " + e.getMessage());
			requests = null;
			System.exit(1);
		}

	}

	public synchronized int look_arround() {
		Request req = null;
		while (!waiterIsBusy) {
			try {
				wait();
			} catch (InterruptedException e) {

			}

		}

		try {
			req = requests.read();
			waiterIsBusy = false;

		} catch (MemException e) {
			System.exit(1); // NAO SEI SE TA BEM
		}

		studentId = req.requestorId;
		return req.requestId;

	}

	public synchronized void enter() {
		synchronized (this) {
			Student st = (Student) Thread.currentThread();
			int id = st.getStudentId();
			students[id] = st;
			students[id].setStudentState(States.GOING_TO_THE_RESTAURANT);
			
			studentCount++;
			
			if(studentCount == 1) {
				repository.setFirstStudent(id);
			}
			else if(studentCount == SimulPar.N){
				repository.setLastStudent(id);
			}
			try {
				requests.write(new Request(id, 0));

			} catch (MemException e) {

			}
			
			waiterIsBusy = true; // NAO SEI SE TA BEM
			
		
			
			
			students[id].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);
			repository.setStudentState(id, st.getStudentState());
			repository.setStudentSeat(id,studentCount);

			notifyAll();

		}

		table.seat();
	}

	public synchronized void exit() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		Request req = new Request(id, 4);

		try {
			requests.write(req);
		} catch (MemException e) {

		}
		waiterIsBusy = true;
		students[id].setStudentState(States.GOING_HOME);
		repository.setStudentState(id, st.getStudentState());
		while (!Arrays.asList(goodbyeIds).contains(id)) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

	}

	public synchronized void alert_waiter() {
		while (!courseHasReady) {
			try {
				wait();
			} catch (InterruptedException e) {
			}

			Request req = new Request(SimulPar.N + 1, 2);
			try {
				requests.write(req);
			} catch (MemException e) {

			}
			waiterIsBusy = true;
			courseHasReady = false;

			Chef c = (Chef) Thread.currentThread();
			c.setChefState(States.DELIVERING_THE_PORTIONS);
			repository.setChefState(c.getChefState());

			// notify waiter
			notifyAll();
		}
	}

	public synchronized void prepare_the_bill() {
		Waiter w = (Waiter) Thread.currentThread();
		w.setWaiterState(States.PROCESSING_THE_BILL);
		repository.setWaiterState(w.getWaiterState());

	}

	public synchronized boolean say_goodbey() {
		goodbyeIds[goodbyesCount++] = studentId;
		notifyAll();
		studentId = -1;

		Waiter w = (Waiter) Thread.currentThread();
		repository.setWaiterState(w.getWaiterState());

		if (goodbyesCount == SimulPar.N) {
			return true;
		}
		return false;
	}

	public synchronized void call_the_waiter() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		Request req = new Request(id, 1);

		try {
			requests.write(req);
		} catch (MemException e) {

		}

		waiterIsBusy = true;

		// notify waiter
		notifyAll();

	}

	public synchronized void signal_the_waiter() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		if (st.getStudentState() == States.PAYING_THE_BILL) {

			Request req = new Request(id, 3);

			try {
				requests.write(req);
			} catch (MemException e) {

			}

			waiterIsBusy = true;
		} else {
			courseHasReady = true;

			// notify chef
			notifyAll();
		}
	}

	public int getStudentId() {
		return studentId;
	}


}
