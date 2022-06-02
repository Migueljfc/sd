package serverSide.sharedRegions;

import java.util.Objects;
import serverSide.main.*;
import genclass.GenericIO;
import genclass.TextFile;
import clientSide.entities.*;

/**
 *  @summary
 * Implementation of the Bar shared region
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class GeneralRepository
{
	/**
	 *  Name of the logging file.
	 */

	private final String logFileName;

	/**
	 *  State of the Chef
	 */

	private int chefState;

	/**
	 *	State of the Waiter
	 */

	private int waiterState;

	/**
	 *  State of the Chef
	 */

	private int[] studentState;

	/**
	 *	Number of courses delivered
	 */

	private int courses;

	/**
	 * 	Number of Portions delivered
	 */

	private int portions;

	/**
	 * 	Seats at the table
	 */
	private int[] seatsAtTable;

	/**
	 * Number of entities that requested shutdown
	 */
	private int entities;


	/**
	 *	Instantiation of a general repository object.
	 *
	 *	@param logFileName name of the logging file
	 */

	public GeneralRepository (String logFileName)
	{
		entities = 0;
		if ((logFileName == null) || Objects.equals (logFileName, ""))
			this.logFileName = "logger";
		else this.logFileName = logFileName;
		chefState = States.WAIT_FOR_AN_ORDER;
		waiterState = States.APPRAISING_SITUATION;
		studentState = new int[SimulPar.N];
		for (int i = 0; i < SimulPar.N; i++)
			studentState[i] = States.GOING_TO_THE_RESTAURANT;
		courses = 0;
		portions = 0;
		seatsAtTable = new int[SimulPar.N];
		for(int i = 0; i < SimulPar.N; i++)
		{
			seatsAtTable[i] = -1;
		}
		reportInitialStatus ();
	}

	/**
	 *  Write the header to the logging file
	 */

	private void reportInitialStatus ()
	{
		TextFile log = new TextFile ();                      // instantiation of a text file handler

		if (!log.openForWriting (".", logFileName))
		{
			GenericIO.writelnString ("The operation of creating the file " + logFileName + " failed!");
			System.exit (1);
		}
		log.writelnString ("                                        The Restaurant - Description of the internal state");
		log.writelnString (" Chef Waiter  Stu0  Stu1  Stu2  Stu3  Stu4  Stu5  Stu6  NCourse  NPortion                    Table");
		log.writelnString ("State State  State State State State State State State                     Seat0 Seat1 Seat2 Seat3 Seat4 Seat5 Seat6");
		if (!log.close ())
		{
			GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
			System.exit (1);
		}
	}

	/**
	 *  Write a line at the end of the logging file.
	 */

	private void reportStatus ()
	{
		TextFile log = new TextFile ();                  	// instantiation of a text file handler
		String line = "";                              		// state line to be printed
		if (!log.openForAppending (".", logFileName))
		{
			GenericIO.writelnString ("The operation of opening for appending the file " + logFileName + " failed!");
			System.exit (1);
		}
		switch(chefState)
		{
			case States.WAIT_FOR_AN_ORDER: line += "WAFOR "; break;
			case States.PREPARING_A_COURSE: line += "PRPCS "; break;
			case States.DISHING_THE_PORTIONS: line += "DSHPT "; break;
			case States.DELIVERING_THE_PORTIONS: line += "DLVPT "; break;
			case States.CLOSING_SERVICE: line += "CLSSV "; break;
		}

		switch(waiterState)
		{
			case States.APPRAISING_SITUATION: line += "APPST  "; break;
			case States.PRESENTING_THE_MENU: line += "PRSMN  "; break;
			case States.TAKING_THE_ORDER: line += "TKODR  "; break;
			case States.PLACING_THE_ORDER: line += "PCODR  "; break;
			case States.WAITING_FOR_AN_PORTION: line += "WTFPT  "; break;
			case States.PROCESSING_THE_BILL: line += "PRCBL  "; break;
			case States.RECEIVING_PAYMENT: line += "RECPM  "; break;
		}

		for(int i = 0; i < SimulPar.N; i++)
		{
			switch(studentState[i])
			{
				case States.GOING_TO_THE_RESTAURANT: line += "GGTRT "; break;
				case States.TAKING_A_SEAT_AT_THE_TABLE: line += "TKSTT "; break;
				case States.SELECTING_THE_COURSES: line += "SELCS "; break;
				case States.ORGANIZING_THE_ORDER: line += "OGODR "; break;
				case States.CHATING_WITH_COMPANIONS: line += "CHTWC "; break;
				case States.ENJOYING_THE_MEAL: line += "EJYML "; break;
				case States.PAYING_THE_BILL: line += "PYTBL "; break;
				case States.GOING_HOME: line += "GGHOM "; break;
			}
		}

		line += "    " + String.valueOf(courses);
		line += "        " + String.valueOf(portions);
		line += "        " + (seatsAtTable[0] >= 0 ? String.valueOf(seatsAtTable[0]) : "-");
		for(int i = 1; i < SimulPar.N; i++)
		{
			line += "     " + (seatsAtTable[i] >= 0 ? String.valueOf(seatsAtTable[i]) : "-");
		}

		log.writelnString (line);
		if (!log.close ())
		{
			GenericIO.writelnString ("The operation of closing the file " + logFileName + " failed!");
			System.exit (1);
		}
	}



	/**
	 * Write in the logging file the new chef state
	 * @param value chef state to set
	 */
	public synchronized void setChefState(int value) {
		this.chefState = value;
		reportStatus();
	}

	/**
	 * Write in the logging file the new waiter state
	 * @param value waiter state to set
	 */
	public synchronized void setWaiterState(int value) {
		this.waiterState = value;
		reportStatus();
	}

	/**
	 * Updated student state and report status
	 * @param id student id
	 * @param value student state to set
	 */
	public synchronized void updateStudentState(int id, int value) {
		this.studentState[id] = value;
		reportStatus();
	}

	/**
	 * Update student state
	 * @param id student id
	 * @param value student state to set
	 * @param hold specifies if prints line of report status
	 */
	public synchronized void updateStudentState(int id, int value, boolean hold) {
		this.studentState[id] = value;
	}

	/**
	 * Set variable courses
	 * @param n courses value to set
	 */
	public synchronized void setnCourses(int n) {
		this.courses = n;
	}

	/**
	 * set variable portion
	 *
	 * @param n portions value to set
	 */
	public synchronized void setnPortions(int n) {
		this.portions = n;
	}

	/**
	 * Write to the logging file the updated seats values at the table
	 *
	 * @param seat seat at the table
	 * @param id student id to sit
	 */
	public synchronized void updateSeatsAtTable(int seat, int id) {
		this.seatsAtTable[seat] = id;
		reportStatus();
	}

	/**
	 * Update student seat to -1 when he leaves the table to report status
	 *
	 * @param id student id that leaves the table
	 */
	public synchronized void updateSeatsAtLeaving(int id) {
		int seat = 0;

		for(int i=0; i < this.seatsAtTable.length; i++) {
			if(this.seatsAtTable[i] == id)
				seat = i;
		}

		this.seatsAtTable[seat] = -1;
	}

	/**
	 * Operation Table server shutdown
	 */
	public synchronized void shutdown()
	{
		entities += 1;
		if(entities >= SimulPar.S) {
			GeneralRepositoryMain.waitConnection = false;
		}
		notifyAll ();
	}
}









