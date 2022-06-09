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
	 * Id of student first
	 */
	private int firstStudent;

	/**
	 * Id of student last to arrive at restaurant
	 */
	private int lastStudent;

	/**
	 * Used to count number of orders made by students
	 */
	private int ordersCount;

	/**
	 * Used to count number of students that finished the course
	 */
	private int finishedCourses;

	/**
	 * Id of last student to eat
	 */
	private int lastToEat;

	/**
	 * courses eaten
	 */
	private int currentCourse;

	/**
	 * count number of students served
	 */
	private int studentsServed;

	/**
	 * Id of the student whose request the waiter is taking care of
	 */
	private int currentStudent;

	/**
	 * Boolean variable to check if waiter is presenting the menu or not
	 */
	private boolean isReading;

	/**
	 * Control if waiter receive order
	 */
	private boolean receiveTheOrder;

	/**
	 * Control if first student has waiting for the other students choices
	 */
	private boolean waitingForChoices;

	/**
	 *  Count the number of students that wake up after the last student end the course
	 */
	private int studentsCount;

	/**
	 * Control if the paying process already have initiated
	 */
	private boolean paying;

	/**
	 * Array to check if student already seated
	 */
	private boolean seat[];

	/**
	 * Array to check if student have read the menu
	 */
	private boolean read[];

	/**
	 * Reference to the student threads
	 */
	private final TableClientProxy [] students;

	/**
	 * Reference to the General Repository stub
	 */
	private final GeneralRepositoryStub reposStub;

	/**
	 * Number of entities that must make shutdown
	 */
	private int nEntities;


	/**
	 * Table instantiation
	 *
	 * @param reposStub reference to the general repository stub
	 */
	public Table(GeneralRepositoryStub reposStub)
	{

		this.firstStudent = -1;
		this.lastStudent = -1;
		this.ordersCount = 0;
		this.finishedCourses = 0;
		this.lastToEat = -1;
		this.currentCourse = 0;
		this.studentsServed = 0;
		this.currentStudent = 0;
		this.studentsCount = 0;
		this.isReading = false;
		this.receiveTheOrder = false;
		this.waitingForChoices = false;
		this.paying = false;
		this.reposStub = reposStub;
		this.nEntities = 0;


		seat = new boolean[SimulPar.N];
		read = new boolean[SimulPar.N];
		for(int i = 0; i < SimulPar.N; i++)
		{
			seat[i] = false;
			read[i] = false;
		}

		students = new TableClientProxy[SimulPar.N];
		for(int i = 0; i < SimulPar.N; i++ )
			students[i] = null;

	}




	/**
	 * Get id of the first student to enter
	 * @return id of the first student to enter in the restaurant
	 */
	public int getfirstStudent() { return firstStudent; }

	/**
	 * Get id of the last student to enter
	 * @return id of the last student to finish eating a meal
	 */
	public int getLastToEat() { return lastToEat; }

	/**
	 * Set id of the first student to arrive
	 * @param id id of the first student to arrive
	 */
	public synchronized void setFirstStudent(int id) {
		System.out.printf("___________________________ FIRST STUDENT = %s__________________________", id);
		this.firstStudent = id; }

	/**
	 * Set id of the last student to arrive
	 * @param id if of the last student to arrive to the restaurant
	 */
	public synchronized void setLastStudent(int id) { this.lastStudent = id; }






	/**
	 * Part of the waiter lifecycle is called when a student enter the restaurant
	 * @param id id of the student to be saluted
	 */
	public synchronized void salute_client(int id)
	{
		currentStudent = id;
		((TableClientProxy) Thread.currentThread()).setWaiterState(States.PRESENTING_THE_MENU);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());

		isReading = true;


		while(seat[currentStudent] == false)
		{
			try {
				wait();
			} catch (InterruptedException e1) {
			}
		}

		//Notify student
		notifyAll();

		while(read[currentStudent] == false)
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
		((TableClientProxy) Thread.currentThread()).setWaiterState(States.APPRAISING_SITUATION);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());
	}




	/**
	 * Part of the waiter lifecycle is called when the first student intent to describe the order
	 */
	public synchronized void get_the_pad()
	{

		((TableClientProxy) Thread.currentThread()).setWaiterState(States.TAKING_THE_ORDER);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());

		receiveTheOrder = true;

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

		if(studentsServed == SimulPar.N) {
			lastToEat = -1;
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
		studentsServed++;
	}





	/**
	 * Part of the waiter lifecycle is used present the bill and signal the last student to pay
	 */
	public synchronized void present_the_bill()
	{
		paying = true;

		//Notify student
		notifyAll();

		((TableClientProxy) Thread.currentThread()).setWaiterState(States.RECEIVING_PAYMENT);
		reposStub.setWaiterState(((TableClientProxy) Thread.currentThread()).getWaiterState());
		while(paying){
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}


	}


	/**
	 * Called when a student enter the bar to register the position in the table and to wait by the waiter to present the menu
	 */
	public synchronized void seat()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		students[studentId] = (TableClientProxy) Thread.currentThread();
		students[studentId].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);

		seat[studentId] = true;
		//notify waiter
		notifyAll();

		do
		{
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		while(studentId != currentStudent && isReading == false);


	}



	/**
	 *  Part of the student lifecycle used to update the student state and update the read array to notify that the student already read the menu
	 */
	public synchronized void read_menu()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		students[studentId].setStudentState(States.SELECTING_THE_COURSES);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.SELECTING_THE_COURSES);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());
		read[studentId] = true;

		//Notify waiter
		notifyAll();
	}






	/**
	 * Part of the 1º student lifecycle to update his state and signal that is organizing the order
	 */
	public synchronized void prepare_the_order()
	{
		ordersCount++;

		students[firstStudent].setStudentState(States.ORGANIZING_THE_ORDER);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.ORGANIZING_THE_ORDER);
		reposStub.updateStudentState(firstStudent, students[firstStudent].getStudentState());
	}




	/**
	 * Part of the 1º student lifecycle to check if the others students chosen their orders
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
		students[firstStudent].setStudentState(States.CHATING_WITH_COMPANIONS);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.CHATING_WITH_COMPANIONS);
		reposStub.updateStudentState(firstStudent, students[firstStudent].getStudentState());
	}





	/**
	 * Part of the students' lifecycle to inform the 1º student about his course option
	 */
	public synchronized void inform_companion()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

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

		students[studentId].setStudentState(States.CHATING_WITH_COMPANIONS);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.CHATING_WITH_COMPANIONS);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());
	}




	/**
	 * Part of the students' lifecycle to start eating and update his state, for simulate that is used the function sleep
	 */
	public void start_eating()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		students[studentId].setStudentState(States.ENJOYING_THE_MEAL);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.ENJOYING_THE_MEAL);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());

		try
		{ Thread.sleep ((long) (1 + 100 * Math.random ()));
		}
		catch (InterruptedException e) {}
	}



	/**
	 * Part of the student lifecycle to update his state and signal that he end his course and register last student to eat
	 *
	 */
	public synchronized void end_eating()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		finishedCourses++;

		if(finishedCourses == SimulPar.N)
		{
			currentCourse++;

			lastToEat = studentId;
		}

		students[studentId].setStudentState(States.CHATING_WITH_COMPANIONS);
		((TableClientProxy) Thread.currentThread()).setStudentState(States.CHATING_WITH_COMPANIONS);
		reposStub.updateStudentState(studentId, students[studentId].getStudentState());
	}





	/**
	 * Part of the student lifecycle to wait for the last student to finish his course
	 */
	public synchronized boolean has_everybody_finished()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		if(studentId == lastToEat)
		{
			finishedCourses = 0;
			studentsServed = 0;
			studentsCount++;
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
	 * Part of the student lifecycle to wait for the waiter to give him the bill
	 */
	public synchronized void honor_the_bill()
	{
		while(!paying)
		{
			try {
				wait();
			} catch (InterruptedException e) {

			}
		}
		paying = false;
		//Notify Waiter
		notifyAll();
	}






	/**
	 * Part of the students' lifecycle to check if all courses have been delivered or not
	 * @return true if all courses have been delivered
	 */
	public synchronized boolean have_all_courses_delivery()
	{
		if(currentCourse == SimulPar.M)
			return true;
		else {
			while(studentsServed != SimulPar.N)
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
	 * Part of the student lifecycle to check if he is the last to arrive and change his state to pay the bill
	 * @return True if student was the last to arrive
	 */
	public synchronized boolean should_have_arrived_earlier()
	{
		int studentId = ((TableClientProxy) Thread.currentThread()).getStudentId();

		if(studentId == lastStudent) {
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
		if(nEntities >= 1)
			TableMain.waitConnection = false;
		notifyAll ();
	}

}
