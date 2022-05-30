package serverSide.sharedRegions;

import clientSide.entities.Chef;
import clientSide.entities.States;
import clientSide.entities.Student;
import clientSide.entities.Waiter;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.GeneralRepositoryStub;
import clientSide.stubs.TableStub;
import commInfra.MemFIFO;
import commInfra.MemException;
import genclass.GenericIO;
import serverSide.entities.BarClientProxy;
import serverSide.main.BarMain;
import serverSide.main.SimulPar;


/**
 *  @summary
 * Implementation of the Bar shared region
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class Bar 
{
	/**
	 *   Number of entity requesting the shutdown.
	 */
	private int entities;

	/**
	 * Reference to the table
	 */
	private final TableStub table;

	/**
	 * Reference to students threads
	 */
	private final BarClientProxy[] students;

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
	private int currentStudent;

	/**
	 * Control if the Chef has completed the course
	 */
	private boolean courseHasReady;

	/**
	 * Count the number of requests that have not been attended
	 */
	private int requestsCount;

	/**
	 * Count the number of students in restaurant
	 */
	public static int studentCount;

	/**
	 * Reference to the general repository
	 */
	private GeneralRepositoryStub repository;
	
	
	/**
	 * Bar instantiation
	 * 
	 * @param repository reference to the general repository 
	 */
	public Bar(GeneralRepositoryStub repository, TableStub table) {
		this.repository = repository;
		this.table = table;
		this.courseHasReady = true;
		this.currentStudent = -1;
		this.entities = 0;

		students = new BarClientProxy[SimulPar.N];
		this.goodbyeIds = new int[SimulPar.N];
		for (int i = 0; i < SimulPar.N; i++){
			goodbyeIds[i] = -1;
			students[i] = null;
		}


		try {
			requests = new MemFIFO<>(new Request[SimulPar.N * SimulPar.M]);
		} catch (MemException e) {
			GenericIO.writelnString("Instantiation of requests FIFO failed: " + e.getMessage());
			requests = null;
			System.exit(1);
		}

	}




	/**
	 * Part of the chef lifecycle is called to alert the waiter that a portion has ready
	 */
	public synchronized void alert_the_waiter()
	{
		while(!courseHasReady)
		{
			try {
				wait();
			} catch (InterruptedException e1) {
			}
		}
		
		Request req = new Request(SimulPar.N+1,2);

		try {
			requests.write(req);
		} catch (MemException e) {

		}

		requestsCount++;
		courseHasReady = false;

		((BarClientProxy) Thread.currentThread()).setChefState(States.DELIVERING_THE_PORTIONS);
		repository.setChefState(((BarClientProxy) Thread.currentThread()).getChefState());

		//Notify waiter
		notifyAll();
	}



	/**
	 * Is the part of the waiter life cycle where he waits for requests or served the pending and returns the id of the request
	 * @return request id
	 */
	public synchronized int look_arround()
	{
		Request req;
		
		while(requestsCount == 0)
		{
			try {
				wait();
			} catch (InterruptedException e) {
	
			}
		}
		
		try 
		{
			req = requests.read();
			requestsCount--;
		}
		catch (MemException e)
		{	
			e.printStackTrace();
			return 0;
		}		

		currentStudent = req.requestorId;
		
		return req.requestId;
	}
	
	
	
	
	
	/**
	 * Part of the waiter lifecycle to update his state to signal that is preparing the bill
	 */
	public synchronized void prepare_the_bill()
	{
		((BarClientProxy) Thread.currentThread()).setWaiterState(States.PROCESSING_THE_BILL);
		repository.setWaiterState(((BarClientProxy) Thread.currentThread()).getWaiterState());
	}
	
	
	
	
	/**
	 * Part of the waiter lifecycle to say goodbye to the students when we signal that wants to go home
	 * @return true if all students left the restaurant
	 */
	public synchronized boolean say_goodbye()
	{
		goodbyeIds[currentStudent] = currentStudent;
		//Notify student
		notifyAll();

		studentCount--;
		repository.setSeatsAtLeaving(currentStudent);
		currentStudent = -1;
		
		repository.setWaiterState(((BarClientProxy) Thread.currentThread()).getWaiterState());

		if(studentCount == 0)
			return true;
		return false;
		
	}




	/**
	 * Is the part of student life cycle when we decide to enter the restaurant adding a new request to wake up the waiter
	 */
	public void enter()
	{		
		synchronized(this)
		{
			int id = ((BarClientProxy) Thread.currentThread()).getStudentId();

			students[id] = ((BarClientProxy) Thread.currentThread());
			students[id].setStudentState(States.GOING_TO_THE_RESTAURANT);
			
			studentCount++;

			try {
				requests.write(new Request(id,0));
			} catch (MemException e) {

			}

			requestsCount++;
			if(studentCount == 1) {
				repository.setFirstStudent(id);
			}
			else if (studentCount == SimulPar.N){
				repository.setLastStudent(id);
			}
			students[id].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);
			repository.setStudentState(id, ((BarClientProxy) Thread.currentThread()).getStudentState());
			repository.setStudentSeat(studentCount-1, id);
			//Notify waiter
			notifyAll();
		}

		table.seat();

	}
	
	
	
	
	
	/**
	 * Part of the 1º student lifecycle to alert the waiter that the order has ready to he get
	 */
	public synchronized void call_the_waiter()
	{
		int id = ((BarClientProxy) Thread.currentThread()).getStudentId();
		Request req = new Request(id,1);

		try {
			requests.write(req);
		} catch (MemException e) {

		}
		requestsCount++;	

		notifyAll();
	}

	
	/**
	 * Part of the student lifecycle to signal the waiter that he ends the current course or that the last student wants to pay the bill
	 */
	public synchronized void signal_the_waiter()
	{
		int id = ((BarClientProxy) Thread.currentThread()).getStudentId();

		if(((BarClientProxy) Thread.currentThread()).getStudentState() == States.PAYING_THE_BILL)
		{
			try {
				requests.write(new Request(id, 3));
			} catch (MemException e) {

			}

			requestsCount++;	

			notifyAll();
			
		}
		else
		{
			courseHasReady = true;		
			//Notify chef
			notifyAll();
		}
	}




	/**
	 * Is the part of student life cycle when we decide to leave the restaurant adding a new request to wake up the waiter
	 */
	public synchronized void exit()
	{
		int id = ((BarClientProxy) Thread.currentThread()).getStudentId();
		Request req = new Request(id,4);

		try {
			requests.write(req);
		} catch (MemException e) {

		}

		requestsCount++;
		//Notify waiter
		notifyAll();

		students[id].setStudentState(States.GOING_HOME);
		repository.setStudentState(id, ((BarClientProxy) Thread.currentThread()).getStudentState());

		while(goodbyeIds[id] == -1) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
	}
	/**
	 * @return Id of the student that are being attended
	 */
	public int getCurrentStudent() { return currentStudent; }

	/**
	 *   Operation server shutdown.
	 *
	 *   New operation.
	 */
	public synchronized void shutdown() {
		entities += 1;
		if (entities >= 3)
			BarMain.waitConnection = false;
		notifyAll();
	}
}




