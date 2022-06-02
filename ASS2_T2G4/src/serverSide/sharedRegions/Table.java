package serverSide.sharedRegions;

import clientSide.stubs.GeneralRepositoryStub;
import serverSide.main.*;
import clientSide.entities.States;
import serverSide.entities.TableClientProxy;


/**
 * @author miguel cabral 93091
 *  @author rodrigo santos 93173
 *  @summary Implementation of the Table shared region
 */

public class Table {

	/**
	 * Id of student first to arrive at restaurant
	 */
	private int firstStudent;

	/**
	 * Id of student last to arrive at restaurant
	 */
	private int lastToArrive;

	/**
	 * Used to count number of orders made by students
	 */
	private int ordersCount;

	/**
	 * Used to count number of students that finished the course
	 */
	private int numStudentsFinishedCourse;

	/**
	 * Id of last student to eat
	 */
	private int lastToEat;

	/**
	 * Used to count number of courses eaten
	 */
	private int numOfCoursesEaten;

	/**
	 * Used to count number of students served
	 */
	private int numStudentsServed;

	/**
	 * Id of the student whose request the waiter is taking care of
	 */
	private int currentStudent;

	/**
	 * Boolean variable to check if waiter is presenting the menu or not
	 */
	private boolean isReading;

	/**
	 * Boolean variable to check if waiter is taking the order
	 */
	private boolean receiveTheOrder;

	/**
	 * Boolean variable to check if a student is informing his companion about the order
	 */
	private boolean informingCompanion;

	/**
	 * Used to count number of students that woke up after last student to eat has signalled them to
	 */
	private int numStudentsWokeUp;

	/**
	 * Boolean variable to check if waiter is processing the bill
	 */
	private boolean processingBill;

	/**
	 * Boolean array to check which students have seated already
	 */
	private boolean seat[];

	/**
	 * Boolean array to check which students have already read the menu
	 */
	private boolean read[];

	/**
	 * Reference to the student threads
	 */
	private final TableClientProxy [] students;

	/**
	 * Reference to the stub of the General Repository.
	 */
	private final GeneralRepositoryStub reposStub;

	/**
	 * Number of entities that must make shutdown
	 */
	private int nEntities;


	/**
	 * Table instantiation
	 *
	 * @param reposStub reference to the stub of the general repository
	 */
	public Table(GeneralRepositoryStub reposStub)
	{
		//Initialisation of attributes
		this.firstStudent = -1;
		this.lastToArrive = -1;
		this.ordersCount = 0;
		this.numStudentsFinishedCourse = 0;
		this.lastToEat = -1;
		this.numOfCoursesEaten = 0;
		this.numStudentsServed = 0;
		this.currentStudent = 0;
		this.numStudentsWokeUp = 0;
		this.isReading = false;
		this.receiveTheOrder = false;
		this.informingCompanion = false;
		this.processingBill = false;
		this.reposStub = reposStub;
		this.nEntities = 0;

		//initialisation of the boolean arrays
		seat = new boolean[SimulPar.N];
		read = new boolean[SimulPar.N];
		for(int i = 0; i < SimulPar.N; i++)
		{
			seat[i] = false;
			read[i] = false;
		}

		//Initialisation of students thread
		students = new TableClientProxy[SimulPar.N];
		for(int i = 0; i < SimulPar.N; i++ )
			students[i] = null;

	}




	/**
	 * Obtain id of the first student to arrive
	 * @return id of the first student to arrive at the restaurant
	 */
	public int getfirstStudent() { return firstStudent; }

	/**
	 * Obtain id of the last student to arrive
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() { return lastToEat; }

	/**
	 * Set id of the first student to arrive
	 * @param id id of the first student to arrive
	 */
	public synchronized void setFirstStudent(int id) { this.firstStudent = id; }

	/**
	 * Set id of the last student to arrive
	 * @param lastToArrive if of the last student to arrive to the restaurant
	 */
	public synchronized void setLastToArrive(int lastToArrive) { this.lastToArrive = lastToArrive; }






	/**
	 * Operation salute the client
	 *
	 * It is called by the waiter when a student enters the restaurant and needs to be saluted
	 * Waiter waits for the student to take a seat (if he hasn't done it yet)
	 * Waiter waits for student to finish reading the menu
	 */
	public synchronized void salute_client(int id)
	{
		currentStudent = id;
		//Update Waiter state
		((TableClientProxy) Thread.currentThread()).setWaiterState(States.PRESENTING_THE_MENU);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());

		isReading = true;


		//Waiter must wait while student hasn't taken a seat
		while(seat[currentStudent] == false)
		{
			try {
				wait();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		//Waiter wakes student that has just arrived in order to greet him
		notifyAll();
		//Block waiting for student to read the menu
		while(read[currentStudent] == false)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//When student has finished reading the menu his request was completed
		currentStudent = -1;
		isReading  = false;
	}





	/**
	 * Operation return to the bar
	 *
	 * It is called by the waiter to return to the bar to the appraising situation state
	 */
	public synchronized void return_to_bar()
	{
		//Update Waiter state
		((TableClientProxy) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());
	}




	/**
	 * Operation get the pad
	 *
	 * It is called by the waiter when an order is going to be described by the first student to arrive
	 * Waiter Blocks waiting for student to describe him the order
	 */
	public synchronized void get_the_pad()
	{
		//Update Waiter state
		((TableClientProxy) Thread.currentThread()).setWaiterState(States.TAKING_THE_ORDER);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());

		receiveTheOrder = true;

		//notify student that he can describe the order
		notifyAll();

		//Waiter blocks waiting for first student to arrive to describe him the order
		while(receiveTheOrder)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}




	/**
	 * Operation have all clients been served
	 *
	 * Called by the waiter to check if all clients have been served or not
	 * @return true if all clients have been served, false otherwise
	 */
	public synchronized boolean have_all_portions_delivered()
	{
		//If all clients have been served they must be notified
		if(numStudentsServed == SimulPar.N) {
			//Reset lastToEat and number of students who woke up
			lastToEat = -1;
			numStudentsWokeUp = 0;
			notifyAll();
			return true;
		}
		return false;

	}



	/**
	 * Operation deliver portion
	 *
	 * Called by the waiter, to deliver a portion
	 */
	public synchronized void deliver_portion()
	{
		//Update number of Students server after portion delivery
		numStudentsServed++;
	}





	/**
	 * Operation present the bill
	 *
	 * Called by the waiter to present the bill to the last student to arrive
	 */
	public synchronized void present_the_bill()
	{
		processingBill = true;

		//Signal student the he can pay
		notifyAll();

		((TableClientProxy) Thread.currentThread()).setWaiterState(States.RECEIVING_PAYMENT);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());
		//Block waiting for his payment
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}



	/**
	 * Operation siting at the table
	 *
	 * Student comes in the table and sits (blocks) waiting for waiter to bring him the menu
	 * Called by the student (inside enter method in the bar)
	 */
	public synchronized void seat()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		students[studentId] = (TableClientProxy) Thread.currentThread();
		students[studentId].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);

		//Register that student took a seat
		seat[studentId] = true;
		//notify waiter that student took a seat (waiter may be waiting)
		notifyAll();

		//Block waiting for waiter to bring menu specifically to him
		// Student also blocks if he wakes up when waiter is bringing the menu to another student
		do
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(studentId != currentStudent && isReading == false);


	}



	/**
	 * Operation read the menu
	 *
	 * Called by the student to read a menu, wakes up waiter to signal that he already read the menu
	 */
	public synchronized void read_menu()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		//Update student state
		students[studentId].setStudentState(States.SELECTING_THE_COURSES);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.SELECTING_THE_COURSES);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());
		read[studentId] = true;
		//Signal waiter that menu was already read
		notifyAll();
	}






	/**
	 * Operation prepare the order
	 *
	 * Called by the student to begin the preparation of the order (options of his companions)
	 */
	public synchronized void prepare_the_order()
	{
		//Register that first student to arrive already choose his own option
		ordersCount++;

		//Update student state
		students[firstStudent].setStudentState(States.ORGANIZING_THE_ORDER);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.ORGANIZING_THE_ORDER);
		reposStub.updateStudentState(firstStudent, students[firstStudent].getStudentState());
	}




	/**
	 * Operation everybody has chosen
	 *
	 * Called by the first student to arrive to check if all his companions have choose or not
	 * Blocks if not waiting to be waker up be a companion to give him his preference
	 * @return true if everybody choose their course choice, false otherwise
	 */
	public synchronized boolean has_everybody_chosen()
	{
		if(ordersCount == SimulPar.N)
			return true;
		else {
			//Block if not everybody has choosen and while companions are not describing their choices
			while(informingCompanion == false)
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
	 * Operation add up ones choices
	 *
	 * Called by the first student to arrive to add up a companions choice to the order
	 */
	public synchronized void add_up_ones_choice()
	{
		ordersCount++;
		informingCompanion = false;
		//Notify sleeping student threads that order was registered
		notifyAll();
	}




	/**
	 * Operation describe the order
	 *
	 * Called by the first student to arrive to describe the order to the waiter
	 * Blocks waiting for waiter to come with pad
	 * Wake waiter up so he can take the order
	 */
	public synchronized void describe_the_order()
	{
		//After student just putted a request in the queue in the bar
		// now it must block waiting for waiter to come with the pad
		while(receiveTheOrder == false)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		receiveTheOrder = false;
		//Wake waiter to describe him the order
		notifyAll();
	}





	/**
	 * Operation join the talk
	 *
	 * Called by the first student to arrive so he can join his companions
	 * while waiting for the courses to be delivered
	 */
	public synchronized void join_the_talk()
	{
		//Update student state
		students[firstStudent].setStudentState(States.CHATING_WITH_COMPANIONS);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.CHATING_WITH_COMPANIONS);
		reposStub.updateStudentState(firstStudent, students[firstStudent].getStudentState());
	}





	/**
	 * Operation inform companion
	 *
	 * Called by a student to inform the first student to arrive about their preferences
	 * Blocks if someone else is informing at the same time
	 */
	public synchronized void inform_companion()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		//If some other student is informing about his order then wait must be done
		while(informingCompanion)
		{
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		informingCompanion = true;
		//notify first student to arrive, so that he can register ones preference
		notifyAll();

		//Update student state
		students[studentId].setStudentState(States.CHATING_WITH_COMPANIONS);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.CHATING_WITH_COMPANIONS);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());
	}




	/**
	 * Operation start eating
	 *
	 * Called by the student to start eating the meal (During random time)
	 */
	public void start_eating()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();
		//Update student state
		students[studentId].setStudentState(States.ENJOYING_THE_MEAL);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.ENJOYING_THE_MEAL);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());

		//Enjoy meal during random time
		try
		{ Thread.sleep ((long) (1 + 100 * Math.random ()));
		}
		catch (InterruptedException e) {}
	}



	/**
	 * Operation end eating
	 *
	 * Called by the student to signal that he has finished eating his meal
	 */
	public synchronized void end_eating()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		//Update number of students that finished course
		numStudentsFinishedCourse++;

		//If all students have finished means that one more course was eaten
		if(numStudentsFinishedCourse == SimulPar.N)
		{
			numOfCoursesEaten++;

			lastToEat = studentId;
		}

		students[studentId].setStudentState(States.CHATING_WITH_COMPANIONS);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.CHATING_WITH_COMPANIONS);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());
	}





	/**
	 * Operation has everybody finished eating
	 *
	 * Called by the student to wait for his companions to finish eating
	 */
	public synchronized boolean has_everybody_finished()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		//Notify all students that the last one to eat has already finished
		if(studentId == lastToEat)
		{
			//Reset number of students that finished the course
			numStudentsFinishedCourse = 0;
			//Reset number of students served
			numStudentsServed = 0;
			numStudentsWokeUp++;
			notifyAll();

			//Last student to eat must wait for every companion to wake up from waiting for everybody to finish eating
			//before he can proceed to signal waiter
			while(numStudentsWokeUp != SimulPar.N)
			{
				try {
					wait();
				} catch (InterruptedException e) {

				}
			}
		}

		//Wait while not all students have finished
		while(numStudentsFinishedCourse != 0) {
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		//Update that a student woke up
		numStudentsWokeUp++;
		//If all students have woken up from last to eat signal, then student that was last to eat
		//must be notified
		if(numStudentsWokeUp == SimulPar.N)
			notifyAll();

		return true;
	}





	/**
	 * Operation honour the bill
	 *
	 * Called by the student to pay the bill
	 * Student blocks waiting for bill to be presented and signals waiter when it's time to pay it
	 */
	public synchronized void honor_the_bill()
	{
		//Block waiting for waiter to present the bill
		while(!processingBill)
		{
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}

		//After waiter presents the bill, student signals waiter so he can wake up and receive it
		notifyAll();
	}






	/**
	 * 	Operation have all courses been eaten
	 *
	 * 	Called by the student to check if there are more courses to be eaten
	 * 	Student blocks waiting for the course to be served
	 * 	@return true if all courses have been eaten, false otherwise
	 */
	public synchronized boolean have_all_courses_delivery()
	{
		if(numOfCoursesEaten == SimulPar.M)
			return true;
		else {
			//Student blocks waiting for all companions to be served
			while(numStudentsServed != SimulPar.N)
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
	 * Operation should have arrived earlier
	 *
	 * Called by the student to check which one was last to arrive
	 * @return True if current student was the last to arrive, false otherwise
	 */
	public synchronized boolean should_have_arrived_earlier()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		if(studentId == lastToArrive) {
			//Update student state
			students[studentId].setStudentState(States.PAYING_THE_BILL);
			((TableClientProxy) Thread.currentThread()).setStudentState(States.PAYING_THE_BILL);
			reposStub.updateStudentState(studentId, students[studentId].getStudentState());
			return true;
		}
		else
			return false;
	}


	/**
	 * Operation Table server shutdown
	 */
	public synchronized void shutdown()
	{
		nEntities += 1;
		if(nEntities >= SimulPar.E)
			TableMain.waitConnection = false;
		notifyAll ();
	}

}
