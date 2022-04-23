/**
 * 
 */
package sharedRegions;

import java.util.Arrays;
import java.util.Currency;

import entities.*;
import main.SimulPar;

/**
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class Table {
	/**
	 * Reference to GeneralRepository
	 */
	private GeneralRepository repository;

	/**
	 * Store the student ids that have seated
	 */
	private int seats[];

	/**
	 * Count the number of students that are seated
	 */
	private int seatCount;

	/**
	 * Current student that is being attended
	 */
	private int studentId;
	/**
	 * Reference to students threads
	 */
	private final Student students[];

	/**
	 * Count the number of portions Delivered
	 */
	private int portionsDelivery;

	/**
	 * Count the number of orders
	 */
	private int ordersCount;

	/**
	 * Count the number of students that have read the menu
	 */
	private int readCount;

	/**
	 * Store the student ids that have read the menu
	 */
	private int read[];
	/**
	 * Control if first student has waiting for the other students choices
	 */
	private boolean waitingForChoices;

	/**
	 * Control if waiter already receives the order
	 */
	private boolean receiveTheOrder;

	/**
	 * Control if the paying process already have initiate
	 */
	private boolean paying;

	/**
	 * Current course that has been eaten
	 */
	private int currentCourse;

	/**
	 * Count the number of finished courses
	 */
	private int finishedCourses;

	/**
	 * Last student id to finish the current course
	 */
	private int lastStudent;
	/**
	 * Control if a student is reading the menu
	 */
	private boolean isReading;
	/**
	 * Control if already exists a student informing about his order
	 */
	private boolean firstStudentBusy;
	/**
	 * Count the number of students that wake up after the last student end the
	 * course
	 */
	private int studentsCount;

	public Table(GeneralRepository repository) {
		this.repository = repository;
		this.ordersCount = 0;
		this.readCount = 0;
		this.portionsDelivery = 0;
		this.studentId = -1;
		this.lastStudent = -1;
		this.finishedCourses = 0;
		this.currentCourse = 0;
		this.seatCount = 0;
		this.studentsCount = 0;
		this.paying = false;
		this.waitingForChoices = false;
		this.receiveTheOrder = false;
		this.isReading = false;

		seats = new int[SimulPar.N];
		students = new Student[SimulPar.N];

		for (int i = 0; i < SimulPar.N; i++) {
			seats[i] = -1;
			students[i] = null;
		}

	}

	public synchronized void salute_client(int id) {
		studentId = id;
		Waiter w = (Waiter) Thread.currentThread();
		w.setWaiterState(States.PRESENTING_THE_MENU);
		repository.setWaiterState(w.getWaiterState());

		isReading = true;

		while (!Arrays.asList(seats).contains(studentId)) {
			try {
				wait();
			} catch (InterruptedException e) {
			}

		}

		notifyAll();

		while (!Arrays.asList(read).contains(studentId)) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		isReading = false;
		studentId = -1;

	}

	public synchronized void prepare_the_order() {
		ordersCount++;
		students[repository.getFirstStudent()].setStudentState(States.ORGANIZING_THE_ORDER);
		Student s = (Student) Thread.currentThread();
		repository.setStudentState(repository.getFirstStudent(), s.getStudentState());

	}

	public synchronized boolean has_everybody_chosen() {
		if (ordersCount == SimulPar.N) {
			return true;
		}
		while (!waitingForChoices) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return false;
	}

	public synchronized void add_up_ones_choice() {
		ordersCount++;
		waitingForChoices = false;

		// notify students
		notifyAll();
	}

	public synchronized void describe_the_order() {
		while (!receiveTheOrder) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		receiveTheOrder = false;

		// notify waiter
		notifyAll();

	}

	public synchronized void join_the_talk() {
		students[repository.getFirstStudent()].setStudentState(States.CHATING_WITH_COMPANIONS);
		Student s = (Student) Thread.currentThread();
		repository.setStudentState(repository.getFirstStudent(), s.getStudentState());
	}

	public synchronized void inform_companion() {
		Student s = (Student) Thread.currentThread();
		int id = s.getStudentId();
		while (firstStudentBusy) {
			try {
				wait();
			} catch (InterruptedException e) {
			}

		}
		firstStudentBusy = false;
		// notify first student
		notifyAll();
		students[id].setStudentState(States.CHATING_WITH_COMPANIONS);
		repository.setStudentState(id, s.getStudentState());
	}

	public synchronized boolean have_all_courses_delivery() {
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

	public synchronized void start_eating() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		students[id].setStudentState(States.ENJOYING_THE_MEAL);
		repository.setStudentState(id, st.getStudentState());
		try {
			Thread.sleep((long) (1 + 100 * Math.random()));
		} catch (InterruptedException e) {
		}
	}

	public synchronized boolean has_everybody_finished() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		if (id == lastStudent) {
			studentsCount++;
			portionsDelivery = 0;
			finishedCourses = 0;
			notifyAll();
		}
		while (studentsCount != SimulPar.N) {
			try {
				wait();
			} catch (InterruptedException e) {

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

	public synchronized int last_finished() {
		return lastStudent;
	}

	public synchronized boolean should_have_arrived_earlier() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		return false;
	}

	public synchronized void honor_the_bill() {
		while (!paying) {
			try {
				wait();
			} catch (InterruptedException e) {
			}

		}
		notifyAll();
	}

	public synchronized void get_the_pad() {
		Waiter w = (Waiter) Thread.currentThread();
		w.setWaiterState(States.TAKING_THE_ORDER);
		repository.setWaiterState(w.getWaiterState());
		receiveTheOrder = true;
		// notify student
		notifyAll();

		while (receiveTheOrder) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

	}

	public synchronized boolean have_all_portions_delivered() {
		if (portionsDelivery == SimulPar.N) {
			lastStudent = -1;
			studentsCount = 0;

			notifyAll();
			return true;
		}
		return false;
	}

	public synchronized void deliver_portion() {
		portionsDelivery++;

	}

	public synchronized void return_to_bar() {
		Waiter w = (Waiter) Thread.currentThread();
		w.setWaiterState(States.APPRAISING_SITUATION);
		repository.setWaiterState(w.getWaiterState());
	}

	public synchronized void seat() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		students[id].setStudentState(States.TAKING_A_SEAT_AT_THE_TABLE);
		repository.setStudentState(id, st.getStudentState());
		seats[seatCount++] = id;
		// notify waiter
		notifyAll();

		while (id == studentId && isReading) {
			try {
				wait();
			} catch (InterruptedException e) {
			}

		}
	}

	public synchronized void present_the_bill() {
		paying = true;
		notifyAll();

		Waiter w = (Waiter) Thread.currentThread();
		w.setWaiterState(States.RECEIVING_PAYMENT);
		repository.setWaiterState(w.getWaiterState());
		try {
			wait();
		} catch (Exception e) {
		}

	}

	public synchronized void read_the_menu() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		students[id].setStudentState(States.GOING_TO_THE_RESTAURANT);
		repository.setStudentState(id, st.getStudentState());

		read[readCount++] = id;

		// notify waiter
		notifyAll();
	}

	public synchronized void end_eating() {
		Student st = (Student) Thread.currentThread();
		int id = st.getStudentId();
		finishedCourses++;
		if (finishedCourses == SimulPar.N) {
			currentCourse++;
			lastStudent = id;
		}
		students[id].setStudentState(States.CHATING_WITH_COMPANIONS);
		repository.setStudentState(id, st.getStudentState());
	}

}
