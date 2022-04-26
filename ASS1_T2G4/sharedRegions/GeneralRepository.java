/**
 * 
 */
package sharedRegions;
import entities.States;
import main.SimulPar;
import genclass.GenericIO;
import genclass.TextFile;


import java.util.Objects;

/**
 {@summary}It is responsible to keep the visible internal state of the problem and to provide means for it
 *    to be printed in the logging file.
 * 
 * @author miguel cabral 93091
 * @author rodrigo santos 93173
 */

public class GeneralRepository {
	
	/**
	 *  Name of the logging file.
	 */
	private final String logName;
	
	/**
	 *  Number of portions served.
	 */
	private int nPortions;
	
	/**
	 *  Number of Courses served.
	 */
	private int nCourses;
	
	/**
	 *  State of the Chef.
	 */
	private States chefState;
   
	/**
	 *  State of the Waiter.
	 */
	private States waiterState;
	
	/**
	 *  State of the Students.
	 */
	private States [] studentStates;

	/**
	 *  seat where each student is.
   	*/
	private int [] seats;
	
	/**
	 *  Id of first student
	 */
	private int firstStudent;
	
	/**
	 *  Id of last student
	 */
	private int lastStudent;
	   
	public GeneralRepository(String logName) {
		if ((logName == null) || Objects.equals (logName, "")) {
	         this.logName = "logger";
		} else {
			this.logName = logName;
		}
		studentStates = new States[SimulPar.N];
		seats = new int[SimulPar.N];
		for (int i = 0; i < SimulPar.N; i++) {
			studentStates[i] = States.GOING_TO_THE_RESTAURANT;
			seats[i] = -1;
		}
		
		chefState = States.WAIT_FOR_AN_ORDER;
      
		waiterState = States.APPRAISING_SITUATION;

		
		printInitialStatus();
	      		
	}
	
	/**
   *  Print header.
   */
   private void printInitialStatus() {
	   TextFile logFile = new TextFile();                      
	   
	   if (!logFile.openForWriting(".", logName)) { 
		   GenericIO.writelnString("The operation of creating the file " + logName + " failed!");
           System.exit(1);
       }
	   logFile.writelnString("\t\t\t\t\t\t  The Restaurant - Description of the internal state");
	   logFile.writelnString("\n\tChef\tWaiter\tStu0\tStu1\tStu2\tStu3\tStu4\tStu5\tStu6\tNCourse\tNPortion\t\t\tTable\n");
	   logFile.writelnString("\n\tState\tState\tState\tState\tState\tState\tState\tState\tState\t\t\tSeat0\tSeat1\tSeat2\tSeat3\tSeat4\tSeat5\tSeat6\n");
	   if (!logFile.close()) { 
		   GenericIO.writelnString("The operation of closing the file " + logName + " failed!");
           System.exit(1);
       }
	   printStatus();
   }
   
   /**
    *  Write the body of the logging file.
    */
    private void printStatus() {
    	TextFile logFile = new TextFile();                      

        String lineStatus = "";                              			

        if (!logFile.openForAppending(".", logName)) {
        	GenericIO.writelnString("The operation of opening for appending the file " + logName + " failed!");
            System.exit(1);
        }
        
        switch(chefState){ 
			case WAIT_FOR_AN_ORDER:  lineStatus += "\tWAFOR";
		                                    break;
			case PREPARING_A_COURSE: lineStatus += "\tPRPCS";
		                                    break;
			case DISHING_THE_PORTIONS:      lineStatus += "\tDSHPT";
		                                    break;
			case DELIVERING_THE_PORTIONS:    lineStatus += "\tDLVPT";
		                                    break;
			case CLOSING_SERVICE:    lineStatus += "\tCLSSV";
                                			break;
        }
        switch (waiterState){ 
         	case APPRAISING_SITUATION:  lineStatus += "\tAPPST";
                                                break;
         	case PRESENTING_THE_MENU: lineStatus += "\tPRSMN";  
                                                break;
         	case TAKING_THE_ORDER:      lineStatus += "\tTKODR";
                                                break;
         	case PLACING_THE_ORDER:    lineStatus += "\tPCODR";
                                                break;
         	case WAITING_FOR_AN_PORTION:    lineStatus += "\tWTFPT";
                                                break;
         	case PROCESSING_THE_BILL:    lineStatus += "\tPRCBL";
                                                break;                                              
         	case RECEIVING_PAYMENT:    lineStatus += "\tRECPM";
                                                break;                                            
        }
        for (int i = 0; i < SimulPar.N; i++)
        	switch (studentStates[i]){ 
             	case GOING_TO_THE_RESTAURANT:   lineStatus += "\tGGTRT";
                                             break;
             	case TAKING_A_SEAT_AT_THE_TABLE: lineStatus += "\tTKSTT";
                                               break;    
             	case SELECTING_THE_COURSES: lineStatus += "\tSELCS";
                                               break; 
             	case ORGANIZING_THE_ORDER: lineStatus += "\tOGODR";
                                               break; 
             	case CHATING_WITH_COMPANIONS: lineStatus += "\tCHTWC";
                                               break; 
             	case PAYING_THE_BILL: lineStatus += "\tPYTML";
                                               break; 
             	case ENJOYING_THE_MEAL: lineStatus += "\tEJTML";
                                               break; 
             	case GOING_HOME: lineStatus += "\tGGHOM";
                                               break; 
                 
        	}
        lineStatus += "\t"+nCourses+"\t"+nPortions;
         
        for(int i = 0;i< SimulPar.N;i++) {
        	lineStatus += "\t";
        	if(seats[i] == -1){
        		lineStatus += "-";
        	} else {
        		lineStatus += seats[i];
             }
         }
      
        logFile.writelnString (lineStatus);
        if (!logFile.close()) { 
        	GenericIO.writelnString("The operation of closing the file " + logName + " failed!");
        	System.exit(1);
        }
    }
	
	/**
	   *   Set Chef state.
	   *
	   *     @param chefState chef state
	   */
	public synchronized void setChefState(States chefState) {
		this.chefState = chefState;
        printStatus();
	}
	
	/**
	   *   Set Waiter state.
	   *
	   *     @param waiterState waiter state
	   */
	public synchronized void setWaiterState(States waiterState) {
		this.waiterState = waiterState;
		printStatus();
	}
	
	/**
	   *   Set Student state.
	   *
	   *	@param id student id
	   *    @param studentState student state
	   */
	public synchronized void setStudentState(int id, States studentState) {
		studentStates[id] = studentState;
        printStatus();
	}
	
	/**
	   *   Set who is seated at the table.
	   *
	   *     @param id student id
	   *     @param seat of the student at the restaurant
	   */
	public synchronized void setStudentSeat(int seat, int id) {
		seats[seat] = id;
	}

	/**@return the student seat position in the table */
	public synchronized int getStudentSeat(int id) {
		for(int i = 0; i<seats.length;i++){
			if(seats[i] == id) return i;
		}
		return -1;
	}

	/**
	   *   Set number of portions.
	   *
	   *     @param portionsDelivery number of portions delivered
	   */
	public synchronized void setPortions(int portionsDelivery) {
		nPortions = portionsDelivery;
	}
	
	/**
	   *   Set number of Courses.
	   *
	   *     @param i number of courses
	   */
	public synchronized void setCourses(int i) {
		nCourses= i;
	}
	
	/**
	 * @return the firstStudent
	 */
	public synchronized int getFirstStudent() {
		return firstStudent;
	}

	/**
	 * @param id the firstStudent to set
	 */
	public synchronized void setFirstStudent(int id) {
		this.firstStudent = id;
	}

	/**
	 * @return the lastStudent
	 */
	public synchronized int getLastStudent() {
		return lastStudent;
	}

	/**
	 * @param id the lastStudent to set
	 */
	public synchronized void setLastStudent(int id) {
		this.lastStudent = id;
	}
	
}
