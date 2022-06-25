package serverSide.objects;

import clientSide.entities.States;
import clientSide.entities.Student;
import clientSide.entities.Waiter;
import interfaces.GeneralRepositoryInterface;
import interfaces.TableInterface;
import serverSide.main.BarMain;
import serverSide.main.SimulPar;

import java.rmi.RemoteException;


/**
 *  @summary
 * Implementation of the Table shared region
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class Table implements TableInterface {

	/**
	 * Count the number of orders
	 */
	private int ordersCount;

	/**
	 * Count the number of finished courses
	 */
	private int finishedCourses;

	/**
	 * Number of courses eaten
	 */
	private int currentCourse;

	/**
	 * Count the number of portions Delivered
	 */
	private int portionsDelivery;

	/**
	 * Current student that is being attended
	 */
	private int currentStudent;

	/**
	 * Control if a student is reading the menu
	 */
	private boolean isReading;

	/**
	 * Control if waiter already receives the order
	 */
	private boolean receiveTheOrder;

	/**
	 * Control if first student has waiting for the other students choices
	 */
	private boolean waitingForChoices;

	/**
	 * Count the number of students that wake up after the last student end the
	 * course
	 */
	private int studentsCount;

	/**
	 * Control if the paying process already have initiated
	 */
	private boolean paying;

	/**
	 * Last student id to finish the current course
	 */
	private int lastFinish;

	/**
	 * Check if student already seated
	 */
	private int seat[];

	/**
	 * Check if student have read the menu
	 */
	private int read[];

	/**
	 * Reference to the student threads
	 */
	private final int [] students;
	
	/**
     * Reference to the GeneralRepository.
     */
    private final GeneralRepositoryInterface repository;

	/**
	 * Number of entity groups requesting the shutdown
	 */
	private int entities;

	/**
	 * last student to arrive
	 */
	private int lastStudent;

	/**
	 * first student to arrive
	 */
	private int firstStudent;

	/**
	 * last student to eat
	 */
	private int lastToEat;

	/**
	 * Instantiation of Table object
	 *
	 * @param repository reference to the general repository
	 */
    public Table(GeneralRepositoryInterface repository)
    {
		this.repository = repository;
    	this.ordersCount = 0;
    	this.finishedCourses = 0;
    	this.currentCourse = 0;
    	this.portionsDelivery = 0;
    	this.currentStudent = 0;
    	this.studentsCount = 0;
    	this.isReading = false;
    	this.receiveTheOrder = false;
    	this.waitingForChoices = false;
    	this.paying = false;
		this.firstStudent = 0;
		this.lastStudent = 0;
		this.lastToEat = 0;

    	
    	seat = new int[SimulPar.N];
    	read = new int[SimulPar.N];
		students = new int[SimulPar.N];
    	for(int i = 0; i < SimulPar.N; i++)
    	{
    		seat[i] = -1;
    		read[i] = -1;
			students[i] = -1;
    	}
		

    }

    /**
     * Part of the waiter lifecycle is called when a student enter the restaurant
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int salute_client(int id) throws RemoteException {

		currentStudent = id;

		//((Waiter) Thread.currentThread()).setWaiterState(States.PRESENTING_THE_MENU);
		//repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());

		int waiterState;
		waiterState = States.PRESENTING_THE_MENU;
		repository.setWaiterState(waiterState);

		isReading = true;

		while (seat[currentStudent] == -1) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		//Notify student
		notifyAll();

		while (read[currentStudent] == -1) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		currentStudent = -1;
		isReading = false;

		return waiterState;
	}
    
    /**
     * Part of the waiter lifecycle is called when we return to bar
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int return_to_bar() throws RemoteException
    {
    	//((Waiter) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
    	//repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
		int waiterState;
		waiterState = States.APPRAISING_SITUATION;
		repository.setWaiterState(waiterState);

		return waiterState;
    }

    
    /**
     *Part of the waiter lifecycle is called when the first student intent to describe the order
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int get_the_pad() throws RemoteException {
		//((Waiter) Thread.currentThread()).setWaiterState(States.TAKING_THE_ORDER);
		//repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
		int waiterState;
		waiterState = States.TAKING_THE_ORDER;
		repository.setWaiterState(waiterState);

		receiveTheOrder = true;

		//Notify student
		notifyAll();

		while (receiveTheOrder) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		return waiterState;

	}

    
    /**
     * Part of the waiter lifecycle is called to check if all portions have been delivered
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     * @return true if all portions been delivered
     */
    public synchronized boolean have_all_portions_delivered() throws RemoteException
    {
    	if(portionsDelivery == SimulPar.N) {
    		lastFinish = -1;
    		studentsCount = 0;
    		notifyAll();
    		return true;
    	}
    	return false;
    	
    }

    /**
	 * Part of the waiter lifecycle is used to signal that a portion have been delivered
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void deliver_portion()
    {
    	portionsDelivery++; 
    }

    
    /**
     *  Part of the waiter lifecycle is used present the bill and signal the last student to pay
	 *
	 *  @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int present_the_bill() throws RemoteException
    {
    	paying = true;
		//Notify student to pay
    	notifyAll();
    	
    	//((Waiter) Thread.currentThread()).setWaiterState(States.RECEIVING_PAYMENT);
    	//repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());

		int waiterState;
		waiterState = States.RECEIVING_PAYMENT;
		repository.setWaiterState(waiterState);

		while(paying){
			/**Fita cola preta*/
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		return waiterState;
    	
    }
    
    
    
    /**
     * Called when a student enter in the bar to register the position in the table and to wait by the waiter to present the menu
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void seat(int id) throws RemoteException
    {
    	//int id = ((Student) Thread.currentThread()).getStudentId();
    	
		//students[id] = ((Student) Thread.currentThread());
		//students[id].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);

		students[id] = States.TAKING_A_SEAT_AT_THE_TABLE;

    	seat[id] = id;

    	//notify waiter
    	notifyAll();

    	while (!(currentStudent == id && isReading == true))
    	{
	    	try {
				wait();
			} catch (InterruptedException e) {
			}
	    }

    }
    
    
    
    /**
     * Part of the student lifecycle used to update the student state and update the read array to notify that the student already read the menu
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int read_menu(int id) throws RemoteException
    {
    	//int id = ((Student) Thread.currentThread()).getStudentId();

    	//students[id].setStudentState(States.SELECTING_THE_COURSES);
    	//repository.updateStudentState(id, ((Student) Thread.currentThread()).getStudentState());

		students[id] = States.SELECTING_THE_COURSES;
		repository.updateStudentState(id, students[id]);

    	read[id] = id;

    	//Notify waiter
    	notifyAll();

		return students[id];
    }    

    
    /**
     *  Part of the 1º student lifecycle to update his state and signal that is organizing the order
	 *
	 *  @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int prepare_the_order() throws RemoteException
    {
    	ordersCount++;

    	//students[firstStudent].setStudentState(States.ORGANIZING_THE_ORDER);
    	//repository.updateStudentState(firstStudent, ((Student) Thread.currentThread()).getStudentState());

		students[firstStudent] = States.ORGANIZING_THE_ORDER;
		repository.updateStudentState(firstStudent, students[firstStudent]);

		return students[firstStudent];
    }
    
    

    
    /**
     *  Part of the 1º student lifecycle to check if the others students chosen their orders
	 *
	 *  @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     *  @return true if the others students already chosen their orders
     */
    public synchronized boolean has_everybody_chosen() throws RemoteException
    {
    	if(ordersCount == SimulPar.N)
    		return true;
    	else {

	    	while(waitingForChoices == false)
	    	{
	    		try {
					wait();
				} catch (InterruptedException e) {
				}
	    	}
	    	return false;
    	}
    	
    }
    
    
    
    
    /**
     * Part of the 1º student lifecycle to update the number os orders and notify the other students of that
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void add_up_ones_choice() throws RemoteException
    {
    	ordersCount++;
    	waitingForChoices = false;
    	//Notify students
    	notifyAll();
    }
    
    
    
    
    /**
     * Part of the 1º student lifecycle to wake up the waiter and describe the order
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void describe_the_order() throws RemoteException
    {
    	while(receiveTheOrder == false) 
    	{
	    	try {
				wait();
			} catch (InterruptedException e) {
			}
    	}

    	receiveTheOrder = false;
    	//Notify waiter
    	notifyAll();
    }
    
    
    
    
    
    /**
     * Part of the 1º student lifecycle to join the talk with the other students
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int join_the_talk() throws RemoteException {
		//students[firstStudent].setStudentState(States.CHATING_WITH_COMPANIONS);
		//repository.updateStudentState(firstStudent, ((Student) Thread.currentThread()).getStudentState());

		students[firstStudent] = States.CHATING_WITH_COMPANIONS;
		repository.updateStudentState(firstStudent, students[firstStudent]);

		return students[firstStudent];
	}
    
    
    /**
     * Part of the students' lifecycle to inform the 1º student about his course option
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int inform_companion(int id) throws RemoteException {
		//int id = ((Student) Thread.currentThread()).getStudentId();
		while (waitingForChoices) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		waitingForChoices = true;
		//notify student
		notifyAll();

		//students[id].setStudentState(States.CHATING_WITH_COMPANIONS);
		//repository.updateStudentState(id, ((Student) Thread.currentThread()).getStudentState());

		students[id] = States.CHATING_WITH_COMPANIONS;
		repository.updateStudentState(id, students[id]);

		return students[id];
	}


	/**
	 *  Part of the students' lifecycle to check if all courses have been delivered or not
	 *
	 *  @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 * 	@return true if all courses have been delivered
	 */
	public synchronized boolean have_all_courses_delivery() throws RemoteException {
		if (currentCourse == SimulPar.M)
			return true;
		else {
			while (portionsDelivery != SimulPar.N) {
				try {
					wait();
				} catch (InterruptedException e) {
				}
			}
			return false;
		}

	}
    
    
    
    /**
     * Operation start eating
     * 
     *  Part of the students' lifecycle to start eating and update his state, for simulate that is used the function sleep
	 *
	 *  @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */    
    public synchronized int start_eating(int id) throws RemoteException {
		//int id = ((Student) Thread.currentThread()).getStudentId();
		//students[id].setStudentState(States.ENJOYING_THE_MEAL);
		//repository.updateStudentState(id, ((Student) Thread.currentThread()).getStudentState());

		students[id] = States.ENJOYING_THE_MEAL;
		repository.updateStudentState(id, students[id]);

		try {
			Thread.sleep((long) (1 + 100 * Math.random()));
		} catch (InterruptedException e) {
		}

		return students[id];
	}



	/**
     * Part of the student lifecycle to update his state and signal that he end his course and register last student to eat
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized int end_eating(int id) throws RemoteException {
		//int id = ((Student) Thread.currentThread()).getStudentId();
		finishedCourses++;

		if (finishedCourses == SimulPar.N) {
			currentCourse++;
			lastFinish = id;
		}
		//students[id].setStudentState(States.CHATING_WITH_COMPANIONS);
		//repository.updateStudentState(id, ((Student) Thread.currentThread()).getStudentState());

		students[id] = States.CHATING_WITH_COMPANIONS;
		repository.updateStudentState(id, students[id]);

		return students[id];
	}
    
    
    
    
    
    /**
     * Part of the student lifecycle to wait for the last student to finish his course
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized boolean has_everybody_finished() throws RemoteException {
		int id = ((Student) Thread.currentThread()).getStudentId();


		if (id == lastFinish) {
			finishedCourses = 0;
			portionsDelivery = 0;
			studentsCount++;
			//Notify students
			notifyAll();
			while (studentsCount != SimulPar.N) {
				try {
					wait();
				} catch (InterruptedException e) {

				}
			}
		}
		while (finishedCourses != 0) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		studentsCount++;
		if (studentsCount == SimulPar.N)
			notifyAll();

		return true;
	}


	/**
	 * Part of the student lifecycle to check if he is the last to arrive and change his state to pay the bill
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 * @return True if student was the last to arrive
	 */
	public synchronized boolean should_have_arrived_earlier(int id) throws RemoteException
	{
		//int id = ((Student) Thread.currentThread()).getStudentId();

		if(id == lastStudent) {
			students[id] = States.PAYING_THE_BILL;
			repository.updateStudentState(id, students[id]);
			return true;
		}
		else
			return false;
	}

	/**
	 * Get id of the first student to enter
	 * @return id of the first student to enter in the restaurant
	 */
	public int getfirstStudent() throws RemoteException { return firstStudent; }

	/**
	 * Get id of the last student to enter
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() throws RemoteException { return lastToEat; }

	/**
	 * Set id of the first student to arrive
	 * @param id id of the first student to arrive
	 */
	public synchronized void setFirstStudent(int id) throws RemoteException { this.firstStudent = id; }

	/**
	 * Set id of the last student to arrive
	 * @param id if of the last student to arrive to the restaurant
	 */
	public synchronized void setLastStudent(int id) throws RemoteException { this.lastStudent = id; }


	/**
     * Part of the student lifecycle to wait for the waiter to give him the bill
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
     */
    public synchronized void honor_the_bill() throws RemoteException {
    	//Block waiting for waiter to present the bill
    	while(!paying)
    	{
    		try {
				wait();
			} catch (InterruptedException e) {
			}
    	}
		paying = false;
		//Notify waiter
    	notifyAll();
    }


	/**
	 * Operation bar server shutdown
	 *
	 * @throws RemoteException if either the invocation of the remote method, or the communication with the register service fails
	 */
	public synchronized void shutdown() throws RemoteException {
		entities += 1;
		if (entities >= 1)
			BarMain.shutdown();
		notifyAll();
	}
    
}
