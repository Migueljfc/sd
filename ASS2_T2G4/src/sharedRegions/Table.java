package sharedRegions;

import entities.States;
import entities.Student;
import entities.Waiter;
import mainProgram.*;


/**
 *  @summary
 * Implementation of the Table shared region
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class Table {

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
	private final Student [] students;
	
	/**
     * Reference to the GeneralRepository.
     */
    private final GeneralRepository repository;

    public Table(GeneralRepository repository)
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


    	
    	seat = new int[SimulPar.N];
    	read = new int[SimulPar.N];
		students = new Student[SimulPar.N];
    	for(int i = 0; i < SimulPar.N; i++)
    	{
    		seat[i] = -1;
    		read[i] = -1;
			students[i] = null;
    	}
		

    }
    

    
    /**
     * Part of the waiter lifecycle is called when a student enter the restaurant
     */
    public synchronized void salute_client(int id)
    {
    	currentStudent = id;

    	((Waiter) Thread.currentThread()).setWaiterState(States.PRESENTING_THE_MENU);
    	repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
    	
    	isReading = true;

    	while(seat[currentStudent] == -1)
    	{
			try {
				wait();
			} catch (InterruptedException e) {

			}
    	}

    	//Notify student
    	notifyAll();

    	while(read[currentStudent] == -1)
    	{
	    	try {
				wait();
			} catch (InterruptedException e) {
			}    
    	}
    	
    	currentStudent = -1;
    	isReading  = false;
    }
    
    
    
    
    /**
     * Part of the waiter lifecycle is called when we return to bar
     */
    public synchronized void return_to_bar()
    {
    	((Waiter) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
    	repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());    	
    }

    
    /**
     *Part of the waiter lifecycle is called when the first student intent to describe the order
     */
    public synchronized void get_the_pad()
    {
    	((Waiter) Thread.currentThread()).setWaiterState(States.TAKING_THE_ORDER);
    	repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());
    	
    	receiveTheOrder = true;
    	
    	//Notify student
    	notifyAll();

    	while(receiveTheOrder)
    	{
	    	try {
				wait();
			} catch (InterruptedException e) {

			}
    	}
    	
    }
    
    
    
    
    /**
     * Part of the waiter lifecycle is called to check if all portions have been delivered
     * @return true if all portions been delivered
     */
    public synchronized boolean have_all_portions_delivered()
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
     */
    public synchronized void deliver_portion()
    {
    	portionsDelivery++; 
    }
    

    
    /**
     *  Part of the waiter lifecycle is used present the bill and signal the last student to pay
     */
    public synchronized void present_the_bill()
    {
    	paying = true;
		//Notify student to pay
    	notifyAll();
    	
    	((Waiter) Thread.currentThread()).setWaiterState(States.RECEIVING_PAYMENT);
    	repository.setWaiterState(((Waiter) Thread.currentThread()).getWaiterState());

		/**TIVESTE 12 CHUPA BOI */
    	try {
			wait();
		} catch (InterruptedException e) {

		}
    	
    }
    
    
    
    /**
     * Called when a student enter in the bar to register the position in the table and to wait by the waiter to present the menu
     */
    public synchronized void seat()
    {
    	int id = ((Student) Thread.currentThread()).getStudentId();
    	
		students[id] = ((Student) Thread.currentThread());
		students[id].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);

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
     */
    public synchronized void read_menu()
    {
    	int id = ((Student) Thread.currentThread()).getStudentId();

    	students[id].setStudentState(States.SELECTING_THE_COURSES);
    	repository.setStudentState(id, ((Student) Thread.currentThread()).getStudentState());
    	
    	read[id] = id;

    	//Notify waiter
    	notifyAll();

    }    

    
    /**
     *  Part of the 1º student lifecycle to update his state and signal that is organizing the order
     */
    public synchronized void prepare_the_order()
    {
    	ordersCount++;

    	students[repository.getFirstStudent()].setStudentState(States.ORGANIZING_THE_ORDER);
    	repository.setStudentState(repository.getFirstStudent(), ((Student) Thread.currentThread()).getStudentState());
    	
    }
    
    

    
    /**
     *  Part of the 1º student lifecycle to check if the others students chosen their orders
     * @return true if the others students already chosen their orders
     */
    public synchronized boolean has_everybody_chosen()
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
     */
    public synchronized void add_up_ones_choice()
    {
    	ordersCount++;
    	waitingForChoices = false;
    	//Notify students
    	notifyAll();
    }
    
    
    
    
    /**
     * Part of the 1º student lifecycle to wake up the waiter and describe the order
     */
    public synchronized void describe_the_order()
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
     */
    public synchronized void join_the_talk()
    {
    	students[repository.getFirstStudent()].setStudentState(States.CHATING_WITH_COMPANIONS);
    	repository.setStudentState(repository.getFirstStudent(), ((Student) Thread.currentThread()).getStudentState());
    }
    
    
    
    
    
    /**
     * Part of the students' lifecycle to inform the 1º student about his course option
     */
    public synchronized void inform_companion()
    {
    	int id = ((Student) Thread.currentThread()).getStudentId();

    	while(waitingForChoices)
    	{
    		try {
				wait();
			} catch (InterruptedException e) {

			}
    	}
    	
    	waitingForChoices = true;
    	//notify student
    	notifyAll();

    	students[id].setStudentState(States.CHATING_WITH_COMPANIONS);
    	repository.setStudentState(id, ((Student) Thread.currentThread()).getStudentState());
    	
    	
    }


	/**
	 *  Part of the students' lifecycle to check if all courses have been delivered or not
	 * 	@return true if all courses have been delivered
	 */
	public synchronized boolean have_all_courses_delivery()
	{
		if(currentCourse == SimulPar.M)
			return true;
		else {
			while(portionsDelivery != SimulPar.N)
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
     * Operation start eating
     * 
     *  Part of the students' lifecycle to start eating and update his state, for simulate that is used the function sleep
     */    
    public synchronized void start_eating()
    {
    	int id = ((Student) Thread.currentThread()).getStudentId();

    	students[id].setStudentState(States.ENJOYING_THE_MEAL);
    	repository.setStudentState(id, ((Student) Thread.currentThread()).getStudentState());

        try
        { Thread.sleep ((long) (1 + 100 * Math.random ()));
        }
        catch (InterruptedException e) {}
    }



	/**
     * Part of the student lifecycle to update his state and signal that he end his course and register last student to eat
     */
    public synchronized void end_eating()
    {
    	int id = ((Student) Thread.currentThread()).getStudentId();

    	finishedCourses++;

    	if(finishedCourses == SimulPar.N)
    	{
    		currentCourse++;
    		lastFinish = id;
    	}

    	students[id].setStudentState(States.CHATING_WITH_COMPANIONS);
    	repository.setStudentState(id, ((Student) Thread.currentThread()).getStudentState());
    }
    
    
    
    
    
    /**
     * Part of the student lifecycle to wait for the last student to finish his course
     */
    public synchronized boolean has_everybody_finished()
    {
    	int id = ((Student) Thread.currentThread()).getStudentId();
    	

    	if(id == lastFinish)
    	{
    		finishedCourses = 0;
    		portionsDelivery = 0;
    		studentsCount++;
			//Notify students
    		notifyAll();
    		while(studentsCount != SimulPar.N)
    		{
    			try {
					wait();
				} catch (InterruptedException e) {

				}
    		}
    	}
    	while(finishedCourses != 0) {
    		try {
				wait();
			} catch (InterruptedException e) {

			}
    	}
    	studentsCount++;
    	if(studentsCount == SimulPar.N)
    		notifyAll();
    	
    	return true;
    }


	/**
	 * Part of the student lifecycle to check if he is the last to arrive and change his state to pay the bill
	 * @return True if student was the last to arrive
	 */
	public synchronized boolean should_have_arrived_earlier()
	{
		int id = ((Student) Thread.currentThread()).getStudentId();

		if(id == repository.getLastStudent()) {
			students[id].setStudentState(States.PAYING_THE_BILL);
			repository.setStudentState(id, ((Student) Thread.currentThread()).getStudentState());
			return true;
		}
		else
			return false;
	}




	/**
     * Part of the student lifecycle to wait for the waiter to give him the bill
     */
    public synchronized void honor_the_bill()
    {    	
    	//Block waiting for waiter to present the bill
    	while(!paying)
    	{
    		try {
				wait();
			} catch (InterruptedException e) {
			}
    	}
		//Notify waiter
    	notifyAll();
    }

    
}
